// MONITOR de la pista: solo un aeroplano a la vez y prioridad para los aviones
public class Pista {

    private boolean ocupada = false;
    private int avionesEsperando = 0;

    public synchronized void entrar(Aeroplano a) throws InterruptedException {
        if (a.esAvion()) avionesEsperando++;

        // Espera si la pista está ocupada,
        // o si es avioneta y hay algún avión esperando (prioridad)
        while (ocupada || (!a.esAvion() && avionesEsperando > 0)) {
            wait();
        }

        if (a.esAvion()) avionesEsperando--;
        ocupada = true;
        System.out.println("Aeroplano de tipo: " + a.getTipo() + " con número "
                + a.getName() + " entra en la pista de despegue...");
    }

    public synchronized void salir() {
        ocupada = false;
        notifyAll();      // despierta a todos; cada uno vuelve a comprobar su condición
    }
}
