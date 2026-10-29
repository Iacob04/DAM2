package examenficheros;

import java.io.Serializable;

public class Personaje implements Serializable {

    private static final long serialVersionUID = 1L;

    private String anime;
    private String nombre;

    public Personaje(String anime, String nombre) {
        this.anime = anime;
        this.nombre = nombre;
    }

    public String getAnime() { return anime; }
    public String getNombre() { return nombre; }

    @Override
    public String toString() {
        return nombre + " (" + anime + ")";
    }
}
