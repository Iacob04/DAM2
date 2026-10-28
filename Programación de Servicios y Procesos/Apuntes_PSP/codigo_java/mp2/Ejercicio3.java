import java.text.Normalizer;

public class Ejercicio3 {

    public static void main(String[] args) {
        if (args.length == 0) {
            System.exit(-1); // sin texto -> error
        }

        // Une todos los argumentos por si la frase llega partida en varios
        // (pasaría desde terminal sin comillas: java Ejercicio3 Ana lava lana)
        String texto = String.join("", args);

        // Limpieza en 3 pasos:
        // 1. toLowerCase()          -> todo a minúsculas
        // 2. Normalize NFD + \p{M}  -> separa la letra de su tilde y borra la tilde
        // 3. [^a-z0-9ñ]             -> borra lo que no sea letra o número
        texto = Normalizer.normalize(texto.toLowerCase(), Normalizer.Form.NFD)
                .replaceAll("\\p{M}", "")
                .replaceAll("[^a-z0-9ñ]", "");

        // Da la vuelta al texto
        String invertido = new StringBuilder(texto).reverse().toString();

        // Es palíndromo si no está vacío y es igual al revés
        if (!texto.isEmpty() && texto.equals(invertido)) {
            System.out.println("Es palíndromo");
        } else {
            System.out.println("NO es palíndromo");
        }
        System.exit(0);
    }
}
