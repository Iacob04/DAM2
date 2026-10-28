public class Reloj extends Thread {

    private Operacion op;
    private Playa playa;
    private int horas = 0;

    public Reloj(Operacion op, Playa playa) {
        this.op = op;
        this.playa = playa;
        setPriority(Thread.MAX_PRIORITY);       // el más prioritario
    }

    @Override
    public void run() {
        try {
            while (horas < Operacion.DIAS * 24) {
                Thread.sleep(Operacion.HORA);
                horas++;
                if (horas % 12 == 0) {
                    System.out.printf("[ALTO MANDO] Día %d %02d:00 · en playa: %d · rescatados: %d%n",
                            horas / 24 + 1, horas % 24, op.getEnPlaya(), op.getRescatados());
                }
                if (op.getEnPlaya() == 0 && op.getEnTransito() == 0) break;
            }
        } catch (InterruptedException e) { }
        op.terminar();
        playa.cerrar();
    }

    public int getHoras() { return horas; }
}
