// Cada hilo es la flota de lanchas o la de botes salvavidas
public class Embarcacion extends Thread {

    private String tipo;
    private int min, max;
    private Operacion op;
    private Zona playa, altaMar;
    private int viajes = 0, total = 0;

    public Embarcacion(String nombre, String tipo, int min, int max,
                       Operacion op, Zona playa, Zona altaMar) {
        super(nombre);
        this.tipo = tipo;
        this.min = min;
        this.max = max;
        this.op = op;
        this.playa = playa;
        this.altaMar = altaMar;
    }

    @Override
    public void run() {
        try {
            while (!op.isFin() && op.getEnPlaya() > 0) {
                // ---- 1. PLAYA ----
                if (!playa.entrar(tipo)) break;          // operación terminada
                int capacidad = min + (int) (Math.random() * (max - min + 1));
                int soldados = op.embarcar(capacidad);
                if (soldados == 0) {                     // ya no queda nadie
                    playa.salir();
                    break;
                }
                System.out.printf("  %s embarca %d soldados (quedan %d en la playa)%n",
                        getName(), soldados, op.getEnPlaya());
                Thread.sleep(Operacion.HORA / 2);        // embarcar: media hora
                playa.salir();

                // ---- 2. ALTA MAR ----
                if (op.isFin()) {                        // los barcos se han ido
                    op.vuelvenPorSuCuenta(soldados);
                    System.out.printf("  %s pone rumbo a Gran Bretaña con %d soldados%n", getName(), soldados);
                    break;
                }
                altaMar.entrar(tipo);
                Thread.sleep(Operacion.HORA / 2);        // desembarcar: media hora
                op.desembarcar(soldados);
                altaMar.salir();
                viajes++;
                total += soldados;
                System.out.printf("  %s deja %d soldados en los barcos%n", getName(), soldados);
            }
        } catch (InterruptedException e) {
            // no se usa: el fin se controla con op.isFin()
        }
    }

    public int getViajes() { return viajes; }
    public int getTotal()  { return total; }
}
