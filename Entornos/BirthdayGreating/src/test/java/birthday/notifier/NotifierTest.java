package birthday.notifier;

import birthday.model.Friend;
import birthday.model.Message;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class NotifierTest {

    private final Friend john = new Friend("Doe", "John", LocalDate.of(1982, 10, 8), "john.doe@foobar.com", "+34600111222");

    @Test
    void consoleNotifierMuestraDestinatarioAsuntoYCuerpo() {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        new ConsoleNotifier(new PrintStream(out, true)).send(john, Message.greeting(john));

        String text = out.toString();
        assertTrue(text.contains("john.doe@foobar.com"));
        assertTrue(text.contains("Asunto: ¡Feliz cumpleaños!"));
        assertTrue(text.contains("¡Feliz cumpleaños, querido/a John!"));
    }

    @Test
    void smsNotifierEnviaAlTelefono() {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        new SmsNotifier(new PrintStream(out, true)).send(john, Message.greeting(john));
        assertTrue(out.toString().contains("+34600111222"));
    }

    @Test
    void smsNotifierFallaSinTelefono() {
        Friend sinTelefono = new Friend("Ann", "Mary", LocalDate.of(1975, 9, 11), "mary.ann@foobar.com");
        SmsNotifier sms = new SmsNotifier(new PrintStream(new ByteArrayOutputStream()));
        assertThrows(NotificationException.class, () -> sms.send(sinTelefono, Message.greeting(sinTelefono)));
    }
}
