package ficheros;

import java.util.List;

import com.google.gson.annotations.SerializedName;


public class Agenda {
	
	@SerializedName("agenda")
	private List<Contacto> contactos;
	
	public List<Contacto> getContactos() {
		return contactos;
	}
	

}
