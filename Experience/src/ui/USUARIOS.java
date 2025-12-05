package ui;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;

import bll.Persona;
import dll.DtoPersona;

import java.awt.*;
import java.util.LinkedList;

public class USUARIOS extends JFrame {

    private JPanel contentPane;
    private JTable tabla;
    private DefaultTableModel modelo;
    private JTextField txtBuscar;
    private JComboBox<String> filtroRol;

    private JFrame ventanaAnterior;

    public USUARIOS(JFrame anterior) {
        this.ventanaAnterior = anterior;

        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setBounds(100, 100, 750, 420);

        contentPane = new JPanel();
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        contentPane.setLayout(new BorderLayout());
        setContentPane(contentPane);

        JPanel panelSuperior = new JPanel(new FlowLayout(FlowLayout.LEFT));

        txtBuscar = new JTextField(15);
        panelSuperior.add(txtBuscar);

        JButton btnBuscar = new JButton("Buscar");
        btnBuscar.addActionListener(e -> cargarTabla());
        panelSuperior.add(btnBuscar);

        filtroRol = new JComboBox<>();
        filtroRol.addItem("Todos");
        filtroRol.addItem("admin");
        filtroRol.addItem("cliente");
        filtroRol.addActionListener(e -> cargarTabla());
        panelSuperior.add(filtroRol);

        JButton btnGuardar = new JButton("Confirmar Cambios");
        btnGuardar.addActionListener(e -> guardarTodo());
        panelSuperior.add(btnGuardar);

        JButton btnEliminar = new JButton("Eliminar");
        btnEliminar.addActionListener(e -> eliminarSeleccionado());
        panelSuperior.add(btnEliminar);

        JButton btnVolver = new JButton("Volver");
        btnVolver.addActionListener(e -> {
            dispose();
            ventanaAnterior.setVisible(true);
        });
        panelSuperior.add(btnVolver);

        contentPane.add(panelSuperior, BorderLayout.NORTH);

        modelo = new DefaultTableModel(
                new Object[]{"ID", "Nombre", "Correo", "Contraseña", "Rol"},
                0
        ) {
            public boolean isCellEditable(int row, int column) {
                return true;
            }
        };

        tabla = new JTable(modelo);
        JScrollPane scroll = new JScrollPane(tabla);
        contentPane.add(scroll, BorderLayout.CENTER);

        cargarTabla();
    }

    private void cargarTabla() {
        modelo.setRowCount(0);

        LinkedList<Persona> lista = Persona.mostrarPersonas();

        String buscar = txtBuscar.getText().trim().toLowerCase();
        String rolFiltro = filtroRol.getSelectedItem().toString();

        for (Persona p : lista) {
            boolean coincide =
                    p.getNombre().toLowerCase().contains(buscar) ||
                    p.getCorreo().toLowerCase().contains(buscar) ||
                    String.valueOf(p.getId()).contains(buscar);

            boolean coincideRol =
                    rolFiltro.equals("Todos") || p.getRol().equals(rolFiltro);

            if (coincide && coincideRol) {
                modelo.addRow(new Object[]{
                        p.getId(),
                        p.getNombre(),
                        p.getCorreo(),
                        p.getContrasenia(),
                        p.getRol()
                });
            }
        }

        modelo.addRow(new Object[]{"", "", "", "", ""});
    }

    private void guardarTodo() {
        for (int i = 0; i < modelo.getRowCount(); i++) {

            Object idObj = modelo.getValueAt(i, 0);
            Object nombreObj = modelo.getValueAt(i, 1);
            Object correoObj = modelo.getValueAt(i, 2);
            Object passObj = modelo.getValueAt(i, 3);
            Object rolObj = modelo.getValueAt(i, 4);

            String nombre = nombreObj == null ? "" : nombreObj.toString().trim();
            String correo = correoObj == null ? "" : correoObj.toString().trim();
            String contrasenia = passObj == null ? "" : passObj.toString().trim();
            String rol = rolObj == null ? "" : rolObj.toString().trim();

            boolean filaVacia = nombre.isEmpty() && correo.isEmpty() && contrasenia.isEmpty() && rol.isEmpty();
            if (filaVacia) continue;

            if (idObj == null || idObj.toString().isEmpty()) {
                Persona nuevo = new Persona(nombre, correo, contrasenia, rol);
                DtoPersona.agregarPersona(nuevo);
            } else {
                int id = Integer.parseInt(idObj.toString());
                Persona editado = new Persona(id, nombre, correo, contrasenia, rol);
                DtoPersona.EditarPersona(editado);
            }
        }

        cargarTabla();
    }

    private void eliminarSeleccionado() {
        int fila = tabla.getSelectedRow();
        if (fila < 0) return;

        Object idObj = modelo.getValueAt(fila, 0);
        if (idObj == null || idObj.toString().isEmpty()) return;

        int id = Integer.parseInt(idObj.toString());
        DtoPersona.EliminarPersona(id);

        cargarTabla();
    }
}
