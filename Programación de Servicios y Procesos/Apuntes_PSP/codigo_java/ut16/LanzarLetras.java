import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

// Lanza dos procesos a la vez (A y B) con la clase que se pase por argumento
// Uso: java LanzarLetras EscribeLetrasFileLock
public class LanzarLetras {

    public static void main(String[] args) throws IOException, InterruptedException {
        String clase = args.length > 0 ? args[0] : "EscribeLetrasSinBloqueo";
        String cp = System.getProperty("java.class.path");
        new File("letras.txt").delete();                   // empezar limpio

        Process a = new ProcessBuilder("java", "-cp", cp, clase, "A").inheritIO().start();
        Process b = new ProcessBuilder("java", "-cp", cp, clase, "B").inheritIO().start();
        a.waitFor();
        b.waitFor();

        System.out.println(clase + " -> " + Files.readString(Path.of("letras.txt")));
    }
}
