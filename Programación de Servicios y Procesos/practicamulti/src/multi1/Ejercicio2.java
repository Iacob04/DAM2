package multi1;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class Ejercicio2 {

    public static void main(String[] args) {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        double suma = 0;
        String linea;

        try {
            while ((linea = br.readLine()) != null) {
                linea = linea.trim();
                System.out.println("Escrito " + linea);

                if (linea.equals("*")) {
                    break; // deja de leer la entrada estándar
                }

                try {
                    suma += Double.parseDouble(linea);
                } catch (NumberFormatException e) {
                    System.exit(-1); // es una cadena
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        System.out.println("Suma: " + suma);
        System.exit(0); // todo correcto
    }
}
