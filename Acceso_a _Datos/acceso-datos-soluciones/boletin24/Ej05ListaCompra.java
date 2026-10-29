package boletin24;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Locale;
import java.util.Scanner;

public class Ej05ListaCompra {

    // 2.0 -> "2", 2.5 -> "2.5", 31.25 -> "31.25"
    static final DecimalFormat DF = new DecimalFormat("#.##", DecimalFormatSymbols.getInstance(Locale.US));

    public static void main(String[] args) {
        Scanner teclado = new Scanner(System.in);
        StringBuilder lista = new StringBuilder();
        int articulos = 0;
        double total = 0;

        String seguir;
        do {
            System.out.print("Introduce un artículo: ");
            String articulo = teclado.nextLine().trim();
            double cantidad = leerNumero(teclado, "Introduce la cantidad: ");
            double precio = leerNumero(teclado, "Introduce el precio: ");

            lista.append(DF.format(cantidad)).append(" ").append(articulo).append("\n");
            articulos++;
            total += cantidad * precio;

            System.out.print("Quieres seguir introduciendo artículos en la lista (si/no): ");
            seguir = teclado.nextLine().trim();
        } while (seguir.equalsIgnoreCase("si"));

        String fichero = "compra.txt";
        try (PrintWriter pw = new PrintWriter(new FileWriter(fichero, StandardCharsets.UTF_8))) {
            pw.print(lista);
            pw.println("Total artículos en la lista: " + articulos);
            pw.println("Precio de la compra: " + DF.format(total));
            System.out.println("Tu lista de la compra se encuentra en el fichero " + fichero);
        } catch (IOException e) {
            System.out.println("Error al escribir " + fichero + ": " + e.getMessage());
        }
    }

    static double leerNumero(Scanner teclado, String mensaje) {
        while (true) {
            System.out.print(mensaje);
            try {
                double n = Double.parseDouble(teclado.nextLine().trim().replace(',', '.'));
                if (n > 0) return n;
            } catch (NumberFormatException e) {
                // cae al mensaje de abajo
            }
            System.out.println("Introduce un número mayor que 0");
        }
    }
}
