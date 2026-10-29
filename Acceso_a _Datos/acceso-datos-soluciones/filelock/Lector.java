package filelock;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.channels.FileChannel;
import java.nio.channels.FileLock;
import java.nio.charset.StandardCharsets;

public class Lector {

    public static void main(String[] args) {
        System.out.println("Esperando que se libere el bloqueo...");

        // Mismo fichero de bloqueo que el Escritor. "rw" porque un lock exclusivo exige canal de escritura.
        try (RandomAccessFile raf = new RandomAccessFile("datos.lock", "rw");
             FileChannel canal = raf.getChannel();
             FileLock bloqueo = canal.lock()) {                 // se queda aquí parado hasta que el Escritor libere

            try (BufferedReader br = new BufferedReader(new FileReader("datos.txt", StandardCharsets.UTF_8))) {
                String linea;
                while ((linea = br.readLine()) != null) {
                    System.out.println("Leído mensaje: " + linea);
                }
            }
            System.out.println("Fin de lectura del fichero.");

        } catch (IOException e) {
            System.out.println("Error con los ficheros: " + e.getMessage());
        }
    }
}
