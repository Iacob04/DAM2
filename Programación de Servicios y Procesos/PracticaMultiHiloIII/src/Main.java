import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

// PROCESO PADRE (el barco): lanza 5 procesos Balsa y atiende a cada uno con un hilo.
public class Main {

    static final int NUM_BALSAS = 5;
    static final int PLAZAS = 15;
    static final int CAPACIDAD_PLAYA = 2;

    // ---------- ISLA ----------
    static class Isla {
        private int naufragos;
        Isla(int n) { naufragos = n; }

        synchronized int[] embarcar(int plazas) {
            int suben = Math.min(plazas, naufragos);
            naufragos -= suben;
            return new int[]{suben, naufragos};
        }
    }

    // ---------- PLAYA (2 a la vez, balsa 1 con prioridad) ----------
    static class Playa {
        private static class Turno implements Comparable<Turno> {
            final int id; final long ticket;
            Turno(int id, long ticket) { this.id = id; this.ticket = ticket; }
            public int compareTo(Turno o) {
                int p1 = id == 1 ? 0 : 1, p2 = o.id == 1 ? 0 : 1;
                if (p1 != p2) return Integer.compare(p1, p2);
                return Long.compare(ticket, o.ticket);
            }
        }
        private final PriorityQueue<Turno> cola = new PriorityQueue<>();
        private int ocupadas = 0;
        private long tickets = 0;

        synchronized void entrar(int id) throws InterruptedException {
            Turno yo = new Turno(id, tickets++);
            cola.add(yo);
            while (ocupadas >= CAPACIDAD_PLAYA || cola.peek() != yo) wait();
            cola.poll();
            ocupadas++;
            notifyAll();
        }

        synchronized void salir() { ocupadas--; notifyAll(); }
    }

    // ---------- HILO que atiende a un proceso Balsa ----------
    static class Atendedor implements Runnable {
        final int id;
        final Isla isla;
        final Playa playa;
        volatile int rescatados = 0;

        Atendedor(int id, Isla isla, Playa playa) {
            this.id = id; this.isla = isla; this.playa = playa;
        }

        @Override
        public void run() {
            Process p = null;
            try {
                String java = System.getProperty("java.home") + File.separator + "bin" + File.separator + "java";
                String cp = System.getProperty("java.class.path");
                ProcessBuilder pb = new ProcessBuilder(java, "-cp", cp, "Balsa", String.valueOf(id));
                pb.redirectError(ProcessBuilder.Redirect.DISCARD);
                p = pb.start();

                BufferedReader in = new BufferedReader(new InputStreamReader(p.getInputStream()));
                PrintWriter out = new PrintWriter(p.getOutputStream(), true);

                String msg;
                while ((msg = in.readLine()) != null) {
                    switch (msg) {
                        case "ENTRAR":
                            playa.entrar(id);
                            out.println("OK");
                            break;
                        case "EMBARCAR":
                            int[] r = isla.embarcar(PLAZAS);
                            rescatados += r[0];
                            System.out.println("\nQuedan " + r[1] + " náufragos");
                            System.out.println("La balsa actual es Balsa " + id + " y rescatamos a  " + r[0]);
                            out.println(r[0] + " " + r[1]);
                            break;
                        case "SALIR":
                            playa.salir();
                            out.println("OK");
                            break;
                        case "FIN":
                            System.out.println("\nYa no quedan náufragos y la Balsa " + id
                                    + " está volviendo a su puesto. Ha rescatado " + rescatados + " naufragos en total");
                            break;
                    }
                }
                p.waitFor();
            } catch (Exception e) {
                e.printStackTrace();
                if (p != null) p.destroyForcibly();
            }
        }
    }

    // ---------- CONTADOR ----------
    static class Contador implements Runnable {
        private volatile boolean parar = false;
        void parar() { parar = true; }

        public void run() {
            int seg = 0;
            try {
                while (!parar) {
                    System.out.println(seg++);
                    Thread.sleep(1000);
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
            System.out.println("Contador parado");
        }
    }

    public static void main(String[] args) throws Exception {
        int total = 800 + new Random().nextInt(201);
        Isla isla = new Isla(total);
        Playa playa = new Playa();

        for (int i = 1; i <= NUM_BALSAS; i++)
            System.out.println("La balsa actual es Balsa " + i + " y tiene  " + PLAZAS + " plazas");
        System.out.println("\nQuedan " + total + " náufragos");

        Contador contador = new Contador();
        long inicio = System.currentTimeMillis();

        ExecutorService executor = Executors.newFixedThreadPool(NUM_BALSAS + 1);
        executor.execute(contador);

        Atendedor[] at = new Atendedor[NUM_BALSAS];
        List<Future<?>> futuros = new ArrayList<>();
        for (int i = 0; i < NUM_BALSAS; i++) {
            at[i] = new Atendedor(i + 1, isla, playa);
            futuros.add(executor.submit(at[i]));
        }

        for (Future<?> f : futuros) f.get();   // espera a que acaben todos los procesos

        contador.parar();
        executor.shutdown();
        executor.awaitTermination(10, TimeUnit.SECONDS);
        long fin = System.currentTimeMillis();

        System.out.println("\nEl rescate ha concluido");
        System.out.println("\n===== INFORME DEL RESCATE =====");
        System.out.println("Náufragos iniciales: " + total);
        int suma = 0;
        for (Atendedor a : at) {
            System.out.println("Balsa " + a.id + ": " + a.rescatados + " náufragos");
            suma += a.rescatados;
        }
        System.out.println("Total rescatados: " + suma);
        System.out.printf("Tiempo total: %.2f segundos%n", (fin - inicio) / 1000.0);
    }
}
