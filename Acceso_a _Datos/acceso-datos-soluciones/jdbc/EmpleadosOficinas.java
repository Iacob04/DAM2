package jdbc;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

// Examen Conector J, ejercicios 1 y 2 (classicmodels)
public class EmpleadosOficinas {

    static final String URL = "jdbc:mysql://localhost:3306/classicmodels";
    static final String USUARIO = "alumno";
    static final String PASSWORD = "abc123";

    public static void main(String[] args) {
        listarEmpleados("Paris");
        listarEmpleados("Madrid");
        moverEmpleados("Tokyo", "Paris");
        listarEmpleados("Tokyo");          // ahora sí: oficina sin empleados
        moverEmpleados("Madrid", "Paris");
    }

    static Connection conectar() throws SQLException {
        return DriverManager.getConnection(URL, USUARIO, PASSWORD);
    }

    // Código de la oficina de una ciudad, o null si no hay oficina
    static String codigoOficina(Connection con, String ciudad) throws SQLException {
        try (PreparedStatement ps = con.prepareStatement("SELECT officeCode FROM offices WHERE city = ?")) {
            ps.setString(1, ciudad);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() ? rs.getString(1) : null;
            }
        }
    }

    static int contarEmpleados(Connection con, String officeCode) throws SQLException {
        try (PreparedStatement ps = con.prepareStatement("SELECT COUNT(*) FROM employees WHERE officeCode = ?")) {
            ps.setString(1, officeCode);
            try (ResultSet rs = ps.executeQuery()) {
                rs.next();
                return rs.getInt(1);
            }
        }
    }

    // ---------------- Ejercicio 1 ----------------
    public static void listarEmpleados(String ciudad) {
        try (Connection con = conectar()) {
            String oficina = codigoOficina(con, ciudad);
            if (oficina == null) {
                System.out.println("No existe oficina en " + ciudad);
                return;
            }

            List<String> empleados = new ArrayList<>();
            String sql = "SELECT lastName, firstName, email FROM employees WHERE officeCode = ?";
            try (PreparedStatement ps = con.prepareStatement(sql)) {
                ps.setString(1, oficina);
                try (ResultSet rs = ps.executeQuery()) {
                    while (rs.next()) {
                        empleados.add(rs.getString("lastName") + ", " + rs.getString("firstName")
                                + " (" + rs.getString("email") + ")");
                    }
                }
            }

            // Primero se recoge todo: así sé cuántos hay antes de escribir la cabecera
            if (empleados.isEmpty()) {
                System.out.println("La oficina de " + ciudad + " no tiene empleados");
            } else {
                System.out.println("Hay " + empleados.size() + " empleados en la oficina de " + ciudad + ". Sus datos son:");
                for (String e : empleados) System.out.println(e);
            }
        } catch (SQLException e) {
            System.out.println("Error de base de datos: " + e.getMessage());
        }
    }

    // ---------------- Ejercicio 2 ----------------
    public static void moverEmpleados(String origen, String destino) {
        System.out.println("Moviendo todos los empleados de " + origen + " a " + destino + ".");
        try (Connection con = conectar()) {
            String ofOrigen = codigoOficina(con, origen);
            String ofDestino = codigoOficina(con, destino);
            if (ofOrigen == null) {
                System.out.println("No existe oficina en " + origen);
                return;
            }
            if (ofDestino == null) {
                System.out.println("No existe oficina en " + destino);
                return;
            }

            System.out.println("Se van a mover " + contarEmpleados(con, ofOrigen) + " empleados de "
                    + origen + " a " + destino);

            try (PreparedStatement ps = con.prepareStatement(
                    "UPDATE employees SET officeCode = ? WHERE officeCode = ?")) {
                ps.setString(1, ofDestino);         // SET  -> primer ?
                ps.setString(2, ofOrigen);          // WHERE -> segundo ?
                ps.executeUpdate();
            }

            System.out.println("La oficina de " + destino + " tiene ahora " + contarEmpleados(con, ofDestino) + " empleados");
        } catch (SQLException e) {
            System.out.println("Error de base de datos: " + e.getMessage());
        }
    }
}
