package ficheros;

import java.util.List;

public class Contacto {
	
	private String nombre;
	//private String teléfono;
	private List<String> telefono ;
	private String dni;
	
	/*public Contacto (String n, String d, String t) {
		this.nombre = n;
		this.teléfono = t;
		this.dni = d;
		
	}*/
	
	public Contacto (String n, String d, List<String> t) {
		this.nombre = n;
		this.telefono = t;
		this.dni = d;
		
	}
	
	public void setTelefono(List<String> telefono) {
		this.telefono = telefono;
	}
	
	
	@Override
	public String toString() {
		
		
		String entrada = "Nombre: "+ this.nombre + "\nTelefono: "+ this.telefono + "\nDNI: "+ 
		this.dni +"\nTeléfonos" ;
		
		for(String tlf: this.telefono) {
			entrada += "\n - " + tlf;
			entrada += "\n";
					
		}
		return entrada;
	}
	
	public String getNombre(Contacto nombre) {
		return this.nombre;
	}
	
	
}
