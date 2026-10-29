package boletin24;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;

public class Ej07AltaUsuario {

    public static void main(String[] args) {
        String fichero = "/home/josemaria/login.txt";
        Scanner teclado = new Scanner(System.in);

        System.out.print("Introduce el nombre del usuario: ");
        String usuario = teclado.nextLine();
        System.out.print("Introduce la contraseña: ");
        String pass1 = teclado.nextLine();
        System.out.print("Vuelve a introducir la contraseña de nuevo: ");
        String pass2 = teclado.nextLine();

        altaUsuario(fichero, usuario, pass1, pass2);
    }

    public static void altaUsuario(String fichero, String usuario, String pass1, String pass2) {
        if (!pass1.equals(pass2)) {
            System.out.println("Las contraseñas no son iguales. No se puede grabar la nueva cuenta");
            return;
        }
        if (usuario.isEmpty() || pass1.isEmpty() || usuario.contains(":") || pass1.contains(":")) {
            System.out.println("Usuario y contraseña no pueden estar vacíos ni contener ':'");
            return;
        }
        if (existeUsuario(fichero, usuario)) {
            System.out.println("El usuario " + usuario + " ya existe");
            return;
        }
        // true = añadir al final, no machacar el fichero
        try (PrintWriter pw = new PrintWriter(new FileWriter(fichero, StandardCharsets.UTF_8, true))) {
            pw.println(usuario + ":" + pass1);
            System.out.println("Cuenta de usuario grabada correctamente");
        } catch (IOException e) {
            System.out.println("Error al grabar en " + fichero + ": " + e.getMessage());
        }
    }

    // Si el fichero aún no existe, no hay usuarios
    static boolean existeUsuario(String fichero, String usuario) {
        try (BufferedReader br = new BufferedReader(new FileReader(fichero, StandardCharsets.UTF_8))) {
            String linea;
            while ((linea = br.readLine()) != null) {
                if (linea.split(":")[0].equals(usuario)) return true;
            }
        } catch (IOException e) {
            return false;
        }
        return false;
    }
}
