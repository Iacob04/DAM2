import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

// UT2_1_TIC_TAC_ExecutorService
public class TicTacExecutor {

    public static void main(String[] args) throws InterruptedException {
        ExecutorService executor = Executors.newFixedThreadPool(2);

        executor.execute(new TicTacRunnable("TIC"));   // execute: Runnable, sin resultado
        executor.submit(new TicTacRunnable("TAC"));    // submit: también vale (devuelve Future)

        Thread.sleep(5000);           // dejamos que suenen 5 segundos
        executor.shutdownNow();       // interrumpe los hilos (los bucles son infinitos)
        executor.awaitTermination(1, TimeUnit.SECONDS);
        System.out.println("Fin");
    }
}
