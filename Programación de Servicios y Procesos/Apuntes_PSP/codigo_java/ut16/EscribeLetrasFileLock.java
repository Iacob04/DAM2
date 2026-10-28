import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.channels.FileChannel;
import java.nio.channels.FileLock;

// Ejemplo 4/5: bloqueo real con FileChannel.lock() (forma recomendada)
public class EscribeLetrasFileLock {

    public static void main(String[] args) throws IOException, InterruptedException {
        String letra = args.length > 0 ? args[0] : "A";
        File lockFile = new File("lock.tmp");     // fichero SOLO para bloquear
        lockFile.createNewFile();

        try (FileOutputStream fos = new FileOutputStream(lockFile);
             FileChannel canal = fos.getChannel();
             FileLock lock = canal.lock()) {       // bloquea el proceso hasta tener el lock
            // ---- región crítica ----
            for (int i = 0; i < 5; i++) {
                try (FileWriter fw = new FileWriter("letras.txt", true)) {   // OTRO fichero para escribir
                    fw.write(letra);
                }
                Thread.sleep((long) (Math.random() * 100));
            }
        }   // al salir del try el FileLock se libera solo (AutoCloseable)
    }
}
