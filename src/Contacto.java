/**
 * Clase que representa un contacto telefónico
 * Un contacto está definido por un nombre y un teléfono
 */
public class Contacto {
    private String nombre;
    private String telefono;

    /**
     * Constructor de la clase Contacto
     * @param nombre Nombre del contacto (no puede estar vacío)
     * @param telefono Teléfono del contacto (formato validado)
     */
    public Contacto(String nombre, String telefono) {
        this.nombre = nombre;
        this.telefono = telefono;
    }

    // Getters
    public String getNombre() {
        return nombre;
    }

    public String getTelefono() {
        return telefono;
    }

    // Setters con validación
    public void setNombre(String nombre) {
        if (nombre != null && !nombre.trim().isEmpty()) {
            this.nombre = nombre;
        }
    }

    public void setTelefono(String telefono) {
        if (validarTelefono(telefono)) {
            this.telefono = telefono;
        }
    }

    /**
     * Valida el formato del teléfono
     * @param telefono Número a validar
     * @return true si el formato es válido
     */
    public static boolean validarTelefono(String telefono) {
        return telefono != null && telefono.matches("^\\+?[0-9]{6,15}$");
    }

    /**
     * Dos contactos son iguales si tienen el mismo nombre (case insensitive)
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;

        Contacto otro = (Contacto) obj;
        return this.nombre.equalsIgnoreCase(otro.nombre);
    }

    @Override
    public String toString() {
        return nombre + " - " + telefono;
    }
}