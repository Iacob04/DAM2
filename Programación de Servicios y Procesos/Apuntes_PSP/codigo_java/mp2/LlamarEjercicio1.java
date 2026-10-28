import java.io.IOException;
import java.util.Scanner;

public class LlamarEjercicio1 {

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        System.out.println("Escribe un número entero positivo:");

        // nextLine() lee la línea entera (aunque esté vacía)
        String dato = sc.nextLine();

        // Classpath actual para que el hijo encuentre Ejercicio1.class
        String classpath = System.getProperty("java.class.path");

        // java -cp <classpath> Ejercicio1 <dato>  -> dato llega al hijo en args[0]
        ProcessBuilder pb = new ProcessBuilder("java", "-cp", classpath, "Ejercicio1", dato);

        try {
            Process p = pb.start();      // lanza el hijo
            int salida = p.waitFor();    // espera a que acabe y recoge su System.exit(n)

            // Se ponen los dos valores porque en Linux/macOS
            // los negativos llegan como 256 + valor (-1 -> 255...)
            switch (salida) {
                case -3:
                case 253:
                    System.out.println("Has escrito un entero positivo");
                    break;
                case 0:
                    System.out.println("El entero debe ser positivo");
                    break;
                case -2:
                case 254:
                    System.out.println("No has escrito un entero");
                    break;
                case -1:
                case 255:
                    System.out.println("No has escrito nada");
                    break;
                default:
                    // Por ejemplo 1 si la JVM no encuentra la clase Ejercicio1
                    System.out.println("Valor de salida inesperado: " + salida);
            }
        } catch (IOException | InterruptedException e) {
            // IOException: no se pudo lanzar 'java'
            // InterruptedException: interrumpieron al padre mientras esperaba
            e.printStackTrace();
        }
        sc.close();
    }
}
