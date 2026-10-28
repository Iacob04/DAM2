public class CompartirInfo {

    public static void main(String[] args) throws InterruptedException {
        // UN SOLO objeto Contador para los dos hilos
        Contador contador = new Contador(100);

        Sumador s = new Sumador("Sumador", contador);
        Restador r = new Restador("Restador", contador);
        s.start();
        r.start();
        s.join();
        r.join();

        // Con synchronized siempre 100. Sin él, a veces 98, 103...
        System.out.println("Valor final: " + contador.valor());
    }
}
