package examenficheros;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.EOFException;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class Ej05Elecciones {

    static final String FICHERO = "votos.dat";
    static final String[] PARTIDOS = {"PA", "PB", "PC", "PD"};

    public static void main(String[] args) {
        new File(FICHERO).delete();          // solo para la prueba: empezar de cero
        anyadirVotos("PA", 134);
        anyadirVotos("PB", 98);
        anyadirVotos("PC", 220);
        anyadirVotos("PD", 177);
        anyadirVotos("PC", 236);
        anyadirVotos("PX", 10);
        verResultados(1730);
    }

    // Lee el fichero. Si no existe, los cuatro partidos a 0.
    static Map<String, Integer> leerVotos() {
        Map<String, Integer> votos = new LinkedHashMap<>();       // mantiene el orden PA, PB, PC, PD
        for (String p : PARTIDOS) votos.put(p, 0);
        if (!new File(FICHERO).exists()) return votos;

        try (DataInputStream dis = new DataInputStream(new FileInputStream(FICHERO))) {
            while (true) {
                String partido = dis.readUTF();
                int n = dis.readInt();
                votos.put(partido, n);
            }
        } catch (EOFException e) {
            // fin del fichero: es la forma normal de salir del bucle
        } catch (IOException e) {
            System.out.println("Error al leer " + FICHERO + ": " + e.getMessage());
        }
        return votos;
    }

    static void grabarVotos(Map<String, Integer> votos) {
        try (DataOutputStream dos = new DataOutputStream(new FileOutputStream(FICHERO))) {
            for (Map.Entry<String, Integer> e : votos.entrySet()) {
                dos.writeUTF(e.getKey());
                dos.writeInt(e.getValue());
            }
        } catch (IOException e) {
            System.out.println("Error al grabar " + FICHERO + ": " + e.getMessage());
        }
    }

    public static void anyadirVotos(String partido, int nuevos) {
        Map<String, Integer> votos = leerVotos();
        if (!votos.containsKey(partido)) {
            System.out.println("El partido " + partido + " no se presenta a estas elecciones");
            return;
        }
        votos.put(partido, votos.get(partido) + nuevos);
        grabarVotos(votos);

        System.out.println("Nuevos votos para el partido " + partido + ": " + nuevos + ".");
        System.out.println("Votos hasta el momento:");
        for (Map.Entry<String, Integer> e : votos.entrySet()) {
            System.out.println("Partido " + e.getKey() + ": " + e.getValue() + " votos");
        }
    }

    public static void verResultados(int censo) {
        Map<String, Integer> votos = leerVotos();
        int total = 0;
        for (int v : votos.values()) total += v;

        List<Map.Entry<String, Integer>> orden = new ArrayList<>(votos.entrySet());
        orden.sort((a, b) -> b.getValue() - a.getValue());      // de más a menos votos

        DecimalFormat df = new DecimalFormat("#.##");            // 50 -> "50", 33.333 -> "33,33"
        System.out.println("Resultados con un " + df.format(total * 100.0 / censo) + "% de escrutinio:");
        for (Map.Entry<String, Integer> e : orden) {
            System.out.println("Partido " + e.getKey() + ": " + e.getValue() + " votos");
        }
    }
}
