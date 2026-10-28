import java.io.File;
import java.io.IOException;

public class LlamarEjercicio2 {

    public static void main(String[] args) {
        String cp = System.getProperty("java.class.path");
        ProcessBuilder pb = new ProcessBuilder("java", "-cp", cp, "Ejercicio2");

        // Ya no se pide nada por consola: los datos salen de datos.txt
        pb.redirectInput(new File("datos.txt"));   // entrada  <- datos.txt
        pb.redirectOutput(new File("suma.txt"));   // salida   -> suma.txt
        pb.redirectError(new File("error.txt"));   // errores  -> error.txt

        try {
            Process p = pb.start();
            int salida = p.waitFor();
            System.out.println("Valor de Salida: " + salida);
            if (salida == 0) {
                System.out.println("Correcto: la suma está en suma.txt");
            } else {
                System.out.println("Error: revisa error.txt");
            }
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }
}
