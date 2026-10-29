package boletin23;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class Ej08AgendaTipos {

    public static void main(String[] args) {
        List<Persona> agenda = leerAgenda("agenda2.txt");
        if (agenda != null) {
            System.out.println("El formato del fichero es correcto");
            for (Persona p : agenda) System.out.println(p);
        } else {
            System.out.println("El formato del fichero no es correcto");
        }
    }

    public static boolean esTipo(String s) {
        return s.equals("Familia") || s.equals("Amigo") || s.equals("Conocido");
    }

    // Los registros ya no miden siempre 3 líneas: nombre, [apellidos], tipo, edad.
    // Se recorre con un índice que avanza 3 o 4 según haya apellidos.
    // Devuelve null si algo no cuadra.
    public static List<Persona> leerAgenda(String fichero) {
        List<String> l;
        try {
            l = Files.readAllLines(Path.of(fichero), StandardCharsets.UTF_8);
        } catch (IOException e) {
            System.out.println("No se puede leer " + fichero + ": " + e.getMessage());
            return null;
        }

        List<Persona> agenda = new ArrayList<>();
        int i = 0;
        while (i < l.size()) {
            if (i + 2 >= l.size()) return null;                     // no caben ni 3 líneas

            String nombre = l.get(i).trim();
            if (!Ej06ValidarAgenda.esTexto(nombre) || esTipo(nombre)) return null;

            String apellidos = null;
            String segunda = l.get(i + 1).trim();
            int pos;                                                // posición del tipo
            if (esTipo(segunda)) {
                pos = i + 1;                                        // sin apellidos
            } else {
                if (!Ej06ValidarAgenda.esTexto(segunda)) return null;
                apellidos = segunda;
                pos = i + 2;
            }

            if (pos + 1 >= l.size()) return null;
            String tipo = l.get(pos).trim();
            String edad = l.get(pos + 1).trim();
            if (!esTipo(tipo) || !Ej06ValidarAgenda.esEntero(edad)) return null;

            agenda.add(new Persona(nombre, apellidos, tipo, Integer.parseInt(edad)));
            i = pos + 2;                                            // siguiente registro
        }
        return agenda.isEmpty() ? null : agenda;
    }
}
