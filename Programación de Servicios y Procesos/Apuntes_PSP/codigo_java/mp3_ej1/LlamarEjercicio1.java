import java.io.File;
import java.io.IOException;

public class LlamarEjercicio1 {

    public static void main(String[] args) {
        String cp = System.getProperty("java.class.path");
        ProcessBuilder pb = new ProcessBuilder("java", "-cp", cp, "Ejercicio1");

        // El System.in del hijo pasa a ser el fichero dato.txt
        // (se busca en el directorio de trabajo: la carpeta del proyecto en Eclipse)
        pb.redirectInput(new File("dato.txt"));

        try {
            Process p = pb.start();
            int salida = p.waitFor();

            switch (salida) {
                case -3: case 253: System.out.println("Has escrito un entero positivo"); break;
                case 0:            System.out.println("El entero debe ser positivo");    break;
                case -2: case 254: System.out.println("No has escrito un entero");       break;
                case -1: case 255: System.out.println("No has escrito nada");            break;
                default:           System.out.println("Valor inesperado: " + salida);
            }
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }
}
