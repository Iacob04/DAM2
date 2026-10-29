package jdbc;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

// Lo mismo que Main, con PreparedStatement, rollback y mensajes
public class BorrarAlumno {

    public static void main(String[] args) {
        borrarAlumno("ana@educa.madrid.org");
        borrarAlumno("ana@educa.madrid.org");     // segunda vez: ya no está
        listarAlumnos();
    }

    public static void borrarAlumno(String email) {
        try (Connection cn = PoolConexiones.getConnection()) {
            cn.setAutoCommit(false);                          // empieza la transacción
            try (PreparedStatement ps = cn.prepareStatement("DELETE FROM alumnos WHERE email = ?")) {
                ps.setString(1, email);
                int filas = ps.executeUpdate();               // cuántos ha borrado
                cn.commit();                                  // todo bien: se guarda
                if (filas == 0) System.out.println("No hay ningún alumno con el email " + email);
                else System.out.println("Alumno " + email + " borrado");
            } catch (SQLException e) {
                cn.rollback();                                // algo falló: se deshace
                System.out.println("No se ha borrado nada: " + e.getMessage());
            }
        } catch (SQLException e) {
            System.out.println("Error de conexión: " + e.getMessage());
        }
    }

    public static void listarAlumnos() {
        try (Connection cn = PoolConexiones.getConnection();
             PreparedStatement ps = cn.prepareStatement("SELECT email FROM alumnos ORDER BY email");
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) System.out.println(rs.getString("email"));
        } catch (SQLException e) {
            System.out.println("Error de base de datos: " + e.getMessage());
        }
    }
}
