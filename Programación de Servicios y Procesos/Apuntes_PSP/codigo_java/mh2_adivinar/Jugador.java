public class Jugador extends Thread {

    private int id;
    private Arbitro arbitro;      // el mismo árbitro para todos

    public Jugador(int id, Arbitro arbitro) {
        this.id = id;
        this.arbitro = arbitro;
    }

    @Override
    public void run() {
        while (!arbitro.isAcabado()) {
            if (arbitro.getTurno() == id) {
                int numero = 1 + (int) (10 * Math.random());
                arbitro.comprobarJugada(id, numero);
            }
            try {
                Thread.sleep(10);   // evita consumir CPU sin parar mientras no es su turno
            } catch (InterruptedException e) {
                return;
            }
        }
    }
}
