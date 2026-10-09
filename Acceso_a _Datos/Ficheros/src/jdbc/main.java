package jdbc;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class main {

	public static void main(String[] args) {
		
		
		String url = "jdbc:mysql://localhost/dam2";
		String usuario = "alumno";
		String password = "abc123";
		
		try {
			
			Connection cn = DriverManager.getConnection(url,usuario,password);
			System.out.println("Conexion realizada");
			Statement sql = cn.createStatement();
			ResultSet resultado = sql.executeQuery("SELECT * FROM alumnos");
			
			while(resultado.next()) {
				
			}
			
			cn.close();
		}catch(SQLException e) {
			e.printStackTrace();
		}

	}

}
