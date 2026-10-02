import java.text.Normalizer;

public class Ejercicio3 {

    public static void main(String[] args) {
        if (args.length == 0) {
            System.exit(-1);
        }

        // Une todos los argumentos por si la frase llega partida
        String texto = String.join("", args);

        // Minúsculas, sin tildes y sin espacios/signos
        texto = Normalizer.normalize(texto.toLowerCase(), Normalizer.Form.NFD)
                .replaceAll("\\p{M}", "")
                .replaceAll("[^a-z0-9ñ]", "");

        String invertido = new StringBuilder(texto).reverse().toString();

        if (!texto.isEmpty() && texto.equals(invertido)) {
            System.out.println("Es palíndromo");
        } else {
            System.out.println("NO es palíndromo");
        }
        System.exit(0);
    }
}
