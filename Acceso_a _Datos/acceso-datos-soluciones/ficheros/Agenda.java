package ficheros;

import java.util.List;

import com.google.gson.annotations.SerializedName;

public class Agenda {

    @SerializedName("agenda")         // en el JSON la clave es "agenda", en Java se llama contactos
    private List<Contacto> contactos;

    public List<Contacto> getContactos() {
        return contactos;
    }

    public void setContactos(List<Contacto> contactos) {
        this.contactos = contactos;
    }
}