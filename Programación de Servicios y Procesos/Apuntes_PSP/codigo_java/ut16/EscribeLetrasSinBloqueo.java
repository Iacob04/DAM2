import java.io.FileWriter;
import java.io.IOException;

// Ejemplo 1: dos procesos escriben en el MISMO fichero sin sincronizar.
// Uso: java EscribeLetrasSinBloqueo A
public class EscribeLetrasSinBloqueo {

    public static void main(String[] args) throws IOException, InterruptedException {
        String letra = args.length > 0 ? args[0] : "A";
        for (int i = 0; i < 5; i++) {
            // true = añadir al final (append). Cada write es atómico,
            // pero entre write y write el otro proceso puede colarse
            try (FileWriter fw = new FileWriter("letras.txt", true)) {
                fw.write(letra);
            }
            Thread.sleep((long) (Math.random() * 100));
        }
    }
}
