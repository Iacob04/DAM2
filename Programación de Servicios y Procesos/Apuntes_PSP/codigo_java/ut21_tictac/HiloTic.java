public class HiloTic extends Thread {
    @Override
    public void run() {
        while (true) {
            System.out.println("TIC");
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                return;            // si lo interrumpen, termina el hilo
            }
        }
    }
}
