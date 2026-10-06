package ordenes.transferencia;

import ordenes.documentos.Documento;

import java.util.List;
import java.util.UUID;

public class SolicitudDTO {
    private UUID identificadorOperacion;
    private List<Documento> documentos;

    public SolicitudDTO(List<Documento> documentos) {
        identificadorOperacion = UUID.randomUUID();
        this.documentos = documentos;
    }

    public List<Documento> getDocumentos() {
        return documentos.stream().toList();
    }
}
