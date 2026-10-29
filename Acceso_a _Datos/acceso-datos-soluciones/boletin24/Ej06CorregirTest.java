package boletin24;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class Ej06CorregirTest {

    // Una o más respuestas A-D separadas por ", "
    static final String RESPUESTAS = "[ABCD](, [ABCD])*";

    public static void main(String[] args) {
        corregir("soluciones.txt", "respuestas.txt", "notas.txt");
    }

    public static void corregir(String fSoluciones, String fRespuestas, String fNotas) {
        // 1. Soluciones: si están mal, no se hace nada más
        String[] soluciones;
        try {
            List<String> l = Files.readAllLines(Path.of(fSoluciones), StandardCharsets.UTF_8);
            if (l.size() != 1 || !l.get(0).matches(RESPUESTAS)) {
                System.out.println("El fichero de soluciones tiene un formato incorrecto");
                return;
            }
            soluciones = l.get(0).split(", ");
        } catch (IOException e) {
            System.out.println("No se puede leer " + fSoluciones + ": " + e.getMessage());
            return;
        }

        // 2. Respuestas: una línea mala se salta, las demás se procesan
        List<String> notas = new ArrayList<>();
        try {
            for (String linea : Files.readAllLines(Path.of(fRespuestas), StandardCharsets.UTF_8)) {
                if (!linea.matches("[^:]+: " + RESPUESTAS)) {
                    System.out.println("Línea con formato incorrecto: " + linea);
                    continue;
                }
                String[] partes = linea.split(": ");
                String[] resp = partes[1].split(", ");
                if (resp.length != soluciones.length) {
                    System.out.println("Número de respuestas incorrecto: " + linea);
                    continue;
                }
                double nota = 0;
                for (int i = 0; i < resp.length; i++) {
                    nota += resp[i].equals(soluciones[i]) ? 1 : -0.3;
                }
                notas.add(partes[0] + ": " + String.format(Locale.US, "%.1f", nota));
            }
        } catch (IOException e) {
            System.out.println("No se puede leer " + fRespuestas + ": " + e.getMessage());
            return;
        }

        // 3. Notas
        try {
            Files.write(Path.of(fNotas), notas, StandardCharsets.UTF_8);
            System.out.println("Notas guardadas en " + fNotas);
        } catch (IOException e) {
            System.out.println("Error al escribir " + fNotas + ": " + e.getMessage());
        }
    }
}
