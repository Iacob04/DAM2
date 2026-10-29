package boletin23;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;

public class Ej01ContarPalabra {

    public static void main(String[] args) {
        Scanner teclado = new Scanner(System.in);

        // Se repite hasta que el fichero exista
        String fichero;
        while (true) {
            System.out.print("Introduce el nombre del fichero: ");
            fichero = teclado.nextLine();
            if (new File(fichero).isFile()) break;
            System.out.println("El fichero " + fichero + " no existe");
        }
        System.out.print("Introduce la palabra a buscar: ");
        String palabra = teclado.nextLine().trim();

        contarLineasYPalabra(fichero, palabra);
    }

    public static void contarLineasYPalabra(String fichero, String palabra) {
        int lineas = 0;
        int veces = 0;
        try (BufferedReader br = new BufferedReader(new FileReader(fichero, StandardCharsets.UTF_8))) {
            String linea;
            while ((linea = br.readLine()) != null) {
                lineas++;
                // Trocea por todo lo que no sea letra o número: "pera," cuenta como "pera"
                for (String p : linea.split("[^\\p{L}\\p{N}]+")) {
                    if (p.equalsIgnoreCase(palabra)) veces++;
                }
            }
            System.out.println("El fichero tiene " + lineas + " líneas");
            System.out.println("La palabra " + palabra + " aparece " + veces + " veces");
        } catch (IOException e) {
            System.out.println("Error al leer el fichero: " + e.getMessage());
        }
    }
}
