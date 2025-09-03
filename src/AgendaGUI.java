import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;

public class AgendaGUI extends JFrame {
    private Agenda agenda;
    private JTextField txtNombre, txtTelefono;
    private JTable tablaContactos;
    private DefaultTableModel modeloTabla;
    private JLabel lblEstado;

    public AgendaGUI() {

        agenda = new Agenda(10);

        setTitle("Agenda Telefónica ☎️");
        setSize(650, 450);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(10, 10));


        getContentPane().setBackground(new Color(245, 245, 245));


        JPanel panelSuperior = new JPanel(new GridLayout(2, 3, 10, 10));
        panelSuperior.setBackground(new Color(200, 220, 255));
        panelSuperior.setBorder(BorderFactory.createTitledBorder("➕ Agregar Contacto"));

        txtNombre = new JTextField();
        txtTelefono = new JTextField();

        panelSuperior.add(new JLabel("Nombre:"));
        panelSuperior.add(new JLabel("Teléfono:"));
        panelSuperior.add(new JLabel(""));

        panelSuperior.add(txtNombre);
        panelSuperior.add(txtTelefono);

        JButton btnAgregar = new JButton("Agregar");
        btnAgregar.setBackground(new Color(100, 180, 100));
        btnAgregar.setForeground(Color.WHITE);
        btnAgregar.setFont(new Font("Arial", Font.BOLD, 13));
        panelSuperior.add(btnAgregar);

        add(panelSuperior, BorderLayout.NORTH);


        String[] columnas = {"Nombre", "Teléfono"};
        modeloTabla = new DefaultTableModel(columnas, 0);
        tablaContactos = new JTable(modeloTabla);

        tablaContactos.getTableHeader().setBackground(new Color(150, 180, 255));
        tablaContactos.getTableHeader().setFont(new Font("Arial", Font.BOLD, 12));
        tablaContactos.setRowHeight(25);

        JScrollPane scroll = new JScrollPane(tablaContactos);
        scroll.setBorder(BorderFactory.createTitledBorder("📋 Lista de contactos"));
        add(scroll, BorderLayout.CENTER);


        JPanel panelInferior = new JPanel(new GridLayout(2, 1));
        panelInferior.setBackground(new Color(245, 245, 245));

        JPanel panelBotones = new JPanel(new FlowLayout());
        panelBotones.setBackground(new Color(245, 245, 245));

        JButton btnBuscar = new JButton("Buscar");
        JButton btnEliminar = new JButton("Eliminar");
        JButton btnModificar = new JButton("Modificar");
        JButton btnListar = new JButton("Listar");
        JButton btnInfo = new JButton("Info");

        JButton[] botones = {btnBuscar, btnEliminar, btnModificar, btnListar, btnInfo};
        for (JButton b : botones) {
            b.setBackground(new Color(70, 130, 180));
            b.setForeground(Color.WHITE);
            b.setFont(new Font("Arial", Font.BOLD, 12));
            panelBotones.add(b);
        }

        lblEstado = new JLabel(" Bienvenido a tu Agenda", SwingConstants.CENTER);
        lblEstado.setFont(new Font("Arial", Font.ITALIC, 14));

        panelInferior.add(panelBotones);
        panelInferior.add(lblEstado);

        add(panelInferior, BorderLayout.SOUTH);

        btnAgregar.addActionListener(e -> {
            String nombre = txtNombre.getText().trim();
            String telefono = txtTelefono.getText().trim();

            if (nombre.isEmpty() || telefono.isEmpty()) {
                mostrarEstado("El nombre y teléfono no pueden estar vacíos", Color.RED);
                return;
            }

            if (!Contacto.validarTelefono(telefono)) {
                mostrarEstado("Teléfono inválido", Color.RED);
                return;
            }

            Contacto nuevo = new Contacto(nombre, telefono);
            if (agenda.añadirContacto(nuevo)) {
                actualizarTabla();
                mostrarEstado("Contacto añadido", new Color(0, 128, 0));
                txtNombre.setText("");
                txtTelefono.setText("");
            } else {
                mostrarEstado("No se pudo añadir (duplicado, agenda llena o inválido)", Color.RED);
            }
        });


