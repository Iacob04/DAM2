package ficheros;

import java.io.FileReader;
import java.io.Reader;
import java.util.List;

import com.google.gson.Gson;

public class AgendaJSON {

	public static void main(String[] args) {
		
		final String ruta = "agenda.json";
		leerAgenda(ruta);
		Contacto nuevo = new Contacto("Alexandru Iacob", "Y1225789H", "642335122");
		crearContacto (nuevo, ruta);
		
	}
	
	public static List<Contacto> cargarListaContactos(String ruta){
		List<Contacto> contactos = null;
		try(Reader lector = new FileReader(ruta)){
			Gson gson = new Gson();
			Agenda agenda = gson.fromJson(lector, Agenda.class);
			contactos = agenda.getContactos();
			
		}catch (Exception e) {
			System.out.println("Error: "+ e.getMessage());
		}
		
		return contactos;
	}
	
	
	public static void leerAgenda(String ruta) {
		
		try(Reader lector = new FileReader(ruta)){
			
			Gson gson = new Gson();
			Agenda agenda = gson.fromJson(lector, Agenda.class);
			List<Contacto> contactos = cargarListaContactos(ruta);
			
			for(Contacto c:contactos) {
				System.out.println(c);
			}
			
				
			
		}catch (Exception e) {
			System.out.println("Error al leer el archivo");
		}
		
	}
	
	public static void guardarAgenda(List<Contacto> contactos ,String ruta) {
		Agenda agenda = new Agenda();
		agenda.setContactos(contactos);
		
	}
	
	
	public static void crearContacto(Contacto nuevo, String ruta) {
		List<Contacto> contactos = cargarListaContactos(ruta);
		boolean encontrado = false;
		if(contactos != null) {
			for (Contacto c:contactos) {
				if(c.getNombre().equalsIgnoreCase(nuevo.getNombre())) {
					encontrado = true;
				}
				if (encontrado == false){
					contactos.add(nuevo);
					guardarAgenda(contactos, ruta);
				}
				else {
					System.out.println("Ya existe un contqacto con ese nombre");
				}
				
			}
		}
	}

}
