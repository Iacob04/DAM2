package examen;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.nio.charset.StandardCharsets;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

// SOLUCIÓN: exportar los pokemon de un tipo de pokemondb a JSON y a XML, y leerlos de vuelta.
public class ExportarPokemon {

    static final String URL = "jdbc:mysql://localhost:3306/pokemondb";
    static final String USUARIO = "alumno";
    static final String PASSWORD = "abc123";

    // ---------- Clases modelo: los nombres de los atributos SON las claves del JSON ----------
    static class Estadisticas {
        int ps, ataque, defensa, especial, velocidad;

        int total() {
            return ps + ataque + defensa + especial + velocidad;
        }
    }

    static class PokemonExp {
        int numero;
        String nombre;
        double peso;
        double altura;
        List<String> tipos = new ArrayList<>();
        Estadisticas estadisticas;                    // null si no tiene fila (Doduo)
        List<String> evolucionaA = new ArrayList<>();
    }

    static class Exportacion {                        // raíz del JSON
        String tipo;
        int total;
        List<PokemonExp> pokemon = new ArrayList<>();
    }

    public static void main(String[] args) {
        exportarJSON("Fuego", "fuego.json");
        exportarXML("Fuego", "fuego.xml");
        exportarJSON("Volador", "volador.json");
        exportarXML("Volador", "volador.xml");
        exportarJSON("Luz", "luz.json");
        System.out.println();
        resumenJSON("fuego.json");
        System.out.println();
        resumenXML("volador.xml");
        System.out.println();
        resumenXML("noexiste.xml");
    }

    // ================= Lectura de la BD (común a a y b) =================
    // Devuelve null si el tipo no existe o hay error: así no se crea un fichero vacío.
    static Exportacion cargar(String tipo) {
        try (Connection con = DriverManager.getConnection(URL, USUARIO, PASSWORD)) {
            int idTipo;
            try (PreparedStatement ps = con.prepareStatement("SELECT id_tipo, nombre FROM tipo WHERE nombre = ?")) {
                ps.setString(1, tipo);
                try (ResultSet rs = ps.executeQuery()) {
                    if (!rs.next()) {
                        System.out.println("El tipo " + tipo + " no existe");
                        return null;
                    }
                    idTipo = rs.getInt(1);
                    tipo = rs.getString(2);           // nombre tal cual está en la BD
                }
            }
            Exportacion exp = new Exportacion();
            exp.tipo = tipo;

            String sqlPokemon = "SELECT p.numero_pokedex, p.nombre, p.peso, p.altura FROM pokemon p "
                              + "JOIN pokemon_tipo pt ON pt.numero_pokedex = p.numero_pokedex "
                              + "WHERE pt.id_tipo = ? ORDER BY p.numero_pokedex";
            String sqlTipos = "SELECT t.nombre FROM pokemon_tipo pt JOIN tipo t ON t.id_tipo = pt.id_tipo "
                            + "WHERE pt.numero_pokedex = ? ORDER BY t.nombre";
            String sqlStats = "SELECT ps, ataque, defensa, especial, velocidad FROM estadisticas_base WHERE numero_pokedex = ?";
            String sqlEvo = "SELECT d.nombre FROM evoluciona_de e JOIN pokemon d ON d.numero_pokedex = e.pokemon_evolucionado "
                          + "WHERE e.pokemon_origen = ? ORDER BY d.numero_pokedex";

            try (PreparedStatement psP = con.prepareStatement(sqlPokemon);
                 PreparedStatement psT = con.prepareStatement(sqlTipos);
                 PreparedStatement psS = con.prepareStatement(sqlStats);
                 PreparedStatement psE = con.prepareStatement(sqlEvo)) {
                psP.setInt(1, idTipo);
                try (ResultSet rs = psP.executeQuery()) {
                    while (rs.next()) {
                        PokemonExp p = new PokemonExp();
                        p.numero = rs.getInt("numero_pokedex");
                        p.nombre = rs.getString("nombre");
                        p.peso = rs.getDouble("peso");
                        p.altura = rs.getDouble("altura");
                        // las otras 3 consultas reutilizan el PreparedStatement: solo cambia el ?
                        psT.setInt(1, p.numero);
                        try (ResultSet r = psT.executeQuery()) {
                            while (r.next()) p.tipos.add(r.getString(1));
                        }
                        psS.setInt(1, p.numero);
                        try (ResultSet r = psS.executeQuery()) {
                            if (r.next()) {
                                Estadisticas s = new Estadisticas();
                                s.ps = r.getInt("ps");
                                s.ataque = r.getInt("ataque");
                                s.defensa = r.getInt("defensa");
                                s.especial = r.getInt("especial");
                                s.velocidad = r.getInt("velocidad");
                                p.estadisticas = s;
                            }
                        }
                        psE.setInt(1, p.numero);
                        try (ResultSet r = psE.executeQuery()) {
                            while (r.next()) p.evolucionaA.add(r.getString(1));
                        }
                        exp.pokemon.add(p);
                    }
                }
            }
            exp.total = exp.pokemon.size();
            return exp;
        } catch (SQLException e) {
            System.out.println("Error de base de datos: " + e.getMessage());
            return null;
        }
    }

    // ================= a) BD -> JSON =================
    public static void exportarJSON(String tipo, String fichero) {
        Exportacion exp = cargar(tipo);
        if (exp == null) return;
        Gson gson = new GsonBuilder().setPrettyPrinting().serializeNulls().create();   // serializeNulls: "estadisticas": null
        try (Writer w = new FileWriter(fichero, StandardCharsets.UTF_8)) {
            gson.toJson(exp, w);
            System.out.println("Exportados " + exp.total + " pokemon de tipo " + exp.tipo + " a " + fichero);
        } catch (IOException e) {
            System.out.println("Error al escribir " + fichero + ": " + e.getMessage());
        }
    }

