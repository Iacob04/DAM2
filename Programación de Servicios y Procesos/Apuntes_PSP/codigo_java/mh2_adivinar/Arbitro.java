// Árbitro: lo COMPARTEN todos los jugadores (monitor)
public class Arbitro {

    private int numJugadores;
    private int turno = 1;            // empieza el jugador 1
    private int numeroAdivinar;
    private boolean acabado = false;

    public Arbitro(int numJugadores) {
        this.numJugadores = numJugadores;
        this.numeroAdivinar = 1 + (int) (10 * Math.random());   // 1..10
        System.out.println("NÚMERO A ADIVINAR: " + numeroAdivinar);
    }

    public synchronized int getTurno() {
        return turno;
    }

    public synchronized boolean isAcabado() {
        return acabado;
    }

    // synchronized: mientras un jugador hace su jugada nadie más puede interferir
    public synchronized void comprobarJugada(int jugador, int numero) {
        if (acabado || jugador != turno) {
            return;                   // no es su turno (o ya terminó): se ignora
        }
        System.out.println("Jugador" + jugador + " dice: " + numero);

        if (numero == numeroAdivinar) {
            acabado = true;
            System.out.println("Jugador " + jugador + " gana, adivinó el número!!!");
        } else {
            turno = turno % numJugadores + 1;     // 1 -> 2 -> 3 -> 1 ...
            System.out.println("\tLe toca a Jug" + turno);
        }
    }
}
