import java.util.ArrayList;
import java.util.List;

/**
 * Clase que representa una agenda telefónica
 */
public class Agenda {
    private List<Contacto> contactos;
    private int capacidadMaxima;

    /**
     * Constructor con tamaño personalizado
     */
    public Agenda(int capacidadMaxima) {
        this.capacidadMaxima = capacidadMaxima;
        this.contactos = new ArrayList<>();
    }

    /**
     * Constructor con tamaño por defecto (10 contactos)
     */
    public Agenda() {
        this(10);
    }

    /**
     * Añade un contacto a la agenda
     */
    public boolean añadirContacto(Contacto c) {
        if (agendaLlena()) {
            return false;
        }
        if (c.getNombre() == null || c.getNombre().trim().isEmpty() ||
                c.getApellido() == null || c.getApellido().trim().isEmpty()) {
            return false;
        }
        if (!Contacto.validarTelefono(c.getTelefono())) {
            return false;
        }
        if (existeContacto(c)) {
            return false;
        }
        contactos.add(c);
        return true;
    }

    /**
     * Verifica si un contacto existe en la agenda
     */
    public boolean existeContacto(Contacto c) {
        for (Contacto contacto : contactos) {
            if (contacto.equals(c)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Lista todos los contactos de la agenda
     */
    public List<Contacto> listarContactos() {
        return new ArrayList<>(contactos);
    }

    /**
     * Busca un contacto por nombre y apellido
     */
    public Contacto buscaContacto(String nombre, String apellido) {
        for (Contacto contacto : contactos) {
            if (contacto.getNombre().equalsIgnoreCase(nombre) &&
                    contacto.getApellido().equalsIgnoreCase(apellido)) {
                return contacto;
            }
        }
        return null;
    }

    /**
     * Busca contacto por nombre completo
     */
    public Contacto buscaContacto(String nombreCompleto) {
        String[] partes = nombreCompleto.split(" ", 2);
        if (partes.length >= 2) {
            return buscaContacto(partes[0], partes[1]);
        }
        return null;
    }

    /**
     * Elimina un contacto de la agenda
     */
    public boolean eliminarContacto(Contacto c) {
        return contactos.removeIf(contacto -> contacto.equals(c));
    }

    /**
     * Verifica si la agenda está llena
     */
    public boolean agendaLlena() {
        return contactos.size() >= capacidadMaxima;
    }

    /**
     * Indica cuántos contactos más podemos tener
     */
    public int espaciosLibres() {
        return capacidadMaxima - contactos.size();
    }

    // Getters para información
    public int getCantidadContactos() {
        return contactos.size();
    }

    public int getCapacidadMaxima() {
        return capacidadMaxima;
    }
}