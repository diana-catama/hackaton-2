import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * Clase que representa una agenda telefónica
 * Utiliza una lista dinámica para mejor gestión de memoria
 */
public class Agenda {
    private List<Contacto> contactos;
    private int capacidadMaxima;

    /**
     * Constructor con tamaño personalizado
     * @param capacidadMaxima Tamaño máximo de la agenda
     */
    public Agenda(int capacidadMaxima) {
        this.capacidadMaxima = Math.max(capacidadMaxima, 1); // Mínimo 1 contacto
        this.contactos = new ArrayList<>(capacidadMaxima);
    }

    /**
     * Constructor con tamaño por defecto (10 contactos)
     */
    public Agenda() {
        this(10);
    }

    /**
     * Añade un contacto a la agenda
     * @param c Contacto a añadir
     * @return true si se añadió correctamente
     */
    public boolean añadirContacto(Contacto c) {
        // Validar agenda llena
        if (agendaLlena()) {
            System.out.println("No se puede añadir: Agenda llena");
            return false;
        }

        // Validar nombre vacío
        if (c.getNombre() == null || c.getNombre().trim().isEmpty()) {
            System.out.println("No se puede añadir: Nombre vacío");
            return false;
        }

        // Validar teléfono
        if (!Contacto.validarTelefono(c.getTelefono())) {
            System.out.println("No se puede añadir: Teléfono inválido");
            return false;
        }

        // Validar duplicado
        if (existeContacto(c)) {
            System.out.println("No se puede añadir: Contacto duplicado");
            return false;
        }

        contactos.add(c);
        System.out.println("Contacto añadido correctamente");
        return true;
    }

    /**
     * Verifica si un contacto existe en la agenda
     * @param c Contacto a verificar
     * @return true si existe
     */
    public boolean existeContacto(Contacto c) {
        return contactos.stream().anyMatch(contacto -> contacto.equals(c));
    }

    /**
     * Lista todos los contactos ordenados alfabéticamente
     */
    public void listarContactos() {
        if (contactos.isEmpty()) {
            System.out.println("La agenda está vacía");
            return;
        }

        // Ordenar alfabéticamente
        List<Contacto> contactosOrdenados = new ArrayList<>(contactos);
        Collections.sort(contactosOrdenados, Comparator.comparing(Contacto::getNombre, String.CASE_INSENSITIVE_ORDER));

        System.out.println("\n--- LISTA DE CONTACTOS ---");
        contactosOrdenados.forEach(System.out::println);
        System.out.println("--------------------------");
    }

    /**
     * Busca un contacto por nombre
     * @param nombre Nombre del contacto a buscar
     */
    public void buscaContacto(String nombre) {
        if (nombre == null || nombre.trim().isEmpty()) {
            System.out.println("Nombre no válido");
            return;
        }

        contactos.stream()
                .filter(contacto -> contacto.getNombre().equalsIgnoreCase(nombre.trim()))
                .findFirst()
                .ifPresentOrElse(
                        contacto -> System.out.println("Teléfono: " + contacto.getTelefono()),
                        () -> System.out.println("Contacto no encontrado: " + nombre)
                );
    }

    /**
     * Elimina un contacto de la agenda
     * @param c Contacto a eliminar
     * @return true si se eliminó correctamente
     */
    public boolean eliminarContacto(Contacto c) {
        boolean eliminado = contactos.removeIf(contacto -> contacto.equals(c));

        if (eliminado) {
            System.out.println("Contacto eliminado correctamente");
        } else {
            System.out.println("No se encontró el contacto para eliminar");
        }

        return eliminado;
    }

    /**
     * Verifica si la agenda está llena
     * @return true si está llena
     */
    public boolean agendaLlena() {
        return contactos.size() >= capacidadMaxima;
    }

    /**
     * Calcula el espacio libre disponible
     * @return Cantidad de contactos que se pueden añadir
     */
    public int espacioLibre() {
        return capacidadMaxima - contactos.size();
    }

    // Getters para información de la agenda
    public int getCantidadContactos() {
        return contactos.size();
    }

    public int getCapacidadMaxima() {
        return capacidadMaxima;
    }
}