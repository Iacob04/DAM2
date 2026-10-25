package birthday.service;

import birthday.model.Friend;
import birthday.model.Message;
import birthday.notifier.NotificationException;
import birthday.notifier.Notifier;
import birthday.repository.FriendRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class BirthdayServiceTest {

    @Mock
    FriendRepository repository;

    @Mock
    Notifier notifier;

    private final Friend john = new Friend("Doe", "John", LocalDate.of(1982, 10, 8), "john.doe@foobar.com");
    private final Friend mary = new Friend("Ann", "Mary", LocalDate.of(1975, 9, 11), "mary.ann@foobar.com");
    private final Friend peter = new Friend("Leap", "Peter", LocalDate.of(2000, 2, 29), "peter.leap@foobar.com");

    private static final LocalDate JOHN_BIRTHDAY = LocalDate.of(2026, 10, 8);

    @Test
    void enviaFelicitacionSoloAlQueCumpleAnos() {
        when(repository.findAll()).thenReturn(List.of(john, mary));
        BirthdayService service = new BirthdayService(repository, notifier, false);

        BirthdayService.Result result = service.sendGreetings(JOHN_BIRTHDAY);

        verify(notifier).send(john, Message.greeting(john));
        verify(notifier, never()).send(eq(mary), any());
        assertEquals(1, result.greetings());
    }

    @Test
    void elMensajeEnviadoTieneAsuntoYCuerpoCorrectos() {
        when(repository.findAll()).thenReturn(List.of(john));
        ArgumentCaptor<Message> captor = ArgumentCaptor.forClass(Message.class);

        new BirthdayService(repository, notifier, false).sendGreetings(JOHN_BIRTHDAY);

        verify(notifier).send(eq(john), captor.capture());
        assertEquals("¡Feliz cumpleaños!", captor.getValue().subject());
        assertEquals("¡Feliz cumpleaños, querido/a John!", captor.getValue().body());
    }

    @Test
    void noEnviaNadaSiNadieCumpleAnos() {
        when(repository.findAll()).thenReturn(List.of(john, mary));

        BirthdayService.Result result = new BirthdayService(repository, notifier, true)
                .sendGreetings(LocalDate.of(2026, 1, 1));

        verifyNoInteractions(notifier);
        assertEquals(0, result.greetings());
        assertEquals(0, result.reminders());
    }

    @Test
    void enviaRecordatorioAlRestoDeAmigos() {
        when(repository.findAll()).thenReturn(List.of(john, mary, peter));

        BirthdayService.Result result = new BirthdayService(repository, notifier, true)
                .sendGreetings(JOHN_BIRTHDAY);

        verify(notifier).send(john, Message.greeting(john));
        verify(notifier).send(mary, Message.reminder(mary, john));
        verify(notifier).send(peter, Message.reminder(peter, john));
        verify(notifier, never()).send(john, Message.reminder(john, john));
        verify(notifier, times(3)).send(any(), any());
        assertEquals(1, result.greetings());
        assertEquals(2, result.reminders());
    }

    @Test
    void felicitaEl28DeFebreroANacidoEl29EnAnoNoBisiesto() {
        when(repository.findAll()).thenReturn(List.of(peter, john));

        new BirthdayService(repository, notifier, false).sendGreetings(LocalDate.of(2027, 2, 28));

        verify(notifier).send(peter, Message.greeting(peter));
        verify(notifier, times(1)).send(any(), any());
    }

    @Test
    void unFalloDeEnvioNoDetieneElResto() {
        Friend john2 = new Friend("Smith", "Anna", LocalDate.of(1990, 10, 8), "anna@foobar.com");
        when(repository.findAll()).thenReturn(List.of(john, john2));
        doAnswer(inv -> {
            if (inv.getArgument(0).equals(john)) {
                throw new NotificationException("SMTP caído");
            }
            return null;
        }).when(notifier).send(any(), any());

        BirthdayService.Result result = new BirthdayService(repository, notifier, false)
                .sendGreetings(JOHN_BIRTHDAY);

        verify(notifier).send(john2, Message.greeting(john2));
        assertEquals(1, result.greetings());
        assertEquals(1, result.errors());
    }
}
