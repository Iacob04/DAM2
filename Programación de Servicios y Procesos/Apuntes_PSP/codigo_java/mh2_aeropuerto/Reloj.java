// Muestra los segundos (minutos simulados) para documentar la simulación
public class Reloj extends Thread {

    @Override
    public void run() {
        int segundos = 0;
        try {
            while (true) {
                System.out.println(segundos++);
                Thread.sleep(1000);
            }
        } catch (InterruptedException e) {
            // fin del reloj
        }
    }
}
