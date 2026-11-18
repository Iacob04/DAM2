package main;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import model.*;

public class Main {
	
	private static final Logger LOG_HIBERNATE = Logger.getLogger("org.hibernate");

	public static void main(String[] args) {
		

		LOG_HIBERNATE.setLevel(Level.SEVERE);
		EntityManagerFactory emf = Persistence.createEntityManagerFactory("clasePU");
		EntityManager em = emf.createEntityManager();
		try {
			listadoNombresEmpleados(em);
		} catch (Exception e) {
			System.out.println("Error: " + e.getMessage());
		}
		em.close();
		emf.close();

	}

	public static void listadoNombresEmpleados(EntityManager em) {
		String jpql = "SELECT e FROM Employees e";
		List<Employees> listaNombres = em.createQuery(jpql, Employees.class).getResultList();
		
		for (Employees e : listaNombres) {
			System.out.printf("%9s | %8s | %s\n", e.getLastName(),e.getFirstName(),e.getEmail());
		}
	}

}
