package examen1;

import java.io.FileReader;
import java.nio.charset.StandardCharsets;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;

import com.google.gson.Gson;



public class Ej2 {

	public static void main(String[] args) {
		
		
		

	}
	
	
	public static List<Marvel> leerJson(String fichero) {
	    try (FileReader r = new FileReader(fichero, StandardCharsets.UTF_8)) {
	        Marvel ucm = new Gson().fromJson(r, Marvel.class);
	        if (ucm == null || ucm.getPeliculas() == null) return new ArrayList<>();
	        return ucm.getPeliculas();
	    } catch (Exception e) {
	        System.out.println("Error al leer " + fichero + ": " + e.getMessage());
	        return null;
	    }
	}
	
	public class Conexion {

	    
	    static final String URL = "jdbc:mysql://localhost:3306/peliculas";
	    static final String USUARIO = "alumno";
	    static final String PASSWORD = "abc123";

	    
	    
	}

}
