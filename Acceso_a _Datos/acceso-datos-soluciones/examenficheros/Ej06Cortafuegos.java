package examenficheros;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.Scanner;
import java.util.TreeMap;

public class Ej06Cortafuegos {

    static final int MAX_ACCESOS = 2;

    public static void main(String[] args) {
        Scanner teclado = new Scanner(System.in);
        System.out.print("Introduce el nombre del fichero de IPs: ");
        analizarIPs(teclado.nextLine().trim());
    }

    public static void analizarIPs(String fichero) {
        Map<String, Integer> accesos = new TreeMap<>();     // TreeMap: salen ordenadas
        try (BufferedReader br = new BufferedReader(new FileReader(fichero, StandardCharsets.UTF_8))) {
            String linea;
            while ((linea = br.readLine()) != null) {
                linea = linea.trim();
                if (linea.isEmpty()) continue;
                accesos.put(linea, accesos.getOrDefault(linea, 0) + 1);
            }
        } catch (FileNotFoundException e) {
            System.out.println("Atención: el fichero " + fichero + " no existe");
            return;
        } catch (IOException e) {
            System.out.println("Error al leer " + fichero + ": " + e.getMessage());
            return;
        }

        if (accesos.isEmpty()) {
            System.out.println("Atención: el fichero " + fichero + " está vacío");
            return;
        }

        StringBuilder permitidas = new StringBuilder();
        StringBuilder bloqueadas = new StringBuilder();
        for (Map.Entry<String, Integer> e : accesos.entrySet()) {
            if (e.getValue() <= MAX_ACCESOS) {
                permitidas.append("- ").append(e.getKey()).append("\n");
            } else {
                bloqueadas.append("- ").append(e.getKey())
                          .append(". Accesos superiores a lo permitido: ")
                          .append(e.getValue() - MAX_ACCESOS).append("\n");
            }
        }

        // Los bloques vacíos no se muestran
        if (permitidas.length() > 0) System.out.print("IP con acceso permitido:\n" + permitidas);
        if (bloqueadas.length() > 0) System.out.print("IPs con acceso restringido:\n" + bloqueadas);
    }
}
