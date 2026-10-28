import java.util.LinkedList;

// Monitor con sincronización por CONDICIÓN: wait() / notifyAll()
public class Buffer {

    private final LinkedList<Integer> cola = new LinkedList<>();
    private final int capacidad;

    public Buffer(int capacidad) {
        this.capacidad = capacidad;
    }

    public synchronized void poner(int dato) throws InterruptedException {
        while (cola.size() == capacidad) {   // SIEMPRE while, nunca if
            wait();                          // suelta el monitor y se duerme
        }
        cola.add(dato);
        System.out.println("Producido " + dato + "  -> buffer " + cola);
        notifyAll();                         // despierta a los que esperan
    }

    public synchronized int quitar() throws InterruptedException {
        while (cola.isEmpty()) {
            wait();
        }
        int dato = cola.removeFirst();
        System.out.println("Consumido " + dato + " <- buffer " + cola);
        notifyAll();
        return dato;
    }
}
