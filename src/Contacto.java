/**
 * Clase que representa un contacto telefónico
 * Un contacto está definido por: nombre, apellido y teléfono
 */
public class Contacto {
    private String nombre;
    private String apellido;
    private String telefono;

    /**
     * Constructor de la clase Contacto
     * @param nombre Nombre del contacto
     * @param apellido Apellido del contacto
     * @param telefono Teléfono del contacto
     */
    public Contacto(String nombre, String apellido, String telefono) {
        this.nombre = nombre;
        this.apellido = apellido;
        this.telefono = telefono;
    }

    // Getters
    public String getNombre() {
        return nombre;
    }

    public String getApellido() {
        return apellido;
    }

    public String getTelefono() {
        return telefono;
    }

    // Setters
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public void setApellido(String apellido) {
        this.apellido = apellido;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    /**
     * Valida el formato del teléfono
     */
    public static boolean validarTelefono(String telefono) {
        return telefono != null && telefono.matches("^\\+?[0-9]{6,15}$");
    }

    /**
     * Dos contactos son iguales si tienen el mismo nombre y apellido (case insensitive)
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;

        Contacto otro = (Contacto) obj;
        return this.nombre.equalsIgnoreCase(otro.nombre) &&
                this.apellido.equalsIgnoreCase(otro.apellido);
    }

    @Override
    public String toString() {
        return nombre + " " + apellido + " - " + telefono;
    }

    /**
     * Obtiene el nombre completo
     */
    public String getNombreCompleto() {
        return nombre + " " + apellido;
    }
}