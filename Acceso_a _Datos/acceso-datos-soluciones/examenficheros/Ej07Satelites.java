package examenficheros;

import java.io.DataInputStream;
import java.io.EOFException;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Locale;

public class Ej07Satelites {

    public static void main(String[] args) {
        leerCoordenadas("coordenadas.dat");
    }

    public static void leerCoordenadas(String fichero) {
        Locale es = Locale.forLanguageTag("es-ES");           // coma decimal, como en el enunciado: 40,4167
        try (DataInputStream dis = new DataInputStream(new FileInputStream(fichero))) {
            while (true) {
                // El orden de lectura TIENE que ser el de escritura: int, float, float, 4 char
                int id = dis.readInt();
                float lat = dis.readFloat();
                float lon = dis.readFloat();
                StringBuilder estado = new StringBuilder();
                for (int i = 0; i < 4; i++) estado.append(dis.readChar());

                System.out.println(String.format(es, "Satélite ID: %d | Posición: (%.4f, %.4f) | Estado: %s",
                        id, lat, lon, estado));
            }
        } catch (EOFException e) {
            // se acabaron los registros
        } catch (FileNotFoundException e) {
            System.out.println("No existe el fichero " + fichero);
        } catch (IOException e) {
            System.out.println("Error al leer " + fichero + ": " + e.getMessage());
        }
    }
}
