package birthday.repository;

import birthday.model.Friend;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

public final class FriendParser {

    public static final DateTimeFormatter DATE_FORMAT = DateTimeFormatter.ofPattern("yyyy/MM/dd");

    private FriendParser() {
    }

    // Formato: last_name, first_name, date_of_birth, email[, phone]
    public static Friend parse(String line) {
        String[] parts = line.split(",");
        if (parts.length < 4 || parts.length > 5) {
            throw new RepositoryException("Línea con formato incorrecto: " + line);
        }
        try {
            LocalDate dob = LocalDate.parse(parts[2].trim(), DATE_FORMAT);
            String phone = parts.length == 5 ? parts[4].trim() : null;
            return new Friend(parts[0].trim(), parts[1].trim(), dob, parts[3].trim(), phone);
        } catch (DateTimeParseException e) {
            throw new RepositoryException("Fecha no válida en la línea: " + line, e);
        }
    }
}
