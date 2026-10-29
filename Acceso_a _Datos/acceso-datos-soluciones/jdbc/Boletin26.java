package jdbc;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

// Ejercicios genéricos de programación 26 (classicmodels)
public class Boletin26 {

    static final String URL = "jdbc:mysql://localhost:3306/classicmodels";
    static final String USUARIO = "alumno";
    static final String PASSWORD = "abc123";

    public static void main(String[] args) {
        empleadosPorApellido("Patterson");
        empleadosPorApellido("Morales");
        System.out.println();
        cambiarEmpleadoCliente("Morales", 1612);
        cambiarEmpleadoCliente("Kelly's Gift Shop", 666);
        cambiarEmpleadoCliente("Kelly's Gift Shop", 1611);
        cambiarEmpleadoCliente("Warburg Exchange", 1612);
        System.out.println();
        productosConPocoStock(500);
        productosConPocoStock(15);
        productosConPocoStock(10);
    }

    static Connection conectar() throws SQLException {
        return DriverManager.getConnection(URL, USUARIO, PASSWORD);
    }

    // "Nombre Apellido" de un empleado, o null si el código no existe
    static String nombreEmpleado(Connection con, int numero) throws SQLException {
        try (PreparedStatement ps = con.prepareStatement(
                "SELECT firstName, lastName FROM employees WHERE employeeNumber = ?")) {
            ps.setInt(1, numero);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() ? rs.getString(1) + " " + rs.getString(2) : null;
            }
        }
    }

    // ---------------- Ejercicio 1 ----------------
    public static void empleadosPorApellido(String apellido) {
        try (Connection con = conectar();
             PreparedStatement ps = con.prepareStatement(
                     "SELECT employeeNumber, firstName, lastName FROM employees WHERE lastName = ?")) {
            ps.setString(1, apellido);
            try (ResultSet rs = ps.executeQuery()) {
                boolean hay = false;
                while (rs.next()) {
                    hay = true;
                    System.out.println(rs.getString("firstName") + " " + rs.getString("lastName"));

                    List<String> subordinados = new ArrayList<>();
                    try (PreparedStatement ps2 = con.prepareStatement(
                            "SELECT firstName, lastName FROM employees WHERE reportsTo = ?")) {
                        ps2.setInt(1, rs.getInt("employeeNumber"));
                        try (ResultSet rs2 = ps2.executeQuery()) {      // otro ResultSet: no reutilices rs
                            while (rs2.next()) subordinados.add(rs2.getString(1) + " " + rs2.getString(2));
                        }
                    }
                    if (subordinados.isEmpty()) {
                        System.out.println(" - No le reporta nadie");
                    } else {
                        System.out.println(" - Le reportan " + subordinados.size() + ":");
                        for (String s : subordinados) System.out.println("   · " + s);
                    }
                }
                if (!hay) System.out.println("No existe ningún empleado con el apellido " + apellido);
            }
        } catch (SQLException e) {
            System.out.println("Error de base de datos: " + e.getMessage());
        }
    }

    // ---------------- Ejercicio 2 ----------------
    public static void cambiarEmpleadoCliente(String cliente, int nuevoEmpleado) {
        try (Connection con = conectar()) {
            int numCliente;
            Integer antiguo;                                 // Integer: puede ser NULL en la BD
            try (PreparedStatement ps = con.prepareStatement(
                    "SELECT customerNumber, salesRepEmployeeNumber FROM customers WHERE customerName = ?")) {
                ps.setString(1, cliente);
                try (ResultSet rs = ps.executeQuery()) {
                    if (!rs.next()) {
                        System.out.println("El cliente " + cliente + " no existe");
                        return;
                    }
                    numCliente = rs.getInt(1);
                    antiguo = rs.getInt(2);
                    if (rs.wasNull()) antiguo = null;         // getInt devuelve 0 si es NULL: hay que preguntar
                }
            }

            String nuevo = nombreEmpleado(con, nuevoEmpleado);
            if (nuevo == null) {
                System.out.println("El código de empleado " + nuevoEmpleado + " no existe");
                return;
            }

            if (antiguo == null) {
                System.out.println("El cliente " + cliente + " no tenía asignado ningún empleado");
            } else {
                System.out.println("Empleado anterior de " + cliente + ": " + nombreEmpleado(con, antiguo));
            }

            try (PreparedStatement ps = con.prepareStatement(
                    "UPDATE customers SET salesRepEmployeeNumber = ? WHERE customerNumber = ?")) {
                ps.setInt(1, nuevoEmpleado);
                ps.setInt(2, numCliente);
                ps.executeUpdate();
            }
            System.out.println("Nuevo empleado de " + cliente + ": " + nuevo);
        } catch (SQLException e) {
            System.out.println("Error de base de datos: " + e.getMessage());
        }
    }

    // ---------------- Ejercicio 3 ----------------
    public static void productosConPocoStock(int maximo) {
        // LEFT JOIN: un producto que no está en ningún pedido sale con 0, no desaparece
        String sql = "SELECT p.productName, p.quantityInStock, COUNT(od.orderNumber) AS pedidos "
                   + "FROM products p LEFT JOIN orderdetails od ON p.productCode = od.productCode "
                   + "WHERE p.quantityInStock <= ? "
                   + "GROUP BY p.productCode, p.productName, p.quantityInStock "
                   + "ORDER BY p.quantityInStock";
        try (Connection con = conectar();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, maximo);
            try (ResultSet rs = ps.executeQuery()) {
                boolean hay = false;
                while (rs.next()) {
                    if (!hay) System.out.println("Productos con " + maximo + " unidades o menos:");
                    hay = true;
                    System.out.println(" - " + rs.getString("productName") + ": " + rs.getInt("quantityInStock")
                            + " unidades. Aparece en " + rs.getInt("pedidos") + " pedidos");
                }
                if (!hay) System.out.println("No hay ningún producto con stock inferior o igual a " + maximo + " unidades");
            }
        } catch (SQLException e) {
            System.out.println("Error de base de datos: " + e.getMessage());
        }
    }
}
