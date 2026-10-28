public class LeerNombre {

    public static void main(String[] args) {
        // Sin argumento o con menos de 3 caracteres -> error -1
        if (args.length == 0 || args[0].trim().length() < 3) {
            System.exit(-1);
        }

        String nombre = args[0].trim().toLowerCase();
        int vocales = 0;
        for (char c : nombre.toCharArray()) {
            if ("aeiouáéíóúü".indexOf(c) != -1) {
                vocales++;
            }
        }
        System.out.println("El nombre " + args[0].trim() + " tiene " + vocales + " vocales");
        System.exit(0); // OK
    }
}
