package examenficheros;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Ej02PersonajesBinario {

    public static void main(String[] args) {
        List<Personaje> lista = crearPersonajes("animes.txt", "personajes.txt");
        grabarPersonajes(lista, "personajes.dat");
        mostrarPersonajes("personajes.dat");
    }

    // Reutiliza los métodos del ejercicio 1. Solo entran los que tienen anime y personaje.
    public static List<Personaje> crearPersonajes(String fAnimes, String fPersonajes) {
        List<Personaje> lista = new ArrayList<>();
        Map<Integer, String> animes = Ej01Animes.leerFichero(fAnimes);
        if (animes == null) return lista;
        for (Map.Entry<Integer, String> anime : animes.entrySet()) {
            for (String nombre : Ej01Animes.personajesDe(anime.getKey(), fPersonajes)) {
                lista.add(new Personaje(anime.getValue(), nombre));
            }
        }
        return lista;
    }

    public static void grabarPersonajes(List<Personaje> lista, String fichero) {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(fichero))) {
            oos.writeObject(lista);                 // la lista entera de una vez (ArrayList es Serializable)
        } catch (IOException e) {
            System.out.println("Error al grabar " + fichero + ": " + e.getMessage());
        }
    }

    @SuppressWarnings("unchecked")
    public static void mostrarPersonajes(String fichero) {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(fichero))) {
            List<Personaje> lista = (List<Personaje>) ois.readObject();
            for (Personaje p : lista) System.out.println(p);
        } catch (FileNotFoundException e) {
            System.out.println("No existe " + fichero);
        } catch (IOException | ClassNotFoundException e) {
            System.out.println("Error al leer " + fichero + ": " + e.getMessage());
        }
    }
}
