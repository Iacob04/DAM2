import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

// UT3_7_PoolHilos: 10 tareas repartidas entre solo 3 hilos reutilizados
public class PoolHilos {

    public static void main(String[] args) throws InterruptedException {
        ExecutorService pool = Executors.newFixedThreadPool(3);

        for (int i = 1; i <= 10; i++) {
            final int tarea = i;                    // las lambdas solo ven variables "final"
            pool.submit(() -> {
                System.out.println("Tarea " + tarea + " en " + Thread.currentThread().getName());
                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            });
        }

        pool.shutdown();                            // no acepta más tareas; deja terminar las que hay
        pool.awaitTermination(1, TimeUnit.MINUTES); // como un join() de todas las tareas
        System.out.println("¿Terminado? " + pool.isTerminated());
    }
}
