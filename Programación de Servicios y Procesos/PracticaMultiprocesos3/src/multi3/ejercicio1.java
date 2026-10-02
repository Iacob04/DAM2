package multi3;

import java.io.BufferedReader;      // CAMBIO: imports para leer de la entrada estándar
import java.io.IOException;
import java.io.InputStreamReader;

public class ejercicio1 {

	public static void main(String[] args) {

		// CAMBIO: ya no usamos args; leemos la primera línea de System.in,
		// que el padre conecta al fichero dato.txt
		String linea;
		try (BufferedReader br = new BufferedReader(new InputStreamReader(System.in))) {
			linea = br.readLine();
		} catch (IOException e) {
			System.exit(-1);
			return;
		}

		// -1 -> fichero vacío
		if (linea == null || linea.trim().isEmpty()) {   // CAMBIO: antes comprobaba args
			System.exit(-1);
		}

		// -2 -> no es un entero
		int numero;
		try {
			numero = Integer.parseInt(linea.trim());      // CAMBIO: antes parseaba args[0]
		} catch (NumberFormatException e) {
			System.exit(-2);
			return;
		}

		// -3 -> positivo, 0 -> negativo o cero
		if (numero > 0) {
			System.exit(-3);
		} else {
			System.exit(0);
		}
	}
}