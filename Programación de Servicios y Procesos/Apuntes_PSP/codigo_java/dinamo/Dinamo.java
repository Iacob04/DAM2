public class Dinamo {

    static final int SOLDADOS = 400_000;
    static final int NUM_LANCHAS = 1;   // flotas de lanchas (todas embarcan a la vez)
    static final int NUM_BOTES = 1;     // flotas de botes salvavidas

    public static void main(String[] args) throws InterruptedException {
        Operacion op = new Operacion(SOLDADOS);
        Zona playa = new Zona("Playa");
        Zona altaMar = new Zona("Alta mar");

        Reloj reloj = new Reloj(op, playa);
        Embarcacion[] flota = new Embarcacion[NUM_LANCHAS + NUM_BOTES];
        for (int i = 0; i < NUM_LANCHAS; i++)
            flota[i] = new Embarcacion("Lanchas-" + (i + 1), "LANCHA", 500, 1000, op, playa, altaMar);
        for (int i = 0; i < NUM_BOTES; i++)
            flota[NUM_LANCHAS + i] = new Embarcacion("Botes-" + (i + 1), "BOTE", 1500, 2000, op, playa, altaMar);

        System.out.println("26 de mayo de 1940: comienza la OPERACIÓN DINAMO");
        reloj.start();
        for (Embarcacion e : flota) e.start();

        for (Embarcacion e : flota) e.join();
        reloj.join();

        System.out.println("\n========== RESUMEN ==========");
        System.out.printf("Duración: %d horas (%.1f días)%n", reloj.getHoras(), reloj.getHoras() / 24.0);
        for (Embarcacion e : flota)
            System.out.printf("%-10s %4d viajes · %7d soldados%n", e.getName(), e.getViajes(), e.getTotal());
        System.out.println("Rescatados en los barcos:    " + op.getRescatados());
        System.out.println("Llegan a GB en lancha/bote:  " + op.getPorSuCuenta());
        System.out.println("Se rinden en la playa:       " + op.getEnPlaya());
    }
}
