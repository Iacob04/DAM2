package ficheros;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class Ficheros1 {
	
	private static final String DIR_CONFIG = "DAM2"+File.separator+"josemaria";

	public static void main(String[] args) {
		
		File directoroioActual = new File(".");
		System.out.println(directoroioActual.getAbsolutePath());
		
		File dirConfig = new File(DIR_CONFIG);
		if(dirConfig.exists() == true)
			System.out.println("El directorio "+ DIR_CONFIG+ " existe");
		else
			System.out.println("El directorio "+ DIR_CONFIG+ " no existe");
			dirConfig.mkdirs();
			
		try (FileWriter pl = new FileWriter(DIR_CONFIG+File.separator+"fichero.txt", true)) {  // true = añadir
		
		    pl.write("Primera línea\n");
			} catch (Exception e) { }
	}

}
