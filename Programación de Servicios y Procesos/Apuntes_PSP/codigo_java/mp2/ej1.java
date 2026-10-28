package Ejercicios;

import java.io.IOException;
import java.util.Scanner;

public class ej1 {

    // throws: en vez de try-catch, main deja pasar las excepciones
    public static void main(String[] args) throws IOException, InterruptedException {

        Scanner teclado = new Scanner(System.in);

        System.out.println("Introduzca la web a la que desea acceder: ");

        // next() lee solo hasta el primer espacio (para una URL vale)
        String url = teclado.next();

        // Comando: google-chrome <url>  (nombre del ejecutable en Linux)
        ProcessBuilder pb = new ProcessBuilder("google-chrome", url);

        // Lanza Chrome como proceso hijo. El padre sigue y termina sin esperarlo
        Process p = pb.start();

        teclado.close();
    }
}
