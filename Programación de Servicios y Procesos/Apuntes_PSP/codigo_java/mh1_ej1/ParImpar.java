// Ejercicio 1 - PAR / IMPAR  (extends Thread)
public class ParImpar extends Thread {

    private boolean pares;     // true = pares, false = impares

    public ParImpar(boolean pares) {
        this.pares = pares;
    }

    public void mostrar() {
        int inicio = pares ? 2 : 1;
        for (int i = inicio; i <= 10; i += 2) {
            System.out.println(i);
        }
    }

    @Override
    public void run() {
        mostrar();
    }

    public static void main(String[] args) {
        ParImpar hiloPares = new ParImpar(true);
        ParImpar hiloImpares = new ParImpar(false);
        hiloImpares.start();
        hiloPares.start();
        // Cada ejecución puede salir en un orden distinto: 1 2 4 3 6 5 8 7 10 9...
    }
}
