import java.util.Scanner;

/**
 * Clase principal con menú interactivo para gestionar la agenda
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
        System.out.println("SISTEMA DE AGENDA TELEFÓNICA");

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
            System.out.println("\nMENÚ PRINCIPAL");
            System.out.println("1. Añadir contacto");
            System.out.println("2. Buscar contacto");
            System.out.println("3. Listar contactos");
            System.out.println("4. Eliminar contacto");
            System.out.println("5. Verificar si agenda está llena");
            System.out.println("6. Ver espacio libre");
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
            case 3 -> agenda.listarContactos();
            case 4 -> eliminarContacto();
            case 5 -> System.out.println("Agenda llena: " + agenda.agendaLlena());
            case 6 -> System.out.println("Espacio libre: " + agenda.espacioLibre() + " contactos");
            case 7 -> System.out.println("Saliendo del sistema...");
            default -> System.out.println("Opción inválida. Intente nuevamente.");
        }
    }

    /**
     * Solicita datos para añadir un nuevo contacto
     */
    private static void añadirContacto() {
        System.out.println("\n--- AÑADIR CONTACTO ---");

        String nombre = solicitarDato("Nombre: ", false);
        String telefono = solicitarDato("Teléfono: ", true);

        Contacto nuevoContacto = new Contacto(nombre, telefono);
        agenda.añadirContacto(nuevoContacto);
    }

    /**
     * Solicita nombre para buscar un contacto
     */
    private static void buscarContacto() {
        System.out.println("\n--- BUSCAR CONTACTO ---");
        String nombre = solicitarDato("Ingrese nombre del contacto: ", false);
        agenda.buscaContacto(nombre);
    }

    /**
     * Solicita datos para eliminar un contacto
     */
    private static void eliminarContacto() {
        System.out.println("\n--- ELIMINAR CONTACTO ---");
        String nombre = solicitarDato("Ingrese nombre del contacto a eliminar: ", false);

        // Crear contacto temporal para búsqueda
        Contacto contactoAEliminar = new Contacto(nombre, "000000000");
        agenda.eliminarContacto(contactoAEliminar);
    }

    /**
     * Solicita y valida datos de entrada
     */
    private static String solicitarDato(String mensaje, boolean esTelefono) {
        String dato;

        do {
            System.out.print(mensaje);
            dato = scanner.nextLine().trim();

            if (dato.isEmpty()) {
                System.out.println("Este campo no puede estar vacío");
                continue;
            }

            if (esTelefono && !Contacto.validarTelefono(dato)) {
                System.out.println("Formato de teléfono inválido. Use solo números y opcionalmente '+' al inicio");
                dato = "";
            }

        } while (dato.isEmpty());

        return dato;
    }
}