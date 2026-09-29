package ficheros;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class Ficheros1 {
	
	private static final String DIR_CONFIG = "DAM2"+File.separator+"josemaria";
	private static final String ARCHIVO_CONFIG = DIR_CONFIG + File.separator+ "config.txt";

	public static void main(String[] args) {
		
		try {
		
		File directoroioActual = new File(".");
		System.out.println(directoroioActual.getAbsolutePath());
		
		File dirConfig = new File(DIR_CONFIG);
		boolean crearFichero = true;
		if(dirConfig.exists() == true)
			System.out.println("El directorio "+ DIR_CONFIG+ " existe");
		else
			System.out.println("El directorio "+ DIR_CONFIG+ " no existe");
			if(dirConfig.mkdirs()== false) {
				crearFichero = false;
			}
			if (crearFichero == true) {
				/*FileWriter escritor = new FileWriter(ARCHIVO_CONFIG, true);
				if(escritor == null)
					System.out.println("No he podido crear el archivo");
				else {
					System.out.println("Archivo creado o ya existía");
					escritor.close();
				}*/
				
				
				File archivo = new File(ARCHIVO_CONFIG);
				if(archivo.createNewFile())
					System.out.println("Archivo creado");
				else
					System.out.println("No puedo crearlo o ya existe");
				
			}
			
			long espacio = dirConfig.getFreeSpace() /1024 /1024 /1024;
			System.out.println("Espacio libre en disco: "+ espacio + " GB");
			
		}catch(Exception e) {
			System.out.println("Error al manipular los ficheros");
		}
			
	}

}
