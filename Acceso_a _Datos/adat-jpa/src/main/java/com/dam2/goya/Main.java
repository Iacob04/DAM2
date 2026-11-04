package com.dam2.goya;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Main {
// Atributo estático para deshabilitar el logger de hibernate
	private static final Logger LOG_HIBERNATE = Logger.getLogger("org.hibernate");

	public static void main(String[] args) {
// Le decimos a hibernate que solo muestre los errores y no mensajes informativos
		LOG_HIBERNATE.setLevel(Level.SEVERE);
		EntityManagerFactory emf = Persistence.createEntityManagerFactory("clasePU");
		EntityManager em = emf.createEntityManager();
		em.getTransaction().begin();
		Producto p = new Producto("Teclado", 25.5);
		em.persist(p);
		em.getTransaction().commit();
		System.out.println("Producto guardado con id " + p.getId());
		em.close();
		emf.close();
	}
}