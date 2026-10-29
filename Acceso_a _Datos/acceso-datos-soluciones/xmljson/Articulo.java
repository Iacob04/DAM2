package xmljson;

public class Articulo {

    // Los nombres de los atributos son las claves que tendrá el JSON
    private String referencia;
    private String descripcion;
    private int stock;

    public Articulo(String referencia, String descripcion, int stock) {
        this.referencia = referencia;
        this.descripcion = descripcion;
        this.stock = stock;
    }
}
