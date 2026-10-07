package ficheros;

import java.io.FileReader;
import java.io.Reader;
import java.util.List;

import com.google.gson.Gson;

public class AgendaJSON2 {

	public static void main(String[] args) {
		
		
		String fichero = "agenda.json";
		
		try(Reader lector = new FileReader(fichero)){
			
			Gson gson = new Gson();
			Agenda agenda = gson.fromJson(lector, Agenda.class);
			List<Contacto> contactos = agenda.getContactos();
			System.out.println("Contactos: "+ contactos.size());
			
			for(Contacto c: contactos) {
				System.out.println(c);
			}
			
			
		}catch (Exception e) {
			System.out.println("Error"+ e.getMessage());
		}
		
	}

}
