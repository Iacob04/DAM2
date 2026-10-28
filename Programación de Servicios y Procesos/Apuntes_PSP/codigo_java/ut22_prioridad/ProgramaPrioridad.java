public class ProgramaPrioridad {

    public static void main(String[] args) {
        int contador = 5;
        MemoriaComun memoria = new MemoriaComun();
        Thread[] hilosMIN = new Thread[contador];
        Thread[] hilosNORM = new Thread[contador];
        Thread[] hilosMAX = new Thread[contador];

        for (int i = 0; i < contador; i++) {
            hilosMIN[i] = new HiloPrioridad(Thread.MIN_PRIORITY, memoria);
            hilosNORM[i] = new HiloPrioridad(Thread.NORM_PRIORITY, memoria);
            hilosMAX[i] = new HiloPrioridad(Thread.MAX_PRIORITY, memoria);
        }
        System.out.println("Hilos en proceso, espera...\nLos de mayor prioridad TIENDEN a terminar antes...\n");

        for (int i = 0; i < contador; i++) {
            hilosMIN[i].start();
            hilosNORM[i].start();
            hilosMAX[i].start();
        }
        // La prioridad es una SUGERENCIA al SO: no garantiza el orden
    }
}
