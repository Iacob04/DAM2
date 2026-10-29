package boletin23;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class Ej07AgendaObjetos {

    public static void main(String[] args) {
        List<Persona> agenda = leerAgenda("agenda.txt");
        for (Persona p : agenda) System.out.println(p);
    }

    // Mete en la lista solo los registros correctos; los incorrectos se avisan y se saltan
    public static List<Persona> leerAgenda(String fichero) {
        List<Persona> agenda = new ArrayList<>();
        List<String> lineas;
        try {
            lineas = Files.readAllLines(Path.of(fichero), StandardCharsets.UTF_8);
        } catch (IOException e) {
            System.out.println("No se puede leer " + fichero + ": " + e.getMessage());
            return agenda;
        }

        for (int i = 0; i + 2 < lineas.size(); i += 3) {
            String nombre = lineas.get(i).trim();
            String apellidos = lineas.get(i + 1).trim();
            String edad = lineas.get(i + 2).trim();
            // reutiliza las comprobaciones del ejercicio 6
            if (Ej06ValidarAgenda.esTexto(nombre) && Ej06ValidarAgenda.esTexto(apellidos)
                    && Ej06ValidarAgenda.esEntero(edad)) {
                agenda.add(new Persona(nombre, apellidos, Integer.parseInt(edad)));
            } else {
                System.out.println("Registro incorrecto en la línea " + (i + 1));
            }
        }
        if (lineas.size() % 3 != 0) System.out.println("Sobran líneas al final del fichero");
        return agenda;
    }
}
