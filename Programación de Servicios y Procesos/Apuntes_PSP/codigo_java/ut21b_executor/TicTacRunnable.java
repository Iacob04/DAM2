// Versión con Runnable: una sola clase que recibe la palabra
public class TicTacRunnable implements Runnable {

    private String palabra;

    public TicTacRunnable(String palabra) {
        this.palabra = palabra;
    }

    @Override
    public void run() {
        while (true) {
            System.out.println(palabra);
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                return;
            }
        }
    }

    public static void main(String[] args) {
        new Thread(new TicTacRunnable("TIC")).start();
        new Thread(new TicTacRunnable("TAC")).start();
    }
}
