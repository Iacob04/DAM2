package com.bdd.goya;


import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "alumnos")
public class Alumno {
	@Id
	@Column(name = "DNI", length = 9 , nullable = true , unique = true)
	private String id;
	@Column(name = "Nombre", length = 50, nullable = true, unique = false)
	private String nombre;
	@Column(name = "Apellidos", length = 100, nullable = true, unique = false)
	private String apellidos;
	@Column(name = "edad", length = 2, nullable = true, unique = false)
	private int edad;
	@Column(name = "Matricula", length = 5, nullable = true, unique = false)
	private Double matricula;

	// JPA exige siempre un constructor sin argumentos
	public Alumno() {
	}

	public Alumno( String id, String nombre, String apellidos, int edad, Double matricula) {
		this.id = id;
		this.nombre = nombre;
		this.apellidos = apellidos;
		this.edad = edad;
		this.matricula = matricula;
	}

	public String getDNI() {
		return id;
	}

	public String getNombre() {
		return nombre;
	}

	public double getMatricula() {
		return matricula;
	}
	
	public String getApellidos() {
		return apellidos;
	}
	
	public int getEdad() {
		return edad;
	}

	public void setDNI(String id) {
		this.id = id;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}
	
	public void setApellidos(String apellidos) {
		this.apellidos = apellidos;
	}
	
	public void setEdad(int edad) {
		this.edad = edad;
	}
	
	public void setMatricula(Double matricula) {
		this.matricula = matricula;
	}
	
	
	@Override
	public String toString() {
		String salida = "";
		salida = "DNI: "+ id + "\nNombre y Apellidos: "+ nombre + " "+ apellidos + "\nEdad: "+ edad + "\nCoste Matrícula: "+ matricula;
		return salida;
	}

}

