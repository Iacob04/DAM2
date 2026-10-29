package examen1;

import java.util.ArrayList;
import java.util.List;



public class Pokemon {
	
	private int id;
	private int nivel;
    private String nombre;
    private List<String> tipo; 
    
    public Pokemon() {              
    }
    
    public Pokemon(int id, int nivel, String nombre, List<String> tipo) {
        this.id = id;
        this.nivel = nivel;
        this.nombre = nombre;
        this.tipo = new ArrayList<>(tipo);
    }
    
    public int getNivel() { return nivel; }
    public String getNombre() { return nombre; }
    public int getId() { return id; }
    public List<String> getTipo() { return tipo; }

   @Override
    public String toString() {
	   
	   
        return "#"+id +" "+ nombre + " |" + String.join("/ ", tipo).toUpperCase() + "| " + "Nivel "+ nivel;
    }

}


