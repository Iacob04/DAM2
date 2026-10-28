public class ProductorConsumidor {

    public static void main(String[] args) {
        Buffer buffer = new Buffer(3);

        Thread productor = new Thread(() -> {
            try {
                for (int i = 1; i <= 10; i++) {
                    buffer.poner(i);
                    Thread.sleep(100);
                }
            } catch (InterruptedException e) { }
        });

        Thread consumidor = new Thread(() -> {
            try {
                for (int i = 1; i <= 10; i++) {
                    buffer.quitar();
                    Thread.sleep(300);       // más lento: el buffer se llena y el productor espera
                }
            } catch (InterruptedException e) { }
        });

        productor.start();
        consumidor.start();
    }
}
