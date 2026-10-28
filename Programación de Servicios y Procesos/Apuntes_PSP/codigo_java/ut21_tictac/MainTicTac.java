// ¿Sale TIC TAC TIC TAC ordenado? NO está garantizado:
// el planificador decide qué hilo se ejecuta en cada momento.
public class MainTicTac {
    public static void main(String[] args) {
        new HiloTic().start();
        new HiloTac().start();
    }
}
