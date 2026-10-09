package multi1;
public class Ejercicio1 {

    public static void main(String[] args) {
        // -1 -> argumento vacío
        if (args.length == 0 || args[0].trim().isEmpty()) {
            System.exit(-1);
        }

        int numero;
        try {
            numero = Integer.parseInt(args[0].trim());
        } catch (NumberFormatException e) {
            // -2 -> no es un entero
            System.exit(-2);
            return;
        }

        if (numero > 0) {
            System.exit(-3); // entero positivo
        } else {
            System.exit(0);  // entero negativo (o 0)
        }
    }
}
