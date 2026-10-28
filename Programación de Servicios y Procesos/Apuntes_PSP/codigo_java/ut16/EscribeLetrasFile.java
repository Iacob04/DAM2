import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

// Ejemplo 3: semáforo con File + createNewFile() (espera activa)
public class EscribeLetrasFile {

    public static void main(String[] args) throws IOException, InterruptedException {
        String letra = args.length > 0 ? args[0] : "A";
        File lock = new File("lock.tmp");

        // createNewFile() es atómico: devuelve false si ya existe
        while (!lock.createNewFile()) {
            Thread.sleep(100);                 // ocupado: esperar y reintentar
        }
        try {
            // ---- región crítica ----
            for (int i = 0; i < 5; i++) {
                try (FileWriter fw = new FileWriter("letras.txt", true)) {
                    fw.write(letra);
                }
                Thread.sleep((long) (Math.random() * 100));
            }
        } finally {
            lock.delete();                     // liberar SIEMPRE, aunque haya excepción
        }
    }
}
