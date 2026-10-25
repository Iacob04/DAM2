package birthday.repository;

import birthday.model.Friend;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.nio.file.Path;
import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class SqliteFriendRepositoryTest {

    @TempDir
    Path tmp;

    @Test
    void guardaYRecuperaAmigos() {
        SqliteFriendRepository repo = new SqliteFriendRepository(tmp.resolve("test.db").toString());
        Friend john = new Friend("Doe", "John", LocalDate.of(1982, 10, 8), "john.doe@foobar.com");
        Friend mary = new Friend("Ann", "Mary", LocalDate.of(1975, 9, 11), "mary.ann@foobar.com", "+34600333444");

        repo.save(john);
        repo.save(mary);

        assertEquals(List.of(john, mary), repo.findAll());
    }

    @Test
    void baseDeDatosNuevaEstaVacia() {
        SqliteFriendRepository repo = new SqliteFriendRepository(tmp.resolve("vacia.db").toString());
        assertTrue(repo.findAll().isEmpty());
    }

    @Test
    void deleteAllVaciaLaTabla() {
        SqliteFriendRepository repo = new SqliteFriendRepository(tmp.resolve("test.db").toString());
        repo.save(new Friend("Doe", "John", LocalDate.of(1982, 10, 8), "john.doe@foobar.com"));
        repo.deleteAll();
        assertTrue(repo.findAll().isEmpty());
    }
}
