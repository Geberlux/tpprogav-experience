package ui;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JButton;
import javax.swing.JLabel;
import java.awt.Font;
import javax.swing.SwingConstants;

import bll.Persona;

public class PANEL_ADMIN extends JFrame {

    private static final long serialVersionUID = 1L;
    private JPanel contentPane;
    private Persona personaLogueada;

    public PANEL_ADMIN(Persona persona) {
        this.personaLogueada = persona;

        setTitle("Panel Administrador");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(100, 100, 400, 420);

        contentPane = new JPanel();
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        setContentPane(contentPane);
        contentPane.setLayout(null);

        JLabel lblTitulo = new JLabel("Panel de Administrador");
        lblTitulo.setHorizontalAlignment(SwingConstants.CENTER);
        lblTitulo.setFont(new Font("Tahoma", Font.BOLD, 18));
        lblTitulo.setBounds(50, 10, 300, 40);
        contentPane.add(lblTitulo);

        JButton btnUsuarios = new JButton("Usuarios");
        btnUsuarios.addActionListener(e -> {
            new USUARIOS(this).setVisible(true);
            setVisible(false);
        });

        btnUsuarios.setBounds(120, 70, 150, 30);
        contentPane.add(btnUsuarios);

        JButton btnProductos = new JButton("Productos");
        btnProductos.addActionListener(e -> {
            new ADMIN_PROD(this).setVisible(true);
            setVisible(false);
        });
        btnProductos.setBounds(120, 120, 150, 30);
        contentPane.add(btnProductos);

        JButton btnCategorias = new JButton("Categorías");
        btnCategorias.addActionListener(e -> {
            new CATEGORIA(this).setVisible(true);
            setVisible(false);
        });
        btnCategorias.setBounds(120, 170, 150, 30);
        contentPane.add(btnCategorias);

        JButton btnPedidos = new JButton("Pedidos");
        btnPedidos.addActionListener(e -> {
            new PEDIDO(personaLogueada).setVisible(true);
            dispose();
        });
        btnPedidos.setBounds(120, 220, 150, 30);
        contentPane.add(btnPedidos);

        JButton btnCerrarSesion = new JButton("Cerrar sesión");
        btnCerrarSesion.addActionListener(e -> {
            new INICIO().setVisible(true);
            dispose();
        });
        btnCerrarSesion.setBounds(120, 310, 150, 30);
        contentPane.add(btnCerrarSesion);
    }
}
