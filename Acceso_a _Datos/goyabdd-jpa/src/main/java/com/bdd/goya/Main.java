package com.bdd.goya;

import java.util.logging.Logger;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;

public class Main {
	
	private static final Logger LOG_HIBERNATE = Logger.getLogger("org.hibernate");

	public static void main(String[] args) {
		
		EntityManagerFactory emf = Persistence.createEntityManagerFactory("goyaPU");
		EntityManager em = emf.createEntityManager();
		try {
		em.getTransaction().begin();
		
		Alumno a = new Alumno("12345678D", "Gabriel Alexandru", "Iacob",22, 400.00); em.persist(a);
		a = new Alumno("12345678H", "Sergio", "Serrano",22, 400.00);em.persist(a);
		a = new Alumno("12345678J", "Jordan", "Zuñiga",22, 400.00);em.persist(a);
		a = new Alumno("12345678K", "Kevin", "Villaroel",26, 400.00);em.persist(a);
		a = new Alumno("53464097E", "Pancracio", "Pocholo Chorizo", 37, 263.28); em.persist(a);
		a = new Alumno("19722233D", "Eustaquio", "Berenjena Sinceballa", 28, 963.87); em.persist(a);
		a = new Alumno("17784483D", "Filomena", "Salchichón Mondongo", 18, 312.65); em.persist(a);
		a = new Alumno("68202938A", "Rigoberto", "Gazpacho Churrasco", 24, 318.89); em.persist(a);
		a = new Alumno("83960310Y", "Hermenegilda", "Gazpacho Delmonte", 43, 941.15); em.persist(a);
		a = new Alumno("26616417N", "Cucufato", "Zampabollos Rebanada", 37, 964.14); em.persist(a);
		a = new Alumno("18302983C", "Bartolo", "Pelotilla Pelotilla", 29, 264.99); em.persist(a);
		a = new Alumno("39673100D", "Leocadia", "Delmonte Berenjena", 44, 374.55); em.persist(a);
		a = new Alumno("48870700R", "Gumersindo", "Gazpacho Pocholo", 34, 354.39); em.persist(a);
		a = new Alumno("86626738K", "Petronila", "Calcetín Berenjena", 43, 1093.91); em.persist(a);
		a = new Alumno("34256684R", "Anacleto", "Sinceballa Pelotilla", 35, 1037.43); em.persist(a);
		a = new Alumno("35215622T", "Remedios", "Pimientos Sinceballa", 34, 1133.37); em.persist(a);
		a = new Alumno("18427393T", "Wenceslao", "Pelotilla Delmonte", 36, 469.95); em.persist(a);
		a = new Alumno("76627625C", "Fulgencio", "Fideo Berenjena", 30, 1218.72); em.persist(a);
		a = new Alumno("52164119G", "Facundo", "Rascachapas Pelotilla", 31, 673.93); em.persist(a);
		a = new Alumno("50234045K", "Casimira", "Zampabollos Croqueta", 39, 1222.13); em.persist(a);
		a = new Alumno("42762079L", "Ataúlfo", "Churrasco Pelotilla", 26, 888.38); em.persist(a);
		a = new Alumno("76453392N", "Romualda", "Bocadillo Albóndiga", 31, 577.40); em.persist(a);
		a = new Alumno("91733095X", "Segismundo", "Churrasco Sinceballa", 33, 748.04); em.persist(a);
		a = new Alumno("32140838V", "Escolástica", "Chistorra Bocadillo", 21, 1423.25); em.persist(a);
		a = new Alumno("75627516C", "Nicanor", "Gazpacho Delmonte", 38, 301.73); em.persist(a);
		a = new Alumno("84903659X", "Brígida", "Pelotilla Bocadillo", 27, 1111.33); em.persist(a);
		a = new Alumno("57000147Y", "Teodomiro", "Cachopo Morcilla", 35, 1244.50); em.persist(a);
		a = new Alumno("71230843G", "Pascasia", "Churrasco Churrasco", 25, 821.41); em.persist(a);
		a = new Alumno("99141000Y", "Hilarión", "Churrasco Delmonte", 40, 1119.45); em.persist(a);
		a = new Alumno("51554798E", "Domitila", "Rebanada Pelotilla", 38, 1277.31); em.persist(a);
		a = new Alumno("69812891W", "Ambrosio", "Calcetín Mejillón", 29, 1362.66); em.persist(a);
		a = new Alumno("99745048M", "Clotilde", "Pimientos Patatín", 31, 665.91); em.persist(a);
		a = new Alumno("32555071C", "Saturnino", "Cachopo Sinceballa", 32, 277.27); em.persist(a);
		a = new Alumno("39287351Q", "Rosendo", "Chistorra Calcetín", 21, 1167.78); em.persist(a);
		
		
		em.getTransaction().commit();
		em.close();
		emf.close();
		
		}catch (Exception e) {
			System.out.println("Error: " + e.getMessage());
			em.getTransaction().rollback();
		}

	}
	
	

}
