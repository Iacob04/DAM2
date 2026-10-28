public class Operacion {

    public static final int HORA = Integer.getInteger("hora", 1000);
    public static final int DIAS = 6;

    private int enPlaya;
    private int rescatados = 0;
    private int llegadosPorSuCuenta = 0;
    private int enTransito = 0;
    private volatile boolean fin = false;

    public Operacion(int soldados) { this.enPlaya = soldados; }

    public synchronized int embarcar(int max) {
        int n = Math.min(max, enPlaya);
        enPlaya -= n;
        enTransito += n;
        return n;
    }

    public synchronized void desembarcar(int n)       { enTransito -= n; rescatados += n; }
    public synchronized void vuelvenPorSuCuenta(int n) { enTransito -= n; llegadosPorSuCuenta += n; }

    public synchronized int getEnPlaya()     { return enPlaya; }
    public synchronized int getRescatados()  { return rescatados; }
    public synchronized int getEnTransito()  { return enTransito; }
    public synchronized int getPorSuCuenta() { return llegadosPorSuCuenta; }

    public boolean isFin() { return fin; }
    public void terminar() { fin = true; }
}
