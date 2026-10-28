// Contador de tiempo: el hilo MÁS prioritario. Informa al alto mando cada 12 h
public class Reloj extends Thread {

    private Operacion op;
    private Zona playa;
    private int horas = 0;

    public Reloj(Operacion op, Zona playa) {
        this.op = op;
        this.playa = playa;
        setPriority(Thread.MAX_PRIORITY);
    }

    @Override
    public void run() {
        try {
            while (horas < Operacion.DIAS * 24) {
                Thread.sleep(Operacion.HORA);
                horas++;
                if (horas % 12 == 0) {
                    System.out.printf("[ALTO MANDO] Día %d %02d:00 · en playa: %d · rescatados: %d · en lanchas/botes: %d%n",
                            horas / 24 + 1, horas % 24, op.getEnPlaya(), op.getRescatados(), op.getEnTransito());
                }
                if (op.getEnPlaya() == 0 && op.getEnTransito() == 0) break;   // todos a salvo
            }
        } catch (InterruptedException e) { }
        op.terminar();
        playa.cerrar();
    }

    public int getHoras() { return horas; }
}
