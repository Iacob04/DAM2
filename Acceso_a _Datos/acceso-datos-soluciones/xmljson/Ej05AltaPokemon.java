package xmljson;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

public class Ej05AltaPokemon {

    public static void main(String[] args) {
        altaPokemon(25, "Pikachu", 6.0, 0.4, "Eléctrico", "pokemons.xml");
        altaPokemon(16, "Pidgey", 1.8, 0.3, "Normal", "Volador", "pokemons.json");
        altaPokemon(25, "Pikachu", 6.0, 0.4, "Eléctrico", "pokemons.xml");     // repetido
        altaPokemon(1, "Bulbasaur", 6.9, 0.7, "Planta", "pokemons.csv");        // formato no válido
    }

    // ---- Sobrecarga: un tipo o dos tipos ----
    public static void altaPokemon(int numero, String nombre, double peso, double altura,
                                   String tipo, String fichero) {
        alta(new Pokemon(numero, nombre, peso, altura, List.of(tipo)), fichero);
    }

    public static void altaPokemon(int numero, String nombre, double peso, double altura,
                                   String tipo1, String tipo2, String fichero) {
        alta(new Pokemon(numero, nombre, peso, altura, List.of(tipo1, tipo2)), fichero);
    }

    // La extensión del fichero decide la estructura
    private static void alta(Pokemon p, String fichero) {
        if (fichero.toLowerCase().endsWith(".xml")) altaXML(p, fichero);
        else if (fichero.toLowerCase().endsWith(".json")) altaJSON(p, fichero);
        else System.out.println("Formato no soportado: " + fichero + " (usa .xml o .json)");
    }

    private static void altaXML(Pokemon p, String fichero) {
        try {
            Document doc;
            File f = new File(fichero);
            if (f.exists()) {
                doc = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(f);
                limpiarEspacios(doc.getDocumentElement());
            } else {
                doc = DocumentBuilderFactory.newInstance().newDocumentBuilder().newDocument();
                doc.appendChild(doc.createElement("pokedex"));
            }

            NodeList numeros = doc.getElementsByTagName("numero");
            for (int i = 0; i < numeros.getLength(); i++) {
                if (numeros.item(i).getTextContent().trim().equals(String.valueOf(p.getNumero()))) {
                    System.out.println("Ya existe un pokemon con el número " + p.getNumero() + " en " + fichero);
                    return;
                }
            }
            doc.getDocumentElement().appendChild(Ej03PokemonXML.crearNodo(doc, p));   // reutilizo el ej. 3
            Ej03PokemonXML.guardar(doc, fichero);
            System.out.println("Alta de " + p.getNombre() + " en " + fichero);
        } catch (Exception e) {
            System.out.println("Error con " + fichero + ": " + e.getMessage());
        }
    }

    private static void altaJSON(Pokemon p, String fichero) {
        List<Pokemon> lista = new ArrayList<>();
        if (new File(fichero).exists()) {
            try (Reader r = new FileReader(fichero, StandardCharsets.UTF_8)) {
                Type tipoLista = new TypeToken<List<Pokemon>>() {}.getType();   // List<Pokemon> para Gson
                List<Pokemon> leida = new Gson().fromJson(r, tipoLista);
                if (leida != null) lista = leida;
            } catch (IOException | RuntimeException e) {
                System.out.println("Error al leer " + fichero + ": " + e.getMessage());
                return;                                     // no machaco un fichero que no he podido leer
            }
        }
        for (Pokemon x : lista) {
            if (x.getNumero() == p.getNumero()) {
                System.out.println("Ya existe un pokemon con el número " + p.getNumero() + " en " + fichero);
                return;
            }
        }
        lista.add(p);
        Ej04PokemonJSON.grabarJSON(lista, fichero);       // reutilizo el ej. 4
    }

    // Sin esto, cada alta añade líneas en blanco al XML (ver Agenda XML)
    private static void limpiarEspacios(Node nodo) {
        NodeList hijos = nodo.getChildNodes();
        for (int i = hijos.getLength() - 1; i >= 0; i--) {
            Node h = hijos.item(i);
            if (h.getNodeType() == Node.TEXT_NODE && h.getTextContent().trim().isEmpty()) nodo.removeChild(h);
            else if (h.getNodeType() == Node.ELEMENT_NODE) limpiarEspacios(h);
        }
    }
}
