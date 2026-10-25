package birthday.repository;

import birthday.model.Friend;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class SqliteFriendRepository implements FriendRepository {

    private final String url;

    public SqliteFriendRepository(String dbPath) {
        this.url = "jdbc:sqlite:" + dbPath;
        createTable();
    }

    private Connection connect() throws SQLException {
        return DriverManager.getConnection(url);
    }

    private void createTable() {
        String sql = """
                CREATE TABLE IF NOT EXISTS friends (
                    id INTEGER PRIMARY KEY AUTOINCREMENT,
                    last_name TEXT NOT NULL,
                    first_name TEXT NOT NULL,
                    date_of_birth TEXT NOT NULL,
                    email TEXT NOT NULL,
                    phone TEXT
                )""";
        try (Connection c = connect(); Statement st = c.createStatement()) {
            st.execute(sql);
        } catch (SQLException e) {
            throw new RepositoryException("No se pudo crear la tabla friends", e);
        }
    }

    public void save(Friend friend) {
        String sql = "INSERT INTO friends(last_name, first_name, date_of_birth, email, phone) VALUES (?,?,?,?,?)";
        try (Connection c = connect(); PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, friend.lastName());
            ps.setString(2, friend.firstName());
            ps.setString(3, friend.dateOfBirth().toString());
            ps.setString(4, friend.email());
            ps.setString(5, friend.phone());
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RepositoryException("No se pudo guardar " + friend.fullName(), e);
        }
    }

    public void deleteAll() {
        try (Connection c = connect(); Statement st = c.createStatement()) {
            st.executeUpdate("DELETE FROM friends");
        } catch (SQLException e) {
            throw new RepositoryException("No se pudo vaciar la tabla friends", e);
        }
    }

    @Override
    public List<Friend> findAll() {
        String sql = "SELECT last_name, first_name, date_of_birth, email, phone FROM friends ORDER BY id";
        List<Friend> friends = new ArrayList<>();
        try (Connection c = connect(); Statement st = c.createStatement(); ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) {
                friends.add(new Friend(
                        rs.getString("last_name"),
                        rs.getString("first_name"),
                        LocalDate.parse(rs.getString("date_of_birth")),
                        rs.getString("email"),
                        rs.getString("phone")));
            }
        } catch (SQLException e) {
            throw new RepositoryException("Error leyendo la base de datos", e);
        }
        return friends;
    }
}
