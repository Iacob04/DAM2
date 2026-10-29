package boletin24;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Ej09AnyadirEdad {

    public static void main(String[] args) {
        String fichero = "/home/josemaria/empleados.txt";
        anyadirEdades(fichero, new Scanner(System.in));
    }

    public static void anyadirEdades(String fichero, Scanner teclado) {
        List<String> lineas;
        try {
            lineas = Files.readAllLines(Path.of(fichero), StandardCharsets.UTF_8);   // todo a memoria...
        } catch (IOException e) {
            System.out.println("No se puede leer " + fichero + ": " + e.getMessage());
            return;
        }

        List<String> nuevas = new ArrayList<>();
        for (String linea : lineas) {
            String[] campos = linea.split(";");
            String[] nombre = campos[0].split(", ");            // [apellidos, nombre]
            int edad = pedirEdad(teclado, nombre[1] + " " + nombre[0]);
            nuevas.add(linea + ";" + edad);
        }

        try {
            Files.write(Path.of(fichero), nuevas, StandardCharsets.UTF_8);       // ...y después se sobrescribe
            System.out.println("Fichero actualizado");
        } catch (IOException e) {
            System.out.println("Error al escribir " + fichero + ": " + e.getMessage());
        }
    }

    static int pedirEdad(Scanner teclado, String persona) {
        while (true) {
            System.out.print(persona + ". ¿Cuál es su edad? ");
            try {
                int edad = Integer.parseInt(teclado.nextLine().trim());
                if (edad >= 18 && edad < 67) return edad;
            } catch (NumberFormatException e) {
                // cae al mensaje de abajo
            }
            System.out.println("La edad tiene que ser un entero entre 18 y 66");
        }
    }
}
