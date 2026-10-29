package xmljson;

import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.google.gson.GsonBuilder;

public class Ej02StockAJson {

    public static void main(String[] args) {
        stockAJson("stock.txt", "stock.json");
    }

    public static void stockAJson(String fTxt, String fJson) {
        List<Articulo> articulos = new ArrayList<>();
        try {
            List<String> l = Files.readAllLines(Path.of(fTxt), StandardCharsets.UTF_8);
            for (int i = 0; i + 2 < l.size(); i += 3) {          // 3 líneas por artículo
                articulos.add(new Articulo(l.get(i).trim(), l.get(i + 1).trim(),
                        Integer.parseInt(l.get(i + 2).trim())));
            }
        } catch (IOException | NumberFormatException e) {
            System.out.println("Error al leer " + fTxt + ": " + e.getMessage());
            return;
        }

        // Objeto raíz { "articulos": [...] } sin crear otra clase: un Map
        Map<String, Object> raiz = new LinkedHashMap<>();
        raiz.put("articulos", articulos);

        try (Writer w = new FileWriter(fJson, StandardCharsets.UTF_8)) {
            new GsonBuilder().setPrettyPrinting().create().toJson(raiz, w);
            System.out.println("Guardados " + articulos.size() + " artículos en " + fJson);
        } catch (IOException e) {
            System.out.println("Error al escribir " + fJson + ": " + e.getMessage());
        }
    }
}
