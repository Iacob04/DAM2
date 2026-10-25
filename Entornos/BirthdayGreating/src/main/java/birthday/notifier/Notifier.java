package birthday.notifier;

import birthday.model.Friend;
import birthday.model.Message;

public interface Notifier {
    void send(Friend recipient, Message message);
}
