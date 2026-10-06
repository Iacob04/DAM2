package ordenes.ejecucion;

import ordenes.documentos.Movimientos;
import ordenes.documentos.Talon;
import ordenes.transferencia.OrdenDTO;
import ordenes.transferencia.SolicitudDTO;

import java.util.List;

public class EjecucionOrdenDocumental {

    public SolicitudDTO tramitar(OrdenDTO ordenDTO){
        SolicitudDTO solicitudDTO;

        if(ordenDTO.getTipoOrden().equals("")) {
            solicitudDTO = new SolicitudDTO(List.of(new Talon(), new Movimientos()));
        } else {
            solicitudDTO = new SolicitudDTO(List.of(new Talon()));
        }




        return  solicitudDTO;
    }


}
