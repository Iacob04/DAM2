package examenficheros;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class Ej04QuitarMoneda {

    public static void main(String[] args) {
        quitarMoneda("paises.csv");
    }

    public static void quitarMoneda(String fichero) {
        List<String> lineas;
        try {
            lineas = Files.readAllLines(Path.of(fichero), StandardCharsets.UTF_8);
        } catch (IOException e) {
            System.out.println("Error al leer " + fichero + ": " + e.getMessage());
            return;
        }
        if (lineas.isEmpty()) {
            System.out.println("No hay datos de ningún país en el fichero");
            return;
        }

        List<String> salida = new ArrayList<>();
        for (int i = 0; i < lineas.size(); i++) {           // la cabecera también pierde "Moneda"
            String[] c = lineas.get(i).split(",", -1);
            if (c.length == 4) salida.add(c[0] + "," + c[1] + "," + c[3]);
        }

        // Primero se lee todo y después se escribe: si abro el writer antes, vacía el fichero
        try {
            Files.write(Path.of(fichero), salida, StandardCharsets.UTF_8);
            System.out.println("Fichero " + fichero + " reescrito sin la moneda");
        } catch (IOException e) {
            System.out.println("Error al escribir " + fichero + ": " + e.getMessage());
        }
    }
}
