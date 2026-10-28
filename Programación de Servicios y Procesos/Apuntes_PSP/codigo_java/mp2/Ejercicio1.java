public class Ejercicio1 {

    public static void main(String[] args) {

        // Caso -1: no llega ningún argumento o llega vacío / solo espacios
        if (args.length == 0 || args[0].trim().isEmpty()) {
            System.exit(-1);
        }

        int numero;
        try {
            // Intenta convertir el texto a int (trim quita espacios)
            numero = Integer.parseInt(args[0].trim());
        } catch (NumberFormatException e) {
            // Caso -2: el texto no es un entero ("hola", "3.5"...)
            System.exit(-2);
            return; // nunca se ejecuta, pero el compilador lo necesita
                    // para saber que 'numero' siempre está inicializado
        }

        if (numero > 0) {
            System.exit(-3); // Caso -3: entero positivo
        } else {
            System.exit(0);  // Caso 0: entero negativo o cero
        }
    }
}
