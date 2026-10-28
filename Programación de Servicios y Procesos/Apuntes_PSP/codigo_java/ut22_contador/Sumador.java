public class Sumador extends Thread {

    private Contador contador;

    public Sumador(String nombre, Contador contador) {
        super(nombre);                 // nombre del hilo
        this.contador = contador;
    }

    @Override
    public void run() {
        for (int i = 0; i < 300; i++) {
            contador.incrementa();
            try {
                Thread.sleep(10);
            } catch (InterruptedException e) {
                return;
            }
        }
        System.out.println(getName() + " termina. Valor: " + contador.valor());
    }
}
