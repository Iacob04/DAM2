package jdbc;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

// Examen Conector J, ejercicios 4 y 5 (naciones)
public class Idiomas {

    static final String URL = "jdbc:mysql://localhost:3306/naciones";
    static final String USUARIO = "alumno";
    static final String PASSWORD = "abc123";

    public static void main(String[] args) {
        idiomasDe("Zimbabwe");
        idiomasDe("Zambia");
        idiomasDe("Samoa");
        idiomasDe("Vanuatu");
        idiomasDe("Latveria");
        System.out.println();
        hacerOficial("Zambia", "Bemba");
        hacerOficial("Vanuatu", "Bislama");
        hacerOficial("Latveria", "Bemba");
        hacerOficial("Zambia", "klingon");
        hacerOficial("Zambia", "Spanish");
        idiomasDe("Zambia");
    }

    static Connection conectar() throws SQLException {
        return DriverManager.getConnection(URL, USUARIO, PASSWORD);
    }

    // id a partir de un nombre, o -1 si no existe. Sirve para países e idiomas.
    static int buscarId(Connection con, String sql, String valor) throws SQLException {
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, valor);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() ? rs.getInt(1) : -1;
            }
        }
    }

    static int idPais(Connection con, String pais) throws SQLException {
        return buscarId(con, "SELECT country_id FROM countries WHERE name = ?", pais);
    }

    static int idIdioma(Connection con, String idioma) throws SQLException {
        return buscarId(con, "SELECT language_id FROM languages WHERE language = ?", idioma);
    }

    // ---------------- Ejercicio 4 ----------------
    public static void idiomasDe(String pais) {
        try (Connection con = conectar()) {
            int id = idPais(con, pais);
            if (id == -1) {
                System.out.println(pais + " no es un país real o no está dado de alta en la base de datos");
                return;
            }

            List<String> oficiales = new ArrayList<>();
            List<String> otros = new ArrayList<>();
            String sql = "SELECT l.language, cl.official FROM country_languages cl "
                       + "JOIN languages l ON cl.language_id = l.language_id WHERE cl.country_id = ?";
            try (PreparedStatement ps = con.prepareStatement(sql)) {
                ps.setInt(1, id);
                try (ResultSet rs = ps.executeQuery()) {
                    while (rs.next()) {
                        // official es 0/1 (tinyint): getBoolean lo convierte
                        if (rs.getBoolean("official")) oficiales.add(rs.getString("language"));
                        else otros.add(rs.getString("language"));
                    }
                }
            }

            System.out.println(oficiales.isEmpty() ? "No hay idioma oficial en " + pais
                                                   : "Idioma oficial en " + pais + ": " + String.join(", ", oficiales));
            System.out.println(otros.isEmpty() ? "No hay otros idiomas"
                                               : "Otros idiomas: " + String.join(", ", otros));
        } catch (SQLException e) {
            System.out.println("Error de base de datos: " + e.getMessage());
        }
    }

    // ---------------- Ejercicio 5 ----------------
    public static void hacerOficial(String pais, String idioma) {
        try (Connection con = conectar()) {
            int idP = idPais(con, pais);
            if (idP == -1) {
                System.out.println(pais + " no es un país real o no está dado de alta en la base de datos");
                return;
            }
            int idI = idIdioma(con, idioma);
            if (idI == -1) {
                System.out.println("El " + idioma + " no es un idioma real o no está dado de alta en la base de datos");
                return;
            }

            // ¿Se habla ese idioma en ese país? ¿Ya es oficial?
            Boolean oficial = null;                           // null = no se habla
            try (PreparedStatement ps = con.prepareStatement(
                    "SELECT official FROM country_languages WHERE country_id = ? AND language_id = ?")) {
                ps.setInt(1, idP);
                ps.setInt(2, idI);
                try (ResultSet rs = ps.executeQuery()) {
                    if (rs.next()) oficial = rs.getBoolean(1);
                }
            }

            if (oficial == null) {
                System.out.println("El idioma " + idioma + " no se habla en " + pais + " y no puedo hacerlo oficial");
            } else if (oficial) {
                System.out.println("El " + idioma + " ya es idioma oficial en " + pais);
            } else {
                try (PreparedStatement ps = con.prepareStatement(
                        "UPDATE country_languages SET official = 1 WHERE country_id = ? AND language_id = ?")) {
                    ps.setInt(1, idP);
                    ps.setInt(2, idI);
                    ps.executeUpdate();
                }
                System.out.println("El " + idioma + " es ahora idioma oficial en " + pais);
            }
        } catch (SQLException e) {
            System.out.println("Error de base de datos: " + e.getMessage());
        }
    }
}
