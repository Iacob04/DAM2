package ficheros;

import java.util.ArrayList;
import java.util.List;

public class Contacto {

    // Los nombres de los atributos tienen que coincidir con las claves del JSON
    private String nombre;
    private String dni;
    private List<String> telefono;   // en el JSON es un array: "telefono": ["...", "..."]

    public Contacto() {               // Gson lo usa para crear el objeto al leer
    }

    public Contacto(String nombre, String dni, List<String> telefono) {
        this.nombre = nombre;
        this.dni = dni;
        this.telefono = new ArrayList<>(telefono);   // copia modificable
    }

    public String getNombre() {
        return nombre;
    }

    public String getDni() {
        return dni;
    }

    public List<String> getTelefono() {
        return telefono;
    }

    public void setTelefono(List<String> telefono) {
        this.telefono = telefono;
    }

    @Override
    public String toString() {
        String entrada = "Nombre: " + nombre + "\nDNI: " + dni + "\nTeléfonos:";
        if (telefono == null || telefono.isEmpty()) {
            entrada += " (ninguno)";
        } else {
            for (String tlf : telefono) {
                entrada += "\n - " + tlf;
            }
        }
        return entrada;
    }
}