package dll;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.LinkedList;

import bll.Pedido;

public class DtoPedido {
    
    private static Connection con = Conexion.getInstance().getConnection();
    
    public static int agregarPedido(Pedido pedido) {
        int generatedId = -1;
        String sql = "INSERT INTO pedido (persona_idpersona) VALUES (?)";
        try (PreparedStatement stmt = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setInt(1, pedido.getIdpersona());
            int affected = stmt.executeUpdate();
            if (affected > 0) {
                try (ResultSet rs = stmt.getGeneratedKeys()) {
                    if (rs.next()) {
                        generatedId = rs.getInt(1);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return generatedId;
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
    
    public static boolean eliminarPedido(int idpedido) {
        try (PreparedStatement stmt = con.prepareStatement("DELETE FROM pedido WHERE idpedido = ?")) {
            stmt.setInt(1, idpedido);
            int filas = stmt.executeUpdate();
            return filas > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

}
