package ui;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;

import bll.Pedido;
import bll.Pedido_producto;
import bll.Persona;
import bll.Producto;
import dll.DtoPedido;
import dll.DtoPedido_Producto;
import dll.DtoProducto;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.LinkedList;

public class ELIMINAR_PEDIDO extends JFrame {

    private static final long serialVersionUID = 1L;
    private JPanel contentPane;
    private Persona personaLogueada;

    private JTable tablaPedidos;
    private JTable tablaDetalles;

    private DefaultTableModel modeloPedidos;
    private DefaultTableModel modeloDetalles;

    private int pedidoSeleccionado = -1;

    public ELIMINAR_PEDIDO(Persona persona) {
        this.personaLogueada = persona;

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(100, 100, 650, 500);
        contentPane = new JPanel();
        contentPane.setBorder(new EmptyBorder(10, 10, 10, 10));
        setContentPane(contentPane);
        contentPane.setLayout(null);

        JLabel lblPedidos = new JLabel("Pedidos del usuario:");
        lblPedidos.setBounds(20, 10, 200, 20);
        contentPane.add(lblPedidos);

        modeloPedidos = new DefaultTableModel(
            new Object[][]{},
            new String[]{"ID Pedido"}
        ) {
            @Override public boolean isCellEditable(int r, int c) { return false; }
        };

        tablaPedidos = new JTable(modeloPedidos);
        JScrollPane scrollPedidos = new JScrollPane(tablaPedidos);
        scrollPedidos.setBounds(20, 40, 280, 180);
        contentPane.add(scrollPedidos);

        tablaPedidos.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int fila = tablaPedidos.getSelectedRow();
                if (fila >= 0) {
                    pedidoSeleccionado = Integer.parseInt(modeloPedidos.getValueAt(fila, 0).toString());
                    cargarDetallesPedido(pedidoSeleccionado);
                }
            }
        });

        JLabel lblDetalles = new JLabel("Detalles del pedido:");
        lblDetalles.setBounds(320, 10, 200, 20);
        contentPane.add(lblDetalles);

        modeloDetalles = new DefaultTableModel(
            new Object[][]{},
            new String[]{"Producto", "Precio", "Cantidad"}
        ) {
            @Override public boolean isCellEditable(int r, int c) { return false; }
        };

        tablaDetalles = new JTable(modeloDetalles);
        JScrollPane scrollDetalles = new JScrollPane(tablaDetalles);
        scrollDetalles.setBounds(320, 40, 300, 180);
        contentPane.add(scrollDetalles);

        JButton btnEliminar = new JButton("Eliminar pedido seleccionado");
        btnEliminar.setBounds(200, 250, 250, 35);
        btnEliminar.addActionListener(e -> eliminarPedido());
        contentPane.add(btnEliminar);

        JButton btnVolver = new JButton("Volver");
        btnVolver.setBounds(250, 320, 150, 30);
        btnVolver.addActionListener(e -> {
            new PEDIDO(personaLogueada).setVisible(true);
            dispose();
        });
        contentPane.add(btnVolver);

        cargarPedidosUsuario();
    }

    private void cargarPedidosUsuario() {
        modeloPedidos.setRowCount(0);

        LinkedList<Pedido> pedidos = DtoPedido.obtenerPedidosPorUsuario(personaLogueada.getId());

        for (Pedido p : pedidos) {
            modeloPedidos.addRow(new Object[]{p.getId()});
        }
    }

    private void cargarDetallesPedido(int idPedido) {
        modeloDetalles.setRowCount(0);

        LinkedList<Pedido_producto> detalles = DtoPedido_Producto.obtenerDetallesPorPedido(idPedido);

        for (Pedido_producto pp : detalles) {
            Producto pr = DtoProducto.obtenerProductoPorID(pp.getIdproducto());
            if (pr != null) {
                modeloDetalles.addRow(new Object[]{
                        pr.getNombre(),
                        pr.getPrecio(),
                        pr.getCantidad()
                });
            }
        }
    }

    private void eliminarPedido() {
        if (pedidoSeleccionado == -1) {
            JOptionPane.showMessageDialog(this, "Seleccione un pedido primero.");
            return;
        }

        int confirm = JOptionPane.showConfirmDialog(
                this,
                "¿Seguro que desea eliminar el pedido " + pedidoSeleccionado + "?",
                "Confirmar eliminación",
                JOptionPane.YES_NO_OPTION
        );

        if (confirm != JOptionPane.YES_OPTION) return;

        DtoPedido_Producto.eliminarPorPedido(pedidoSeleccionado);

        boolean ok = DtoPedido.eliminarPedido(pedidoSeleccionado);

        if (ok) {
            JOptionPane.showMessageDialog(this, "Pedido eliminado correctamente.");
            pedidoSeleccionado = -1;
            modeloDetalles.setRowCount(0);
            cargarPedidosUsuario();
        } else {
            JOptionPane.showMessageDialog(this, "Error al eliminar el pedido.");
        }
    }
}
