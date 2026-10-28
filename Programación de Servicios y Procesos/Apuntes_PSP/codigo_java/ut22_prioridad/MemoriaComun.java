public class MemoriaComun {
    private int pasos = 0;
    // Método compartido y sincronizado por todos los hilos
    public synchronized void pasar() {
        pasos++;
    }
}
