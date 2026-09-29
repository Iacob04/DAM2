package ficheros;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Boletin1_Ej1 {
	
	// Lee animes.txt y construye el diccionario número -> título
    static HashMap<Integer, String> leerDatosAnimes(String fich) throws Exception {
        HashMap<Integer, String> animes = new HashMap<>();
        try (BufferedReader br = new BufferedReader(new FileReader(fich))) {
            String linea;
            while ((linea = br.readLine()) != null) {
                int espacio = linea.indexOf(' ');
                int num = Integer.parseInt(linea.substring(0, espacio));
                String titulo = linea.substring(espacio + 1);
                animes.put(num, titulo);
            }
        }
        return animes;
    }

    // Dado un código, devuelve la lista de personajes con ese código
    static ArrayList<String> leerPersonajesAnime(int codigo, String fich) throws Exception {
        ArrayList<String> personajes = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(fich))) {
            String linea;
            while ((linea = br.readLine()) != null) {
                int espacio = linea.indexOf(' ');
                int num = Integer.parseInt(linea.substring(0, espacio));
                if (num == codigo)
                    personajes.add(linea.substring(espacio + 1));
            }
        }
        return personajes;
    }

    // Personajes cuyo código NO está en el diccionario de animes
    static ArrayList<String> leerPersonajesSinAnime(HashMap<Integer, String> animes, String fich) throws Exception {
        ArrayList<String> personajes = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(fich))) {
            String linea;
            while ((linea = br.readLine()) != null) {
                int espacio = linea.indexOf(' ');
                int num = Integer.parseInt(linea.substring(0, espacio));
                if (animes.containsKey(num) == false)
                    personajes.add(linea.substring(espacio + 1));
            }
        }
        return personajes;
    }

	public static void main(String[] args) {
		
		String fPersonajes = "/home/alumno/Escritorio/DAM2/Acceso_a _Datos/Ficheros/src/ficheros/personajes.txt";
		String fAnimes = "/home/alumno/Escritorio/DAM2/Acceso_a _Datos/Ficheros/src/ficheros/animes.txt";
		
		 try {
	            // 1. Diccionario: número -> título del anime
	            HashMap<Integer, String> animes = leerDatosAnimes(fAnimes);
	            // 2. Por cada anime, busca sus personajes
	            for (Map.Entry<Integer, String> anime : animes.entrySet()) {
	                ArrayList<String> personajes = leerPersonajesAnime(anime.getKey(), fPersonajes);
	                System.out.println(anime.getValue());
	                if (personajes.size() == 0)
	                    System.out.println("- No hay personajes");
	                else
	                    for (String personaje : personajes)
	                        System.out.println("- " + personaje);
	                System.out.println();
	            }
	            // 3. Personajes que no tienen anime
	            ArrayList<String> sinAnime = leerPersonajesSinAnime(animes, fPersonajes);
	            if (sinAnime.size() != 0) {
	                System.out.println("Personajes sin anime");
	                for (String personaje : sinAnime)
	                    System.out.println("- " + personaje);
	            }
	        } catch (Exception e) {
	            System.err.println("Error: " + e.getMessage());
	        }
	    }

	    
	}
