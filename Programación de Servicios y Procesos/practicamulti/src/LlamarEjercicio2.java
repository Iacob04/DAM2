import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class LlamarEjercicio2 {

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        List<String> datos = new ArrayList<>();
        String entrada;

        // Recoge datos sin validar hasta que llegue un *
        do {
            System.out.println("Escribe un número:");
            entrada = sc.nextLine();
            datos.add(entrada);
        } while (!entrada.trim().equals("*"));

        String classpath = System.getProperty("java.class.path");
        ProcessBuilder pb = new ProcessBuilder("java", "-cp", classpath, "Ejercicio2");

        try {
            Process p = pb.start();

            // Enviar los datos a la entrada estándar del hijo
            BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(p.getOutputStream()));
            for (String d : datos) {
                bw.write(d);
                bw.newLine();
            }
            bw.close(); // importante: cierra la entrada del hijo

            // Leer la salida del hijo
            BufferedReader br = new BufferedReader(new InputStreamReader(p.getInputStream()));
            StringBuilder salidaHijo = new StringBuilder();
            String linea;
            while ((linea = br.readLine()) != null) {
                salidaHijo.append(linea).append("\n");
            }
            br.close();

            int salida = p.waitFor();
            System.out.println("Valor de Salida: " + salida);

            if (salida == 0) {
                System.out.print(salidaHijo);
            } else {
                System.out.println("Error: has introducido algo que no es un número");
            }
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
        sc.close();
    }
}
