package boletin24;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class Ej04DarLaVuelta {

    public static void main(String[] args) {
        darLaVuelta("fichero1.txt", "fichero2.txt");
    }

    public static void darLaVuelta(String origen, String destino) {
        try {
            List<String> lineas = Files.readAllLines(Path.of(origen), StandardCharsets.UTF_8);
            List<String> alReves = new ArrayList<>();
            for (int i = lineas.size() - 1; i >= 0; i--) {                 // última línea primero
                alReves.add(new StringBuilder(lineas.get(i)).reverse().toString());   // y cada una invertida
            }
            Files.write(Path.of(destino), alReves, StandardCharsets.UTF_8);
            System.out.println("Escrito " + destino);
        } catch (IOException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }
}
