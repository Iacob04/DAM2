package birthday.service;

import birthday.model.Friend;
import birthday.model.Message;
import birthday.notifier.Notifier;
import birthday.repository.FileFriendRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

// Integración: fichero real + servicio real; solo se simula el envío
class BirthdayIntegrationTest {

    @TempDir
    Path tmp;

    @Test
    void leeElFicheroYFelicitaAlCumpleanero() throws IOException {
        Path file = tmp.resolve("friends.txt");
        Files.writeString(file, """
                last_name, first_name, date_of_birth, email
                Doe, John, 1982/10/08, john.doe@foobar.com
                Ann, Mary, 1975/09/11, mary.ann@foobar.com
                """);
        Notifier notifier = mock(Notifier.class);
        BirthdayService service = new BirthdayService(new FileFriendRepository(file), notifier, true);

        BirthdayService.Result result = service.sendGreetings(LocalDate.of(2026, 9, 11));

        Friend mary = new Friend("Ann", "Mary", LocalDate.of(1975, 9, 11), "mary.ann@foobar.com");
        Friend john = new Friend("Doe", "John", LocalDate.of(1982, 10, 8), "john.doe@foobar.com");
        verify(notifier).send(mary, Message.greeting(mary));
        verify(notifier).send(john, Message.reminder(john, mary));
        verify(notifier, times(2)).send(any(), any());
        assertEquals(1, result.greetings());
        assertEquals(1, result.reminders());
    }
}
