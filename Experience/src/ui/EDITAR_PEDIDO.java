package ui;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;

import bll.Persona;
import bll.Pedido_producto;
import dll.DtoPedido;
import dll.DtoPedido_Producto;
import dll.DtoProducto;

import java.awt.event.*;
import java.util.LinkedList;

public class EDITAR_PEDIDO extends JFrame {

    private static final long serialVersionUID = 1L;

    private JPanel contentPane;
    private Persona personaLogueada;

    private JComboBox<String> comboPedidos;
    private JTable tabla;
    private DefaultTableModel modelo;

    private JButton btnEliminarProducto;
    private JButton btnAgregarProducto;

    public EDITAR_PEDIDO(Persona persona) {
        this.personaLogueada = persona;

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(100, 100, 600, 400);

        contentPane = new JPanel();
        contentPane.setBorder(new EmptyBorder(5,5,5,5));
        setContentPane(contentPane);
        contentPane.setLayout(null);

        JLabel lblTitulo = new JLabel("Editar Pedido");
        lblTitulo.setBounds(240, 10, 150, 20);
        contentPane.add(lblTitulo);

        comboPedidos = new JComboBox<>();
        comboPedidos.setBounds(30, 50, 200, 25);
        contentPane.add(comboPedidos);

        cargarPedidos();

        comboPedidos.addActionListener(e -> {
            if (comboPedidos.getSelectedIndex() != -1) {
                cargarDetallesPedido();
            }
        });

        // TABLA
        modelo = new DefaultTableModel(
                new Object[][]{},
                new String[]{"Producto", "Precio", "Cantidad", "DetalleID", "ProductoID"}
        ) {
            @Override
            public boolean isCellEditable(int row, int col) {
                return false;
            }
        };

        tabla = new JTable(modelo);
        tabla.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        // Ocultar columnas ID
        tabla.getColumnModel().getColumn(3).setMinWidth(0);
        tabla.getColumnModel().getColumn(3).setMaxWidth(0);
        tabla.getColumnModel().getColumn(3).setWidth(0);

        tabla.getColumnModel().getColumn(4).setMinWidth(0);
        tabla.getColumnModel().getColumn(4).setMaxWidth(0);
        tabla.getColumnModel().getColumn(4).setWidth(0);

        JScrollPane scroll = new JScrollPane(tabla);
        scroll.setBounds(30, 90, 520, 200);
        contentPane.add(scroll);

        // BOTON ELIMINAR
        btnEliminarProducto = new JButton("Eliminar producto");
        btnEliminarProducto.setBounds(30, 300, 150, 25);
        btnEliminarProducto.addActionListener(e -> eliminarProducto());
        contentPane.add(btnEliminarProducto);

        // BOTON AGREGAR
        btnAgregarProducto = new JButton("Agregar producto");
        btnAgregarProducto.setBounds(200, 300, 150, 25);
        btnAgregarProducto.addActionListener(e -> agregarProducto());
        contentPane.add(btnAgregarProducto);

        // VOLVER
        JButton btnVolver = new JButton("Volver");
        btnVolver.setBounds(400, 300, 120, 25);
        btnVolver.addActionListener(e -> {
            new PEDIDO(personaLogueada).setVisible(true);
            dispose();
        });
        contentPane.add(btnVolver);
    }

    // ============================
    //   CARGAR LISTA DE PEDIDOS
    // ============================
    private void cargarPedidos() {
        comboPedidos.removeAllItems();

        LinkedList<bll.Pedido> pedidos = DtoPedido.obtenerPedidosPorUsuario(personaLogueada.getId());

        for (bll.Pedido p : pedidos) {
            comboPedidos.addItem("Pedido " + p.getId());
        }
    }

    // ============================
    //   CARGAR PRODUCTOS DEL PEDIDO
    // ============================
    private void cargarDetallesPedido() {
        modelo.setRowCount(0);

        if (comboPedidos.getSelectedIndex() == -1)
            return;

        int idPedido = obtenerIDPedidoSeleccionado();

        LinkedList<String> detalles = DtoPedido_Producto.obtenerDetallesConProducto(idPedido);

        for (String d : detalles) {
            String[] partes = d.split("\\|");

            String detalleID = partes[0].replace("DetalleID:", "").trim();
            String productoID = partes[1].replace("ProductoID:", "").trim();
            String nombre = partes[2].trim();
            String precio = partes[3].replace("Precio:", "").trim();
            String cantidad = partes[4].replace("Cant:", "").trim();

            modelo.addRow(new Object[]{
                    nombre, precio, cantidad, detalleID, productoID
            });
        }
    }

    private int obtenerIDPedidoSeleccionado() {
        String texto = comboPedidos.getSelectedItem().toString();
        return Integer.parseInt(texto.replaceAll("\\D+", ""));
    }

    // ============================
    //       ELIMINAR PRODUCTO
    // ============================
    private void eliminarProducto() {
        int fila = tabla.getSelectedRow();
        if (fila == -1) {
            JOptionPane.showMessageDialog(this, "Seleccione un producto.");
            return;
        }

        int idDetalle = Integer.parseInt(modelo.getValueAt(fila, 3).toString());

        if (DtoPedido_Producto.eliminarDetalle(idDetalle)) {
            JOptionPane.showMessageDialog(this, "Producto eliminado.");
            cargarDetallesPedido();
        } else {
            JOptionPane.showMessageDialog(this, "Error eliminando producto.");
        }
    }

    // ============================
    //       AGREGAR PRODUCTO
    // ============================
    private void agregarProducto() {
        int idPedido = obtenerIDPedidoSeleccionado();

        bll.Producto seleccionado = DtoProducto.buscarPorIDConFiltro();
        if (seleccionado == null) return;

        Pedido_producto nuevo = new Pedido_producto(idPedido, seleccionado.getId());
        boolean ok = DtoPedido_Producto.agregarPedido_producto(nuevo);

        if (ok) {
            JOptionPane.showMessageDialog(this, "Producto agregado.");
            cargarDetallesPedido();
        } else {
            JOptionPane.showMessageDialog(this, "Error agregando producto.");
        }
    }
}
