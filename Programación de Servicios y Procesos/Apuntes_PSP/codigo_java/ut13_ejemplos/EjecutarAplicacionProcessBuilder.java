import java.io.IOException;

public class EjecutarAplicacionProcessBuilder {

    public static void main(String[] args) {
        // Comando + argumentos, cada uno en un String separado (varargs)
        // Windows: "notepad.exe"   ·   Linux: "gedit"   ·   macOS: "open", "-a", "TextEdit"
        ProcessBuilder pb = new ProcessBuilder("notepad.exe");
        try {
            Process p = pb.start();          // crea el proceso hijo
            System.out.println("Proceso lanzado. PID: " + p.pid());
            int codigo = p.waitFor();        // el padre espera a que cierres el bloc de notas
            System.out.println("El hijo terminó con código " + codigo);
        } catch (IOException e) {
            System.out.println("No se encontró el programa: " + e.getMessage());
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
