package ordenes.ejecucion;

public class OrdenNoValida extends RuntimeException {
    public OrdenNoValida(String message) {
        super(message);
    }
}
