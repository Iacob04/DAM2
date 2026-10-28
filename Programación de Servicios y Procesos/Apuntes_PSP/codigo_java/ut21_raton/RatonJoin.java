// 0_Ejemplo_Raton_Join: el main espera a que acaben todos con join()
public class RatonJoin {

    public static void main(String[] args) throws InterruptedException {
        Thread fievel = new Thread(new RatonRunnable("Fievel", 4));
        Thread jerry  = new Thread(new RatonRunnable("Jerry", 5));
        Thread pinky  = new Thread(new RatonRunnable("Pinky", 3));

        long inicio = System.currentTimeMillis();
        fievel.start();
        jerry.start();
        pinky.start();

        // join(): el hilo que lo llama (main) se BLOQUEA hasta que ese hilo termine
        fievel.join();
        jerry.join();
        pinky.join();

        System.out.println("Todos han comido en " + (System.currentTimeMillis() - inicio) / 1000.0 + " s");
    }
}
