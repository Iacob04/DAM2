package examenficheros;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public class Ej01Animes {

    public static void main(String[] args) {
        mostrarAnimes("animes.txt", "personajes.txt");
    }

    public static void mostrarAnimes(String fAnimes, String fPersonajes) {
        Map<Integer, String> animes = leerFichero(fAnimes);   // TreeMap: sale ordenado por código (3, 4, 6, 17, 22)
        if (animes == null) return;

        for (Map.Entry<Integer, String> anime : animes.entrySet()) {
            System.out.println(anime.getValue());
            List<String> personajes = personajesDe(anime.getKey(), fPersonajes);
            if (personajes.isEmpty()) {
                System.out.println("- No hay personajes");
            } else {
                for (String p : personajes) System.out.println("- " + p);
            }
        }

        List<String> sinAnime = personajesSinAnime(animes, fPersonajes);
        if (!sinAnime.isEmpty()) {
            System.out.println("Personajes sin anime");
            for (String p : sinAnime) System.out.println("- " + p);
        }
    }

    // Lee "código texto" y devuelve un mapa código -> texto. Vale para los dos ficheros.
    public static Map<Integer, String> leerFichero(String fichero) {
        Map<Integer, String> mapa = new TreeMap<>();
        try (BufferedReader br = new BufferedReader(new FileReader(fichero, StandardCharsets.UTF_8))) {
            String linea;
            while ((linea = br.readLine()) != null) {
                if (linea.isBlank()) continue;
                String[] partes = linea.trim().split(" ", 2);       // 2 = solo corta en el primer espacio
                mapa.put(Integer.parseInt(partes[0]), partes[1].trim());
            }
            return mapa;
        } catch (IOException e) {
            System.out.println("Error al leer " + fichero + ": " + e.getMessage());
            return null;
        }
    }

    // Personajes con un código concreto (el fichero de personajes puede repetir código)
    public static List<String> personajesDe(int codigo, String fPersonajes) {
        List<String> lista = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(fPersonajes, StandardCharsets.UTF_8))) {
            String linea;
            while ((linea = br.readLine()) != null) {
                if (linea.isBlank()) continue;
                String[] partes = linea.trim().split(" ", 2);
                if (Integer.parseInt(partes[0]) == codigo) lista.add(partes[1].trim());
            }
        } catch (IOException e) {
            System.out.println("Error al leer " + fPersonajes + ": " + e.getMessage());
        }
        Collections.sort(lista);                    // en el ejemplo Roronoa sale antes que Vinsmoke
        return lista;
    }

    public static List<String> personajesSinAnime(Map<Integer, String> animes, String fPersonajes) {
        List<String> lista = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(fPersonajes, StandardCharsets.UTF_8))) {
            String linea;
            while ((linea = br.readLine()) != null) {
                if (linea.isBlank()) continue;
                String[] partes = linea.trim().split(" ", 2);
                if (!animes.containsKey(Integer.parseInt(partes[0]))) lista.add(partes[1].trim());
            }
        } catch (IOException e) {
            System.out.println("Error al leer " + fPersonajes + ": " + e.getMessage());
        }
        return lista;
    }
}
