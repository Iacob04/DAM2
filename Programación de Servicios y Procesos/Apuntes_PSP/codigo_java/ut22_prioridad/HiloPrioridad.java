public class HiloPrioridad extends Thread {

    private MemoriaComun miMemoriaComun;

    public HiloPrioridad(int prioridad, MemoriaComun memoria) {
        this.miMemoriaComun = memoria;
        setPriority(prioridad);        // 1..10 (MIN_PRIORITY..MAX_PRIORITY)
    }

    // Ejecuta una tarea "pesada"
    @Override
    public void run() {
        for (int i = 0; i < 5000; ++i) {
            miMemoriaComun.pasar();
            Thread.yield();            // cede la CPU: el planificador elige otro hilo
        }
        System.out.println("Hilo de prioridad " + getPriority() + " termina ahora");
    }
}
