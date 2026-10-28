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
        List<String> datos = new ArrayList<>(); // todo lo que escribe el usuario
        String entrada;

        // 1) Recoger datos SIN validar hasta que llegue un *
        //    (la validación la hace el hijo)
        do {
            System.out.println("Escribe un número:");
            entrada = sc.nextLine();
            datos.add(entrada);              // el * también se guarda y se envía
        } while (!entrada.trim().equals("*"));

        String classpath = System.getProperty("java.class.path");
        // 2) Sin argumentos: los datos van por la entrada estándar
        ProcessBuilder pb = new ProcessBuilder("java", "-cp", classpath, "Ejercicio2");

        try {
            Process p = pb.start();

            // 3) ENVIAR datos al hijo: getOutputStream() = System.in del hijo
            BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(p.getOutputStream()));
            for (String d : datos) {
                bw.write(d);   // escribe la línea
                bw.newLine();  // salto de línea para que el hijo pueda hacer readLine()
            }
            bw.close(); // IMPORTANTE: vacía el buffer y cierra la entrada del hijo.
                        // Sin esto el hijo nunca recibe null y se queda esperando

            // 4) LEER lo que imprimió el hijo: getInputStream() = System.out del hijo
            BufferedReader br = new BufferedReader(new InputStreamReader(p.getInputStream()));
            StringBuilder salidaHijo = new StringBuilder(); // acumula todas las líneas
            String linea;
            while ((linea = br.readLine()) != null) {
                salidaHijo.append(linea).append("\n");
            }
            br.close();

            // 5) Esperar a que termine y recoger el código de salida
            int salida = p.waitFor();
            System.out.println("Valor de Salida: " + salida);

            if (salida == 0) {
                System.out.print(salidaHijo);
            } else {
                // -1 en Windows, 255 en Linux: cualquier valor != 0 es error
                System.out.println("Error: has introducido algo que no es un número");
            }
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
        sc.close();
    }
}
