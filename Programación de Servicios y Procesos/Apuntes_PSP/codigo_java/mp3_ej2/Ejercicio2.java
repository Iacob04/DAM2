import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class Ejercicio2 {

    public static void main(String[] args) throws IOException {
        // System.in = datos.txt (redirigido por el padre)
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        double suma = 0;
        String linea;

        while ((linea = br.readLine()) != null) {      // null = fin del fichero
            linea = linea.trim();
            if (linea.equals("*")) break;              // marca de fin
            if (linea.isEmpty()) continue;             // ignora líneas en blanco
            try {
                suma += Double.parseDouble(linea);
            } catch (NumberFormatException e) {
                // System.err -> acabará en error.txt
                System.err.println("Error: '" + linea + "' no es un número");
                System.exit(-1);
            }
        }
        // System.out -> acabará en suma.txt
        System.out.println("Suma: " + suma);
        System.exit(0);
    }
}
