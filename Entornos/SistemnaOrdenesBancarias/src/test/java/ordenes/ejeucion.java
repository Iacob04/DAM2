package ordenes;

import ordenes.documentos.Talon;
import ordenes.ejecucion.EjecucionOrdenDocumental;
import ordenes.transferencia.OrdenDTO;
import ordenes.transferencia.SolicitudDTO;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.concurrent.RejectedExecutionException;

public class ejeucion {

    @Test
    public void casoValidoBlancoTalonario(){
        OrdenDTO ordenDTO = new OrdenDTO("", 1000,0000, "ABC12C", "");
        EjecucionOrdenDocumental ejecucionOrdenDocumental = new EjecucionOrdenDocumental();

        SolicitudDTO solicitudDTO = ejecucionOrdenDocumental.tramitar(ordenDTO);

        //evaluo que la linea wn solicitudDTO tiene un solo elemento.
        Assertions.assertEquals(1,solicitudDTO.getDocumentos().size());
        //evaluo que el elemento presente en la lista es un documento de tipo talonario
        Assertions.assertTrue(solicitudDTO.getDocumentos().getFirst() instanceof Talon);

    }

    @Test
    public void casoValidoBlancoTalonarioMovimiento(){
        OrdenDTO ordenDTO = new OrdenDTO("", 1000,0000, "ABC12C", "");
        EjecucionOrdenDocumental ejecucionOrdenDocumental = new EjecucionOrdenDocumental();

        SolicitudDTO solicitudDTO = ejecucionOrdenDocumental.tramitar(ordenDTO);

        //evaluo que la linea wn solicitudDTO tiene un solo elemento.
        Assertions.assertEquals(1,solicitudDTO.getDocumentos().size());
        //evaluo que el elemento presente en la lista es un documento de tipo talonario
        Assertions.assertTrue(solicitudDTO.getDocumentos().getFirst() instanceof Talon);
        //evaluo que el elemento segundo en la lista es un documento de tipo movimientos
        Assertions.assertTrue(solicitudDTO.getDocumentos().getLast() instanceof  Talon);

    }


}
