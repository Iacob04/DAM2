import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Scanner;

public class LlamarEjercicio3 {

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        System.out.println("Escribe un texto:");
        String texto = sc.nextLine();

        String classpath = System.getProperty("java.class.path");
        ProcessBuilder pb = new ProcessBuilder("java", "-cp", classpath, "Ejercicio3", texto);

        try {
            Process p = pb.start();

            BufferedReader br = new BufferedReader(new InputStreamReader(p.getInputStream()));
            StringBuilder respuesta = new StringBuilder();
            String linea;
            while ((linea = br.readLine()) != null) {
                respuesta.append(linea).append("\n");
            }
            br.close();

            int salida = p.waitFor();
            System.out.println("Valor de Salida: " + salida);
            System.out.print(respuesta);
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
        sc.close();
    }
}
