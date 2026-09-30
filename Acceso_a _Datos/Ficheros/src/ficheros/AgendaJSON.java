package ficheros;

import java.io.FileReader;
import java.io.Reader;
import java.util.List;

import com.google.gson.Gson;

public class AgendaJSON {

	public static void main(String[] args) {
		
		final String ruta = "agenda.json";
		leerAgenda(ruta);
		
	}
	
	public static List<Contacto> cargarListaContactos(String ruta){
		List<Contacto> contactos = null;
		
		
		return contactos;
	}
	
	
	public static void leerAgenda(String ruta) {
		
		try(Reader lector = new FileReader(ruta)){
			
			Gson gson = new Gson();
			Agenda agenda = gson.fromJson(lector, Agenda.class);
			List<Contacto> contactos = cargarListaContactos(ruta);
			
			for(Contacto c:contactos) {
				c.mostrar();
			}
			
				
			
		}catch (Exception e) {
			System.out.println("Error al leer el archivo");
		}
		
	}

}
