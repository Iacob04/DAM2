package jdbc;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

// ¿Qué pasa si falla algo DESPUÉS del DELETE y antes del commit?
public class PruebaRollback {
    public static void main(String[] args) {
        try (Connection cn = PoolConexiones.getConnection()) {
            cn.setAutoCommit(false);
            Statement sql = cn.createStatement();
            System.out.println("Borrados: " + sql.executeUpdate("DELETE FROM alumnos WHERE email = 'ana@educa.madrid.org'"));
            sql.executeQuery("SELECT * FROM alumno");          // tabla mal escrita -> excepción
            cn.commit();
        } catch (SQLException e) {
            System.out.println("Error: " + e.getMessage());
        }
        try (Connection cn = PoolConexiones.getConnection();
             ResultSet rs = cn.createStatement().executeQuery("SELECT COUNT(*) FROM alumnos")) {
            rs.next();
            System.out.println("Alumnos que quedan: " + rs.getInt(1));
        } catch (SQLException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }
}
