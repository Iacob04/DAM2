package multi3;

import java.io.BufferedReader;      
import java.io.IOException;
import java.io.InputStreamReader;

public class ejercicio1 {

	public static void main(String[] args) {

		
		String linea;
		try (BufferedReader br = new BufferedReader(new InputStreamReader(System.in))) {
			linea = br.readLine();
		} catch (IOException e) {
			System.exit(-1);
			return;
		}

		if (linea == null || linea.trim().isEmpty()) {   
			System.exit(-1);
		}

		// -2 -> no es un entero
		int numero;
		try {
			numero = Integer.parseInt(linea.trim());      
		} catch (NumberFormatException e) {
			System.exit(-2);
			return;
		}

		
		if (numero > 0) {
			System.exit(-3);
		} else {
			System.exit(0);
		}
	}
}