package boletin24;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;

public class Ej08ValidarEmpleados {

    // Una o varias palabras solo de letras (\p{L} incluye tildes y ñ)
    static final String PALABRAS = "\\p{L}+( \\p{L}+)*";
    // Una o varias palabras de letras y/o números
    static final String PUESTO = "[\\p{L}\\d]+( [\\p{L}\\d]+)*";
    // Entero o con decimales
    static final String SALARIO = "\\d+(\\.\\d+)?";

    static final String LINEA_OK = PALABRAS + ", " + PALABRAS + ";" + PUESTO + ";" + SALARIO;

    public static void main(String[] args) {
        String origen = "/home/josemaria/origen.txt";
        String destino = "/home/josemaria/salida.txt";
        validar(origen, destino);
    }

    public static void validar(String origen, String destino) {
        try (BufferedReader br = new BufferedReader(new FileReader(origen, StandardCharsets.UTF_8));
             PrintWriter pw = new PrintWriter(new FileWriter(destino, StandardCharsets.UTF_8))) {

            String linea;
            while ((linea = br.readLine()) != null) {
                if (linea.matches(LINEA_OK)) pw.println(linea);
                else System.out.println("Línea errónea: " + linea);
            }
        } catch (IOException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }
}
