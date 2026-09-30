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

	public static void main(String[] args) throws Exception {

		leerAgenda("agenda.xml");
		buscarEnAgenda("Alexandru", "agenda.xml");
		nuevoContacto("Sergio", "654654654", "agenda.xml");

	}

	// ===================== MÉTODO REUTILIZABLE =====================
	// Devuelve el Element <contacto> cuyo nombre coincide, o null si no existe
	public static Element buscarContacto(Document doc, String nombreB) {

		NodeList listaContactos = doc.getElementsByTagName("contacto");

		for (int i = 0; i < listaContactos.getLength(); i++) {
			Element contacto = (Element) listaContactos.item(i);
			String nombre = contacto.getElementsByTagName("nombre").item(0).getTextContent();

			if (nombre.equalsIgnoreCase(nombreB)) {
				return contacto;
			}
		}
		return null;
	}

	// ===================== OPERACIONES =====================

	public static void leerAgenda(String fichero) throws Exception {

		Document doc = leerXML(fichero);
		NodeList listaContactos = doc.getElementsByTagName("contacto");

		for (int i = 0; i < listaContactos.getLength(); i++) {
			Element contacto = (Element) listaContactos.item(i);
			String nombre = contacto.getElementsByTagName("nombre").item(0).getTextContent();
			String telefono = contacto.getElementsByTagName("telefono").item(0).getTextContent();
			System.out.println(nombre + " - " + telefono);
		}
	}

	public static void buscarEnAgenda(String nombreB, String fichero) throws Exception {

		Document doc = leerXML(fichero);
		Element contacto = buscarContacto(doc, nombreB);

		if (contacto != null) {
			String nombre = contacto.getElementsByTagName("nombre").item(0).getTextContent();
			String telefono = contacto.getElementsByTagName("telefono").item(0).getTextContent();
			System.out.println(nombre + " - " + telefono);
		} else {
			System.out.println("El contacto " + nombreB + " no está en tu agenda");
		}
	}

	public static void eliminarContacto(String nombreB, String fichero) throws Exception {

		Document doc = leerXML(fichero);
		Element contacto = buscarContacto(doc, nombreB);

		if (contacto != null) {
			contacto.getParentNode().removeChild(contacto);
			grabarXML(doc, fichero);
			System.out.println("El contacto " + nombreB + " ha sido eliminado");
		} else {
			System.out.println("El contacto " + nombreB + " no está en tu agenda");
		}
	}

	public static void nuevoContacto(String nombreN, String numeroN, String fichero) throws Exception {

		Document doc = leerXML(fichero);

		if (buscarContacto(doc, nombreN) != null) {
			System.out.println("El contacto " + nombreN + " ya existe");
			return;
		}

		Element nuevoContacto = doc.createElement("contacto");
		Element elementoNombre = doc.createElement("nombre");
		Element elementoTelefono = doc.createElement("telefono");

		elementoNombre.setTextContent(nombreN);
		elementoTelefono.setTextContent(numeroN);

		nuevoContacto.appendChild(elementoNombre);
		nuevoContacto.appendChild(elementoTelefono);

		doc.getDocumentElement().appendChild(nuevoContacto);

		grabarXML(doc, fichero);
		System.out.println("Creado contacto: " + nombreN);
	}

	public static void modificarContacto(String nombreC, String nuevoTelefono, String fichero) throws Exception {

		Document doc = leerXML(fichero);
		Element contacto = buscarContacto(doc, nombreC);

		if (contacto != null) {
			Element telefono = (Element) contacto.getElementsByTagName("telefono").item(0);
			telefono.setTextContent(nuevoTelefono);
			grabarXML(doc, fichero);
			System.out.println("Teléfono modificado en el contacto " + nombreC);
		} else {
			System.out.println("El contacto " + nombreC + " no está en tu agenda por lo que no puede modificarse");
		}
	}

	// ===================== LECTURA / ESCRITURA =====================

	public static void grabarXML(Document doc, String fichero) throws Exception {

		TransformerFactory transformerFactory = TransformerFactory.newInstance();
		Transformer transformer = transformerFactory.newTransformer();
		transformer.setOutputProperty(OutputKeys.INDENT, "yes");
		transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "4");
		DOMSource source = new DOMSource(doc);
		StreamResult result = new StreamResult(fichero);
		transformer.transform(source, result);
	}

	public static Document leerXML(String fichero) throws Exception {

		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		DocumentBuilder builder = factory.newDocumentBuilder();
		return builder.parse(fichero);
	}
}
