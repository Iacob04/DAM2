package boletin23;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Scanner;

public class Ej11Login {

    public static void main(String[] args) {
        String fichero = "/home/josemaria/login.txt";

        Scanner teclado = new Scanner(System.in);
        System.out.print("Usuario: ");
        String usuario = teclado.nextLine();
        System.out.print("Contraseña: ");
        String password = teclado.nextLine();

        login(fichero, usuario, password);
    }

    public static boolean login(String fichero, String usuario, String password) {
        List<String> lineas;
        try {
            lineas = Files.readAllLines(Path.of(fichero), StandardCharsets.UTF_8);
        } catch (IOException e) {
            System.out.println("El fichero " + fichero + " no existe o no se puede acceder a él");
            return false;
        }

        if (lineas.isEmpty()) {
            System.out.println("El fichero de contraseñas está vacío");
            return false;
        }

        for (String linea : lineas) {
            String[] partes = linea.split(":");          // usuario | contraseña
            if (partes.length == 2 && partes[0].equals(usuario)) {
                if (partes[1].equals(password)) {
                    System.out.println("Login correcto. Bienvenido/a " + usuario);
                    return true;
                }
                System.out.println("Contraseña incorrecta");
                return false;                            // no hay usuarios repetidos: no sigo buscando
            }
        }
        System.out.println("El usuario " + usuario + " no existe");
        return false;
    }
}
