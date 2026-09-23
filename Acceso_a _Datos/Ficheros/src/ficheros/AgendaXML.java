package ficheros;


import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.*;



public class AgendaXML {

	public static void main(String[] args) throws Exception{
		
		leerAgenda("agenda.xml");
		buscarEnAgenda("Alexandru", "agenda.xml");

	}
	
	public static void leerAgenda(String fichero) throws Exception {
		
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		DocumentBuilder builder = factory.newDocumentBuilder();
		Document doc = builder.parse(fichero);
		
		
		//creamos una lista iterable
		NodeList listaContactos = doc.getElementsByTagName("contacto");
		
		//recorremos la lista de contactos
		for(int i = 0; i<listaContactos.getLength(); i++) {
			//cojo el elemento i y los guardo en el objeto contacto
			Node nodo = listaContactos.item(i);
			Element contacto = (Element)nodo;
			
			String nombre = contacto.getElementsByTagName("nombre").item(0).getTextContent();
			String telefono = contacto.getElementsByTagName("telefono").item(0).getTextContent();
			System.out.println(nombre + " - "+ telefono);		
			
			
		}
		
	}
	
	public static void buscarEnAgenda(String nombreB, String fichero) throws Exception{
		
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		DocumentBuilder builder = factory.newDocumentBuilder();
		Document doc = builder.parse(fichero);
		
		
		//creamos una lista iterable
		NodeList listaContactos = doc.getElementsByTagName("contacto");
		boolean encontrado = false;
		//recorremos la lista de contactos
		for(int i = 0; i<listaContactos.getLength() && encontrado == false ; i++) {
			//cojo el elemento i y los guardo en el objeto contacto
			Node nodo = listaContactos.item(i);
			Element contacto = (Element)nodo;
			
			String nombre = contacto.getElementsByTagName("nombre").item(0).getTextContent();
			
			
			if (nombre.equalsIgnoreCase(nombreB)) {
				encontrado = true;
				String telefono = contacto.getElementsByTagName("telefono").item(0).getTextContent();
				System.out.println(nombre + " - "+ telefono);	
			}
		
		}
		
		if (encontrado == false) {
			System.out.println("El contacto "+ nombreB + " no está en tu agenda");
		}
		
	}

}
