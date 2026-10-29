package xmljson;

import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.nio.charset.StandardCharsets;
import java.util.List;

import com.google.gson.GsonBuilder;

public class Ej04PokemonJSON {

    public static void main(String[] args) {
        List<Pokemon> lista = Ej03PokemonXML.leerTxt("pokemons.txt");   // misma lectura que el ej. 3
        if (lista != null) grabarJSON(lista, "pokemons.json");
    }

    public static void grabarJSON(List<Pokemon> lista, String fichero) {
        try (Writer w = new FileWriter(fichero, StandardCharsets.UTF_8)) {
            new GsonBuilder().setPrettyPrinting().create().toJson(lista, w);   // un array [ {...}, {...} ]
            System.out.println("Guardados " + lista.size() + " pokemon en " + fichero);
        } catch (IOException e) {
            System.out.println("Error al escribir " + fichero + ": " + e.getMessage());
        }
    }
}
