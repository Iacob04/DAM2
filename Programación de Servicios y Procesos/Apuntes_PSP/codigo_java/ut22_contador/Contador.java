// Objeto COMPARTIDO por los dos hilos (UT2_2_1_CompartirInfo2)
public class Contador {

    private int c;

    public Contador(int inicial) {
        this.c = inicial;
    }

    // c++ NO es atómico: leer c -> sumar 1 -> guardar c.
    // Sin synchronized dos hilos pueden intercalarse y perder operaciones (condición de carrera)
    public synchronized void incrementa() {
        c++;
    }

    public synchronized void decrementa() {
        c--;
    }

    public synchronized int valor() {
        return c;
    }
}
