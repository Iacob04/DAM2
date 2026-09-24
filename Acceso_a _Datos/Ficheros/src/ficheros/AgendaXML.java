package ficheros;


import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.*;



public class AgendaXML {

	public static void main(String[] args) throws Exception{
		
		leerAgenda("agenda.xml");
		buscarEnAgenda("Alexandru", "agenda.xml");
		nuevoContacto("Sergio", "654654654", "agenda.xml");

	}
	
	public static void leerAgenda(String fichero) throws Exception {
		
		Document doc = leerXML(fichero);
		
		
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
		
		Document doc = leerXML(fichero);
		
		
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
	
	public static void eliminarContacto(String nombreB, String fichero) throws Exception{
		
		Document doc = leerXML(fichero);
		
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
				Element raiz = doc.getDocumentElement();
				raiz.removeChild(contacto);
				System.out.println("El contacto "+ nombreB + " ha sido elminidao");
				
				grabarXML(doc,fichero);
				
			}
		
		}
		
		if (encontrado == false) {
			System.out.println("El contacto "+ nombreB + " no está en tu agenda");
		}
		
		
	}
	
	
	public static void nuevoContacto(String nombreN,String numeroN, String fichero) throws Exception{
		
		Document doc = leerXML(fichero);
		
		//creamos una lista iterable
		NodeList listaContactos = doc.getElementsByTagName("contacto");
		boolean encontrado = false;
		//recorremos la lista de contactos
		for(int i = 0; i<listaContactos.getLength() && encontrado == false ; i++) {
			//cojo el elemento i y los guardo en el objeto contacto
			Node nodo = listaContactos.item(i);
			Element contacto = (Element)nodo;
			
			String nombre = contacto.getElementsByTagName("nombre").item(0).getTextContent();
			
			
			if (nombre.equalsIgnoreCase(nombreN)) {
				encontrado = true;
				
				
			}
		
		}
		
		if (encontrado == true) {
			System.out.println("El contacto "+ nombreN + " ya existe");
		}
		else {
			Element nuevoContacto = doc.createElement("contacto");
			Element elementoNombre = doc.createElement("nombre");
			Element elementoTelefono = doc.createElement("telefono");
			
			elementoNombre.setTextContent(nombreN);
			elementoTelefono.setTextContent(numeroN);
			
			nuevoContacto.appendChild(elementoNombre);
			nuevoContacto.appendChild(elementoTelefono);
			
			Element raiz = doc.getDocumentElement();
			raiz.appendChild(nuevoContacto);
			
			grabarXML(doc, fichero);
			
			System.out.println("Creado contacto: "+ nombreN );
		}
		
	}	
	
	public static void modificarContacto(String nombreC, String nuevoTelefono, String fichero) throws Exception{
		
		Document doc = leerXML(fichero);
		
		
		//creamos una lista iterable
		NodeList listaContactos = doc.getElementsByTagName("contacto");
		boolean encontrado = false;
		//recorremos la lista de contactos
		for(int i = 0; i<listaContactos.getLength() && encontrado == false ; i++) {
			//cojo el elemento i y los guardo en el objeto contacto
			Node nodo = listaContactos.item(i);
			Element contacto = (Element)nodo;
			
			String nombre = contacto.getElementsByTagName("nombre").item(0).getTextContent();
			
			
			if (nombre.equalsIgnoreCase(nombreC)) {
				encontrado = true;
				
				Element telefono = (Element)contacto.getElementsByTagName("telefono").item(0);
				telefono.setTextContent(nuevoTelefono);
				
				grabarXML(doc,fichero);
				System.out.println("Telefono modoficado en el contacto "+ nombreC);
				
			}
		
		}
		
		if (encontrado == false) {
			System.out.println("El contacto "+ nombreC + " no está en tu agenda por lo que no puede modificarse");
		}
		
	}
	
	
	
	
	public static void grabarXML (Document doc, String fichero) throws Exception{
		
		TransformerFactory transformerFactory = TransformerFactory.newInstance();
		Transformer transformer = transformerFactory.newTransformer();
		transformer.setOutputProperty(OutputKeys.INDENT, "yes");
		transformer.setOutputProperty("{http://xml.apache.org/xslt}indet-amount", "4");
		DOMSource source = new DOMSource(doc);
		StreamResult result = new StreamResult(fichero);
		transformer.transform(source, result);
		
	}
	
	public static Document leerXML(String fichero) throws Exception{
		
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		DocumentBuilder builder = factory.newDocumentBuilder();
		
		return builder.parse(fichero);
	}

}