    // ================= b) BD -> XML =================
    public static void exportarXML(String tipo, String fichero) {
        Exportacion exp = cargar(tipo);
        if (exp == null) return;
        try {
            Document doc = DocumentBuilderFactory.newInstance().newDocumentBuilder().newDocument();
            Element raiz = doc.createElement("pokedex");
            raiz.setAttribute("tipo", exp.tipo);
            raiz.setAttribute("total", String.valueOf(exp.total));
            doc.appendChild(raiz);
            for (PokemonExp p : exp.pokemon) {
                Element e = doc.createElement("pokemon");
                e.setAttribute("numero", String.valueOf(p.numero));
                hijo(doc, e, "nombre", p.nombre);
                hijo(doc, e, "peso", String.valueOf(p.peso));
                hijo(doc, e, "altura", String.valueOf(p.altura));
                Element tipos = doc.createElement("tipos");
                for (String t : p.tipos) hijo(doc, tipos, "tipo", t);
                e.appendChild(tipos);
                if (p.estadisticas != null) {                     // sin estadísticas: no se pone la etiqueta
                    Element s = doc.createElement("estadisticas");
                    s.setAttribute("ps", String.valueOf(p.estadisticas.ps));
                    s.setAttribute("ataque", String.valueOf(p.estadisticas.ataque));
                    s.setAttribute("defensa", String.valueOf(p.estadisticas.defensa));
                    s.setAttribute("especial", String.valueOf(p.estadisticas.especial));
                    s.setAttribute("velocidad", String.valueOf(p.estadisticas.velocidad));
                    e.appendChild(s);
                }
                Element evo = doc.createElement("evolucionaA");
                for (String d : p.evolucionaA) hijo(doc, evo, "pokemon", d);
                e.appendChild(evo);                               // vacía si no evoluciona: <evolucionaA/>
                raiz.appendChild(e);
            }
            Transformer t = TransformerFactory.newInstance().newTransformer();
            t.setOutputProperty(OutputKeys.INDENT, "yes");
            t.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
            t.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "4");
            t.transform(new DOMSource(doc), new StreamResult(new File(fichero)));
            System.out.println("Exportados " + exp.total + " pokemon de tipo " + exp.tipo + " a " + fichero);
        } catch (Exception e) {
            System.out.println("Error al escribir " + fichero + ": " + e.getMessage());
        }
    }

    static void hijo(Document doc, Element padre, String etiqueta, String texto) {
        Element e = doc.createElement(etiqueta);
        e.setTextContent(texto);
        padre.appendChild(e);
    }

    // ================= c) leer el JSON =================
    // El más fuerte = mayor suma de estadísticas. Los que no tienen estadísticas no cuentan.
    public static void resumenJSON(String fichero) {
        Exportacion exp;
        try (Reader r = new FileReader(fichero, StandardCharsets.UTF_8)) {
            exp = new Gson().fromJson(r, Exportacion.class);    // misma clase que al escribir
        } catch (IOException e) {
            System.out.println("No se puede leer " + fichero + ": " + e.getMessage());
            return;
        }
        System.out.println("== " + fichero + ": " + exp.total + " pokemon de tipo " + exp.tipo + " ==");
        PokemonExp mejor = null;
        int sinEvo = 0;
        for (PokemonExp p : exp.pokemon) {
            if (p.evolucionaA.isEmpty()) sinEvo++;
            if (p.estadisticas != null && (mejor == null || p.estadisticas.total() > mejor.estadisticas.total()))
                mejor = p;
        }
        if (mejor != null)
            System.out.println("Más fuerte: " + mejor.nombre + " (" + mejor.estadisticas.total() + " puntos)");
        System.out.println("No evolucionan: " + sinEvo);
    }

    // ================= c) leer el XML =================
    public static void resumenXML(String fichero) {
        File f = new File(fichero);
        if (!f.exists()) {
            System.out.println("No existe el fichero " + fichero);
            return;
        }
        try {
            Document doc = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(f);
            Element raiz = doc.getDocumentElement();
            System.out.println("== " + fichero + ": " + raiz.getAttribute("total") + " pokemon de tipo " + raiz.getAttribute("tipo") + " ==");
            NodeList lista = raiz.getElementsByTagName("pokemon");
            int conStats = 0, sumaAtaque = 0;
            for (int i = 0; i < lista.getLength(); i++) {
                Element p = (Element) lista.item(i);
                if (!p.hasAttribute("numero")) continue;          // <pokemon> de dentro de <evolucionaA>: no es un registro
                String nombre = p.getElementsByTagName("nombre").item(0).getTextContent();
                NodeList tipos = p.getElementsByTagName("tipo");
                List<String> lt = new ArrayList<>();
                for (int j = 0; j < tipos.getLength(); j++) lt.add(tipos.item(j).getTextContent());
                NodeList st = p.getElementsByTagName("estadisticas");
                String ataque = "sin estadísticas";
                if (st.getLength() > 0) {
                    int a = Integer.parseInt(((Element) st.item(0)).getAttribute("ataque"));
                    ataque = "ataque " + a;
                    sumaAtaque += a;
                    conStats++;
                }
                System.out.printf("#%-3s %-11s %-18s %s%n", p.getAttribute("numero"), nombre, String.join("/", lt), ataque);
            }
            if (conStats > 0)
                System.out.printf("Ataque medio: %.2f (de %d con estadísticas)%n", (double) sumaAtaque / conStats, conStats);
        } catch (Exception e) {
            System.out.println("XML incorrecto en " + fichero + ": " + e.getMessage());
        }
    }
}
