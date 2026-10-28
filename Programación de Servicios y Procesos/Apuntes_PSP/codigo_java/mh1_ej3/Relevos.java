public class Relevos {

    public static void main(String[] args) throws InterruptedException {
        String[] nombres = {"Pepe", "María", "Juan", "Marta"};

        for (String n : nombres) {
            Thread t = new Thread(new Corredor(n, "España"));
            t.start();
            t.join();     // el siguiente no sale hasta que este acabe (pasa el testigo)
        }
        System.out.println("FINAL DE PROGRAMA");
    }
}
