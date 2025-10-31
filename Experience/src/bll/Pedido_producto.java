package bll;

public class Pedido_producto {
	
	private int id;
	private int idpedido;
	private int idproducto;
	
	public Pedido_producto(int id, int idpedido, int idproducto) {
		super();
		this.id = id;
		this.idpedido = idpedido;
		this.idproducto = idproducto;
	}

	public Pedido_producto(int idpedido, int idproducto) {
		super();
		this.idpedido = idpedido;
		this.idproducto = idproducto;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getIdpedido() {
		return idpedido;
	}

	public void setIdpedido(int idpedido) {
		this.idpedido = idpedido;
	}

	public int getIdproducto() {
		return idproducto;
	}

	public void setIdproducto(int idproducto) {
		this.idproducto = idproducto;
	}

	@Override
	public String toString() {
		return "Pedido_producto [id=" + id + ", idpedido=" + idpedido + ", idproducto=" + idproducto + "]";
	}
	
	
}
