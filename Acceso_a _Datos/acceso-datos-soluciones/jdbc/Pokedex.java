package jdbc;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

// Examen Conector J, ejercicios 3 y 6 (pokemondb)
public class Pokedex {

    static final String URL = "jdbc:mysql://localhost:3306/pokemondb";
    static final String USUARIO = "alumno";
    static final String PASSWORD = "abc123";

    public static void main(String[] args) {
        evoluciones("Bulbasaur");
        evoluciones("Ivysaur");
        evoluciones("Venusaur");
        evoluciones("Mew");
        evoluciones("Eevee");
        evoluciones("Sylveon");
        System.out.println();
        pokemonPorTipo("Dragón");
        pokemonPorTipo("Profesor");
        pokemonPorTipo("Acero");
    }

    static Connection conectar() throws SQLException {
        return DriverManager.getConnection(URL, USUARIO, PASSWORD);
    }

    // Lista "Nombre (#n)" de lo que devuelva una consulta con un único ?
    static List<String> listaPokemon(Connection con, String sql, int numero) throws SQLException {
        List<String> lista = new ArrayList<>();
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, numero);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) lista.add(rs.getString("nombre") + " (#" + rs.getInt("numero_pokedex") + ")");
            }
        }
        return lista;
    }

    // ---------------- Ejercicio 3 ----------------
    public static void evoluciones(String nombre) {
        try (Connection con = conectar()) {
            int numero;
            String nombreBD;
            try (PreparedStatement ps = con.prepareStatement(
                    "SELECT numero_pokedex, nombre FROM pokemon WHERE nombre = ?")) {
                ps.setString(1, nombre);
                try (ResultSet rs = ps.executeQuery()) {
                    if (!rs.next()) {
                        System.out.println("El pokemon " + nombre + " no está en la pokedex o no es de la primera generación");
                        return;
                    }
                    numero = rs.getInt("numero_pokedex");
                    nombreBD = rs.getString("nombre");
                }
            }

            // En evoluciona_de, cada fila es: pokemon_evolucionado viene de pokemon_origen
            List<String> evolucionaEn = listaPokemon(con,
                    "SELECT p.numero_pokedex, p.nombre FROM evoluciona_de e "
                  + "JOIN pokemon p ON p.numero_pokedex = e.pokemon_evolucionado "
                  + "WHERE e.pokemon_origen = ? ORDER BY p.numero_pokedex", numero);
            List<String> evolucionaDe = listaPokemon(con,
                    "SELECT p.numero_pokedex, p.nombre FROM evoluciona_de e "
                  + "JOIN pokemon p ON p.numero_pokedex = e.pokemon_origen "
                  + "WHERE e.pokemon_evolucionado = ? ORDER BY p.numero_pokedex", numero);

            System.out.println(nombreBD + " (#" + numero + ")");
            System.out.println(evolucionaEn.isEmpty() ? "- no evoluciona en ningún otro"
                                                      : "- evoluciona en " + String.join(" ", evolucionaEn));
            System.out.println(evolucionaDe.isEmpty() ? "- no evoluciona de ningún otro"
                                                      : "- evoluciona de " + String.join(" ", evolucionaDe));
        } catch (SQLException e) {
            System.out.println("Error de base de datos: " + e.getMessage());
        }
    }

    // ---------------- Ejercicio 6 ----------------
    public static void pokemonPorTipo(String tipo) {
        try (Connection con = conectar()) {
            int idTipo;
            try (PreparedStatement ps = con.prepareStatement("SELECT id_tipo FROM tipo WHERE nombre = ?")) {
                ps.setString(1, tipo);
                try (ResultSet rs = ps.executeQuery()) {
                    if (!rs.next()) {
                        System.out.println(tipo + " no es un tipo reconocido en esta Pokedex");
                        return;
                    }
                    idTipo = rs.getInt(1);
                }
            }

            String sql = "SELECT p.numero_pokedex, p.nombre, p.peso, p.altura FROM pokemon p "
                       + "JOIN pokemon_tipo pt ON p.numero_pokedex = pt.numero_pokedex "
                       + "WHERE pt.id_tipo = ? ORDER BY p.numero_pokedex";
            try (PreparedStatement ps = con.prepareStatement(sql)) {
                ps.setInt(1, idTipo);
                try (ResultSet rs = ps.executeQuery()) {
                    boolean hay = false;
                    while (rs.next()) {
                        if (!hay) {                                  // cabecera solo si hay al menos uno
                            System.out.println("Los pokemon de tipo " + tipo + " son:");
                            hay = true;
                        }
                        int num = rs.getInt("numero_pokedex");
                        System.out.println(num + " – " + rs.getString("nombre"));
                        System.out.println("Peso: " + numero(rs.getDouble("peso")) + ". Altura: " + numero(rs.getDouble("altura")));
                        System.out.println("Tipos: " + String.join(", ", tiposDe(con, num)));
                    }
                    if (!hay) System.out.println("No existe ningún Pokemon de tipo " + tipo + " en esta Pokedex");
                }
            }
        } catch (SQLException e) {
            System.out.println("Error de base de datos: " + e.getMessage());
        }
    }

    // Todos los tipos de un pokemon (Dragonite -> Dragón, Volador)
    static List<String> tiposDe(Connection con, int numero) throws SQLException {
        List<String> tipos = new ArrayList<>();
        try (PreparedStatement ps = con.prepareStatement(
                "SELECT t.nombre FROM pokemon_tipo pt JOIN tipo t ON pt.id_tipo = t.id_tipo "
              + "WHERE pt.numero_pokedex = ? ORDER BY t.id_tipo")) {
            ps.setInt(1, numero);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) tipos.add(rs.getString(1));
            }
        }
        return tipos;
    }

    // 210.0 -> "210", 3.3 -> "3.3" (el enunciado muestra "Peso: 210")
    static String numero(double d) {
        return d == Math.floor(d) ? String.valueOf((long) d) : String.valueOf(d);
    }
}
