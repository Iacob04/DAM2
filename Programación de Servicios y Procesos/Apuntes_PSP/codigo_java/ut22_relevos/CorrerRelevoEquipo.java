// UT2_2_2_Relevos_Sync: objeto compartido por los 4 corredores de UN equipo
public class CorrerRelevoEquipo {

    private String equipo;

    public CorrerRelevoEquipo(String equipo) {
        this.equipo = equipo;
    }

    // synchronized: mientras un corredor del equipo corre, los demás de ESE equipo esperan.
    // El otro equipo tiene su propio objeto -> su propio monitor -> corre en paralelo.
    public synchronized void carrera(String corredor) {
        try {
            int numero = 0;
            System.out.printf("%s:%s comienza su relevo%n", equipo, corredor);
            for (int i = 0; i < 10; i++) {
                int ms = (int) (Math.random() * (1050 - 950 + 1) + 950);
                numero += ms;
                Thread.sleep(ms);
            }
            System.out.printf("%s:%s ha acabado su relevo - Ha tardado: %.2f segundos%n",
                    equipo, corredor, numero / 1000.0);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
