import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class Ejercicio2 {

    public static void main(String[] args) {
        // Lee de System.in, que aquí NO es el teclado: es lo que escribe el padre
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        double suma = 0;   // double para aceptar también decimales
        String linea;

        try {
            // readLine() devuelve null cuando el padre cierra el flujo (bw.close())
            while ((linea = br.readLine()) != null) {
                linea = linea.trim();

                // No sale por pantalla directamente: va a la salida del hijo,
                // que el padre lee y muestra después
                System.out.println("Escrito " + linea);

                if (linea.equals("*")) {
                    break; // marca de fin: deja de leer
                }

                try {
                    suma += Double.parseDouble(linea); // acumula el número
                } catch (NumberFormatException e) {
                    System.exit(-1); // no es un número -> termina con error
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        System.out.println("Suma: " + suma);
        System.exit(0); // todo correcto
    }
}
