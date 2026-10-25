package birthday.model;

import java.time.LocalDate;
import java.time.Month;

public record Friend(String lastName, String firstName, LocalDate dateOfBirth, String email, String phone) {

    public Friend(String lastName, String firstName, LocalDate dateOfBirth, String email) {
        this(lastName, firstName, dateOfBirth, email, null);
    }

    public boolean isBirthday(LocalDate today) {
        if (dateOfBirth.getMonth() == today.getMonth()
                && dateOfBirth.getDayOfMonth() == today.getDayOfMonth()) {
            return true;
        }
        // Mejora: los nacidos el 29/02 lo celebran el 28/02 en años no bisiestos
        return isLeapDay(dateOfBirth)
                && !today.isLeapYear()
                && today.getMonth() == Month.FEBRUARY
                && today.getDayOfMonth() == 28;
    }

    private static boolean isLeapDay(LocalDate date) {
        return date.getMonth() == Month.FEBRUARY && date.getDayOfMonth() == 29;
    }

    public String fullName() {
        return firstName + " " + lastName;
    }
}
