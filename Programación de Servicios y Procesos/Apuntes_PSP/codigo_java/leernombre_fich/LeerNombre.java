import java.util.Scanner;

// Versión que lee el nombre por la ENTRADA ESTÁNDAR (System.in)
// para que el padre pueda redirigirla desde nombre.txt
public class LeerNombre {

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        String nombre = sc.hasNextLine() ? sc.nextLine().trim() : "";
        sc.close();

        if (nombre.length() < 3) {
            System.err.println("El nombre '" + nombre + "' tiene menos de 3 caracteres");
            System.exit(-1);
        }

        int vocales = 0;
        for (char c : nombre.toLowerCase().toCharArray()) {
            if ("aeiouáéíóúü".indexOf(c) != -1) vocales++;
        }
        System.out.println("El nombre " + nombre + " tiene " + vocales + " vocales");
        System.exit(0);
    }
}
