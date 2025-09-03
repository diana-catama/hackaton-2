import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class AgendaGUI extends JFrame {
    private Agenda agenda;
    private JTable tablaContactos;
    private DefaultTableModel modeloTabla;
    private JTextField txtNombre, txtApellido, txtTelefono;
    private JLabel lblEstado;

    public AgendaGUI() {
        // Configurar la agenda
        configurarAgenda();

        // Configurar la ventana principal
        setTitle("Agenda Telefónica");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(800, 550);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(10, 10));

        // Crear componentes
        crearPanelSuperior();
        crearPanelCentral();
        crearPanelInferior();

        // Actualizar interfaz
        actualizarTabla();
        actualizarEstado();

        setVisible(true);
    }

    private void configurarAgenda() {
        Object[] options = {"Tamaño por defecto (10)", "Tamaño personalizado"};
        int eleccion = JOptionPane.showOptionDialog(this,
                "¿Cómo desea configurar la agenda?",
                "Configuración de Agenda",
                JOptionPane.DEFAULT_OPTION,
                JOptionPane.QUESTION_MESSAGE,
                null,
                options,
                options[0]);

        if (eleccion == 0) {
            agenda = new Agenda();
            JOptionPane.showMessageDialog(this,
                    "Agenda creada con tamaño por defecto (10 contactos)");
        } else {
            String input = JOptionPane.showInputDialog(this,
                    "Ingrese el tamaño máximo de la agenda:",
                    "10");
            try {
                int tamaño = input != null ? Integer.parseInt(input) : 10;
                agenda = new Agenda(tamaño);
                JOptionPane.showMessageDialog(this,
                        "Agenda creada con capacidad para " + tamaño + " contactos");
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(this,
                        "Tamaño inválido. Usando tamaño por defecto (10)",
                        "Error",
                        JOptionPane.ERROR_MESSAGE);
                agenda = new Agenda();
            }
        }
    }

    private void crearPanelSuperior() {
        JPanel panelSuperior = new JPanel(new GridBagLayout());
        panelSuperior.setBorder(BorderFactory.createTitledBorder("Gestión de Contactos"));
        panelSuperior.setPreferredSize(new Dimension(780, 150));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Nombre
        gbc.gridx = 0; gbc.gridy = 0;
        panelSuperior.add(new JLabel("Nombre:"), gbc);

        gbc.gridx = 1; gbc.gridy = 0;
        gbc.weightx = 1.0;
        txtNombre = new JTextField(15);
        panelSuperior.add(txtNombre, gbc);

        // Apellido
        gbc.gridx = 0; gbc.gridy = 1;
        gbc.weightx = 0;
        panelSuperior.add(new JLabel("Apellido:"), gbc);

        gbc.gridx = 1; gbc.gridy = 1;
        gbc.weightx = 1.0;
        txtApellido = new JTextField(15);
        panelSuperior.add(txtApellido, gbc);

        // Teléfono
        gbc.gridx = 0; gbc.gridy = 2;
        gbc.weightx = 0;
        panelSuperior.add(new JLabel("Teléfono:"), gbc);

        gbc.gridx = 1; gbc.gridy = 2;
        gbc.weightx = 1.0;
        txtTelefono = new JTextField(15);
        panelSuperior.add(txtTelefono, gbc);

        // Botones
        JPanel panelBotones = new JPanel(new GridLayout(1, 4, 5, 5));

        JButton btnAñadir = new JButton("Añadir Contacto");
        btnAñadir.addActionListener(e -> añadirContacto());

        JButton btnBuscar = new JButton("Buscar Contacto");
        btnBuscar.addActionListener(e -> buscarContacto());

        JButton btnLimpiar = new JButton("Limpiar Campos");
        btnLimpiar.addActionListener(e -> limpiarCampos());

        JButton btnVerEspacio = new JButton("Ver Espacio");
        btnVerEspacio.addActionListener(e -> verEspacio());

        panelBotones.add(btnAñadir);
        panelBotones.add(btnBuscar);
        panelBotones.add(btnLimpiar);
        panelBotones.add(btnVerEspacio);

        gbc.gridx = 0; gbc.gridy = 3;
        gbc.gridwidth = 2;
        gbc.weightx = 0;
        panelSuperior.add(panelBotones, gbc);

        add(panelSuperior, BorderLayout.NORTH);
    }

    private void crearPanelCentral() {
        // Configurar tabla
        modeloTabla = new DefaultTableModel(new Object[]{"Nombre", "Apellido", "Teléfono"}, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        tablaContactos = new JTable(modeloTabla);
        tablaContactos.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        JScrollPane scrollPane = new JScrollPane(tablaContactos);

        // Agregar listener para doble click
        tablaContactos.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                if (evt.getClickCount() == 2) {
                    int fila = tablaContactos.getSelectedRow();
                    if (fila >= 0) {
                        String nombre = (String) modeloTabla.getValueAt(fila, 0);
                        String apellido = (String) modeloTabla.getValueAt(fila, 1);
                        mostrarInfoContacto(nombre, apellido);
                    }
                }
            }
        });

        add(scrollPane, BorderLayout.CENTER);
    }

    private void crearPanelInferior() {
        JPanel panelInferior = new JPanel(new BorderLayout(10, 10));
        panelInferior.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));

        // Label de estado
        lblEstado = new JLabel();
        lblEstado.setFont(new Font("Arial", Font.BOLD, 12));
        panelInferior.add(lblEstado, BorderLayout.WEST);

        // Botones de acción
        JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.RIGHT, 5, 0));

        JButton btnEliminar = new JButton("Eliminar Seleccionado");
        btnEliminar.addActionListener(e -> eliminarContactoSeleccionado());

        JButton btnActualizar = new JButton("Actualizar Lista");
        btnActualizar.addActionListener(e -> actualizarInterfaz());

        JButton btnListar = new JButton("Listar Contactos");
        btnListar.addActionListener(e -> listarContactos());

        panelBotones.add(btnListar);
        panelBotones.add(btnEliminar);
        panelBotones.add(btnActualizar);

        panelInferior.add(panelBotones, BorderLayout.EAST);
        add(panelInferior, BorderLayout.SOUTH);
    }

    private void añadirContacto() {
        String nombre = txtNombre.getText().trim();
        String apellido = txtApellido.getText().trim();
        String telefono = txtTelefono.getText().trim();

        if (nombre.isEmpty() || apellido.isEmpty()) {
            mostrarError("El nombre y apellido no pueden estar vacíos");
            return;
        }

        if (!Contacto.validarTelefono(telefono)) {
            mostrarError("Formato de teléfono inválido. Use solo números y opcionalmente '+' al inicio");
            return;
        }

        Contacto nuevoContacto = new Contacto(nombre, apellido, telefono);

        if (agenda.añadirContacto(nuevoContacto)) {
            mostrarMensaje("Contacto añadido correctamente", "Éxito");
            limpiarCampos();
            actualizarInterfaz();
        } else {
            if (agenda.existeContacto(nuevoContacto)) {
                mostrarError("Ya existe un contacto con el mismo nombre y apellido");
            } else if (agenda.agendaLlena()) {
                mostrarError("La agenda está llena. No se pueden añadir más contactos");
            }
        }
    }

    private void buscarContacto() {
        String nombre = txtNombre.getText().trim();
        String apellido = txtApellido.getText().trim();

        if (nombre.isEmpty() || apellido.isEmpty()) {
            mostrarError("Ingrese nombre y apellido para buscar");
            return;
        }

        Contacto contacto = agenda.buscaContacto(nombre, apellido);

        if (contacto != null) {
            mostrarMensaje("Teléfono de " + nombre + " " + apellido + ": " + contacto.getTelefono(),
                    "Resultado de Búsqueda");
        } else {
            mostrarError("No se encontró el contacto: " + nombre + " " + apellido);
        }
    }

    private void listarContactos() {
        if (agenda.getCantidadContactos() == 0) {
            mostrarMensaje("La agenda está vacía", "Lista de Contactos");
        } else {
            actualizarInterfaz();
            mostrarMensaje("Se listaron " + agenda.getCantidadContactos() + " contactos",
                    "Lista de Contactos");
        }
    }

    private void verEspacio() {
        String mensaje = String.format("Espacio utilizado: %d/%d\nEspacio libre: %d\nAgenda llena: %s",
                agenda.getCantidadContactos(),
                agenda.getCapacidadMaxima(),
                agenda.espaciosLibres(),
                agenda.agendaLlena() ? "Sí" : "No");

        mostrarMensaje(mensaje, "Espacio en Agenda");
    }

    private void eliminarContactoSeleccionado() {
        int fila = tablaContactos.getSelectedRow();
        if (fila >= 0) {
            String nombre = (String) modeloTabla.getValueAt(fila, 0);
            String apellido = (String) modeloTabla.getValueAt(fila, 1);
            eliminarContacto(nombre, apellido);
        } else {
            mostrarError("Seleccione un contacto para eliminar");
        }
    }

    private void eliminarContacto(String nombre, String apellido) {
        int confirmacion = JOptionPane.showConfirmDialog(this,
                "¿Está seguro de que desea eliminar el contacto: " + nombre + " " + apellido + "?",
                "Confirmar Eliminación",
                JOptionPane.YES_NO_OPTION);

        if (confirmacion == JOptionPane.YES_OPTION) {
            Contacto contactoAEliminar = new Contacto(nombre, apellido, "");
            if (agenda.eliminarContacto(contactoAEliminar)) {
                mostrarMensaje("Contacto eliminado correctamente", "Éxito");
                actualizarInterfaz();
            } else {
                mostrarError("Error al eliminar el contacto");
            }
        }
    }

    private void mostrarInfoContacto(String nombre, String apellido) {
        Contacto contacto = agenda.buscaContacto(nombre, apellido);
        if (contacto != null) {
            JOptionPane.showMessageDialog(this,
                    "Nombre: " + nombre + "\n" +
                            "Apellido: " + apellido + "\n" +
                            "Teléfono: " + contacto.getTelefono(),
                    "Información del Contacto",
                    JOptionPane.INFORMATION_MESSAGE);
        }
    }

    private void actualizarInterfaz() {
        actualizarTabla();
        actualizarEstado();
    }

    private void actualizarTabla() {
        modeloTabla.setRowCount(0);
        for (Contacto contacto : agenda.listarContactos()) {
            modeloTabla.addRow(new Object[]{
                    contacto.getNombre(),
                    contacto.getApellido(),
                    contacto.getTelefono()
            });
        }
    }

    private void actualizarEstado() {
        String estado = String.format("Contactos: %d/%d | Espacios libres: %d | Agenda llena: %s",
                agenda.getCantidadContactos(),
                agenda.getCapacidadMaxima(),
                agenda.espaciosLibres(),
                agenda.agendaLlena() ? "Sí" : "No");

        lblEstado.setText(estado);
    }

    private void limpiarCampos() {
        txtNombre.setText("");
        txtApellido.setText("");
        txtTelefono.setText("");
        txtNombre.requestFocus();
    }

    private void mostrarMensaje(String mensaje, String titulo) {
        JOptionPane.showMessageDialog(this, mensaje, titulo, JOptionPane.INFORMATION_MESSAGE);
    }

    private void mostrarError(String mensaje) {
        JOptionPane.showMessageDialog(this, mensaje, "Error", JOptionPane.ERROR_MESSAGE);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            } catch (Exception e) {
                e.printStackTrace();
            }
            new AgendaGUI();
        });
    }
}