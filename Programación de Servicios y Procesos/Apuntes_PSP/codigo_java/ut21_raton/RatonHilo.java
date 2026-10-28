// 0_Ejemplo_Raton_Hilos: cada ratón es un hilo (extends Thread)
public class RatonHilo extends Thread {

    private String nombre;
    private int tiempoAlimentacion;

    public RatonHilo(String nombre, int tiempoAlimentacion) {
        this.nombre = nombre;
        this.tiempoAlimentacion = tiempoAlimentacion;
    }

    // Lo que hace el hilo va SIEMPRE en run()
    @Override
    public void run() {
        try {
            System.out.printf("El ratón %s ha comenzado a alimentarse%n", nombre);
            Thread.sleep(tiempoAlimentacion * 1000L);
            System.out.printf("El ratón %s ha terminado de alimentarse%n", nombre);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        // start() crea el hilo y llama a run(). Si llamas a run() directamente NO hay hilo nuevo
        new RatonHilo("Fievel", 4).start();
        new RatonHilo("Jerry", 5).start();
        new RatonHilo("Pinky", 3).start();
        new RatonHilo("Mickey", 6).start();
        // Tiempo total ≈ el del más lento (6 s)
    }
}
