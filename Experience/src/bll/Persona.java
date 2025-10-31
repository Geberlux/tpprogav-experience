package bll;

import java.util.LinkedList;

import javax.swing.JOptionPane;

import dll.DtoCategoria;
import dll.DtoPersona;
import dll.DtoProducto;
import repository.Validaciones;

public class Persona {

	protected int id;
	protected String nombre;
    protected String correo;
    protected String contrasenia;
    protected String rol;
    
    
	public Persona(int id, String nombre, String correo, String contrasenia, String rol) {
		super();
		this.id = id;
		this.nombre = nombre;
		this.correo = correo;
		this.contrasenia = contrasenia;
		this.rol = rol;
	}
	public Persona( String nombre, String correo, String contrasenia, String rol) {
		super();
		this.nombre = nombre;
		this.correo = correo;
		this.contrasenia = contrasenia;
		this.rol = rol;
	}

	public int getId() {
		return id;
	}


	public void setId(int id) {
		this.id = id;
	}


	public String getNombre() {
		return nombre;
	}


	public void setNombre(String nombre) {
		this.nombre = nombre;
	}


	public String getCorreo() {
		return correo;
	}


	public void setCorreo(String correo) {
		this.correo = correo;
	}


	public String getContrasenia() {
		return contrasenia;
	}


	public void setContrasenia(String contrasenia) {
		this.contrasenia = contrasenia;
	}


	public String getRol() {
		return rol;
	}


	public void setRol(String rol) {
		this.rol = rol;
	}


	@Override
	public String toString() {
		return "Persona [id= '" + id + "', nombre= '" + nombre + "', correo= '" + correo + "', contrasenia= '" + contrasenia
				+ "', rol= '" + rol + "']";
	}
    
	public static Persona Login() {
		String mail,password;
    		mail = JOptionPane.showInputDialog("Ingrese mail");
    		password = JOptionPane.showInputDialog("Ingrese contraseña");
    		if (mail.isEmpty() || password.isEmpty()) {
				JOptionPane.showMessageDialog(null, "Error al ingresar datos");
				return null;
			}else {
				return DtoPersona.login(mail, password);
			}
    	 
	}
	public static boolean agregarPersona() {
		String correo;
		do {
			correo = JOptionPane.showInputDialog("Ingrese mail");
			if (Validaciones.validate(correo)==false) {
				JOptionPane.showMessageDialog(null, "Mail incorrecto");
			}
		} while (Validaciones.validate(correo)==false);
		String contrasenia = Validaciones.ValidarString("Ingrese contraseña");
		String rol = "cliente";
		String nombre = Validaciones.ValidarString("Ingrese nombre");
		Persona nuevo = new Persona(nombre, correo, contrasenia, rol);
		return DtoPersona.agregarPersona(nuevo);
		
		
	}
	public static boolean agregarPersona2() {
		String correo;
		do {
			correo = JOptionPane.showInputDialog("Ingrese mail");
			if (Validaciones.validate(correo)==false) {
				JOptionPane.showMessageDialog(null, "Mail incorrecto");
			}
		} while (Validaciones.validate(correo)==false);
		String contrasenia = Validaciones.ValidarString("Ingrese contraseña");
		String[] roles = {"cliente","admin"};
		String rol = (String) JOptionPane.showInputDialog(null, "Ingrese rol", "", 0, null, roles,
				roles[0]);
		String nombre = Validaciones.ValidarString("Ingrese nombre");
		Persona nuevo = new Persona(nombre, correo, contrasenia, rol);
		return DtoPersona.agregarPersona(nuevo);
		
		
	}
	public static LinkedList<Persona> mostrarPersonas() {
		
		return DtoPersona.mostrarPersonas(); 
		
	}
	public static boolean EditarPersona() {
		
		Persona encontrado = DtoPersona.buscarPorID();
		String correo,contrasenia,rol,nombre;
		do {
			correo = Validaciones.ValidarString("Ingrese correo");
			if (Validaciones.validate(correo)==false) {
				JOptionPane.showMessageDialog(null, "Correo incorrecto");
			}
		} while (Validaciones.validate(correo)==false);
		
		
		contrasenia = Validaciones.ValidarString("Ingrese contraseña");
		  String[] roles = {"cliente","admin"};
		 rol = (String) JOptionPane.showInputDialog(null, "Ingrese tipo", "", 0, null, roles,roles[0]);
		  nombre =  Validaciones.ValidarString("Ingrese nombre");
		Persona nuevo = new Persona(encontrado.getId(), nombre, correo, contrasenia, rol);

	return DtoPersona.EditarPersona(nuevo);
    
	}


