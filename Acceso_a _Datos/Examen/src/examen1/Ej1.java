package examen1;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

public class Ej1 {

	public static void main(String[] args) {
		
		
		escribirTxt(leerXml("pokedex.xml"), "pokedex.txt");

	}
	
	
		public static List<Pokemon> leerXml(String fichero) {
		
		List<Pokemon> lista = new ArrayList<>();
		try {
		Document doc = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(new File(fichero));
		NodeList nodos = doc.getElementsByTagName("pokemon");
		
		for (int i = 0; i < nodos.getLength(); i++) {
			
		    Element e = (Element) nodos.item(i);
		    int id = Integer.parseInt(e.getAttribute("id")); 
		    int nivel = Integer.parseInt(e.getAttribute("nivel"));
		    String nombre = texto(e, "nombre");                        
		    
		    List<String> tipos = new ArrayList<>();                    
		    NodeList ts = e.getElementsByTagName("tipo");
		    
		    
		    for (int j = 0; j < ts.getLength(); j++) tipos.add(ts.item(j).getTextContent().trim().toUpperCase());
		    lista.add(new Pokemon(id, nivel, nombre, tipos));
		}
		/*System.out.println("=== POKEDEX DEL PROFESOR OAK ===");
		
		for (Pokemon p : lista) {
			
		System.out.println(p);
		}*/
		System.out.println("Pokemon procesados " + lista.size() );
		return lista;
		
		
		
		} catch (Exception e) {
			
			System.out.println("Error al leer " + fichero + ": " + e.getMessage());
			return null;
		
			}

		
		}
		
		
		static String texto(Element padre, String etiqueta) {
		NodeList l = padre.getElementsByTagName(etiqueta);
		
		return l.getLength() == 0 ? "" : l.item(0).getTextContent().trim();
		}
		
		
		public static void escribirTxt(List<Pokemon> lista, String fichero) {
			
		    List<String> lineas = new ArrayList<>();
		    
		    
		    
		    
		    for (Pokemon p : lista) {
		        lineas.add(p.getId() + " " + p.getNombre() + " |"   + String.join("/ ", p.getTipo() + "| "+ " Nivel "+ p.getNivel()));
		    }
		    try {
		    	
		        Files.write(Path.of(fichero), lineas, StandardCharsets.UTF_8); 
		     
		        //System.out.println("=== Total : " + lista.size() + " pokemon");
		        
		    } catch (IOException e) {
		        System.out.println("Error al escribir " + fichero + ": " + e.getMessage());
		    }
		    
		    
		}
		
		

}
