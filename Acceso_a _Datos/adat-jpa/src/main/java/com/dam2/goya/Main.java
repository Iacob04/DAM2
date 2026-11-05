package com.dam2.goya;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;

import java.util.List;
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
		try {
			
			String jpql = "FROM Producto p WHERE p.precio > ?1 AND p.nombre like ?2";
			List<Producto> lista = em.createQuery(jpql,Producto.class).setParameter(1, 100).setParameter(2, "Monitor full HD").getResultList();
			
			/*String jpql = "FROM Producto p WHERE p.precio > :precioMax AND p.nombre like :nombreProd";
			List<Producto> lista = em.createQuery(jpql,Producto.class).setParameter("precioMax", 100).setParameter("nombreProd", "Monitor full HD").getResultList();*/
			
			for(Producto p: lista) {
				System.out.println(p);
			
			}
			
			/*String jpql = "SELECT p.nombre, p.precio FROM Producto p ";
			List<Object[]> lista = em.createQuery(jpql,Object[].class).getResultList();
			
			for(Object[] p: lista) {
				System.out.println((String)p[0] + " - "+ (Double)p[1] +("€"));
			
			}*/
			
			//String jpql = "SELECT p.nombre FROM Producto p WHERE p.precio > 30 ORDER BY p.precio DESC ";
			//List<String> lista = em.createQuery(jpql,String.class).getResultList();
			
			/*for(String p: lista) {
				System.out.println(p);
			
			}*/
			
			//String jpql = "FROM Producto p WHERE p.precio > 30 ORDER BY p.precio DESC ";
			
			//List<Producto> lista = em.createQuery(jpql,Producto.class).getResultList();
			
			/*for(Producto p: lista) {
				System.out.println(p);
			
			}*/
			
			
			
			System.out.println("Total de productos en la tabla: " + lista.size());
			
			/*
			 * em.getTransaction().begin();
			 * 
			 * Producto p = new Producto("Teclado", 25.5); em.persist(p);
			 * 
			 * p = new Producto("Teclado mecánico", 29.5); em.persist(p);
			 * 
			 * p = new Producto("RAM 32GB DDR5", 450.5); em.persist(p);
			 * 
			 * p = new Producto("Raton Razer", 50.5); em.persist(p);
			 * 
			 * p = new Producto("Monitor full HD", 150.55); em.persist(p);
			 * 
			 * em.getTransaction().commit();
			 */

			// System.out.println("Producto guardado con id " + p.getId());

		} catch (Exception e) {
			System.out.println("Error: " + e.getMessage());
			// em.getTransaction().rollback();
		}

		em.close();
		emf.close();
	}
}