        btnListar.addActionListener(e -> actualizarTabla());


        btnBuscar.addActionListener(e -> {
            String nombre = JOptionPane.showInputDialog(this, "Ingrese nombre a buscar:");
            if (nombre != null && !nombre.trim().isEmpty()) {
                boolean encontrado = agenda.getCantidadContactos() > 0 &&
                        agenda.getCapacidadMaxima() > 0;


                Contacto contacto = agenda.getCantidadContactos() == 0 ? null :
                        agendaExiste(nombre);

                if (contacto != null) {
                    mostrarEstado("Encontrado: " + contacto.getTelefono(), new Color(0, 128, 0));
                } else {
                    mostrarEstado("Contacto no encontrado", Color.RED);
                }
            }
        });


        btnEliminar.addActionListener(e -> {
            int fila = tablaContactos.getSelectedRow();
            if (fila >= 0) {
                String nombre = (String) modeloTabla.getValueAt(fila, 0);
                Contacto c = new Contacto(nombre, "0000");
                if (agenda.eliminarContacto(c)) {
                    actualizarTabla();
                    mostrarEstado("Contacto eliminado", Color.RED);
                }
            } else {
                mostrarEstado("Seleccione un contacto para eliminar", Color.RED);
            }
        });


        btnModificar.addActionListener(e -> {
            int fila = tablaContactos.getSelectedRow();
            if (fila >= 0) {
                String nombreViejo = (String) modeloTabla.getValueAt(fila, 0);
                String telefonoViejo = (String) modeloTabla.getValueAt(fila, 1);

                String nuevoNombre = JOptionPane.showInputDialog(this, "Nuevo nombre:", nombreViejo);
                String nuevoTelefono = JOptionPane.showInputDialog(this, "Nuevo teléfono:", telefonoViejo);

                if (nuevoNombre != null && nuevoTelefono != null && Contacto.validarTelefono(nuevoTelefono)) {
                    Contacto viejo = new Contacto(nombreViejo, telefonoViejo);
                    agenda.eliminarContacto(viejo);
                    agenda.añadirContacto(new Contacto(nuevoNombre, nuevoTelefono));
                    actualizarTabla();
                    mostrarEstado("Contacto modificado", new Color(0, 128, 0));
                } else {
                    mostrarEstado("Datos inválidos para modificar", Color.RED);
                }
            } else {
                mostrarEstado("Seleccione un contacto para modificar", Color.RED);
            }
        });

        // INFO
        btnInfo.addActionListener(e -> {
            JOptionPane.showMessageDialog(this,
                    "Capacidad máxima: " + agenda.getCapacidadMaxima() +
                            "\nContactos actuales: " + agenda.getCantidadContactos() +
                            "\nEspacio libre: " + agenda.espacioLibre(),
                    "Información de Agenda", JOptionPane.INFORMATION_MESSAGE);
        });
    }

    private void actualizarTabla() {
        modeloTabla.setRowCount(0);
        for (Contacto c : agendaOrdenada()) {
            modeloTabla.addRow(new Object[]{c.getNombre(), c.getTelefono()});
        }
    }

    private java.util.List<Contacto> agendaOrdenada() {
        java.util.List<Contacto> lista = new java.util.ArrayList<>(agenda.getCantidadContactos());

        try {
            java.lang.reflect.Field f = Agenda.class.getDeclaredField("contactos");
            f.setAccessible(true);
            lista = (java.util.List<Contacto>) f.get(agenda);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return lista;
    }

    private Contacto agendaExiste(String nombre) {
        for (Contacto c : agendaOrdenada()) {
            if (c.getNombre().equalsIgnoreCase(nombre)) {
                return c;
            }
        }
        return null;
    }

    private void mostrarEstado(String mensaje, Color color) {
        lblEstado.setText(" " + mensaje);
        lblEstado.setForeground(color);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new AgendaGUI().setVisible(true));
    }
}
