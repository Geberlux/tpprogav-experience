package dll;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.LinkedList;

import javax.swing.JOptionPane;

import bll.Pedido;

public class DtoPedido {
	
	private static Connection con = Conexion.getInstance().getConnection();
	
	public static void agregarPedido(Pedido pedido) {
        try {
            PreparedStatement stmt = con.prepareStatement(
                "INSERT INTO pedido (persona_idpersona) VALUES (?)"
            );
            stmt.setInt(1, pedido.getIdpersona());

            stmt.executeUpdate();
            System.out.println("Pedido agregado correctamente.");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
	
	public static LinkedList<Pedido> obtenerPedidosPorUsuario(int idpersona) {
        LinkedList<Pedido> pedidos = new LinkedList<>();
        try {
            PreparedStatement stmt = con.prepareStatement(
                "SELECT * FROM pedido WHERE persona_idpersona = ?"
            );
            stmt.setInt(1, idpersona);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                int id = rs.getInt("idpedido");
                int idpedido = rs.getInt("persona_idpersona");

                pedidos.add(new Pedido(id, idpedido));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return pedidos;
    }
	
	public static Pedido obtenerPedidosPorID(int id) {
	       Pedido pedido = null;
	        try {
	            PreparedStatement stmt = con.prepareStatement(
	                "SELECT * FROM pedido WHERE idpedido = ?"
	            );
	            stmt.setInt(1, id);
	            ResultSet rs = stmt.executeQuery();

	            if(rs.next()) {
	                int idpersona = rs.getInt("persona_idpersona");
	                

	                pedido = new Pedido(id, idpersona);
	            }
	        } catch (Exception e) {
	            e.printStackTrace();
	        }
	        return pedido;
	    }

}
