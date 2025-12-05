package ui;

import java.awt.BorderLayout;
import java.awt.Font;
import java.util.LinkedList;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;

import javax.swing.JOptionPane;

import bll.Categoria;
import dll.DtoCategoria;

public class CATEGORIA extends JFrame {

    private static final long serialVersionUID = 1L;
    private JTable tabla;
    private DefaultTableModel modelo;
    private JTextField txtBuscar;
    private LinkedList<Categoria> categorias;
    private JFrame ventanaAnterior;

    public CATEGORIA(JFrame anterior) {
        this.ventanaAnterior = anterior;

        setTitle("Administrar Categorías");
        setBounds(250, 200, 600, 400);
        setResizable(false);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        getContentPane().setLayout(new BorderLayout());

        categorias = DtoCategoria.mostrarCategorias();

        JPanel arriba = new JPanel();
        arriba.setLayout(null);
        arriba.setPreferredSize(new java.awt.Dimension(600, 50));

        txtBuscar = new JTextField();
        txtBuscar.setBounds(20, 10, 200, 30);
        arriba.add(txtBuscar);

        JButton btnBuscar = new JButton("Buscar");
        btnBuscar.setBounds(230, 10, 100, 30);
        btnBuscar.addActionListener(e -> buscarCategoria());
        arriba.add(btnBuscar);

        getContentPane().add(arriba, BorderLayout.NORTH);

        String[] columnas = {"ID", "Nombre"};
        modelo = new DefaultTableModel(columnas, 0) {
            public boolean isCellEditable(int row, int column) {
                return column != 0;
            }
        };

        tabla = new JTable(modelo);
        tabla.setFont(new Font("Arial", Font.PLAIN, 14));
        tabla.setRowHeight(25);

        JScrollPane scroll = new JScrollPane(tabla);
        getContentPane().add(scroll, BorderLayout.CENTER);

        JPanel abajo = new JPanel();

        JButton btnAgregar = new JButton("Nueva Fila");
        btnAgregar.addActionListener(e -> agregarFila());

        JButton btnGuardar = new JButton("Guardar Cambios");
        btnGuardar.addActionListener(e -> guardarCambios());

        JButton btnEliminar = new JButton("Eliminar");
        btnEliminar.setBackground(java.awt.Color.RED);
        btnEliminar.setForeground(java.awt.Color.WHITE);
        btnEliminar.addActionListener(e -> eliminarCategoria());

        JButton btnVolver = new JButton("Volver");
        btnVolver.addActionListener(e -> {
            ventanaAnterior.setVisible(true);
            dispose();
        });

        abajo.add(btnAgregar);
        abajo.add(btnGuardar);
        abajo.add(btnEliminar);
        abajo.add(btnVolver);

        getContentPane().add(abajo, BorderLayout.SOUTH);

        cargarTabla();
    }

    private void cargarTabla() {
        modelo.setRowCount(0);
        categorias = DtoCategoria.mostrarCategorias();

        for (Categoria c : categorias) {
            modelo.addRow(new Object[]{
                    c.getId(),
                    c.getNombre()
            });
        }

        modelo.addRow(new Object[]{"", ""});
    }

    private void agregarFila() {
        modelo.addRow(new Object[]{"", ""});
    }

    private void guardarCambios() {
        for (int i = 0; i < modelo.getRowCount(); i++) {

            Object idObj = modelo.getValueAt(i, 0);
            Object nomObj = modelo.getValueAt(i, 1);

            if (nomObj == null || nomObj.toString().trim().isEmpty()) continue;

            String nombre = nomObj.toString();

            if (idObj == null || idObj.toString().trim().isEmpty()) {
                Categoria nueva = new Categoria(nombre);
                DtoCategoria.agregarCategoria(nueva);

            } else {
                int id = Integer.parseInt(idObj.toString());
                Categoria editada = new Categoria(id, nombre);
                DtoCategoria.EditarCategoria(editada);
            }
        }

        cargarTabla();
    }

    private void eliminarCategoria() {
        int fila = tabla.getSelectedRow();

        if (fila == -1) {
            JOptionPane.showMessageDialog(this, "Seleccione una categoría.");
            return;
        }

        Object idObj = modelo.getValueAt(fila, 0);

        if (idObj == null || idObj.toString().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "La fila seleccionada no corresponde a una categoría existente.");
            return;
        }

        int id = Integer.parseInt(idObj.toString());

        int confirmar = JOptionPane.showConfirmDialog(
                this,
                "¿Eliminar la categoría?",
                "Confirmar",
                JOptionPane.YES_NO_OPTION
        );

        if (confirmar == JOptionPane.YES_OPTION) {
            DtoCategoria.EliminarCategoria(id);
            JOptionPane.showMessageDialog(this, "Categoría eliminada.");
            cargarTabla();
        }
    }

    private void buscarCategoria() {
        String texto = txtBuscar.getText().trim().toLowerCase();

        if (texto.isEmpty()) {
            cargarTabla();
            return;
        }

        modelo.setRowCount(0);

        for (Categoria c : categorias) {
            if (c.getNombre().toLowerCase().contains(texto)) {
                modelo.addRow(new Object[]{
                        c.getId(),
                        c.getNombre()
                });
            }
        }
    }
}
