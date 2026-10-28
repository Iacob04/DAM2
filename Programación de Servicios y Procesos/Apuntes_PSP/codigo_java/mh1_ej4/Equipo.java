// Ejercicio 4 - CARRERA DE RELEVOS
// Cada equipo es un hilo (extends Thread) que lanza a sus corredores uno tras otro con join()
public class Equipo extends Thread {

    private String pais;
    private String[] corredores;

    public Equipo(String pais, String[] corredores) {
        this.pais = pais;
        this.corredores = corredores;
    }

    @Override
    public void run() {
        try {
            for (String c : corredores) {
                Thread t = new Thread(new Corredor(c, pais));
                t.start();
                t.join();     // join DENTRO del hilo del equipo: solo espera este equipo
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("FINAL DE PROGRAMA - " + pais);
    }

    public static void main(String[] args) {
        // Los dos equipos corren a la vez
        new Equipo("España", new String[] {"Pepe", "María", "Juan", "Marta"}).start();
        new Equipo("EEUU", new String[] {"John", "Kim", "Mike", "Sarah"}).start();
    }
}
