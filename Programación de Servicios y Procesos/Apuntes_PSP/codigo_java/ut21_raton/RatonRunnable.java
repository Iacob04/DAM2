// 0_Ejemplo_Raton_Runnable: implementando la interfaz Runnable
public class RatonRunnable implements Runnable {

    private String nombre;
    private int tiempoAlimentacion;

    public RatonRunnable(String nombre, int tiempoAlimentacion) {
        this.nombre = nombre;
        this.tiempoAlimentacion = tiempoAlimentacion;
    }

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
        // Con Runnable hay que envolver el objeto en un Thread
        new Thread(new RatonRunnable("Fievel", 4)).start();
        new Thread(new RatonRunnable("Jerry", 5)).start();
        new Thread(new RatonRunnable("Pinky", 3)).start();
        new Thread(new RatonRunnable("Mickey", 6)).start();
    }
}
