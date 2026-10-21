package jdbc;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class Main {

	public static void main(String[] args) {
		
		try (Connection cn = PoolConexiones.getConnection()){
			System.out.println("Conexion realizada");
			cn.setAutoCommit(false);
			Statement sql = cn.createStatement();
			sql.executeUpdate("DELETE FROM alumnos WHERE email = 'ana@educa.madrid.org'");
			ResultSet resultado = sql.executeQuery("SELECT * FROM alumnos");
			
			while(resultado.next()) {
				System.out.println(resultado.getString("email"));
			}
			cn.commit();
			cn.close();
		}catch(SQLException e) {
			e.printStackTrace();
		}

	}

}
