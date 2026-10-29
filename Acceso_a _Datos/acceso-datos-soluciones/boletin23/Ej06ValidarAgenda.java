package boletin23;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

public class Ej06ValidarAgenda {

    public static void main(String[] args) {
        if (formatoCorrecto("agenda.txt"))
            System.out.println("El formato del fichero es correcto");
        else
            System.out.println("El formato del fichero no es correcto");
    }

    public static boolean formatoCorrecto(String fichero) {
        List<String> lineas;
        try {
            lineas = Files.readAllLines(Path.of(fichero), StandardCharsets.UTF_8);
        } catch (IOException e) {
            System.out.println("No se puede leer " + fichero + ": " + e.getMessage());
            return false;
        }

        if (lineas.isEmpty() || lineas.size() % 3 != 0) return false;   // cada registro son 3 líneas

        for (int i = 0; i < lineas.size(); i += 3) {
            if (!esTexto(lineas.get(i)) || !esTexto(lineas.get(i + 1)) || !esEntero(lineas.get(i + 2)))
                return false;
        }
        return true;
    }

    // Línea con contenido y que no sea un número
    public static boolean esTexto(String s) {
        return !s.trim().isEmpty() && !esEntero(s);
    }

    public static boolean esEntero(String s) {
        try {
            Integer.parseInt(s.trim());
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }
}
