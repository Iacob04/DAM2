package tddmicroexercises.telemetrysystem;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InOrder;

public class TelemetryDiagnosticControlsTest
{
    private static final String CONNECTION_STRING = "*111#";
    private static final String DIAGNOSTIC_RESPONSE = "LAST TX rate................ 100 MBPS";

    private TelemetryClient telemetryClient;
    private TelemetryDiagnosticControls controls;

    @BeforeEach
    void setUp()
    {
        telemetryClient = mock(TelemetryClient.class);
        controls = new TelemetryDiagnosticControls(telemetryClient);
    }

    @Test
    public void CheckTransmission_should_send_a_diagnostic_message_and_receive_a_status_message_response() throws Exception
    {
        when(telemetryClient.getOnlineStatus()).thenReturn(false, true);
        when(telemetryClient.receive()).thenReturn(DIAGNOSTIC_RESPONSE);

        controls.checkTransmission();

        InOrder order = inOrder(telemetryClient);
        order.verify(telemetryClient).disconnect();
        order.verify(telemetryClient).connect(CONNECTION_STRING);
        order.verify(telemetryClient).send(TelemetryClient.DIAGNOSTIC_MESSAGE);
        order.verify(telemetryClient).receive();
        assertEquals(DIAGNOSTIC_RESPONSE, controls.getDiagnosticInfo());
    }

    @Test
    public void CheckTransmission_should_send_exactly_the_diagnostic_message_AT_UD() throws Exception
    {
        when(telemetryClient.getOnlineStatus()).thenReturn(false, true);

        controls.checkTransmission();

        verify(telemetryClient).send("AT#UD");
    }

    @Test
    public void CheckTransmission_should_retry_connection_until_online() throws Exception
    {
        when(telemetryClient.getOnlineStatus()).thenReturn(false, false, false, true);
        when(telemetryClient.receive()).thenReturn(DIAGNOSTIC_RESPONSE);

        controls.checkTransmission();

        verify(telemetryClient, times(3)).connect(CONNECTION_STRING);
        verify(telemetryClient).send(TelemetryClient.DIAGNOSTIC_MESSAGE);
        assertEquals(DIAGNOSTIC_RESPONSE, controls.getDiagnosticInfo());
    }

    @Test
    public void CheckTransmission_should_throw_after_three_failed_connection_attempts() throws Exception
    {
        when(telemetryClient.getOnlineStatus()).thenReturn(false);

        Exception ex = assertThrows(Exception.class, () -> controls.checkTransmission());

        assertEquals("Unable to connect.", ex.getMessage());
        verify(telemetryClient, times(3)).connect(CONNECTION_STRING);
    }

    @Test
    public void CheckTransmission_should_not_send_nor_receive_when_connection_fails() throws Exception
    {
        when(telemetryClient.getOnlineStatus()).thenReturn(false);

        assertThrows(Exception.class, () -> controls.checkTransmission());

        verify(telemetryClient, never()).send(org.mockito.ArgumentMatchers.anyString());
        verify(telemetryClient, never()).receive();
    }

    @Test
    public void CheckTransmission_should_clear_previous_diagnostic_info_when_connection_fails() throws Exception
    {
        controls.setDiagnosticInfo("old info");
        when(telemetryClient.getOnlineStatus()).thenReturn(false);

        assertThrows(Exception.class, () -> controls.checkTransmission());

        assertEquals("", controls.getDiagnosticInfo());
    }

    @Test
    public void CheckTransmission_should_connect_only_once_when_first_attempt_succeeds() throws Exception
    {
        when(telemetryClient.getOnlineStatus()).thenReturn(false, true);

        controls.checkTransmission();

        verify(telemetryClient, times(1)).connect(CONNECTION_STRING);
    }

    @Test
    public void CheckTransmission_should_not_connect_if_client_is_already_online_after_disconnect() throws Exception
    {
        when(telemetryClient.getOnlineStatus()).thenReturn(true);
        when(telemetryClient.receive()).thenReturn(DIAGNOSTIC_RESPONSE);

        controls.checkTransmission();

        verify(telemetryClient, never()).connect(org.mockito.ArgumentMatchers.anyString());
        assertEquals(DIAGNOSTIC_RESPONSE, controls.getDiagnosticInfo());
    }

    @Test
    public void DiagnosticInfo_should_be_empty_by_default()
    {
        assertEquals("", controls.getDiagnosticInfo());
    }

    @Test
    public void SetDiagnosticInfo_should_store_the_given_value()
    {
        controls.setDiagnosticInfo("custom");

        assertEquals("custom", controls.getDiagnosticInfo());
    }
}
