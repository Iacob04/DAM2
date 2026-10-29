package xmljson;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import ficheros.Agenda;      // las clases de la Agenda JSON de clase
import ficheros.Contacto;

public class Ej01XmlAJson {

    public static void main(String[] args) {
        xmlAJson("agenda.xml", "agenda_convertida.json");
    }

    public static void xmlAJson(String fXml, String fJson) {
        List<Contacto> contactos = new ArrayList<>();
        try {
            Document doc = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(new File(fXml));
            NodeList lista = doc.getElementsByTagName("contacto");
            for (int i = 0; i < lista.getLength(); i++) {
                Element contacto = (Element) lista.item(i);
                String nombre = contacto.getElementsByTagName("nombre").item(0).getTextContent();

                // En el XML puede haber uno o varios <telefono>; en el JSON es un array
                List<String> telefonos = new ArrayList<>();
                NodeList tels = contacto.getElementsByTagName("telefono");
                for (int j = 0; j < tels.getLength(); j++) telefonos.add(tels.item(j).getTextContent());

                contactos.add(new Contacto(nombre, "", telefonos));   // el XML no tiene DNI: en blanco
            }
        } catch (Exception e) {
            System.out.println("Error al leer " + fXml + ": " + e.getMessage());
            return;
        }

        Agenda agenda = new Agenda();
        agenda.setContactos(contactos);
        try (Writer w = new FileWriter(fJson, StandardCharsets.UTF_8)) {
            Gson gson = new GsonBuilder().setPrettyPrinting().create();
            gson.toJson(agenda, w);
            System.out.println("Convertidos " + contactos.size() + " contactos a " + fJson);
        } catch (IOException e) {
            System.out.println("Error al escribir " + fJson + ": " + e.getMessage());
        }
    }
}
