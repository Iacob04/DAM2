public class CorredorSync extends Thread {

    private String corredor;
    private CorrerRelevoEquipo miEquipoRelevo;   // el MISMO objeto para todo el equipo

    public CorredorSync(String corredor, CorrerRelevoEquipo miEquipoRelevo) {
        super();
        this.corredor = corredor;
        this.miEquipoRelevo = miEquipoRelevo;
    }

    @Override
    public void run() {
        miEquipoRelevo.carrera(corredor);
    }
}
