import java.io.File;
import java.io.IOException;

public class LlamarLeerNombre {

    public static void main(String[] args) {
        String cp = System.getProperty("java.class.path");
        ProcessBuilder pb = new ProcessBuilder("java", "-cp", cp, "LeerNombre");

        // Redirecciones: SIEMPRE antes de start()
        pb.redirectInput(new File("nombre.txt"));     // System.in del hijo  <- nombre.txt
        pb.redirectOutput(new File("minombre.txt"));  // System.out del hijo -> minombre.txt
        pb.redirectError(new File("error.txt"));      // System.err del hijo -> error.txt

        try {
            Process p = pb.start();
            int exitVal = p.waitFor();
            System.out.println("Valor de Salida: " + exitVal);
            System.out.println(exitVal == 0 ? "Resultado en minombre.txt" : "Mira error.txt");
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }
}
