public class Aeroplano extends Thread {

    private boolean avion;      // true = AVION, false = AVIONETA
    private Pista pista;

    public Aeroplano(String nombre, boolean avion, Pista pista) {
        super(nombre);
        this.avion = avion;
        this.pista = pista;
    }

    public boolean esAvion() { return avion; }
    public String getTipo()  { return avion ? "AVION" : "AVIONETA"; }

    @Override
    public void run() {
        try {
            System.out.println("Llega a pista el/la " + getTipo() + " con número " + getName());
            pista.entrar(this);
            // Turbulencias: la pista queda bloqueada 3 s (avión) o 2 s (avioneta)
            Thread.sleep(avion ? 3000 : 2000);
            System.out.println("Aeroplano de tipo: " + getTipo() + " con número " + getName() + " ha despegado.");
            pista.salir();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
