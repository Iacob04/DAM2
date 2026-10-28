import java.io.FileWriter;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.channels.FileChannel;
import java.nio.channels.FileLock;
import java.util.Random;

public class Escritor {

    public static void main(String[] args) {
        Random r = new Random();

        // 1-3. Abrir datos.lock, sacar su FileChannel y bloquearlo.
        //      Los tres recursos van en el try-with-resources: al salir del try
        //      se cierran en orden inverso -> primero se libera el FileLock.
        try (RandomAccessFile raf = new RandomAccessFile("datos.lock", "rw");
             FileChannel canal = raf.getChannel();
             FileLock bloqueo = canal.lock()) {          // si otro proceso lo tiene, ESPERA aquí

            System.out.println("Establecido bloqueo para escribir los mensajes...");

            // 5. Escribir 10 mensajes en datos.txt (false = sobrescribir el fichero)
            try (FileWriter fw = new FileWriter("datos.txt", false)) {
                for (int i = 0; i < 10; i++) {
                    fw.write("Mensaje de iteración :" + i + System.lineSeparator());
                    fw.flush();                                   // que llegue ya al disco
                    System.out.println("Escritor: Escrito mensaje de iteración " + i);
                    Thread.sleep(1000 + r.nextInt(501));          // 1 a 1,5 segundos
                }
            }
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
        // Aquí el bloqueo ya está liberado (lo cerró el try)
        System.out.println("Liberado bloqueo.");
    }
}
