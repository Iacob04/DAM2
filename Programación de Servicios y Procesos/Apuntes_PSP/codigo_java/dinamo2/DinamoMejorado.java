public class DinamoMejorado {

    public static void main(String[] args) throws InterruptedException {
        Operacion op = new Operacion(400_000);
        Playa playa = new Playa();
        Reloj reloj = new Reloj(op, playa);

        Grupo[] grupos = new Grupo[10];
        for (int i = 0; i < 5; i++) {
            grupos[i] = new Grupo("Barcas-" + (i + 1), true, op, playa);
            grupos[i + 5] = new Grupo("Lanchas-" + (i + 1), false, op, playa);
        }

        reloj.start();
        for (Grupo g : grupos) g.start();
        for (Grupo g : grupos) g.join();
        reloj.join();

        System.out.println("\n===== RESUMEN: lo que pudo ser =====");
        System.out.printf("Duración: %d horas (%.1f días)%n", reloj.getHoras(), reloj.getHoras() / 24.0);
        for (Grupo g : grupos)
            System.out.printf("%-10s %4d viajes · %6d soldados%n", g.getName(), g.getViajes(), g.getTotal());
        System.out.println("Rescatados en los barcos:   " + op.getRescatados());
        System.out.println("Llegan a GB por su cuenta:  " + op.getPorSuCuenta());
        System.out.println("Se rinden en la playa:      " + op.getEnPlaya());
    }
}
