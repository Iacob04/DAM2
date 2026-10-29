package examen1;

import java.util.ArrayList;
import java.util.List;

public class Marvel {
	
	private String titulo;
	private int anio;
	private int fase;
	private List<String> director;
	
	
	public Marvel (String titulo, int anio, int fase,List<String> diector) {
		
		this.titulo = titulo;
		this.anio = anio;
		this.fase = fase;
		this.director = new ArrayList<>(director);
		
	}



    public String getTitulo() {
        return titulo;
    }

    public int getAnio() {
        return anio;
    }

    public int getFase() {
        return fase;
    }
    
    public List<String> getDirector() {
        return director;
    }

    public void setDirector(List<String> director) {
        this.director = director;
    }

    @Override
    public String toString() {
        String entrada =  anio + "(Fase " + fase+") " + titulo+" ["+ String.join(" y ", director + " ]");
       
        return entrada;
    }



	public getPeliculas() {
		
		
	}

}
