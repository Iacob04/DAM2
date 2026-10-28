package Ejercicio1;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Main {

	public static void main(String[] args) {
		 listarEmpleados("Paris");          // 5 empleados
		    listarEmpleados("Madrid");         // No existe oficina
		    moverEmpleados("Tokyo", "Paris");  // mueve 2, Paris queda con 7
		    listarEmpleados("Tokyo");          // La oficina de Tokyo no tiene empleados
		    listarEmpleados("Paris");          // 7 empleados
	}
	
	
	public static void listarEmpleados(String ciudad) {

		String consulta = "select employees.firstName, employees.lastName, employees.email "
				+ "from offices left join employees using (officeCode) "
				+ "where offices.city = ?";

		try (Connection cn = PoolConexion.getConnection();
				PreparedStatement ps = cn.prepareStatement(consulta)) {

			ps.setString(1, ciudad);

			try (ResultSet rs = ps.executeQuery()) {

				boolean existeOficina = false;
				int total = 0;
				StringBuilder sb = new StringBuilder();

				while (rs.next()) {
					existeOficina = true;
					String apellido = rs.getString("lastName"); 
					if (apellido != null) {
						total++;
						sb.append(apellido).append(", ")
						  .append(rs.getString("firstName"))
						  .append(" (").append(rs.getString("email")).append(")\n");
					}
				}

				if (!existeOficina) {
					System.out.println("No existe oficina en " + ciudad);
				} else if (total == 0) {
					System.out.println("La oficina de " + ciudad + " no tiene empleados");
				} else {
					System.out.println("Hay " + total + " empleados en la oficina de " + ciudad + ". Sus datos son:");
					System.out.print(sb);
				}
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	private static String obtenerCodigoOficina(Connection cn, String ciudad) throws SQLException {
		try (PreparedStatement ps = cn.prepareStatement("select officeCode from offices where city = ?")) {
			ps.setString(1, ciudad);
			try (ResultSet rs = ps.executeQuery()) {
				return rs.next() ? rs.getString("officeCode") : null;
			}
		}
	}

	private static int contarEmpleados(Connection cn, String codigo) throws SQLException {
		try (PreparedStatement ps = cn.prepareStatement("select count(*) from employees where officeCode = ?")) {
			ps.setString(1, codigo);
			try (ResultSet rs = ps.executeQuery()) {
				rs.next();
				return rs.getInt(1);
			}
		}
	}
	
	
	public static void moverEmpleados(String origen, String destino) {

		System.out.println("Moviendo todos los empleados de " + origen + " a " + destino + ".");

		if (origen.equalsIgnoreCase(destino)) {
			System.out.println("La oficina de origen y de destino es la misma");
			return;
		}

		try (Connection cn = PoolConexion.getConnection()) {

			cn.setAutoCommit(false);

			try {
				String codOrigen = obtenerCodigoOficina(cn, origen);
				if (codOrigen == null) {
					System.out.println("No existe oficina en " + origen);
					return;
				}
				String codDestino = obtenerCodigoOficina(cn, destino);
				if (codDestino == null) {
					System.out.println("No existe oficina en " + destino);
					return;
				}

				int aMover = contarEmpleados(cn, codOrigen);
				if (aMover == 0) {
					System.out.println("La oficina de " + origen + " no tiene empleados");
					return;
				}

				System.out.println("Se van a mover " + aMover + " empleados de " + origen + " a " + destino);

				try (PreparedStatement ps = cn.prepareStatement(
						"update employees set officeCode = ? where officeCode = ?")) {
					ps.setString(1, codDestino);
					ps.setString(2, codOrigen);
					ps.executeUpdate();
				}

				int total = contarEmpleados(cn, codDestino);
				cn.commit();

				System.out.println("La oficina de " + destino + " tiene ahora " + total + " empleados");

			} catch (SQLException e) {
				cn.rollback();
				e.printStackTrace();
			} finally {
				cn.setAutoCommit(true);
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	
}



