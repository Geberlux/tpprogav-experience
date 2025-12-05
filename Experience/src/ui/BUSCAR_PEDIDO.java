package ui;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;

import bll.Persona;
import bll.Pedido;
import bll.Pedido_producto;
import bll.Producto;
import dll.DtoPedido;
import dll.DtoPedido_Producto;
import dll.DtoProducto;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.LinkedList;

public class BUSCAR_PEDIDO extends JFrame {

    private static final long serialVersionUID = 1L;
    private JPanel contentPane;
    private Persona personaLogueada;

    private JTable tablaPedidos;
    private JTable tablaDetalles;

    private DefaultTableModel modeloPedidos;
    private DefaultTableModel modeloDetalles;

    public BUSCAR_PEDIDO(Persona persona) {
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

        // ------------ TABLA DE PEDIDOS ------------
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
                    int idPedido = Integer.parseInt(modeloPedidos.getValueAt(fila, 0).toString());
                    cargarDetallesPedido(idPedido);
                }
            }
        });

        JLabel lblDetalles = new JLabel("Detalles del pedido:");
        lblDetalles.setBounds(320, 10, 200, 20);
        contentPane.add(lblDetalles);

        // ------------ TABLA DE DETALLES ------------
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

        // ------------ BOTON VOLVER ------------
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
}
