package boletin23;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;

public class Ej03ContarPalabras {

    public static void main(String[] args) {
        Scanner teclado = new Scanner(System.in);
        System.out.print("Introduce el nombre del fichero: ");
        String fichero = teclado.nextLine();
        int total = contarPalabras(fichero);
        if (total >= 0) System.out.println("El fichero tiene " + total + " palabras");
    }

    // Devuelve -1 si no se ha podido leer
    public static int contarPalabras(String fichero) {
        int palabras = 0;
        try (BufferedReader br = new BufferedReader(new FileReader(fichero, StandardCharsets.UTF_8))) {
            String linea;
            while ((linea = br.readLine()) != null) {
                linea = linea.trim();                  // sin espacios al principio y al final
                if (linea.isEmpty()) continue;         // "".split(...) daría 1 palabra
                palabras += linea.split(" +").length;  // " +" = uno o más espacios seguidos
            }
            return palabras;
        } catch (IOException e) {
            System.out.println("No se puede leer " + fichero + ": " + e.getMessage());
            return -1;
        }
    }
}
