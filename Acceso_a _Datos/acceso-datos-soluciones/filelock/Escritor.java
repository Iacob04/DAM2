package filelock;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.RandomAccessFile;
import java.nio.channels.FileChannel;
import java.nio.channels.FileLock;
import java.nio.charset.StandardCharsets;
import java.util.Random;

public class Escritor {

    public static void main(String[] args) {
        Random r = new Random();

        // RandomAccessFile "rw": crea datos.lock si no existe. El bloqueo se hace sobre ÉL, no sobre datos.txt
        try (RandomAccessFile raf = new RandomAccessFile("datos.lock", "rw");
             FileChannel canal = raf.getChannel();
             FileLock bloqueo = canal.lock()) {                 // exclusivo; espera si otro lo tiene

            System.out.println("Establecido bloqueo para escribir los mensajes...");

            // false = sobrescribir: cada ejecución deja solo sus 10 mensajes
            try (PrintWriter pw = new PrintWriter(new FileWriter("datos.txt", StandardCharsets.UTF_8, false))) {
                for (int i = 0; i < 10; i++) {
                    pw.println("Mensaje de iteración :" + i);
                    pw.flush();                                 // que llegue al disco ya, no al cerrar
                    System.out.println("Escritor: Escrito mensaje de iteración " + i);
                    Thread.sleep(1000 + r.nextInt(501));        // entre 1 y 1,5 segundos
                }
            }

            bloqueo.release();                                  // también lo libera el try al cerrar
            System.out.println("Liberado bloqueo.");

        } catch (IOException e) {
            System.out.println("Error con los ficheros: " + e.getMessage());
        } catch (InterruptedException e) {
            System.out.println("Escritor interrumpido");
        }
    }
}