	public void menu() {
		
		if (this.getRol().equals("admin")) {
			String[] opciones = {"Usuarios", "Productos", "Categorias", "Pedidos", "Salir" };
			int opcion;
			do {
				opcion = JOptionPane.showOptionDialog(null, "elija opción", null, 0, 0, null, opciones, opciones);
				switch (opcion) {
				case 0:

					menu2();
					break;
				case 1:
					menu3();
				
					break;
				case 2:
					menu4();
					break;
				case 3:
					JOptionPane.showMessageDialog(null, "Estamos en ello!", "Aviso", JOptionPane.INFORMATION_MESSAGE);
			        break;
				case 4:
					JOptionPane.showMessageDialog(null, "Saliendo");
					break;
				default:
					break;
				}

			} while (opcion != 4);
			
		} else if (this.getRol().equals("cliente")){
		
		String[] opciones = { "Armar Pedido", "Productos", "Ver Pedido", "Editar Pedido", "Eliminar Pedido", "Buscar Pedido", "Salir" };
		int opcion;
		do {
			opcion = JOptionPane.showOptionDialog(null, "elija opción", null, 0, 0, null, opciones, opciones);
			switch (opcion) {
			case 0:

				JOptionPane.showMessageDialog(null, "Estamos en ello!", "Aviso", JOptionPane.INFORMATION_MESSAGE);
		        break;
			case 1:
				menu5();
			
				break;
			case 2:
				JOptionPane.showMessageDialog(null, "Estamos en ello!", "Aviso", JOptionPane.INFORMATION_MESSAGE);
		        break;
			case 3:

				JOptionPane.showMessageDialog(null, "Estamos en ello!", "Aviso", JOptionPane.INFORMATION_MESSAGE);
		        break;
			case 4:
				JOptionPane.showMessageDialog(null, "Estamos en ello!", "Aviso", JOptionPane.INFORMATION_MESSAGE);
		        break;
			case 5:
				JOptionPane.showMessageDialog(null, "Estamos en ello!", "Aviso", JOptionPane.INFORMATION_MESSAGE);
		        break;
			case 6:
				JOptionPane.showMessageDialog(null, "Saliendo");
				break;
			default:
				break;
			}

		} while (opcion != 6);
		
		}
		
	}
	
public void menu2() {
		
			String[] opciones = {"Alta", "Usuarios", "Editar", "Eliminar", "Buscar", "Salir" };
			int opcion;
			do {
				opcion = JOptionPane.showOptionDialog(null, "elija opción", null, 0, 0, null, opciones, opciones);
				switch (opcion) {
				case 0:
					JOptionPane.showMessageDialog(null, Persona.agregarPersona2()==true?"Agregado correctamente":"No se pudo agregar");
				
					break;
				case 1:
					JOptionPane.showMessageDialog(null, Persona.mostrarPersonas());
					break;
				case 2:

					Persona.EditarPersona();

					break;
				case 3:
					int id = DtoPersona.buscarPorID().getId();
					DtoPersona.EliminarPersona(id);
					break;
				case 4:
					
					DtoPersona.buscarPorIDConFiltro();
					//JOptionPane.showMessageDialog(null, encontrado);
					break;
				case 5:
					JOptionPane.showMessageDialog(null, "Saliendo");
					break;
				default:
					break;
				}

			} while (opcion != 5);
	
    
	
}

public void menu3() {
	
	String[] opciones = {"Alta", "Productos", "Editar", "Eliminar", "Buscar", "Salir" };
	int opcion;
	do {
		opcion = JOptionPane.showOptionDialog(null, "elija opción", null, 0, 0, null, opciones, opciones);
		switch (opcion) {
		case 0:
			JOptionPane.showMessageDialog(null, Producto.agregarProducto()==true?"Agregado correctamente":"No se pudo agregar");
		
			break;
		case 1:
			JOptionPane.showMessageDialog(null, Producto.mostrarProductos());
			break;
		case 2:

			Producto.EditarProducto();

			break;
		case 3:
			int id = DtoProducto.buscarPorID().getId();
			DtoProducto.EliminarProducto(id);
			break;
		case 4:
			
			DtoProducto.buscarPorIDConFiltro();
			//JOptionPane.showMessageDialog(null, encontrado);
			break;
		case 5:
			JOptionPane.showMessageDialog(null, "Saliendo");
			break;
		default:
			break;
		}

	} while (opcion != 5);



}

public void menu4() {
	
	String[] opciones = {"Alta", "Categorias", "Editar", "Eliminar", "Buscar", "Salir" };
	int opcion;
	do {
		opcion = JOptionPane.showOptionDialog(null, "elija opción", null, 0, 0, null, opciones, opciones);
		switch (opcion) {
		case 0:
			JOptionPane.showMessageDialog(null, Categoria.agregarCategoria()==true?"Agregado correctamente":"No se pudo agregar");
		
			break;
		case 1:
			JOptionPane.showMessageDialog(null, Categoria.mostrarCategorias());
			break;
		case 2:

			Categoria.EditarCategoria();

			break;
		case 3:
			int id = DtoCategoria.buscarPorID().getId();
			DtoCategoria.EliminarCategoria(id);
			break;
		case 4:
			
			DtoCategoria.buscarPorIDConFiltro();
			//JOptionPane.showMessageDialog(null, encontrado);
			break;
		case 5:
			JOptionPane.showMessageDialog(null, "Saliendo");
			break;
		default:
			break;
		}

	} while (opcion != 5);



}

public void menu5() {
	
	String[] opciones = {"Comprar", "Productos", "Editar", "Eliminar", "Buscar", "Salir" };
	int opcion;
	do {
		opcion = JOptionPane.showOptionDialog(null, "elija opción", null, 0, 0, null, opciones, opciones);
		switch (opcion) {
		case 0:
			JOptionPane.showMessageDialog(null, "Estamos en ello!", "Aviso", JOptionPane.INFORMATION_MESSAGE);
	        break;
		case 1:
			JOptionPane.showMessageDialog(null, Producto.mostrarProductos());
			break;
		case 2:

			JOptionPane.showMessageDialog(null, "Estamos en ello!", "Aviso", JOptionPane.INFORMATION_MESSAGE);
	        break;
		case 3:
			JOptionPane.showMessageDialog(null, "Estamos en ello!", "Aviso", JOptionPane.INFORMATION_MESSAGE);
	        break;
		case 4:
			DtoProducto.buscarPorIDConFiltro();
			//JOptionPane.showMessageDialog(null, encontrado);
			break;
		case 5:
			JOptionPane.showMessageDialog(null, "Saliendo");
			break;
		default:
			break;
		}

	} while (opcion != 5);



}
}
