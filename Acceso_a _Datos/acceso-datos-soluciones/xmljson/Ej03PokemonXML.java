package xmljson;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

public class Ej03PokemonXML {

    public static void main(String[] args) {
        List<Pokemon> lista = leerTxt("pokemons.txt");
        if (lista != null) grabarXML(lista, "pokemons.xml");
    }

    // "152, Chikorita, 6.4, 0.9, Planta[, Tipo2]"  -> se reutiliza en el ejercicio 4
    public static List<Pokemon> leerTxt(String fichero) {
        List<Pokemon> lista = new ArrayList<>();
        try {
            for (String linea : Files.readAllLines(Path.of(fichero), StandardCharsets.UTF_8)) {
                if (linea.isBlank()) continue;
                String[] c = linea.split(",");
                for (int i = 0; i < c.length; i++) c[i] = c[i].trim();
                List<String> tipos = Arrays.asList(c).subList(4, c.length);   // del 5º campo al final
                lista.add(new Pokemon(Integer.parseInt(c[0]), c[1],
                        Double.parseDouble(c[2]), Double.parseDouble(c[3]), tipos));
            }
            return lista;
        } catch (IOException | RuntimeException e) {
            System.out.println("Error al leer " + fichero + ": " + e.getMessage());
            return null;
        }
    }

    public static void grabarXML(List<Pokemon> lista, String fichero) {
        try {
            Document doc = DocumentBuilderFactory.newInstance().newDocumentBuilder().newDocument();
            Element raiz = doc.createElement("pokedex");
            doc.appendChild(raiz);
            for (Pokemon p : lista) raiz.appendChild(crearNodo(doc, p));
            guardar(doc, fichero);
            System.out.println("Guardados " + lista.size() + " pokemon en " + fichero);
        } catch (Exception e) {
            System.out.println("Error al crear el XML: " + e.getMessage());
        }
    }

    // <pokemon><numero>..</numero>...<tipo>Bicho, Veneno</tipo></pokemon>  (también lo usa el ej. 5)
    public static Element crearNodo(Document doc, Pokemon p) {
        Element pk = doc.createElement("pokemon");
        anyadir(doc, pk, "numero", String.valueOf(p.getNumero()));
        anyadir(doc, pk, "nombre", p.getNombre());
        anyadir(doc, pk, "peso", String.valueOf(p.getPeso()));
        anyadir(doc, pk, "altura", String.valueOf(p.getAltura()));
        anyadir(doc, pk, "tipo", String.join(", ", p.getTipo()));   // los dos tipos en la misma etiqueta
        return pk;
    }

    static void anyadir(Document doc, Element padre, String etiqueta, String texto) {
        Element e = doc.createElement(etiqueta);
        e.setTextContent(texto);
        padre.appendChild(e);
    }

    public static void guardar(Document doc, String fichero) throws Exception {
        Transformer t = TransformerFactory.newInstance().newTransformer();
        t.setOutputProperty(OutputKeys.INDENT, "yes");
        t.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
        t.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "4");
        t.transform(new DOMSource(doc), new StreamResult(new File(fichero)));
    }
}
