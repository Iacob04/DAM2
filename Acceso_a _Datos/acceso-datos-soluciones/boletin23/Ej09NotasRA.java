package boletin23;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public class Ej09NotasRA {

    public static void main(String[] args) {
        notasModulo("Redes.txt");
    }

    public static void notasModulo(String fichero) {
        List<String> aprobados = new ArrayList<>();
        List<List<String>> suspensosRA = new ArrayList<>();
        for (int i = 0; i < 5; i++) suspensosRA.add(new ArrayList<>());

        try (BufferedReader br = new BufferedReader(new FileReader(fichero, StandardCharsets.UTF_8))) {
            String linea;
            while ((linea = br.readLine()) != null) {
                if (linea.isBlank()) continue;
                String[] partes = linea.split(":");            // "Nombre" | " 5, 2.5, 3, 3, 6.2"
                String nombre = partes[0].trim();
                String[] notas = partes[1].split(",");
                boolean todoAprobado = true;
                for (int i = 0; i < notas.length && i < 5; i++) {
                    if (Double.parseDouble(notas[i].trim()) < 5) {
                        suspensosRA.get(i).add(nombre);
                        todoAprobado = false;
                    }
                }
                if (todoAprobado) aprobados.add(nombre);
            }
        } catch (IOException e) {
            System.out.println("Error al leer " + fichero + ": " + e.getMessage());
            return;
        }

        String modulo = new File(fichero).getName();
        modulo = modulo.substring(0, modulo.lastIndexOf('.'));    // "Redes.txt" -> "Redes"

        System.out.println("Módulo: " + modulo);
        System.out.println("Alumnos/as con todo aprobado:");
        for (String a : aprobados) System.out.println(a);
        System.out.println();
        System.out.println("Resultados de aprendizaje y alumnos suspensos:");
        for (int i = 0; i < 5; i++) {
            List<String> s = suspensosRA.get(i);
            System.out.println("RA" + (i + 1) + ": " + (s.isEmpty() ? "Todos aprobados" : String.join(", ", s)));
        }
    }
}
