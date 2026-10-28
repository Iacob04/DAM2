// 0_Ejemplo_Raton: versión SECUENCIAL (sin hilos)
public class Raton {

    private String nombre;
    private int tiempoAlimentacion;   // segundos

    public Raton(String nombre, int tiempoAlimentacion) {
        this.nombre = nombre;
        this.tiempoAlimentacion = tiempoAlimentacion;
    }

    public void comer() {
        try {
            System.out.printf("El ratón %s ha comenzado a alimentarse%n", nombre);
            Thread.sleep(tiempoAlimentacion * 1000L);
            System.out.printf("El ratón %s ha terminado de alimentarse%n", nombre);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        // Uno detrás de otro: tiempo total = 4+5+3+6 = 18 s
        new Raton("Fievel", 4).comer();
        new Raton("Jerry", 5).comer();
        new Raton("Pinky", 3).comer();
        new Raton("Mickey", 6).comer();
    }
}
