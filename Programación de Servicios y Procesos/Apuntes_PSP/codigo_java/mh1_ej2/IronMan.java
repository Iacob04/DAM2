// Ejercicio 2 - IRON MAN  (implements Runnable)
// 1 hora = 1000 ms  ->  tiempo(ms) = distancia / velocidad * 1000
public class IronMan implements Runnable {

    private String nombre;
    private double velCorrer;   // km/h
    private double velBici;     // km/h
    private double velNadar;    // m/h

    public IronMan(String nombre, double velCorrer, double velBici, double velNadar) {
        this.nombre = nombre;
        this.velCorrer = velCorrer;
        this.velBici = velBici;
        this.velNadar = velNadar;
    }

    private void prueba(String nombrePrueba, double distancia, double velocidad) throws InterruptedException {
        System.out.println(nombre + " comienza la prueba en " + nombrePrueba);
        Thread.sleep((long) (distancia / velocidad * 1000));
        System.out.println(nombre + " ha acabado la prueba en " + nombrePrueba);
    }

    public void bici() throws InterruptedException     { prueba("bici", 180, velBici); }
    public void marathon() throws InterruptedException { prueba("marathon", 42, velCorrer); }
    public void nadar() throws InterruptedException    { prueba("natación", 4000, velNadar); }

    // Enlaza las tres pruebas
    @Override
    public void run() {
        long inicio = System.currentTimeMillis();
        try {
            bici();
            marathon();
            nadar();
            double horas = (System.currentTimeMillis() - inicio) / 1000.0;
            System.out.printf(">>> %s TERMINA el IronMan en %.1f horas%n", nombre, horas);
        } catch (InterruptedException e) {
            System.out.println(nombre + " abandona");
        }
    }

    public static void main(String[] args) {
        // Todos empiezan a la vez
        new Thread(new IronMan("Pedro", 6, 20, 1000)).start();   // 9 + 7 + 4 = 20 h
        new Thread(new IronMan("Juan", 7, 18, 2000)).start();    // 10 + 6 + 2 = 18 h
        new Thread(new IronMan("Vicente", 6, 30, 1000)).start(); // 6 + 7 + 4 = 17 h -> GANA
    }
}
