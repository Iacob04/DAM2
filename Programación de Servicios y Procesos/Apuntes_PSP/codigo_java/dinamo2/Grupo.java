// Un grupo de barcas (botes salvavidas) o de lanchas
public class Grupo extends Thread {

    private boolean barca;
    private Operacion op;
    private Playa playa;
    private int total = 0, viajes = 0;

    public Grupo(String nombre, boolean barca, Operacion op, Playa playa) {
        super(nombre);
        this.barca = barca;
        this.op = op;
        this.playa = playa;
    }

    @Override
    public void run() {
        int min = barca ? 300 : 100;
        int max = barca ? 400 : 200;
        try {
            while (!op.isFin() && op.getEnPlaya() > 0) {
                if (!playa.entrar(barca)) break;
                int soldados = op.embarcar(min + (int) (Math.random() * (max - min + 1)));
                if (soldados == 0) { playa.salir(); break; }
                Thread.sleep(Operacion.HORA * 24 / 60);   // embarque: 24 min = 400 ms
                playa.salir();

                if (op.isFin()) {
                    op.vuelvenPorSuCuenta(soldados);
                    total += soldados;
                    break;
                }
                // Desembarque: sin esperas, cada grupo tiene su barco
                Thread.sleep(Operacion.HORA / 2);
                op.desembarcar(soldados);
                total += soldados;
                viajes++;
            }
        } catch (InterruptedException e) { }
    }

    public int getTotal()  { return total; }
    public int getViajes() { return viajes; }
}
