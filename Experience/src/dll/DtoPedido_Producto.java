package dll;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.LinkedList;

import bll.Pedido_producto;

public class DtoPedido_Producto {
    
    private static Connection con = Conexion.getInstance().getConnection();
    
    public static boolean agregarPedido_producto(Pedido_producto pedido_producto) {
        try (PreparedStatement stmt = con.prepareStatement(
                "INSERT INTO pedido_producto (pedido_idpedido, producto_idproducto) VALUES (?, ?)")) {
            stmt.setInt(1, pedido_producto.getIdpedido());
            stmt.setInt(2, pedido_producto.getIdproducto());
            int filas = stmt.executeUpdate();
            return filas > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
    
    public static LinkedList<Pedido_producto> obtenerDetallesPorPedido(int idpedido) {
        LinkedList<Pedido_producto> detalles = new LinkedList<>();
        try (PreparedStatement stmt = con.prepareStatement(
                "SELECT * FROM pedido_producto WHERE pedido_idpedido = ?")) {
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

    public static LinkedList<String> obtenerDetallesConProducto(int idpedido) {
        LinkedList<String> salida = new LinkedList<>();
        String sql = "SELECT pp.idpedido_producto, pp.producto_idproducto, p.nombre, p.precio, p.cantidad "
                   + "FROM pedido_producto pp JOIN producto p ON pp.producto_idproducto = p.idproducto "
                   + "WHERE pp.pedido_idpedido = ?";
        try (PreparedStatement stmt = con.prepareStatement(sql)) {
            stmt.setInt(1, idpedido);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                int idDetalle = rs.getInt("idpedido_producto");
                int idProducto = rs.getInt("producto_idproducto");
                String nombre = rs.getString("nombre");
                String precio = rs.getString("precio");
                String cantidad = rs.getString("cantidad");
                String linea = "DetalleID: " + idDetalle + " | ProductoID: " + idProducto + " | " + nombre + " | Precio: " + precio + " | Cant: " + cantidad;
                salida.add(linea);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return salida;
    }
    
    public static boolean eliminarPorPedido(int idpedido) {
        try (PreparedStatement stmt = con.prepareStatement("DELETE FROM pedido_producto WHERE pedido_idpedido = ?")) {
            stmt.setInt(1, idpedido);
            int filas = stmt.executeUpdate();
            return filas >= 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
    
    public static boolean eliminarDetalle(int idDetalle) {
        try (PreparedStatement stmt = con.prepareStatement("DELETE FROM pedido_producto WHERE idpedido_producto = ?")) {
            stmt.setInt(1, idDetalle);
            int filas = stmt.executeUpdate();
            return filas > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
    
}
