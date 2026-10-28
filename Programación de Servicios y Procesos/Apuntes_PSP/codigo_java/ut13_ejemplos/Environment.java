import java.io.File;
import java.util.Map;

public class Environment {

    public static void main(String[] args) {
        ProcessBuilder pb = new ProcessBuilder("java", "-version");

        // environment(): mapa con las variables de entorno que heredará el hijo
        Map<String, String> entorno = pb.environment();
        System.out.println("PATH = " + entorno.get("PATH"));
        entorno.put("MI_VARIABLE", "hola");          // se puede añadir o cambiar

        // Directorio de trabajo (por defecto user.dir)
        System.out.println("Directorio por defecto: " + System.getProperty("user.dir"));
        pb.directory(new File(System.getProperty("user.home")));
        System.out.println("Directorio del hijo: " + pb.directory());

        // Recorrer todas las variables
        for (Map.Entry<String, String> e : entorno.entrySet()) {
            System.out.println(e.getKey() + " = " + e.getValue());
        }
    }
}
