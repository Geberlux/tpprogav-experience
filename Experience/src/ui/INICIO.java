package ui;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import bll.Persona;
import dll.DtoPersona;

import java.awt.FlowLayout;
import javax.swing.JTextField;
import javax.swing.JPasswordField;
import javax.swing.JTextArea;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.SwingConstants;
import java.awt.Font;
import javax.swing.JButton;

public class INICIO extends JFrame {

	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private JTextField InicioCorreo;
	private JPasswordField InicioContrasenia;
	private JTextField NombreRegistrar;
	private JPasswordField ContraseniaRegistrar;
	private JLabel lblCorreo;
	private JLabel lblContrasea;
	private JLabel lblNombre;
	private JLabel lblCorreo_1;
	private JLabel lblContrasea_1;
	private JTextField CorreoRegistrar;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					INICIO frame = new INICIO();
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
	public INICIO() {
		setResizable(false);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 720, 480);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		InicioCorreo = new JTextField();
		InicioCorreo.setBounds(52, 167, 201, 20);
		contentPane.add(InicioCorreo);
		InicioCorreo.setColumns(10);
		
		InicioContrasenia = new JPasswordField();
		InicioContrasenia.setBounds(52, 207, 201, 20);
		contentPane.add(InicioContrasenia);
		
		JLabel lblNewLabel = new JLabel("INICIO");
		lblNewLabel.setFont(new Font("Tahoma", Font.BOLD, 20));
		lblNewLabel.setHorizontalAlignment(SwingConstants.CENTER);
		lblNewLabel.setBounds(296, 25, 88, 33);
		contentPane.add(lblNewLabel);
		
		JLabel lblIniciarSesion = new JLabel("INICIO");
		lblIniciarSesion.setHorizontalAlignment(SwingConstants.CENTER);
		lblIniciarSesion.setFont(new Font("Tahoma", Font.BOLD, 15));
		lblIniciarSesion.setBounds(52, 81, 201, 33);
		contentPane.add(lblIniciarSesion);
		
		JLabel lblRegistro = new JLabel("REGISTRO");
		lblRegistro.setHorizontalAlignment(SwingConstants.CENTER);
		lblRegistro.setFont(new Font("Tahoma", Font.BOLD, 15));
		lblRegistro.setBounds(442, 81, 201, 33);
		contentPane.add(lblRegistro);
		
		NombreRegistrar = new JTextField();
		NombreRegistrar.setColumns(10);
		NombreRegistrar.setBounds(442, 167, 201, 20);
		contentPane.add(NombreRegistrar);
		
		ContraseniaRegistrar = new JPasswordField();
		ContraseniaRegistrar.setBounds(442, 251, 201, 20);
		contentPane.add(ContraseniaRegistrar);
		
		lblCorreo = new JLabel("Correo:");
		lblCorreo.setHorizontalAlignment(SwingConstants.LEFT);
		lblCorreo.setFont(new Font("Tahoma", Font.PLAIN, 10));
		lblCorreo.setBounds(52, 146, 201, 20);
		contentPane.add(lblCorreo);
		
		lblContrasea = new JLabel("Contraseña:");
		lblContrasea.setHorizontalAlignment(SwingConstants.LEFT);
		lblContrasea.setFont(new Font("Tahoma", Font.PLAIN, 10));
		lblContrasea.setBounds(52, 188, 201, 20);
		contentPane.add(lblContrasea);
		
		lblNombre = new JLabel("Nombre:");
		lblNombre.setHorizontalAlignment(SwingConstants.LEFT);
		lblNombre.setFont(new Font("Tahoma", Font.PLAIN, 10));
		lblNombre.setBounds(442, 146, 201, 20);
		contentPane.add(lblNombre);
		
		lblCorreo_1 = new JLabel("Correo:");
		lblCorreo_1.setHorizontalAlignment(SwingConstants.LEFT);
		lblCorreo_1.setFont(new Font("Tahoma", Font.PLAIN, 10));
		lblCorreo_1.setBounds(442, 188, 201, 20);
		contentPane.add(lblCorreo_1);
		
		lblContrasea_1 = new JLabel("Contraseña:");
		lblContrasea_1.setHorizontalAlignment(SwingConstants.LEFT);
		lblContrasea_1.setFont(new Font("Tahoma", Font.PLAIN, 10));
		lblContrasea_1.setBounds(442, 231, 201, 20);
		contentPane.add(lblContrasea_1);
		
		JButton BtnIniciar = new JButton("Iniciar Sesión");
		BtnIniciar.setBounds(90, 238, 118, 23);
		contentPane.add(BtnIniciar);
		
		JButton BtnRegistrar = new JButton("Registrar");
		BtnRegistrar.setBounds(502, 282, 89, 23);
		contentPane.add(BtnRegistrar);
		
		CorreoRegistrar = new JTextField();
		CorreoRegistrar.setColumns(10);
		CorreoRegistrar.setBounds(442, 207, 201, 20);
		contentPane.add(CorreoRegistrar);

		
		BtnIniciar.addActionListener(e -> {
		    String correo = InicioCorreo.getText();
		    String contrasenia = new String(InicioContrasenia.getPassword());

		    if (correo.isEmpty() || contrasenia.isEmpty()) {
		        JOptionPane.showMessageDialog(null, "Complete todos los campos");
		        return;
		    }

		    Persona persona = DtoPersona.login(correo, contrasenia);

		    if (persona != null) {
		        JOptionPane.showMessageDialog(null, "Bienvenido " + persona.getNombre());

		        // Redirección según rol
		        if (persona.getRol().equalsIgnoreCase("admin")) {
		            PANEL_ADMIN panelAdmin = new PANEL_ADMIN(persona);
		            panelAdmin.setVisible(true);
		        } else {
		            PANEL_USUARIO panelUsuario = new PANEL_USUARIO(persona);
		            panelUsuario.setVisible(true);
		        }

		        // Cerrar esta ventana
		        this.dispose();

		    } else {
		        JOptionPane.showMessageDialog(null, "Correo o contraseña incorrectos");
		    }
		});


		BtnRegistrar.addActionListener(e -> {
		    String nombre = NombreRegistrar.getText();
		    String correo = CorreoRegistrar.getText();
		    String contrasenia = new String(ContraseniaRegistrar.getPassword());

		    if (nombre.isEmpty() || correo.isEmpty() || contrasenia.isEmpty()) {
		        JOptionPane.showMessageDialog(null, "Complete todos los campos");
		        return;
		    }

		    Persona persona = new Persona(0, nombre, correo, contrasenia, "cliente");

		    boolean agregado = DtoPersona.agregarPersona(persona);

		    if (agregado) {
		        JOptionPane.showMessageDialog(null, "Usuario registrado correctamente.");
		    } else {
		        JOptionPane.showMessageDialog(null, "No se pudo registrar.");
		    }
		});
	}
	
	

}
