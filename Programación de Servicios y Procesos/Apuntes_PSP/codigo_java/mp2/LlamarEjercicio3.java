import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Scanner;

public class LlamarEjercicio3 {

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        System.out.println("Escribe un texto:");
        String texto = sc.nextLine(); // nextLine para coger la frase con espacios

        String classpath = System.getProperty("java.class.path");

        // El texto entero va como UN argumento (ProcessBuilder no lo parte por espacios)
        ProcessBuilder pb = new ProcessBuilder("java", "-cp", classpath, "Ejercicio3", texto);

        try {
            Process p = pb.start();

            // Lee lo que imprime el hijo ("Es palíndromo" / "NO es palíndromo")
            BufferedReader br = new BufferedReader(new InputStreamReader(p.getInputStream()));
            StringBuilder respuesta = new StringBuilder();
            String linea;
            while ((linea = br.readLine()) != null) {
                respuesta.append(linea).append("\n");
            }
            br.close();

            int salida = p.waitFor(); // código de salida del hijo
            System.out.println("Valor de Salida: " + salida);
            System.out.print(respuesta); // muestra el resultado del hijo
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
        sc.close();
    }
}
