import java.util.Scanner;

/**
 * Clase principal con menú por consola
 */
public class Main {
    private static Agenda agenda;
    private static Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        inicializarAgenda();
        mostrarMenuPrincipal();
    }

    /**
     * Inicializa la agenda según la elección del usuario
     */
    private static void inicializarAgenda() {
        System.out.println("=== SISTEMA DE AGENDA TELEFÓNICA ===");

        System.out.print("¿Usar tamaño por defecto (10 contactos)? (s/n): ");
        String respuesta = scanner.nextLine().trim().toLowerCase();

        if (respuesta.equals("s") || respuesta.equals("si")) {
            agenda = new Agenda();
            System.out.println("Agenda creada con tamaño por defecto (10 contactos)");
        } else {
            int tamaño = solicitarTamaño();
            agenda = new Agenda(tamaño);
            System.out.println("Agenda creada con capacidad para " + tamaño + " contactos");
        }
    }

    /**
     * Solicita y valida el tamaño de la agenda
     */
    private static int solicitarTamaño() {
        while (true) {
            System.out.print("Ingrese el tamaño máximo de la agenda: ");
            try {
                int tamaño = Integer.parseInt(scanner.nextLine());
                if (tamaño > 0) {
                    return tamaño;
                }
                System.out.println("El tamaño debe ser mayor a 0");
            } catch (NumberFormatException e) {
                System.out.println("Por favor, ingrese un número válido");
            }
        }
    }

    /**
     * Muestra el menú principal y gestiona las opciones
     */
    private static void mostrarMenuPrincipal() {
        int opcion;

        do {
            System.out.println("\n=== MENÚ PRINCIPAL ===");
            System.out.println("1. Añadir contacto");
            System.out.println("2. Buscar contacto");
            System.out.println("3. Listar contactos");
            System.out.println("4. Eliminar contacto");
            System.out.println("5. Verificar si agenda está llena");
            System.out.println("6. Ver espacios libres");
            System.out.println("7. Salir");
            System.out.print("Seleccione una opción: ");

            try {
                opcion = Integer.parseInt(scanner.nextLine());
                procesarOpcion(opcion);
            } catch (NumberFormatException e) {
                System.out.println("Opción inválida. Intente nuevamente.");
                opcion = 0;
            }

        } while (opcion != 7);

        scanner.close();
        System.out.println("¡Hasta pronto!");
    }

    /**
     * Procesa la opción seleccionada del menú
     */
    private static void procesarOpcion(int opcion) {
        switch (opcion) {
            case 1 -> añadirContacto();
            case 2 -> buscarContacto();
            case 3 -> listarContactos();
            case 4 -> eliminarContacto();
            case 5 -> System.out.println("Agenda llena: " + agenda.agendaLlena());
            case 6 -> System.out.println("Espacios libres: " + agenda.espaciosLibres());
            case 7 -> System.out.println("Saliendo del sistema...");
            default -> System.out.println("Opción inválida. Intente nuevamente.");
        }
    }

    /**
     * Solicita datos para añadir un nuevo contacto
     */
    private static void añadirContacto() {
        System.out.println("\n--- AÑADIR CONTACTO ---");

        System.out.print("Nombre: ");
        String nombre = scanner.nextLine().trim();

        System.out.print("Apellido: ");
        String apellido = scanner.nextLine().trim();

        System.out.print("Teléfono: ");
        String telefono = scanner.nextLine().trim();

        Contacto nuevoContacto = new Contacto(nombre, apellido, telefono);
        agenda.añadirContacto(nuevoContacto);
    }

    /**
     * Solicita nombre y apellido para buscar un contacto
     */
    private static void buscarContacto() {
        System.out.println("\n--- BUSCAR CONTACTO ---");
        System.out.print("Ingrese nombre del contacto: ");
        String nombre = scanner.nextLine().trim();

        System.out.print("Ingrese apellido del contacto: ");
        String apellido = scanner.nextLine().trim();

        Contacto contacto = agenda.buscaContacto(nombre, apellido);
        if (contacto != null) {
            System.out.println("Teléfono de " + nombre + " " + apellido + ": " + contacto.getTelefono());
        } else {
            System.out.println("Contacto no encontrado: " + nombre + " " + apellido);
        }
    }

    /**
     * Lista todos los contactos
     */
    private static void listarContactos() {
        System.out.println("\n--- LISTA DE CONTACTOS ---");
        if (agenda.listarContactos().isEmpty()) {
            System.out.println("La agenda está vacía");
        } else {
            for (Contacto contacto : agenda.listarContactos()) {
                System.out.println(contacto);
            }
        }
        System.out.println("--------------------------");
    }

    /**
     * Solicita datos para eliminar un contacto
     */
    private static void eliminarContacto() {
        System.out.println("\n--- ELIMINAR CONTACTO ---");
        System.out.print("Ingrese nombre del contacto a eliminar: ");
        String nombre = scanner.nextLine().trim();

        System.out.print("Ingrese apellido del contacto a eliminar: ");
        String apellido = scanner.nextLine().trim();

        // Crear contacto temporal para búsqueda
        Contacto contactoAEliminar = new Contacto(nombre, apellido, "");
        agenda.eliminarContacto(contactoAEliminar);
    }
}