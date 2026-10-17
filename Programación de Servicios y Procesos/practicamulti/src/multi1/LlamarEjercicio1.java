package multi1;

import java.io.IOException;
import java.util.Scanner;

public class LlamarEjercicio1 {

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        System.out.println("Escribe un número entero positivo:");
        String dato = sc.nextLine();

        String classpath = System.getProperty("java.class.path");
        ProcessBuilder pb = new ProcessBuilder("java", "-cp", classpath, "Ejercicio1", dato);

        try {
            Process p = pb.start();
            int salida = p.waitFor();

           
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
                    System.out.println("Valor de salida inesperado: " + salida);
            }
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
        sc.close();
    }
}
