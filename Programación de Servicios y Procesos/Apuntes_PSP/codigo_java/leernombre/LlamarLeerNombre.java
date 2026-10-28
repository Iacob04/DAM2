import java.io.IOException;
import java.io.InputStream;
import java.util.Scanner;

public class LlamarLeerNombre {

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        System.out.println("Escribe un nombre:");
        String nombre = sc.nextLine();

        String cp = System.getProperty("java.class.path");
        ProcessBuilder pb = new ProcessBuilder("java", "-cp", cp, "LeerNombre", nombre);

        try {
            Process p = pb.start();
            int exitVal = p.waitFor();                 // salida corta: se puede esperar antes de leer
            System.out.println("Valor de Salida: " + exitVal);

            if (exitVal == 0) {
                // Recoger lo que el hijo escribió por System.out
                InputStream is = p.getInputStream();
                Scanner sc2 = new Scanner(is);
                while (sc2.hasNextLine()) {
                    System.out.println(sc2.nextLine());
                }
                sc2.close();
            } else {
                System.out.println("Error: el nombre debe tener al menos 3 caracteres");
            }
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
        sc.close();
    }
}
