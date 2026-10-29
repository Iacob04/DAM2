package boletin23;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

public class Ej02Estadisticas {

    public static void main(String[] args) {
        estadisticas("fichero1.txt");
    }

    public static void estadisticas(String fichero) {
        int lineas = 0, enBlanco = 0, caracteres = 0, espacios = 0;

        try (BufferedReader br = new BufferedReader(new FileReader(fichero, StandardCharsets.UTF_8))) {
            String linea;
            while ((linea = br.readLine()) != null) {
                lineas++;
                if (linea.trim().isEmpty()) enBlanco++;
                for (char c : linea.toCharArray()) {
                    if (c == ' ') espacios++;
                    else caracteres++;
                }
            }
        } catch (IOException e) {
            System.out.println("Error al leer " + fichero + ": " + e.getMessage());
            return;
        }

        System.out.println("Número de líneas: " + lineas);
        System.out.println("Líneas en blanco: " + enBlanco);
        System.out.println("Cantidad de caracteres sin contar los espacios: " + caracteres);
        System.out.println("Cantidad de espacios: " + espacios);
    }
}
