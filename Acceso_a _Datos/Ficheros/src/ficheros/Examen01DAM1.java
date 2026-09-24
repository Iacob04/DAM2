package ficheros;

import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;


public class Examen01DAM1 {
	
	final static String ruta = "coordenadas.dat";
	

	public static void main(String[] args) {
		final int  Tamanyo_Registro = 20;
		
		try(DataInputStream fichero = new DataInputStream(new FileInputStream(ruta))){
			
			File ficheroFisico = new File(ruta);
			final int NUM_REGISTROS = (int)ficheroFisico.length()/Tamanyo_Registro;
			
			System.out.println("SATELITES Y COORDENADAS");
			
			for(int i = 0; i<NUM_REGISTROS; i++ ) {
				int id = fichero.readInt();
				float latitud = fichero.readFloat();
				float longitud = fichero.readFloat();
				String estado = "";
				for(int j = 0; j<4; j++) {
					estado += fichero.readChar();
					System.out.printf("Staelite ID: %d | Posición: (%.4f, %.4f) | Estado: %s\n", id, latitud,longitud, estado);
				}
			}
			
			
		}catch (Exception e) {
			System.out.println("Error al leer el fichero");
		}

	}

}
