package birthday.model;

public record Message(String subject, String body) {

    public static Message greeting(Friend friend) {
        return new Message("¡Feliz cumpleaños!",
                "¡Feliz cumpleaños, querido/a " + friend.firstName() + "!");
    }

    public static Message reminder(Friend recipient, Friend birthdayFriend) {
        return new Message("Recordatorio de cumpleaños",
                "Querido/a " + recipient.firstName() + ",\n\n"
                        + "Hoy es el cumpleaños de " + birthdayFriend.firstName() + " " + birthdayFriend.lastName() + ".\n"
                        + "¡No olvides enviarle un mensaje!");
    }
}
