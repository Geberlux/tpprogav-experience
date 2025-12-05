package ui;

import java.awt.Font;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Set;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;

import bll.Persona;
import bll.Producto;
import bll.Categoria;
import bll.Pedido;
import bll.Pedido_producto;
import dll.DtoCategoria;
import dll.DtoPedido;
import dll.DtoPedido_Producto;
import dll.DtoProducto;

public class COMPRAR extends JFrame {

    private static final long serialVersionUID = 1L;

    private JPanel contentPane;
    private JTable tabla;
    private JTextField txtBuscar;
    private JComboBox<String> comboCategoria;

    private LinkedList<Producto> listaProductos;
    private LinkedList<Categoria> listaCategorias;

    private Persona personaLogueada;
    private boolean ready = false;

    private Set<Integer> seleccionados = new HashSet<>();

    public COMPRAR(Persona persona) {
        this.personaLogueada = persona;

        setTitle("Realizar Pedido");
        setResizable(false);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(100, 100, 900, 600);
        contentPane = new JPanel();
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        setContentPane(contentPane);
        contentPane.setLayout(null);

        JLabel lblTitulo = new JLabel("Seleccionar Productos");
        lblTitulo.setFont(new Font("Tahoma", Font.BOLD, 20));
        lblTitulo.setBounds(310, 20, 300, 30);
        contentPane.add(lblTitulo);

        JLabel lblBuscar = new JLabel("Buscar:");
        lblBuscar.setFont(new Font("Tahoma", Font.PLAIN, 14));
        lblBuscar.setBounds(30, 80, 100, 20);
        contentPane.add(lblBuscar);

        txtBuscar = new JTextField();
        txtBuscar.setBounds(90, 80, 160, 22);
        txtBuscar.addActionListener(e -> actualizarTabla());
        contentPane.add(txtBuscar);

        JLabel lblCategoria = new JLabel("Categoría:");
        lblCategoria.setFont(new Font("Tahoma", Font.PLAIN, 14));
        lblCategoria.setBounds(280, 80, 140, 20);
        contentPane.add(lblCategoria);

        comboCategoria = new JComboBox<>();
        comboCategoria.setBounds(360, 80, 180, 22);
        comboCategoria.addActionListener(e -> actualizarTabla());
        contentPane.add(comboCategoria);

        JScrollPane scrollPane = new JScrollPane();
        scrollPane.setBounds(30, 130, 830, 350);
        contentPane.add(scrollPane);

        tabla = new JTable();

        DefaultTableModel model = new DefaultTableModel(
                new Object[][]{},
                new String[]{"ID", "Nombre", "Precio", "Stock", "Categoría"}
        ) {
            private static final long serialVersionUID = 1L;

            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        tabla.setModel(model);
        tabla.setDefaultEditor(Object.class, null); // bloquea edición con doble click
        scrollPane.setViewportView(tabla);

        JButton btnAgregar = new JButton("Agregar al pedido");
        btnAgregar.setBounds(200, 500, 150, 30);
        btnAgregar.addActionListener(e -> agregarSeleccionados());
        contentPane.add(btnAgregar);

        JButton btnConfirmar = new JButton("Confirmar Pedido");
        btnConfirmar.setBounds(370, 500, 150, 30);
        btnConfirmar.addActionListener(e -> confirmarPedido());
        contentPane.add(btnConfirmar);

        JButton btnVolver = new JButton("Volver");
        btnVolver.setBounds(540, 500, 150, 30);
        btnVolver.addActionListener(e -> {
            new PEDIDO(personaLogueada).setVisible(true);
            dispose();
        });
        contentPane.add(btnVolver);

        cargarCategorias();
        cargarProductos();
        ready = true;
        actualizarTabla();
    }

    private void cargarCategorias() {
        listaCategorias = DtoCategoria.mostrarCategorias();
        comboCategoria.addItem("Todas");

        for (Categoria c : listaCategorias) {
            comboCategoria.addItem(c.getNombre() + " / " + c.getId());
        }
    }

    private void cargarProductos() {
        listaProductos = DtoProducto.mostrarProductos();
    }

    private void actualizarTabla() {
        if (!ready) return;

        String texto = txtBuscar.getText().trim().toLowerCase();
        String seleccion = (String) comboCategoria.getSelectedItem();

        Integer idCat = null;
        if (seleccion != null && !seleccion.equals("Todas")) {
            try {
                idCat = Integer.parseInt(seleccion.split("/")[1].trim());
            } catch (Exception ignored) {}
        }

        DefaultTableModel model = (DefaultTableModel) tabla.getModel();
        model.setRowCount(0);

        for (Producto p : listaProductos) {
            if (!texto.isEmpty() && !p.getNombre().toLowerCase().contains(texto)) continue;
            if (idCat != null && p.getCategoria() != idCat) continue;

            String nombreCategoria = "N/A";
            for (Categoria c : listaCategorias) {
                if (c.getId() == p.getCategoria()) nombreCategoria = c.getNombre();
            }

            model.addRow(new Object[]{
                    p.getId(),
                    p.getNombre(),
                    p.getPrecio(),
                    p.getCantidad(),
                    nombreCategoria
            });
        }
    }

    private void agregarSeleccionados() {
        int[] filas = tabla.getSelectedRows();
        if (filas.length == 0) {
            JOptionPane.showMessageDialog(this, "Seleccione al menos un producto.");
            return;
        }

        for (int f : filas) {
            int idProd = Integer.parseInt(tabla.getValueAt(f, 0).toString());
            seleccionados.add(idProd);
        }

        JOptionPane.showMessageDialog(this, "Productos agregados al pedido.");
    }

    private void confirmarPedido() {
        if (seleccionados.isEmpty()) {
            JOptionPane.showMessageDialog(this, "No se seleccionaron productos.");
            return;
        }

        StringBuilder resumen = new StringBuilder();
        for (Integer pid : seleccionados) {
            Producto p = DtoProducto.obtenerProductoPorID(pid);
            if (p != null) resumen.append("ID: ").append(p.getId()).append(" - ").append(p.getNombre()).append("\n");
        }

        int confirmar = JOptionPane.showConfirmDialog(
                this,
                "Productos seleccionados:\n\n" + resumen + "\n¿Confirmar pedido?",
                "Confirmar",
                JOptionPane.YES_NO_OPTION
        );

        if (confirmar != JOptionPane.YES_OPTION) return;

        int idPedido = DtoPedido.agregarPedido(new Pedido(personaLogueada.getId()));

        if (idPedido <= 0) {
            JOptionPane.showMessageDialog(this, "Error al crear pedido.");
            return;
        }

        boolean ok = true;
        for (Integer pid : seleccionados) {
            Pedido_producto det = new Pedido_producto(idPedido, pid);
            if (!DtoPedido_Producto.agregarPedido_producto(det)) ok = false;
        }

        JOptionPane.showMessageDialog(this,
                ok ? "Pedido creado correctamente. ID: " + idPedido
                        : "Pedido creado con errores en detalles.");

        new PEDIDO(personaLogueada).setVisible(true);
        dispose();
    }
}
