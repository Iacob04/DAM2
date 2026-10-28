import java.util.Scanner;

// Igual que en la práctica II, pero el dato llega por la ENTRADA ESTÁNDAR
// (el padre la redirige desde dato.txt) en lugar de por argumento.
public class Ejercicio1 {

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        // Si dato.txt está vacío no hay ninguna línea
        String dato = sc.hasNextLine() ? sc.nextLine().trim() : "";
        sc.close();

        if (dato.isEmpty()) {
            System.exit(-1);                 // vacío
        }
        int numero;
        try {
            numero = Integer.parseInt(dato);
        } catch (NumberFormatException e) {
            System.exit(-2);                 // no es entero
            return;
        }
        System.exit(numero > 0 ? -3 : 0);    // -3 positivo · 0 negativo/cero
    }
}
