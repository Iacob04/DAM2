import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Scanner;

public class LlamarNotas {

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        String cp = System.getProperty("java.class.path");

        while (true) {
            System.out.println("Escribe la asignatura:");
            String asignatura = sc.nextLine().trim();
            if (asignatura.equals("*")) break;

            System.out.println("Escribe el nombre del fichero:");
            String fichero = sc.nextLine().trim();
            if (fichero.equals("*")) break;

            // Cada asignatura = un proceso hijo nuevo con 2 argumentos
            ProcessBuilder pb = new ProcessBuilder("java", "-cp", cp, "Notas", asignatura, fichero);
            // Mezcla System.err con System.out para leerlo todo de una vez
            pb.redirectErrorStream(true);

            try {
                Process p = pb.start();

                BufferedReader br = new BufferedReader(new InputStreamReader(p.getInputStream()));
                StringBuilder salidaHijo = new StringBuilder();
                String linea;
                while ((linea = br.readLine()) != null) {
                    salidaHijo.append(linea).append("\n");
                }
                br.close();

                int salida = p.waitFor();
                System.out.println("Valor de Salida: " + salida);
                System.out.print(salidaHijo);
            } catch (IOException | InterruptedException e) {
                e.printStackTrace();
            }
        }
        System.out.println("Fin del programa");
        sc.close();
    }
}
