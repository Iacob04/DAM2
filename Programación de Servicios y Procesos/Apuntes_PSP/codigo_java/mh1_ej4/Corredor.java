// Ejercicio 3 - RELEVOS  (implements Runnable)
public class Corredor implements Runnable {

    private String nombre;
    private String equipo;

    public Corredor(String nombre, String equipo) {
        this.nombre = nombre;
        this.equipo = equipo;
    }

    // Corre 100 m: 10 tramos de 10 m, cada uno entre 950 y 1050 ms
    public void correr() {
        System.out.println(equipo + ":" + nombre + " comienza su relevo");
        long total = 0;
        try {
            for (int i = 0; i < 10; i++) {
                int ms = 950 + (int) (Math.random() * 101);   // 950..1050
                Thread.sleep(ms);
                total += ms;
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.printf("%s:%s ha acabado su relevo - Ha tardado: %.2f segundos%n",
                equipo, nombre, total / 1000.0);
    }

    @Override
    public void run() {
        correr();
    }
}
