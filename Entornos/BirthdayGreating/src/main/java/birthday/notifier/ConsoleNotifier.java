package birthday.notifier;

import birthday.model.Friend;
import birthday.model.Message;

import java.io.PrintStream;

// Simula el envío de correos mostrándolos por consola (útil para pruebas y demostración)
public class ConsoleNotifier implements Notifier {

    private final PrintStream out;

    public ConsoleNotifier() {
        this(System.out);
    }

    public ConsoleNotifier(PrintStream out) {
        this.out = out;
    }

    @Override
    public void send(Friend recipient, Message message) {
        out.println("----------------------------------------");
        out.println("Para: " + recipient.fullName() + " <" + recipient.email() + ">");
        out.println("Asunto: " + message.subject());
        out.println();
        out.println(message.body());
    }
}
