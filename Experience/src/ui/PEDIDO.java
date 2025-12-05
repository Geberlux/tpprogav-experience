package ui;

import java.awt.EventQueue;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JLabel;
import java.awt.Font;
import javax.swing.SwingConstants;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

import bll.Persona;

public class PEDIDO extends JFrame {

    private static final long serialVersionUID = 1L;
    private JPanel contentPane;

    private Persona personaLogueada;

    public static void main(String[] args) {
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    PEDIDO frame = new PEDIDO(null);
                    frame.setVisible(true);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public PEDIDO(Persona persona) {
        this.personaLogueada = persona;

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(100, 100, 925, 508);
        contentPane = new JPanel();
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        setContentPane(contentPane);
        contentPane.setLayout(null);

        JLabel lblNewLabel = new JLabel("Pedido");
        lblNewLabel.setHorizontalAlignment(SwingConstants.CENTER);
        lblNewLabel.setFont(new Font("Tahoma", Font.BOLD, 15));
        lblNewLabel.setBounds(393, 11, 73, 45);
        contentPane.add(lblNewLabel);

        JButton btnComprar = new JButton("Comprar");
        btnComprar.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                new COMPRAR(personaLogueada).setVisible(true);
                dispose();
            }
        });
        btnComprar.setBounds(176, 81, 89, 23);
        contentPane.add(btnComprar);

        JButton btnProductos = new JButton("Productos");
        btnProductos.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                new PRODUCTOS_CLIENTE(personaLogueada).setVisible(true);
                dispose();
            }
        });
        btnProductos.setBounds(288, 81, 89, 23);
        contentPane.add(btnProductos);

        JButton btnEditar = new JButton("Editar");
        btnEditar.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                new EDITAR_PEDIDO(personaLogueada).setVisible(true);
                dispose();
            }
        });
        btnEditar.setBounds(387, 81, 89, 23);
        contentPane.add(btnEditar);

        JButton btnEliminar = new JButton("Eliminar");
        btnEliminar.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                new ELIMINAR_PEDIDO(personaLogueada).setVisible(true);
                dispose();
            }
        });
        btnEliminar.setBounds(486, 81, 89, 23);
        contentPane.add(btnEliminar);

        JButton btnBuscar = new JButton("Buscar");
        btnBuscar.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                new BUSCAR_PEDIDO(personaLogueada).setVisible(true);
                dispose();
            }
        });
        btnBuscar.setBounds(585, 81, 89, 23);
        contentPane.add(btnBuscar);

        JButton btnVolver = new JButton("Volver");
        btnVolver.addActionListener(e -> {
            if (personaLogueada.getRol().equalsIgnoreCase("admin")) {
                new PANEL_ADMIN(personaLogueada).setVisible(true);
            } else {
                new PANEL_USUARIO(personaLogueada).setVisible(true);
            }
            dispose();
        });
        btnVolver.setBounds(387, 164, 89, 23);
        contentPane.add(btnVolver);
    }
}
