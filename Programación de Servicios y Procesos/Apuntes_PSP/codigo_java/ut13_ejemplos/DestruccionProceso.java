import java.io.IOException;

public class DestruccionProceso {

    public static void main(String[] args) throws IOException, InterruptedException {
        Process p = new ProcessBuilder("notepad.exe").start();

        Thread.sleep(3000);                 // dejamos el hijo vivo 3 segundos
        System.out.println("¿Vivo? " + p.isAlive());

        p.destroy();                        // pide al SO que lo mate (destroyForcibly() = kill -9)
        p.waitFor();                        // esperar a que muera de verdad
        System.out.println("¿Vivo? " + p.isAlive() + " · código: " + p.exitValue());
    }
}
