package birthday.notifier;

public class NotificationException extends RuntimeException {
    public NotificationException(String message, Throwable cause) {
        super(message, cause);
    }

    public NotificationException(String message) {
        super(message);
    }
}
