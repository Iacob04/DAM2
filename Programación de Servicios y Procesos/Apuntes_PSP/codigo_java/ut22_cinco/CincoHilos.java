// Ejercicio: 5 hilos incrementan 5000 veces una variable compartida
public class CincoHilos implements Runnable {

    private static int contador = 0;                    // compartida (static)
    private static final Object CERROJO = new Object(); // objeto monitor (final!)

    private boolean sincronizado;

    public CincoHilos(boolean sincronizado) {
        this.sincronizado = sincronizado;
    }

    @Override
    public void run() {
        for (int i = 0; i < 5000; i++) {
            if (sincronizado) {
                synchronized (CERROJO) {   // bloque sincronizado: solo 1 hilo a la vez
                    contador++;
                }
            } else {
                contador++;                // condición de carrera
            }
        }
    }

    private static int probar(boolean sync) throws InterruptedException {
        contador = 0;
        Thread[] hilos = new Thread[5];
        for (int i = 0; i < 5; i++) {
            hilos[i] = new Thread(new CincoHilos(sync));
            hilos[i].start();
        }
        for (Thread h : hilos) h.join();   // esperar a los 5
        return contador;
    }

    public static void main(String[] args) throws InterruptedException {
        System.out.println("Sin sincronizar: " + probar(false) + " (esperado 25000)");
        System.out.println("Sincronizado:    " + probar(true) + " (esperado 25000)");
    }
}
