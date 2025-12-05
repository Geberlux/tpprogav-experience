package ui;

import java.awt.EventQueue;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JLabel;
import bll.Persona;
import javax.swing.SwingConstants;
import java.awt.Font;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class PANEL_USUARIO extends JFrame {

	private static final long serialVersionUID = 1L;
	private JPanel contentPane;

	// Guardamos la persona logueada
	private Persona personaLogueada;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					// Llamado SIN persona (solo para pruebas)
					PANEL_USUARIO frame = new PANEL_USUARIO(null);
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public PANEL_USUARIO(Persona persona) {
		this.personaLogueada = persona; // Guardamos el usuario que viene del login

		setResizable(false);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 840, 491);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);

		// Mostrar nombre del usuario logueado
		JLabel lblBienvenida = new JLabel();
		lblBienvenida.setBounds(10, 11, 167, 13);
		lblBienvenida.setFont(new Font("Tahoma", Font.PLAIN, 9));

		if (persona != null) {
			lblBienvenida.setText("Bienvenido: " + persona.getNombre());
		} else {
			lblBienvenida.setText("Bienvenido (modo prueba)");
		}
		contentPane.setLayout(null);

		contentPane.add(lblBienvenida);
		
		JLabel lblNewLabel = new JLabel("Opciones");
		lblNewLabel.setBounds(349, 68, 126, 32);
		lblNewLabel.setHorizontalAlignment(SwingConstants.CENTER);
		lblNewLabel.setFont(new Font("Tahoma", Font.PLAIN, 15));
		contentPane.add(lblNewLabel);
		
		JButton btnNewButton_1 = new JButton("Pedido");
		btnNewButton_1.setBounds(240, 143, 89, 23);
		btnNewButton_1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				PEDIDO ventana = new PEDIDO(personaLogueada);
		        ventana.setVisible(true);

		        dispose();
			}
		});
		contentPane.add(btnNewButton_1);
		
		JButton btnNewButton = new JButton("Productos");
		btnNewButton.setBounds(505, 143, 126, 23);
		btnNewButton.addActionListener(new ActionListener() {
		    public void actionPerformed(ActionEvent e) {

		        PRODUCTOS_CLIENTE ventana = new PRODUCTOS_CLIENTE(personaLogueada);
		        ventana.setVisible(true);

		        dispose(); // Cierra el panel actual
		    }
		});
		contentPane.add(btnNewButton);
		
		JLabel lblNewLabel_1 = new JLabel("Para Comprar y/o ver sus pedidos");
		lblNewLabel_1.setBounds(158, 118, 247, 14);
		lblNewLabel_1.setHorizontalAlignment(SwingConstants.CENTER);
		contentPane.add(lblNewLabel_1);
		
		JLabel lblNewLabel_2 = new JLabel("Para ver el listado de nuestros productos");
		lblNewLabel_2.setBounds(425, 118, 279, 14);
		lblNewLabel_2.setHorizontalAlignment(SwingConstants.CENTER);
		contentPane.add(lblNewLabel_2);
		
		JButton btnNewButton_2 = new JButton("Cerrar Sesión");
		btnNewButton_2.setBounds(349, 229, 126, 23);
		contentPane.add(btnNewButton_2);
		
		btnNewButton_2.addActionListener(new ActionListener() {
		    public void actionPerformed(ActionEvent e) {
		        INICIO ventana = new INICIO();
		        ventana.setVisible(true);
		        dispose();
		    }
		});

	}

}
