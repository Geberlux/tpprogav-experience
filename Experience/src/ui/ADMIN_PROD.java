package ui;

import java.awt.BorderLayout;
import java.awt.Font;
import java.util.LinkedList;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.DefaultCellEditor;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

import javax.swing.JOptionPane;

import bll.Categoria;
import bll.Producto;
import dll.DtoCategoria;
import dll.DtoProducto;

public class ADMIN_PROD extends JFrame {

    private static final long serialVersionUID = 1L;
    private JTable tabla;
    private DefaultTableModel modelo;
    private JComboBox<String> filtroCategoria;
    private LinkedList<Categoria> categorias;
    private JFrame ventanaAnterior;

    public ADMIN_PROD(JFrame anterior) {
        this.ventanaAnterior = anterior;

        setTitle("Administrar Productos");
        setBounds(200, 200, 900, 500);
        setResizable(false);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        getContentPane().setLayout(new BorderLayout());

        categorias = DtoCategoria.mostrarCategorias();

        JPanel arriba = new JPanel();
        arriba.setLayout(null);
        arriba.setPreferredSize(new java.awt.Dimension(900, 50));

        filtroCategoria = new JComboBox<>();
        filtroCategoria.setBounds(20, 10, 200, 30);

        filtroCategoria.addItem("Todas las categorías");
        for (Categoria c : categorias) filtroCategoria.addItem(c.getNombre());

        filtroCategoria.addActionListener(e -> cargarTablaSegunFiltro());

        arriba.add(filtroCategoria);
        getContentPane().add(arriba, BorderLayout.NORTH);

        String[] columnas = {"ID", "Categoría", "Nombre", "Precio", "Cantidad"};
        modelo = new DefaultTableModel(columnas, 0) {
            public boolean isCellEditable(int row, int column) {
                return column != 0;
            }
        };

        tabla = new JTable(modelo);
        tabla.setRowHeight(25);
        tabla.setFont(new Font("Arial", Font.PLAIN, 14));

        JComboBox<String> comboCategorias = new JComboBox<>();
        for (Categoria c : categorias) comboCategorias.addItem(c.getNombre());
        tabla.getColumnModel().getColumn(1).setCellEditor(new DefaultCellEditor(comboCategorias));

        JScrollPane scroll = new JScrollPane(tabla);
        getContentPane().add(scroll, BorderLayout.CENTER);

        JPanel abajo = new JPanel();

        JButton btnAgregarFila = new JButton("Nueva Fila");
        btnAgregarFila.addActionListener(e -> agregarFila());

        JButton btnConfirmar = new JButton("Confirmar Cambios");
        btnConfirmar.addActionListener(e -> guardarCambios());

        JButton btnEliminar = new JButton("Eliminar");
        btnEliminar.setBackground(java.awt.Color.RED);
        btnEliminar.setForeground(java.awt.Color.WHITE);
        btnEliminar.addActionListener(e -> eliminarProducto());

        JButton btnVolver = new JButton("Volver");
        btnVolver.addActionListener(e -> {
            ventanaAnterior.setVisible(true);
            dispose();
        });

        abajo.add(btnAgregarFila);
        abajo.add(btnConfirmar);
        abajo.add(btnEliminar);
        abajo.add(btnVolver);

        getContentPane().add(abajo, BorderLayout.SOUTH);

        cargarTabla();
    }

    private void cargarTabla() {
        modelo.setRowCount(0);
        LinkedList<Producto> productos = DtoProducto.mostrarProductos();

        for (Producto p : productos) modelo.addRow(new Object[]{
                p.getId(),
                obtenerNombreCategoria(p.getCategoria()),
                p.getNombre(),
                p.getPrecio(),
                p.getCantidad()
        });

        modelo.addRow(new Object[]{"", "", "", "", ""});
    }

    private void cargarTablaSegunFiltro() {
        modelo.setRowCount(0);
        LinkedList<Producto> productos = DtoProducto.mostrarProductos();

        String filtro = (String) filtroCategoria.getSelectedItem();

        for (Producto p : productos) {
            if (!filtro.equals("Todas las categorías")) {
                if (!obtenerNombreCategoria(p.getCategoria()).equals(filtro)) continue;
            }

            modelo.addRow(new Object[]{
                    p.getId(),
                    obtenerNombreCategoria(p.getCategoria()),
                    p.getNombre(),
                    p.getPrecio(),
                    p.getCantidad()
            });
        }

        modelo.addRow(new Object[]{"", "", "", "", ""});
    }

    private String obtenerNombreCategoria(int id) {
        for (Categoria c : categorias) {
            if (c.getId() == id) return c.getNombre();
        }
        return "";
    }

    private int obtenerIdCategoria(String nombre) {
        for (Categoria c : categorias) {
            if (c.getNombre().equals(nombre)) return c.getId();
        }
        return -1;
    }

    private void agregarFila() {
        modelo.addRow(new Object[]{"", "", "", "", ""});
    }

    private void guardarCambios() {
        for (int i = 0; i < modelo.getRowCount(); i++) {
            Object idObj = modelo.getValueAt(i, 0);
            Object catObj = modelo.getValueAt(i, 1);
            Object nomObj = modelo.getValueAt(i, 2);
            Object preObj = modelo.getValueAt(i, 3);
            Object canObj = modelo.getValueAt(i, 4);

            if (nomObj == null || nomObj.toString().trim().isEmpty()) continue;
            if (preObj == null || preObj.toString().trim().isEmpty()) continue;
            if (canObj == null || canObj.toString().trim().isEmpty()) continue;
            if (catObj == null || catObj.toString().trim().isEmpty()) continue;

            int idCategoria = obtenerIdCategoria(catObj.toString());
            String nombre = nomObj.toString();
            String precio = preObj.toString();
            String cantidad = canObj.toString();

            if (idObj == null || idObj.toString().trim().isEmpty()) {
                Producto nuevo = new Producto(idCategoria, nombre, precio, cantidad);
                DtoProducto.agregarProducto(nuevo);

            } else {
                int id = Integer.parseInt(idObj.toString());
                Producto edit = new Producto(id, idCategoria, nombre, precio, cantidad);
                DtoProducto.EditarProducto(edit);
            }
        }

        cargarTablaSegunFiltro();
    }

    private void eliminarProducto() {
        int fila = tabla.getSelectedRow();

        if (fila == -1) {
            JOptionPane.showMessageDialog(this, "Seleccione un producto a eliminar.");
            return;
        }

        Object idObj = modelo.getValueAt(fila, 0);

        if (idObj == null || idObj.toString().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "La fila seleccionada no corresponde a un producto existente.");
            return;
        }

        int id = Integer.parseInt(idObj.toString());

        int confirmar = JOptionPane.showConfirmDialog(
                this,
                "¿Seguro que desea eliminar este producto?",
                "Confirmar eliminación",
                JOptionPane.YES_NO_OPTION
        );

        if (confirmar == JOptionPane.YES_OPTION) {
            DtoProducto.EliminarProducto(id);
            JOptionPane.showMessageDialog(this, "Producto eliminado correctamente.");
            cargarTablaSegunFiltro();
        }
    }
}
