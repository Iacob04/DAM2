package boletin23;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Locale;

public class Ej05Alturas {

    public static void main(String[] args) {
        estadisticasAlturas("estadisticas.txt");
    }

    public static void estadisticasAlturas(String fichero) {
        int hombres = 0, mujeres = 0;
        double suma = 0;

        try (BufferedReader br = new BufferedReader(new FileReader(fichero, StandardCharsets.UTF_8))) {
            String sexo;
            while ((sexo = br.readLine()) != null) {      // registro de 2 líneas: sexo y altura
                double altura = Double.parseDouble(br.readLine().trim());
                if (sexo.trim().equals("Hombre")) hombres++;
                else mujeres++;
                suma += altura;
            }
        } catch (IOException e) {
            System.out.println("Error al leer " + fichero + ": " + e.getMessage());
            return;
        }

        int total = hombres + mujeres;
        System.out.println("Hombres: " + hombres + ".");
        System.out.println("Mujeres: " + mujeres + ".");
        if (total > 0)
            // Locale.US para que salga 1.75 y no 1,75 en un Eclipse en español
            System.out.println("Estatura media: " + String.format(Locale.US, "%.2f", suma / total));
    }
}
