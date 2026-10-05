package multi3;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Scanner;

public class llamadaej4 {

	public static void main(String[] args) {
		
		boolean salida = false;
		String classpath = System.getProperty("java.class.path");
        
        
		do {
			
		Scanner sc = new Scanner(System.in);
        System.out.println("Escribe la asignatura:");
        String asignatura = sc.nextLine();
        System.out.println("Escribe el nombre del fichero:");
        String fichero = sc.nextLine();
        if(asignatura.matches("*")) {
        	salida = true;
        }
        if(fichero.matches("*")) {
        	salida = true;
        }
        ProcessBuilder pb = new ProcessBuilder("java", "-cp", classpath, "ejercicio4", fichero);
        
        try {
            Process p = pb.start();

            BufferedReader br = new BufferedReader(new InputStreamReader(p.getInputStream()));
            StringBuilder respuesta = new StringBuilder();
            String linea;
            while ((linea = br.readLine()) != null) {
                respuesta.append(linea).append("\n");
            }
            br.close();

            int salidan = p.waitFor();
            System.out.println("Valor de Salida: " + salidan);
            System.out.print(respuesta);
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
        sc.close();
        
		}while(salida == false);
        
        
        
        
        
	}

}
