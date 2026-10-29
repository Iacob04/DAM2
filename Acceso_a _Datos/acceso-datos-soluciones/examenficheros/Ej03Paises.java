package examenficheros;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class Ej03Paises {

    public static void main(String[] args) {
        mostrarPaises("paises.csv");
    }

    // Devuelve las líneas válidas ya troceadas (sin cabecera). null si no se puede leer.
    // Se reutiliza en el ejercicio 4.
    public static List<String[]> leerPaises(String fichero) {
        List<String[]> paises = new ArrayList<>();
        List<String> lineas;
        try {
            lineas = Files.readAllLines(Path.of(fichero), StandardCharsets.UTF_8);
        } catch (IOException e) {
            System.out.println("Error al leer " + fichero + ": " + e.getMessage());
            return null;
        }
        for (int i = 1; i < lineas.size(); i++) {               // i = 1: la 0 es la cabecera
            String[] campos = lineas.get(i).split(",", -1);    // -1: no se come los campos vacíos del final
            if (campos.length == 4) paises.add(campos);         // si no son 4 datos, se ignora
        }
        return paises;
    }

    public static void mostrarPaises(String fichero) {
        List<String[]> paises = leerPaises(fichero);
        if (paises == null) return;
        if (paises.isEmpty()) {
            System.out.println("No hay datos de ningún país en el fichero");
            return;
        }
        System.out.println("Países en el fichero: " + paises.size());
        System.out.println("Nombres: " + enumerar(columna(paises, 0)));
        System.out.println("Las capitales de los mismos son: " + enumerar(columna(paises, 1)));
        System.out.println("Sus monedas oficiales son: " + enumerar(columna(paises, 2)));
        System.out.println("Sus animales mas representativos son " + enumerar(columna(paises, 3)));
    }

    public static List<String> columna(List<String[]> filas, int n) {
        List<String> col = new ArrayList<>();
        for (String[] f : filas) col.add(f[n]);
        return col;
    }

    // [A, B, C] -> "A, B y C"   [A] -> "A"
    public static String enumerar(List<String> lista) {
        if (lista.size() == 1) return lista.get(0);
        String primeros = String.join(", ", lista.subList(0, lista.size() - 1));
        return primeros + " y " + lista.get(lista.size() - 1);
    }
}
