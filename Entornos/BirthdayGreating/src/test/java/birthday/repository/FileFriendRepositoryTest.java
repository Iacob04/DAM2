package birthday.repository;

import birthday.model.Friend;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class FileFriendRepositoryTest {

    @TempDir
    Path tmp;

    private Path write(String content) throws IOException {
        Path file = tmp.resolve("friends.txt");
        Files.writeString(file, content);
        return file;
    }

    @Test
    void leeAmigosIgnorandoCabeceraYLineasVacias() throws IOException {
        Path file = write("""
                last_name, first_name, date_of_birth, email
                Doe, John, 1982/10/08, john.doe@foobar.com

                Ann, Mary, 1975/09/11, mary.ann@foobar.com
                """);

        List<Friend> friends = new FileFriendRepository(file).findAll();

        assertEquals(2, friends.size());
        Friend john = friends.get(0);
        assertEquals("Doe", john.lastName());
        assertEquals("John", john.firstName());
        assertEquals(LocalDate.of(1982, 10, 8), john.dateOfBirth());
        assertEquals("john.doe@foobar.com", john.email());
        assertNull(john.phone());
    }

    @Test
    void leeTelefonoOpcional() throws IOException {
        Path file = write("Doe, John, 1982/10/08, john.doe@foobar.com, +34600111222\n");
        assertEquals("+34600111222", new FileFriendRepository(file).findAll().get(0).phone());
    }

    @Test
    void ficheroVacioDevuelveListaVacia() throws IOException {
        assertTrue(new FileFriendRepository(write("")).findAll().isEmpty());
    }

    @Test
    void lineaMalFormadaLanzaExcepcion() throws IOException {
        Path file = write("Doe, John, john.doe@foobar.com\n");
        assertThrows(RepositoryException.class, () -> new FileFriendRepository(file).findAll());
    }

    @Test
    void fechaIncorrectaLanzaExcepcion() throws IOException {
        Path file = write("Doe, John, 08-10-1982, john.doe@foobar.com\n");
        assertThrows(RepositoryException.class, () -> new FileFriendRepository(file).findAll());
    }

    @Test
    void ficheroInexistenteLanzaExcepcion() {
        FileFriendRepository repo = new FileFriendRepository(tmp.resolve("no_existe.txt"));
        assertThrows(RepositoryException.class, repo::findAll);
    }
}
