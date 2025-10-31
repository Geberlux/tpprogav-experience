package dll;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.LinkedList;

import bll.Pedido_producto;


public class DtoPedido_Producto {
	
	private static Connection con = Conexion.getInstance().getConnection();
	
	public static void agregarPedido_producto(Pedido_producto pedido_producto) {
        try {
            PreparedStatement stmt = con.prepareStatement(
                "INSERT INTO pedido_producto (pedido_idpedido, producto_idproducto) VALUES (?, ?)"
            );
            stmt.setInt(1, pedido_producto.getIdpedido());
            stmt.setInt(2, pedido_producto.getIdproducto());

            stmt.executeUpdate();
            System.out.println("Detalle agregado correctamente.");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
	
	public static LinkedList<Pedido_producto> obtenerDetallesPorPedido(int idpedido) {
        LinkedList<Pedido_producto> detalles = new LinkedList<>();
        try {
            PreparedStatement stmt = con.prepareStatement(
                "SELECT * FROM pedido_producto WHERE pedido_idpedido = ?"
            );
            stmt.setInt(1, idpedido);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                int id = rs.getInt("idpedido_producto");
                
                int idproducto = rs.getInt("producto_idproducto");

                detalles.add(new Pedido_producto(id, idpedido, idproducto));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return detalles;
    }

}
