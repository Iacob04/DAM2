import java.io.File;
import java.io.IOException;

public class LlamarEjercicio3 {

    public static void main(String[] args) {
        String cp = System.getProperty("java.class.path");
        ProcessBuilder pb = new ProcessBuilder("java", "-cp", cp, "Ejercicio3");

        pb.redirectInput(new File("texto.txt"));        // entrada
        pb.redirectOutput(new File("palindromo.txt"));  // salida
        pb.redirectError(new File("error.txt"));        // errores

        try {
            Process p = pb.start();
            int salida = p.waitFor();
            System.out.println("Valor de Salida: " + salida);
            System.out.println(salida == 0 ? "Resultado en palindromo.txt" : "Error: revisa error.txt");
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }
}
