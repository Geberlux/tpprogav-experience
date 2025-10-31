
package bll;

import java.sql.PreparedStatement;
import java.util.LinkedList;

import javax.swing.JOptionPane;

import dll.DtoCategoria;
import dll.DtoPedido;

public class Pedido {

    private int id;
    private int idpersona;
    
	public Pedido(int id, int idpersona) {
		super();
		this.id = id;
		this.idpersona = idpersona;
	}

	public Pedido(int idpersona) {
		super();
		this.idpersona = idpersona;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getIdpersona() {
		return idpersona;
	}

	public void setIdpersona(int idpersona) {
		this.idpersona = idpersona;
	}

	@Override
	public String toString() {
		return "Pedido [id=" + id + ", idpersona=" + idpersona + "]";
	}
    
	public static boolean agregarPedido() {
	
     
	}
    
}