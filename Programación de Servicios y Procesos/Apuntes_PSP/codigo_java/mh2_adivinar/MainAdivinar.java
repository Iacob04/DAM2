public class MainAdivinar {

    public static void main(String[] args) {
        int numJugadores = 3;
        Arbitro arbitro = new Arbitro(numJugadores);

        for (int i = 1; i <= numJugadores; i++) {
            new Jugador(i, arbitro).start();
        }
    }
}
