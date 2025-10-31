package dll;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.LinkedList;
import java.util.List;


import javax.swing.JOptionPane;


import bll.Producto;



public class DtoProducto {
	
	private static Connection con = Conexion.getInstance().getConnection();
	
	public static Producto buscarPorID() {
    	

    	int id = 0;
    	
    	List<Producto> productos = DtoProducto.mostrarProductos();
    			
    	
    	String[] productosArray = new String[productos.size()];
    	for (int i = 0; i < productosArray.length; i++) {
    		productosArray[i] = productos.get(i).getNombre() +"/"+productos.get(i).getId();
		}
    	String elegido = (String)JOptionPane.showInputDialog(null,
    			"Elija producto", null, 0, null, productosArray, productosArray[0]);
    	id = Integer.parseInt( elegido.split("/")[1]);
    	Producto producto = null;
        try {
            PreparedStatement stmt = con.prepareStatement(
                "SELECT * FROM producto WHERE idproducto = ?"
            );
            stmt.setInt(1, id);
  
            //executequery se utiliza cuando no hay cambios en la bdd
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
            	int categoria = rs.getInt("categoria_idcategoria");
                String nombre = rs.getString("nombre");
                String precio = rs.getString("precio");
                String cantidad = rs.getString("cantidad");

                producto = new Producto(id,categoria,nombre,precio,cantidad);
                }
       
        } catch (Exception e) {
            e.printStackTrace();
        }
        return producto;
    }
	
	public static Producto buscarPorIDConFiltro() {

	    int id = 0;

	    // Traer todas las categorías de la base de datos
	    List<Producto> productos = DtoProducto.mostrarProductos();
	    
	    if (productos.isEmpty()) {
	        JOptionPane.showMessageDialog(null, "No hay productos disponibles.");
	        return null;
	    }

	    // Crear el array de opciones para el JOptionPane con "nombre/id"
	    String[] productosArray = new String[productos.size()];
	    for (int i = 0; i < productosArray.length; i++) {
	    	productosArray[i] = productos.get(i).getNombre() + "/" + productos.get(i).getId();
	    }

	    // Mostrar el desplegable al usuario
	    String elegido = (String) JOptionPane.showInputDialog(
	        null,
	        "Elija producto",
	        "",
	        JOptionPane.QUESTION_MESSAGE,
	        null,
	        productosArray,
	        productosArray[0]
	    );
	    
	    if (elegido == null) return null; // Si cancela

	    // Extraer el ID de la opción elegida
	    id = Integer.parseInt(elegido.split("/")[1]);

	    Producto producto = null;
	    try {
	        PreparedStatement stmt = con.prepareStatement(
	            "SELECT * FROM producto WHERE idproducto = ?"
	        );
	        stmt.setInt(1, id);

	        ResultSet rs = stmt.executeQuery();

	        if (rs.next()) {
	            // <-- Cambiar "nombre" si tu columna se llama distinto
	        	int categoria = rs.getInt("categoria_idcategoria");
                String nombre = rs.getString("nombre");
                String precio = rs.getString("precio");
                String cantidad = rs.getString("cantidad");
	            producto = new Producto(id, categoria, nombre, precio, cantidad);
	        }

	    } catch (Exception e) {
	        e.printStackTrace();
	    }
	    return producto;
	}
	
	public static boolean EditarProducto(Producto producto) {
    	System.out.println(producto);
        try {
            PreparedStatement statement = con.prepareStatement(
                
            	"UPDATE `producto` SET categoria_idcategoria=?,nombre=?,precio=?,cantidad=? WHERE idproducto=?"
            );
            statement.setInt(1, producto.getCategoria());
            statement.setString(2, producto.getNombre());
            statement.setString(3, producto.getPrecio());
            statement.setString(4, producto.getCantidad());
            statement.setInt(5, producto.getId());

            int filas = statement.executeUpdate();
            if (filas > 0) {
                System.out.println("Producto editado correctamente.");
                return true;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return false;
        }
	
	public static void EliminarProducto(int id) {
        try {
            PreparedStatement statement = con.prepareStatement(
                "DELETE FROM `producto` WHERE idproducto = ?"
            );
           
            statement.setInt(1, id);

            int filas = statement.executeUpdate();
            if (filas > 0) {
                System.out.println("Producto eliminado correctamente.");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
	
	public static boolean agregarProducto(Producto p) {
	    String sql = "INSERT INTO producto (nombre, precio, cantidad, categoria_idcategoria) VALUES (?, ?, ?, ?)";

	    try (PreparedStatement ps = con.prepareStatement(sql)) {
	        ps.setString(1, p.getNombre());
	        ps.setString(2, p.getPrecio());
	        ps.setString(3, p.getCantidad());
	        ps.setInt(4, p.getCategoria()); // FK

	        ps.executeUpdate();
	        return true;

	    } catch (Exception e) {
	        JOptionPane.showMessageDialog(null, "❌ Error al agregar producto: " + e.getMessage());
	        return false;
	    }
	}
	
	public static LinkedList<Producto> mostrarProductos() {
        LinkedList<Producto> productos = new LinkedList<Producto>();
        try {
            PreparedStatement stmt = con.prepareStatement("SELECT * FROM producto");
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                int id = rs.getInt("idproducto");
                int categoria = rs.getInt("categoria_idcategoria");
                String nombre = rs.getString("nombre");
                String precio = rs.getString("precio");
                String cantidad = rs.getString("cantidad");

                productos.add(new Producto(id,categoria,nombre,precio,cantidad));
                }
      
        } catch (Exception e) {
            e.printStackTrace();
        }
        return productos;
    }

}
