// Ejercicio 5 - El artificiero y la bomba  (extends Thread)
public class ArtificieroBomba extends Thread {

    private boolean esBomba;   // true = contador, false = artificiero

    public ArtificieroBomba(boolean esBomba) {
        this.esBomba = esBomba;
    }

    // Cuenta atrás 9..0 (1 s por número)
    public void contador() {
        try {
            for (int i = 9; i >= 0; i--) {
                System.out.println(i);
                Thread.sleep(1000);
            }
            System.out.println("PUUUUUUMMMMMMMMMM");
        } catch (InterruptedException e) {
            // Nos interrumpen = el artificiero la ha desactivado
            return;                 // return sale del hilo; System.exit(0) saldría del programa
        }
    }

    // Tarda entre 9500 y 10500 ms
    public void artificiero() {
        try {
            int ms = 9500 + (int) (Math.random() * 1001);
            System.out.println("El artificiero comienza a desactivar la bomba");
            Thread.sleep(ms);
            System.out.println("El artificiero ha desactivado la bomba");
        } catch (InterruptedException e) {
            return;                 // interrumpido = ha explotado
        }
    }

    @Override
    public void run() {
        if (esBomba) contador();
        else artificiero();
    }

    public static void main(String[] args) throws InterruptedException {
        ArtificieroBomba artificiero = new ArtificieroBomba(false);
        ArtificieroBomba bomba = new ArtificieroBomba(true);

        bomba.start();
        artificiero.start();

        // Esperamos mientras LOS DOS sigan vivos
        do {
            Thread.sleep(50);
        } while (bomba.isAlive() && artificiero.isAlive());

        if (bomba.isAlive()) {          // terminó antes el artificiero
            bomba.interrupt();
            System.out.println("La bomba ha sido desactivada");
        } else {                        // terminó antes la bomba
            artificiero.interrupt();
            System.out.println("El artificiero ha muerto");
        }
    }
}
