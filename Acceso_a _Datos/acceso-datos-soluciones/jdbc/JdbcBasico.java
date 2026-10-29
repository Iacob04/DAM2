package jdbc;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

// Plantilla: las 5 cosas que hacen falta en todos los ejercicios
public class JdbcBasico {

    // Docker: el puerto 3306 del contenedor está redirigido a localhost:3306
    static final String URL = "jdbc:mysql://localhost:3306/classicmodels";
    static final String USUARIO = "alumno";
    static final String PASSWORD = "abc123";

    public static void main(String[] args) {
        consultaSinParametros();
        consultaConParametro("Paris");
        System.out.println("Filas cambiadas: " + actualizar("Paris", "+33 1 00 00 00 00"));
    }

    // 1. Conectar. try-with-resources cierra Connection, Statement y ResultSet solos.
    static Connection conectar() throws SQLException {
        return DriverManager.getConnection(URL, USUARIO, PASSWORD);
    }

    // 2. SELECT fijo: Statement + executeQuery + while (rs.next())
    static void consultaSinParametros() {
        String sql = "SELECT officeCode, city FROM offices ORDER BY city";
        try (Connection con = conectar();
             Statement st = con.createStatement();
             ResultSet rs = st.executeQuery(sql)) {

            while (rs.next()) {                                     // una vuelta por fila
                System.out.println(rs.getString("officeCode") + " " + rs.getString("city"));
            }
        } catch (SQLException e) {
            System.out.println("Error de base de datos: " + e.getMessage());
        }
    }

    // 3. SELECT con datos que vienen de fuera: SIEMPRE PreparedStatement con ?
    static void consultaConParametro(String ciudad) {
        String sql = "SELECT COUNT(*) FROM employees e JOIN offices o ON e.officeCode = o.officeCode WHERE o.city = ?";
        try (Connection con = conectar();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, ciudad);                                 // el 1 es el primer ? (empiezan en 1, no en 0)
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {                                     // COUNT siempre devuelve una fila
                    System.out.println("Empleados en " + ciudad + ": " + rs.getInt(1));
                }
            }
        } catch (SQLException e) {
            System.out.println("Error de base de datos: " + e.getMessage());
        }
    }

    // 4. INSERT / UPDATE / DELETE: executeUpdate devuelve cuántas filas ha tocado
    static int actualizar(String ciudad, String telefono) {
        String sql = "UPDATE offices SET phone = ? WHERE city = ?";
        try (Connection con = conectar();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, telefono);
            ps.setString(2, ciudad);
            return ps.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Error de base de datos: " + e.getMessage());
            return 0;
        }
    }

    // 5. Varias operaciones que van juntas: transacción
    static void transaccion() {
        try (Connection con = conectar()) {
            con.setAutoCommit(false);                // a partir de aquí nada se guarda hasta commit()
            try (PreparedStatement ps = con.prepareStatement("UPDATE ...")) {
                ps.executeUpdate();
                con.commit();                         // todo bien: se guarda
            } catch (SQLException e) {
                con.rollback();                       // algo falló: se deshace todo
                System.out.println("Deshecho: " + e.getMessage());
            }
        } catch (SQLException e) {
            System.out.println("Error de base de datos: " + e.getMessage());
        }
    }
}
