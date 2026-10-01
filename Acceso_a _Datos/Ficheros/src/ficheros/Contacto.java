package ficheros;

public class Contacto {
	
	private String nombre;
	private String teléfono;
	private String dni;
	
	public Contacto (String n, String d, String t) {
		this.nombre = n;
		this.teléfono = t;
		this.dni = d;
		
	}
	@Override
	public String toString() {
		
		
		
		return "Nombre: "+ this.nombre + "\nTelefono: "+ this.teléfono + "\nDNI: "+ this.dni +"\n" ;
	}
	
	public String getNombre(Contacto nombre) {
		return this.nombre;
	}
	
	
}
