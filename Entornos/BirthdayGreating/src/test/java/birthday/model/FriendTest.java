package birthday.model;

import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class FriendTest {

    private final Friend john = new Friend("Doe", "John", LocalDate.of(1982, 10, 8), "john.doe@foobar.com");
    private final Friend leap = new Friend("Leap", "Peter", LocalDate.of(2000, 2, 29), "peter@foobar.com");

    @Test
    void esCumpleanosElMismoDiaYMes() {
        assertTrue(john.isBirthday(LocalDate.of(2026, 10, 8)));
    }

    @Test
    void noEsCumpleanosOtroDia() {
        assertFalse(john.isBirthday(LocalDate.of(2026, 10, 9)));
        assertFalse(john.isBirthday(LocalDate.of(2026, 11, 8)));
    }

    @Test
    void nacido29FebreroCelebra28EnAnoNoBisiesto() {
        assertTrue(leap.isBirthday(LocalDate.of(2026, 2, 28)));
    }

    @Test
    void nacido29FebreroCelebra29EnAnoBisiesto() {
        assertTrue(leap.isBirthday(LocalDate.of(2028, 2, 29)));
        assertFalse(leap.isBirthday(LocalDate.of(2028, 2, 28)));
    }

    @Test
    void nacido28FebreroNoSeVeAfectado() {
        Friend f = new Friend("A", "B", LocalDate.of(1990, 2, 28), "a@b.com");
        assertTrue(f.isBirthday(LocalDate.of(2026, 2, 28)));
        assertTrue(f.isBirthday(LocalDate.of(2028, 2, 28)));
    }

    @Test
    void mensajesTienenElFormatoPedido() {
        Message greeting = Message.greeting(john);
        assertEquals("¡Feliz cumpleaños!", greeting.subject());
        assertEquals("¡Feliz cumpleaños, querido/a John!", greeting.body());

        Message reminder = Message.reminder(leap, john);
        assertEquals("Recordatorio de cumpleaños", reminder.subject());
        assertEquals("Querido/a Peter,\n\nHoy es el cumpleaños de John Doe.\n¡No olvides enviarle un mensaje!",
                reminder.body());
    }
}
