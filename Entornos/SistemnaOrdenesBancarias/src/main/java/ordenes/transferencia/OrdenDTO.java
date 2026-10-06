package ordenes.transferencia;

import java.util.Objects;

public class OrdenDTO {
    private String banco;
    private int sucursal;
    private int cuenta;
    private String clave;
    private String tipoOrden;

    public OrdenDTO(String banco, int sucursal, int cuenta, String clave, String tipoOrden) {
        this.banco = banco;
        this.sucursal = sucursal;
        this.cuenta = cuenta;
        this.clave = clave;
        this.tipoOrden = tipoOrden;
    }

    public String getBanco() {
        return banco;
    }

    public int getSucursal() {
        return sucursal;
    }

    public int getCuenta() {
        return cuenta;
    }

    public String getClave() {
        return clave;
    }

    public String getTipoOrden() {
        return tipoOrden;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        OrdenDTO ordenDTO = (OrdenDTO) o;
        return sucursal == ordenDTO.sucursal && cuenta == ordenDTO.cuenta && Objects.equals(banco, ordenDTO.banco) && Objects.equals(clave, ordenDTO.clave) && Objects.equals(tipoOrden, ordenDTO.tipoOrden);
    }

    @Override
    public int hashCode() {
        return Objects.hash(banco, sucursal, cuenta, clave, tipoOrden);
    }

    @Override
    public String toString() {
        return "OrdenDTO{" +
                "banco='" + banco + '\'' +
                ", sucursal=" + sucursal +
                ", cuenta=" + cuenta +
                ", clave='" + clave + '\'' +
                ", tipoOrden='" + tipoOrden + '\'' +
                '}';
    }
}
