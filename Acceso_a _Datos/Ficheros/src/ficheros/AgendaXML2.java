package ficheros;


import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

public class AgendaXML2 {

	public static void main(String[] args) {
		
		String fichero = "agenda.xml";
		
		try {
			Document doc = leerXML(fichero);
			NodeList listaContactos = doc.getElementsByTagName("contacto");
			System.out.println("Contactor: " + listaContactos.getLength());
			
			for(int i = 0; i<listaContactos.getLength(); i++) {
				
				Element contacto = (Element) listaContactos.item(i);
				String nombre = contacto.getElementsByTagName("nombre").item(0).getTextContent();
				System.out.println(nombre);
				
				NodeList listaTelefono = contacto.getElementsByTagName("telefono");
				
				for(int j = 0 ; j<listaTelefono.getLength(); j++) {
					
					Element telefono = (Element) listaTelefono.item(j);
					String tipo = telefono.getAttribute("tipo");
					String tel = telefono.getTextContent();
					System.out.println(" - "+ tel+ "(" + tipo + ")");
					
				}
				
			}
	
			
		}catch(Exception e) {
			System.out.println("Error");
		}
		

	}
	
	public static Document leerXML(String fichero) throws Exception {

		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		DocumentBuilder builder = factory.newDocumentBuilder();
		return builder.parse(fichero);
	}

}
