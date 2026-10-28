import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

// Uso: java Notas <asignatura> <fichero>
public class Notas {

    public static void main(String[] args) {
        if (args.length < 2) {
            System.err.println("Uso: Notas <asignatura> <fichero>");
            System.exit(-1);
        }
        String asignatura = args[0];
        String fichero = args[1];

        double suma = 0;
        int numNotas = 0, aprobados = 0;

        // try-with-resources: cierra el fichero solo
        try (BufferedReader br = new BufferedReader(new FileReader(fichero))) {
            String linea;
            while ((linea = br.readLine()) != null) {
                linea = linea.trim().replace(",", ".");   // admite 8,5
                if (linea.isEmpty()) continue;
                try {
                    double nota = Double.parseDouble(linea);
                    suma += nota;
                    numNotas++;
                    if (nota >= 5) aprobados++;
                } catch (NumberFormatException e) {
                    System.err.println("Línea ignorada (no es una nota): " + linea);
                }
            }
        } catch (IOException e) {
            System.err.println("No se puede leer el fichero " + fichero);
            System.exit(-2);
        }

        if (numNotas == 0) {
            System.err.println("El fichero no tiene notas");
            System.exit(-3);
        }

        System.out.println("En la asignatura " + asignatura);
        System.out.println("Han aprobado " + aprobados + " alumnos");
        System.out.printf("La media es %.1f%n", suma / numNotas);
        System.exit(0);
    }
}
