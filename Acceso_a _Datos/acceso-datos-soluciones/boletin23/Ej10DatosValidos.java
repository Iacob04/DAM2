package boletin23;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Locale;

public class Ej10DatosValidos {

    public static void main(String[] args) {
        analizarDatos("datos.txt");
    }

    public static void analizarDatos(String fichero) {
        int validos = 0, invalidos = 0;
        double min = Double.MAX_VALUE, max = -Double.MAX_VALUE, suma = 0;

        try (BufferedReader br = new BufferedReader(new FileReader(fichero, StandardCharsets.UTF_8))) {
            String linea;
            while ((linea = br.readLine()) != null) {
                try {                                         // try DENTRO del bucle: una línea mala no corta la lectura
                    double n = Double.parseDouble(linea.trim());
                    validos++;
                    suma += n;
                    if (n < min) min = n;
                    if (n > max) max = n;
                } catch (NumberFormatException e) {
                    invalidos++;
                }
            }
        } catch (IOException e) {
            System.out.println("Error al leer " + fichero + ": " + e.getMessage());
            return;
        }

        // "#.###" = como mucho 3 decimales, sin ceros de relleno, y con punto
        DecimalFormat df = new DecimalFormat("#.###", DecimalFormatSymbols.getInstance(Locale.US));
        System.out.println("Número de datos válidos: " + validos);
        System.out.println("Número de datos inválidos: " + invalidos);
        if (validos > 0) {
            System.out.println("Mínimo: " + df.format(min));
            System.out.println("Máximo: " + df.format(max));
            System.out.println("Media aritmética: " + df.format(suma / validos));
        }
    }
}
