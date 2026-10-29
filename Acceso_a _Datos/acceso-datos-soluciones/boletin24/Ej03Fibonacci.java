package boletin24;

import java.io.FileWriter;
import java.io.IOException;
import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Ej03Fibonacci {

    public static void main(String[] args) {
        Scanner teclado = new Scanner(System.in);
        System.out.print("¿Cuántos números de la sucesión quieres? ");
        String entrada = teclado.nextLine().trim();

        int cantidad;
        try {
            cantidad = Integer.parseInt(entrada);
        } catch (NumberFormatException e) {
            System.out.println("Error: " + entrada + " no es un número entero");
            return;
        }
        if (cantidad < 2) {
            System.out.println("Error: hay que pedir al menos 2 números");
            return;
        }
        grabarFibonacci(cantidad, "fibonacci.txt");
    }

    public static void grabarFibonacci(int cantidad, String fichero) {
        // BigInteger: con long, si pides más de 93 números se desborda y salen negativos
        List<String> numeros = new ArrayList<>();
        BigInteger a = BigInteger.ZERO, b = BigInteger.ONE;
        for (int i = 0; i < cantidad; i++) {
            numeros.add(a.toString());
            BigInteger siguiente = a.add(b);
            a = b;
            b = siguiente;
        }

        try (FileWriter fw = new FileWriter(fichero, StandardCharsets.UTF_8)) {
            fw.write(String.join(", ", numeros));
            System.out.println("Sucesión guardada en " + fichero);
        } catch (IOException e) {
            System.out.println("Error al escribir " + fichero + ": " + e.getMessage());
        }
    }
}
