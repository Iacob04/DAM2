// Monitor de la playa: máximo 7 grupos a la vez y prioridad para las BARCAS
public class Playa {

    private static final int MAX_GRUPOS = 7;
    private int dentro = 0;
    private int barcasEsperando = 0;
    private boolean cerrada = false;

    public synchronized boolean entrar(boolean esBarca) throws InterruptedException {
        if (esBarca) barcasEsperando++;

        while (!cerrada && (dentro >= MAX_GRUPOS || (!esBarca && barcasEsperando > 0))) {
            wait();
        }

        if (esBarca) barcasEsperando--;
        if (cerrada) return false;
        dentro++;
        return true;
    }

    public synchronized void salir() {
        dentro--;
        notifyAll();
    }

    public synchronized void cerrar() {
        cerrada = true;
        notifyAll();
    }
}
