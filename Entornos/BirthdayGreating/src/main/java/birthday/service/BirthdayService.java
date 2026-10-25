package birthday.service;

import birthday.model.Friend;
import birthday.model.Message;
import birthday.notifier.NotificationException;
import birthday.notifier.Notifier;
import birthday.repository.FriendRepository;

import java.time.LocalDate;
import java.util.List;

public class BirthdayService {

    private final FriendRepository repository;
    private final Notifier notifier;
    private final boolean sendReminders;

    public BirthdayService(FriendRepository repository, Notifier notifier, boolean sendReminders) {
        this.repository = repository;
        this.notifier = notifier;
        this.sendReminders = sendReminders;
    }

    public Result sendGreetings(LocalDate today) {
        List<Friend> friends = repository.findAll();
        List<Friend> birthdayFriends = friends.stream()
                .filter(f -> f.isBirthday(today))
                .toList();

        Result result = new Result();
        for (Friend birthdayFriend : birthdayFriends) {
            if (trySend(birthdayFriend, Message.greeting(birthdayFriend))) {
                result.greetings++;
            } else {
                result.errors++;
            }
            if (sendReminders) {
                for (Friend other : friends) {
                    if (other.equals(birthdayFriend)) {
                        continue;
                    }
                    if (trySend(other, Message.reminder(other, birthdayFriend))) {
                        result.reminders++;
                    } else {
                        result.errors++;
                    }
                }
            }
        }
        return result;
    }

    private boolean trySend(Friend recipient, Message message) {
        try {
            notifier.send(recipient, message);
            return true;
        } catch (NotificationException e) {
            System.err.println("No se pudo notificar a " + recipient.fullName() + ": " + e.getMessage());
            return false;
        }
    }

    public static class Result {
        int greetings;
        int reminders;
        int errors;

        public int greetings() { return greetings; }
        public int reminders() { return reminders; }
        public int errors() { return errors; }

        @Override
        public String toString() {
            return "Felicitaciones: " + greetings + " | Recordatorios: " + reminders + " | Errores: " + errors;
        }
    }
}
