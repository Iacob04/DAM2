package xmljson;

import java.util.ArrayList;
import java.util.List;

public class Pokemon {

    private int numero;
    private String nombre;
    private double peso;
    private double altura;
    private List<String> tipo;      // uno o dos tipos

    public Pokemon() {              // Gson lo necesita para leer
    }

    public Pokemon(int numero, String nombre, double peso, double altura, List<String> tipo) {
        this.numero = numero;
        this.nombre = nombre;
        this.peso = peso;
        this.altura = altura;
        this.tipo = new ArrayList<>(tipo);
    }

    public int getNumero() { return numero; }
    public String getNombre() { return nombre; }
    public double getPeso() { return peso; }
    public double getAltura() { return altura; }
    public List<String> getTipo() { return tipo; }

   /* @Override
    public String toString() {
        return numero + " - " + nombre + " (" + String.join(", ", tipo) + ")";
    }*/
}
