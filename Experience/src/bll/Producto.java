package bll;

import java.util.LinkedList;

import javax.swing.JOptionPane;

import dll.DtoCategoria;
import dll.DtoProducto;

public class Producto {
	
	protected int id;
	protected int categoria;
	protected String nombre;
    protected String precio;
    protected String cantidad;
    
	public Producto(int id, int categoria, String nombre, String precio, String cantidad) {
		super();
		this.id = id;
		this.categoria = categoria;
		this.nombre = nombre;
		this.precio = precio;
		this.cantidad = cantidad;
	}

	public Producto(int categoria, String nombre, String precio, String cantidad) {
		super();
		this.categoria = categoria;
		this.nombre = nombre;
		this.precio = precio;
		this.cantidad = cantidad;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getCategoria() {
		return categoria;
	}

	public void setCategoria(int categoria) {
		this.categoria = categoria;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public String getPrecio() {
		return precio;
	}

	public void setPrecio(String precio) {
		this.precio = precio;
	}

	public String getCantidad() {
		return cantidad;
	}

	public void setCantidad(String cantidad) {
		this.cantidad = cantidad;
	}

	@Override
	public String toString() {
		return "Producto [id=" + id + ", categoria=" + categoria + ", nombre=" + nombre + ", precio=" + precio
				+ ", cantidad=" + cantidad + "]";
	}
    
	public static boolean agregarProducto() {

	    // 1. Obtener las categorías desde la base de datos
	    LinkedList<Categoria> categorias = DtoCategoria.mostrarCategorias();

	    // 2. Crear un array con los nombres de las categorías
	    String[] nombresCategorias = new String[categorias.size()];
	    for (int i = 0; i < categorias.size(); i++) {
	        nombresCategorias[i] = categorias.get(i).getNombre();
	    }

	    // 3. Mostrar un menú desplegable con las categorías
	    String seleccion = (String) JOptionPane.showInputDialog(
	            null,
	            "Seleccione una categoría:",
	            "Categorías disponibles",
	            JOptionPane.QUESTION_MESSAGE,
	            null,
	            nombresCategorias,
	            nombresCategorias[0]
	    );

	    // 4. Buscar la categoría seleccionada y obtener su ID
	    int idCategoria = 0;
	    for (Categoria c : categorias) {
	        if (c.getNombre().equals(seleccion)) {
	            idCategoria = c.getId();
	            break;
	        }
	    }

	    // 5. Pedir los datos del producto
	    String nombre = JOptionPane.showInputDialog("Ingrese nombre del producto:");
	    String precio = JOptionPane.showInputDialog("Ingrese precio del producto:");
	    String cantidad = JOptionPane.showInputDialog("Ingrese cantidad del producto:");

	    // 6. Crear el objeto Producto (usa idCategoria como FK)
	    Producto nuevo = new Producto(idCategoria, nombre, precio, cantidad);

	    // 7. Guardar en la base de datos
	    boolean exito = DtoProducto.agregarProducto(nuevo);

	    // 8. Mensaje final
	    JOptionPane.showMessageDialog(null, "✅ Producto agregado correctamente.");

	    return exito;
	}
	
	public static LinkedList<Producto> mostrarProductos() {
		
		return DtoProducto.mostrarProductos(); 
		
	}
public static boolean EditarProducto() {
		
		Producto encontrado = DtoProducto.buscarPorID();
		if (encontrado == null) {
			JOptionPane.showMessageDialog(null, "No se encontró el producto.");
			return false;
		}

		// 1. Obtener las categorías desde la base de datos
	    LinkedList<Categoria> categorias = DtoCategoria.mostrarCategorias();

	    // 2. Crear un array con los nombres de las categorías
	    String[] nombresCategorias = new String[categorias.size()];
	    for (int i = 0; i < categorias.size(); i++) {
	        nombresCategorias[i] = categorias.get(i).getNombre();
	    }

	    // 3. Buscar el nombre de la categoría actual
	    String categoriaActual = "";
	    for (Categoria c : categorias) {
	        if (c.getId() == encontrado.getCategoria()) {
	            categoriaActual = c.getNombre();
	            break;
	        }
	    }

	    // 4. Mostrar un menú desplegable con las categorías
	    String seleccion = (String) JOptionPane.showInputDialog(
	            null,
	            "Seleccione una categoría:",
	            "Editar producto",
	            JOptionPane.QUESTION_MESSAGE,
	            null,
	            nombresCategorias,
	            categoriaActual
	    );

	    // 5. Buscar la categoría seleccionada y obtener su ID
	    int idCategoria = 0;
	    for (Categoria c : categorias) {
	        if (c.getNombre().equals(seleccion)) {
	            idCategoria = c.getId();
	            break;
	        }
	    }

		// 6. Pedir los nuevos datos (mostrando los actuales por defecto)
		String nombre = JOptionPane.showInputDialog("Ingrese nombre:", encontrado.getNombre());
		String precio = JOptionPane.showInputDialog("Ingrese precio:", encontrado.getPrecio());
		String cantidad = JOptionPane.showInputDialog("Ingrese cantidad:", encontrado.getCantidad());

		// 7. Crear el nuevo producto con los valores editados
		Producto nuevo = new Producto(encontrado.getId(), idCategoria, nombre, precio, cantidad);

		// 8. Devolver directamente el resultado del update
		return DtoProducto.EditarProducto(nuevo);
    
	}


	
}
