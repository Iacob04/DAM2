import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.channels.FileChannel;
import java.nio.channels.FileLock;

public class Lector {

    public static void main(String[] args) {
        System.out.println("Esperando que se libere el bloqueo...");

        // Mismo fichero de bloqueo que el Escritor
        try (RandomAccessFile raf = new RandomAccessFile("datos.lock", "rw");
             FileChannel canal = raf.getChannel();
             FileLock bloqueo = canal.lock();             // se queda bloqueado hasta que Escritor termine
             BufferedReader br = new BufferedReader(new FileReader("datos.txt"))) {

            String linea;
            while ((linea = br.readLine()) != null) {
                System.out.println("Leído mensaje: " + linea);
            }
        } catch (IOException e) {
            System.out.println("Error: " + e.getMessage());
        }
        System.out.println("Fin de lectura del fichero.");
    }
}
