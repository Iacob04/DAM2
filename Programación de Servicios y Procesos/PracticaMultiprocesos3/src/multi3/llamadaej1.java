package multi3;

import java.io.File;                 
import java.io.IOException;


public class llamadaej1 {
	public static void main(String[] args) {

		
		String classpath = System.getProperty("java.class.path");

		
		ProcessBuilder pb = new ProcessBuilder("java", "-cp", classpath, "multi3.ejercicio1");

		// CAMBIO: la entrada estándar del hijo pasa a ser el fichero dato.txt
		pb.redirectInput(new File("dato.txt"));

		try {
			// Lanzamos el hijo y esperamos su código de salida
			Process p = pb.start();
			int salida = p.waitFor();

			// En Linux/macOS los negativos llegan como 256 + valor (-1 -> 255, etc.)
			switch (salida) {
				case -3:
				case 253:
					System.out.println("Has escrito un entero positivo");
					break;
				case 0:
					System.out.println("El entero debe ser positivo");
					break;
				case -2:
				case 254:
					System.out.println("No has escrito un entero");
					break;
				case -1:
				case 255:
					System.out.println("El fichero está vacío");   // CAMBIO: mensaje adaptado al fichero
					break;
				default:
					System.out.println("Valor de salida inesperado: " + salida);
			}
		} catch (IOException | InterruptedException e) {
			e.printStackTrace();
		}
		// CAMBIO: eliminado sc.close()
	}
}