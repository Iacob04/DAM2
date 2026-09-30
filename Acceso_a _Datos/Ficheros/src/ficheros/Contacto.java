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
	
	public  void mostrar() {
		System.out.println("Nombre: "+ this.nombre);
		System.out.println("Telefono: "+ this.teléfono);
		System.out.println("DNI: "+ this.dni);
		System.out.println();
	}

}
