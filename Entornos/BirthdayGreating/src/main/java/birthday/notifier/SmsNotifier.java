package birthday.notifier;

import birthday.model.Friend;
import birthday.model.Message;

import java.io.PrintStream;

// Envío por SMS simulado: en un caso real aquí se llamaría a la API de un proveedor (Twilio, Vonage...)
public class SmsNotifier implements Notifier {

    private final PrintStream out;

    public SmsNotifier() {
        this(System.out);
    }

    public SmsNotifier(PrintStream out) {
        this.out = out;
    }

    @Override
    public void send(Friend recipient, Message message) {
        if (recipient.phone() == null || recipient.phone().isBlank()) {
            throw new NotificationException(recipient.fullName() + " no tiene teléfono");
        }
        out.println("[SMS -> " + recipient.phone() + "] " + message.body().replaceAll("\\s*\\n+\\s*", " "));
    }
}
