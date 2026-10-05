package multi3;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class ejercicio4 {

    public static void main(String[] args) {

        if (args.length != 2) {          
            System.exit(-1);
        }

        String asignatura = args[0];
        String fichero = args[1];

        try (BufferedReader br = new BufferedReader(new FileReader(fichero))) {
            String linea;
            while ((linea = br.readLine()) != null) {
                System.out.println("Leído: " + linea);
            }
        } catch (IOException e) {
            System.out.println("No se pudo leer el fichero: " + fichero);
            System.exit(-2);
        }
    }
}
