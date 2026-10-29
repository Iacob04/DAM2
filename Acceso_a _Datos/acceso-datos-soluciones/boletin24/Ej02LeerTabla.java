package boletin24;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;

public class Ej02LeerTabla {

    public static void main(String[] args) {
        int n = Ej01TablaMultiplicar.pedirNumero(new Scanner(System.in));   // reutilizo la validación
        mostrarTabla(n);
    }

    public static void mostrarTabla(int n) {
        String fichero = "tabla-" + n + ".txt";
        try (BufferedReader br = new BufferedReader(new FileReader(fichero, StandardCharsets.UTF_8))) {
            String linea;
            while ((linea = br.readLine()) != null) System.out.println(linea);
        } catch (FileNotFoundException e) {
            System.out.println("El fichero " + fichero + " no existe");
        } catch (IOException e) {
            System.out.println("Error al leer " + fichero + ": " + e.getMessage());
        }
    }
}
