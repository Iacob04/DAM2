// Monitor para la PLAYA y para el DESEMBARCO en alta mar:
// dentro solo puede haber lanchas o solo botes, nunca los dos tipos a la vez
public class Zona {

    private String nombre;
    private String tipoDentro = null;
    private int dentro = 0;
    private int esperandoLanchas = 0, esperandoBotes = 0;
    private boolean cerrada = false;

    public Zona(String nombre) {
        this.nombre = nombre;
    }

    private int esperandoOtro(String tipo) {
        return tipo.equals("LANCHA") ? esperandoBotes : esperandoLanchas;
    }

    // Devuelve false si la zona se ha cerrado (fin de la operación)
    public synchronized boolean entrar(String tipo) throws InterruptedException {
        if (tipo.equals("LANCHA")) esperandoLanchas++; else esperandoBotes++;

        // Puede entrar si está vacía, o si dentro hay de su tipo y el otro tipo no espera
        // (lo segundo evita que un tipo acapare la zona: INANICIÓN)
        while (!cerrada && !(dentro == 0
                || (tipo.equals(tipoDentro) && esperandoOtro(tipo) == 0))) {
            wait();
        }

        if (tipo.equals("LANCHA")) esperandoLanchas--; else esperandoBotes--;
        if (cerrada) return false;
        tipoDentro = tipo;
        dentro++;
        return true;
    }

    public synchronized void salir() {
        dentro--;
        if (dentro == 0) tipoDentro = null;
        notifyAll();
    }

    public synchronized void cerrar() {
        cerrada = true;
        notifyAll();      // despierta a los que esperaban para que terminen
    }
}
