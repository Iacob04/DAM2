package boletin24;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;

public class Ej01TablaMultiplicar {

    public static void main(String[] args) {
        int n = pedirNumero(new Scanner(System.in));
        grabarTabla(n);
    }

    // Reutilizado en el ejercicio 2
    public static int pedirNumero(Scanner teclado) {
        while (true) {
            System.out.print("Introduce un número entero entre 1 y 10: ");
            try {
                int n = Integer.parseInt(teclado.nextLine().trim());
                if (n >= 1 && n <= 10) return n;
            } catch (NumberFormatException e) {
                // cae al mensaje de abajo
            }
            System.out.println("Tiene que ser un entero entre 1 y 10");
        }
    }

    public static void grabarTabla(int n) {
        String fichero = "tabla-" + n + ".txt";
        try (PrintWriter pw = new PrintWriter(new FileWriter(fichero, StandardCharsets.UTF_8))) {
            for (int i = 1; i <= 10; i++) {
                // %2d y %3d rellenan con espacios a la izquierda: x, = y resultados quedan en columna
                pw.printf("%d x %2d = %3d%n", n, i, n * i);
            }
            System.out.println("Tabla del " + n + " guardada en " + fichero);
        } catch (IOException e) {
            System.out.println("Error al escribir " + fichero + ": " + e.getMessage());
        }
    }
}
