package Ejercicios;

import java.io.IOException;
import java.util.Scanner;

public class ej1 {

	public static void main(String[] args) throws IOException, InterruptedException{
		
		Scanner teclado = new Scanner(System.in);
		
		System.out.println("Introduzca la web a la que desea acceder: ");
		
		String url = teclado.next();
		
		ProcessBuilder pb = new ProcessBuilder("google-chrome", url);
		Process p = pb.start();

	}

}
