package ui;

import java.awt.Font;
import java.util.LinkedList;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;

import bll.Persona;
import bll.Producto;
import bll.Categoria;
import dll.DtoCategoria;
import dll.DtoProducto;

public class PRODUCTOS_CLIENTE extends JFrame {

    private static final long serialVersionUID = 1L;
    private JPanel contentPane;
    private JTable tableProductos;
    private JTextField txtBuscar;

    private LinkedList<Producto> listaProductos;
    private LinkedList<Categoria> listaCategorias;

    private Persona personaLogueada;
    private JComboBox<String> comboCategoria;

    private boolean ready = false;

    
    public PRODUCTOS_CLIENTE(Persona persona) {
        this.personaLogueada = persona;

        setTitle("Productos disponibles");
        setResizable(false);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(100, 100, 900, 600);
        contentPane = new JPanel();
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        setContentPane(contentPane);
        contentPane.setLayout(null);

        JLabel lblTitulo = new JLabel("Listado de Productos");
        lblTitulo.setFont(new Font("Tahoma", Font.BOLD, 20));
        lblTitulo.setBounds(320, 20, 300, 30);
        contentPane.add(lblTitulo);

        JLabel lblBuscar = new JLabel("Buscar por nombre:");
        lblBuscar.setFont(new Font("Tahoma", Font.PLAIN, 14));
        lblBuscar.setBounds(30, 80, 150, 20);
        contentPane.add(lblBuscar);

        txtBuscar = new JTextField();
        txtBuscar.setBounds(170, 80, 160, 22);
        txtBuscar.addActionListener(e -> actualizarTabla());
        contentPane.add(txtBuscar);

        JLabel lblCategoria = new JLabel("Filtrar categoría:");
        lblCategoria.setFont(new Font("Tahoma", Font.PLAIN, 14));
        lblCategoria.setBounds(350, 80, 140, 20);
        contentPane.add(lblCategoria);

        comboCategoria = new JComboBox<>();
        comboCategoria.setBounds(480, 80, 180, 22);
        comboCategoria.addActionListener(e -> actualizarTabla());
        contentPane.add(comboCategoria);

        JScrollPane scrollPane = new JScrollPane();
        scrollPane.setBounds(30, 130, 830, 360);
        contentPane.add(scrollPane);

        tableProductos = new JTable();

        DefaultTableModel model = new DefaultTableModel(
                new Object[][]{},
                new String[]{"Nombre", "Precio", "Cantidad", "Categoría"}
        ) {
            private static final long serialVersionUID = 1L;

            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // segunda capa de protección
            }
        };

        tableProductos.setModel(model);

        // bloquea totalmente la edición, incluso con doble click
        tableProductos.setDefaultEditor(Object.class, null);

        scrollPane.setViewportView(tableProductos);

        JButton btnVolver = new JButton("Volver");
        btnVolver.setBounds(380, 510, 120, 30);
        btnVolver.addActionListener(e -> {
            PANEL_USUARIO panel = new PANEL_USUARIO(personaLogueada);
            panel.setVisible(true);
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

        DefaultTableModel model = (DefaultTableModel) tableProductos.getModel();
        model.setRowCount(0);

        for (Producto p : listaProductos) {
            if (!texto.isEmpty() && !p.getNombre().toLowerCase().contains(texto)) continue;
            if (idCat != null && p.getCategoria() != idCat) continue;

            String nombreCategoria = "N/A";
            for (Categoria c : listaCategorias) {
                if (c.getId() == p.getCategoria()) {
                    nombreCategoria = c.getNombre();
                    break;
                }
            }

            model.addRow(new Object[]{
                    p.getNombre(),
                    p.getPrecio(),
                    p.getCantidad(),
                    nombreCategoria
            });
        }
    }
}
