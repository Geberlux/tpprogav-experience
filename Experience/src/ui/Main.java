package ui;

import javax.swing.JOptionPane;

import bll.Persona;
import dll.Conexion;

public class Main {

	public static void main(String[] args) {
		Conexion.getInstance();
		//String[] opciones = { "Login", "Registrarse", "Ver usuario", "Editar", "Eliminar", "Buscar", "Salir" };
		String[] opciones = { "Login", "Registrarse", "Salir"};
		int opcion;
		do {
			opcion = JOptionPane.showOptionDialog(null, "elija opción", null, 0, 0, null, opciones, opciones);
			switch (opcion) {
			case 0:

				Persona encontrado = Persona.Login();

				JOptionPane.showMessageDialog(null, encontrado != null ? "Encontrado " + encontrado : "No encontrado");
				encontrado.menu();
				break;
			case 1:
				JOptionPane.showMessageDialog(null, Persona.agregarPersona()==true?"Agregado correctamente":"No se pudo agregar");
			
				break;
			case 2:
				JOptionPane.showMessageDialog(null, "Saliendo");
				break;
			default:
				break;
			}

		} while (opcion != 2);
		
		

	}
}
