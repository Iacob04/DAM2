// Datos COMPARTIDOS de toda la operación (monitor)
public class Operacion {

    // 1 h = 1000 ms. Para probar rápido: java -Dhora=20 Dinamo
    public static final int HORA = Integer.getInteger("hora", 1000);
    public static final int DIAS = 6;

    private int enPlaya;
    private int rescatados = 0;          // llegados a los barcos de alta mar
    private int llegadosPorSuCuenta = 0; // iban en lancha/bote al acabar los 6 días
    private int enTransito = 0;          // a bordo de lanchas o botes ahora mismo
    private volatile boolean fin = false;

    public Operacion(int soldados) {
        this.enPlaya = soldados;
    }

    // Saca de la playa como mucho 'max' soldados
    public synchronized int embarcar(int max) {
        int n = Math.min(max, enPlaya);
        enPlaya -= n;
        enTransito += n;
        return n;
    }

    public synchronized void desembarcar(int n) {
        enTransito -= n;
        rescatados += n;
    }

    public synchronized void vuelvenPorSuCuenta(int n) {
        enTransito -= n;
        llegadosPorSuCuenta += n;
    }

    public synchronized int getEnPlaya()     { return enPlaya; }
    public synchronized int getRescatados()  { return rescatados; }
    public synchronized int getEnTransito()  { return enTransito; }
    public synchronized int getPorSuCuenta() { return llegadosPorSuCuenta; }

    public boolean isFin()   { return fin; }
    public void terminar()   { fin = true; }
}
