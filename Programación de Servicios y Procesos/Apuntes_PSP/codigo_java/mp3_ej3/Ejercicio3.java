import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.Normalizer;

public class Ejercicio3 {

    public static void main(String[] args) throws IOException {
        // Lee TODO el texto de la entrada estándar (texto.txt)
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        StringBuilder sb = new StringBuilder();
        String linea;
        while ((linea = br.readLine()) != null) {
            sb.append(linea);
        }
        String original = sb.toString().trim();

        if (original.isEmpty()) {
            System.err.println("Error: texto.txt está vacío");   // -> error.txt
            System.exit(-1);
        }

        String limpio = Normalizer.normalize(original.toLowerCase(), Normalizer.Form.NFD)
                .replaceAll("\\p{M}", "")          // quita tildes
                .replaceAll("[^a-z0-9]", "");      // quita espacios y signos

        if (limpio.isEmpty()) {
            System.err.println("Error: el texto no tiene letras ni números");
            System.exit(-2);
        }

        String invertido = new StringBuilder(limpio).reverse().toString();
        if (limpio.equals(invertido)) {
            System.out.println("\"" + original + "\" es palíndromo");      // -> palindromo.txt
        } else {
            System.out.println("\"" + original + "\" NO es palíndromo");
        }
        System.exit(0);
    }
}
