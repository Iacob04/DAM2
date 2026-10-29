package boletin23;

public class Persona {

    private String nombre;
    private String apellidos;   // puede ser null (ejercicio 8)
    private String tipo;        // Familia, Amigo o Conocido (ejercicio 8)
    private int edad;

    public Persona(String nombre, String apellidos, int edad) {
        this(nombre, apellidos, null, edad);
    }

    public Persona(String nombre, String apellidos, String tipo, int edad) {
        this.nombre = nombre;
        this.apellidos = apellidos;
        this.tipo = tipo;
        this.edad = edad;
    }

    public String getNombre() { return nombre; }
    public String getApellidos() { return apellidos; }
    public String getTipo() { return tipo; }
    public int getEdad() { return edad; }

    @Override
    public String toString() {
        String s = nombre;
        if (apellidos != null) s += " " + apellidos;
        s += " (" + edad + " años)";
        if (tipo != null) s += " - " + tipo;
        return s;
    }
}
