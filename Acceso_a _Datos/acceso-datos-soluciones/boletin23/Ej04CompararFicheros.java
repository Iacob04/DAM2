package boletin23;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

public class Ej04CompararFicheros {

    public static void main(String[] args) {
        if (compararFicheros("fichero1.txt", "fichero2.txt"))
            System.out.println("El contenido de los ficheros es el mismo");
        else
            System.out.println("El contenido de los ficheros no es el mismo");
    }

    public static boolean compararFicheros(String f1, String f2) {
        try (BufferedReader br1 = new BufferedReader(new FileReader(f1, StandardCharsets.UTF_8));
             BufferedReader br2 = new BufferedReader(new FileReader(f2, StandardCharsets.UTF_8))) {

            int c1, c2;
            do {                                    // carácter a carácter: también detecta saltos de línea distintos
                c1 = br1.read();
                c2 = br2.read();
                if (c1 != c2) return false;
            } while (c1 != -1);                     // los dos han llegado al final a la vez
            return true;

        } catch (IOException e) {
            System.out.println("Error al comparar: " + e.getMessage());
            return false;
        }
    }
}
