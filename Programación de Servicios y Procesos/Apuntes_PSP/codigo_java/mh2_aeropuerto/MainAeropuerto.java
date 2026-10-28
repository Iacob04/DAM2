public class MainAeropuerto {

    public static void main(String[] args) throws InterruptedException {
        Pista pista = new Pista();
        Aeroplano[] aeroplanos = new Aeroplano[20];
        int aviones = 0;

        Reloj reloj = new Reloj();
        reloj.setDaemon(true);        // daemon: no impide que el programa termine
        reloj.start();

        for (int i = 0; i < 20; i++) {
            boolean esAvion = Math.random() < 0.5;   // no se sabe cuántos de cada
            if (esAvion) aviones++;
            aeroplanos[i] = new Aeroplano("Thread-" + (i + 1), esAvion, pista);
            aeroplanos[i].start();
            Thread.sleep(2000);       // llega uno cada 2 minutos (2000 ms)
        }

        for (Aeroplano a : aeroplanos) a.join();      // esperar a que despeguen todos
        reloj.interrupt();
        System.out.println("Han despegado los 20 aeroplanos: " + aviones
                + " aviones y " + (20 - aviones) + " avionetas");
    }
}
