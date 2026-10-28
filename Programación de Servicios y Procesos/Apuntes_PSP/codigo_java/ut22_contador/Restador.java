public class Restador extends Thread {

    private Contador contador;

    public Restador(String nombre, Contador contador) {
        super(nombre);
        this.contador = contador;
    }

    @Override
    public void run() {
        for (int i = 0; i < 300; i++) {
            contador.decrementa();
            try {
                Thread.sleep(10);
            } catch (InterruptedException e) {
                return;
            }
        }
        System.out.println(getName() + " termina. Valor: " + contador.valor());
    }
}
