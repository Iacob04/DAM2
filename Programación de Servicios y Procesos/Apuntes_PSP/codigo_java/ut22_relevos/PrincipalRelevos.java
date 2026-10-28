import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class PrincipalRelevos {

    public static void main(String[] args) {
        CorrerRelevoEquipo relevoEspana = new CorrerRelevoEquipo("España");
        CorrerRelevoEquipo relevoEEUU = new CorrerRelevoEquipo("EEUU");

        String[] equipo1 = {"Pepe", "María", "Juan", "Marta"};
        String[] equipo2 = {"John", "Kate", "Larry", "Sarah"};

        ExecutorService executor = Executors.newFixedThreadPool(8);
        // Se lanzan los 8 a la vez: el synchronized hace que se "relevan" sin join.
        // OJO: el orden de los corredores dentro del equipo NO está garantizado.
        for (int i = 0; i < equipo1.length; i++) {
            executor.submit(new CorredorSync(equipo1[i], relevoEspana));
            executor.submit(new CorredorSync(equipo2[i], relevoEEUU));
        }
        executor.shutdown();
    }
}
