package bll;

import java.util.LinkedList;



import dll.DtoCategoria;
import repository.Validaciones;

public class Categoria {
	
	protected int id;
	protected String nombre;
	
	public Categoria(int id, String nombre) {
		super();
		this.id = id;
		this.nombre = nombre;
	}
	
	public Categoria(String nombre) {
		super();
		this.nombre = nombre;
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

	@Override
	public String toString() {
		return "Categoria [id=" + id + ", nombre=" + nombre + "]";
	}
	
	public static boolean agregarCategoria() {
		String nombre = Validaciones.ValidarString("Ingrese nombre");
		Categoria nuevo = new Categoria(nombre);
		return DtoCategoria.agregarCategoria(nuevo);
		
	}
	
	public static LinkedList<Categoria> mostrarCategorias() {
		
		return DtoCategoria.mostrarCategorias(); 
		
	}
	
	public static boolean EditarCategoria() {
		
		Categoria encontrado = DtoCategoria.buscarPorID();
		String nombre = Validaciones.ValidarString("Ingrese nombre");
		Categoria nuevo = new Categoria(encontrado.getId(), nombre);
		return DtoCategoria.EditarCategoria(nuevo);
    
	}

}
