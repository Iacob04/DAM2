import java.io.IOException;

public class EjecutarAplicacionRuntime {

    public static void main(String[] args) {
        // Runtime: el proceso se crea "en vacío" y el comando se pasa al ejecutar
        Runtime rt = Runtime.getRuntime();
        try {
            // exec(String[]) evita el exec(String) deprecado desde Java 18
            Process p = rt.exec(new String[] {"notepad.exe"});
            p.waitFor();
            System.out.println("Fin. Código: " + p.exitValue());
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }
}
