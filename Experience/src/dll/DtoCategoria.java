package dll;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.LinkedList;
import java.util.List;


import javax.swing.JOptionPane;

import com.mysql.jdbc.exceptions.jdbc4.MySQLIntegrityConstraintViolationException;

import bll.Categoria;

public class DtoCategoria {

	private static Connection con = Conexion.getInstance().getConnection();
	
	public static Categoria buscarPorID() {
    	
    	int id = 0;
    	
    	List<Categoria> categorias = DtoCategoria.mostrarCategorias();
    			
    	
    	String[] categoriasArray = new String[categorias.size()];
    	for (int i = 0; i < categoriasArray.length; i++) {
    		categoriasArray[i] = categorias.get(i).getNombre() +"/"+categorias.get(i).getId();
		}
    	String elegido = (String)JOptionPane.showInputDialog(null,
    			"Elija categoria", null, 0, null, categoriasArray, categoriasArray[0]);
    	id = Integer.parseInt( elegido.split("/")[1]);
    	Categoria categoria = null;
        try {
            PreparedStatement stmt = con.prepareStatement(
                "SELECT * FROM categoria WHERE idcategoria = ?"
            );
            stmt.setInt(1, id);
  
            //executequery se utiliza cuando no hay cambios en la bdd
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                String nombre = rs.getString("tipo");

                categoria = new Categoria(id,nombre);
                }
       
        } catch (Exception e) {
            e.printStackTrace();
        }
        return categoria;
    }
	
	public static Categoria buscarPorIDConFiltro() {

	    int id = 0;

	    // Traer todas las categorías de la base de datos
	    List<Categoria> categorias = DtoCategoria.mostrarCategorias();
	    
	    if (categorias.isEmpty()) {
	        JOptionPane.showMessageDialog(null, "No hay categorías disponibles.");
	        return null;
	    }

	    // Crear el array de opciones para el JOptionPane con "nombre/id"
	    String[] categoriasArray = new String[categorias.size()];
	    for (int i = 0; i < categoriasArray.length; i++) {
	        categoriasArray[i] = categorias.get(i).getNombre() + "/" + categorias.get(i).getId();
	    }

	    // Mostrar el desplegable al usuario
	    String elegido = (String) JOptionPane.showInputDialog(
	        null,
	        "Elija categoria",
	        "",
	        JOptionPane.QUESTION_MESSAGE,
	        null,
	        categoriasArray,
	        categoriasArray[0]
	    );
	    
	    if (elegido == null) return null; // Si cancela

	    // Extraer el ID de la opción elegida
	    id = Integer.parseInt(elegido.split("/")[1]);

	    Categoria categoria = null;
	    try {
	        PreparedStatement stmt = con.prepareStatement(
	            "SELECT * FROM categoria WHERE idcategoria = ?"
	        );
	        stmt.setInt(1, id);

	        ResultSet rs = stmt.executeQuery();

	        if (rs.next()) {
	            // <-- Cambiar "nombre" si tu columna se llama distinto
	            String nombre = rs.getString("tipo");
	            categoria = new Categoria(id, nombre);
	        }

	    } catch (Exception e) {
	        e.printStackTrace();
	    }
	    return categoria;
	}
	
	public static boolean EditarCategoria(Categoria categoria) {
    	System.out.println(categoria);
        try {
            PreparedStatement statement = con.prepareStatement(
                "UPDATE `Categoria` SET tipo=? WHERE idcategoria=?"
            );
            statement.setString(1, categoria.getNombre());
            statement.setInt(2, categoria.getId());

            int filas = statement.executeUpdate();
            if (filas > 0) {
                System.out.println("Categoria editada correctamente.");
                return true;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return false;
        }
	
	public static void EliminarCategoria(int id) {
        try {
            PreparedStatement statement = con.prepareStatement(
                "DELETE FROM `categoria` WHERE idcategoria = ?"
            );
           
            statement.setInt(1, id);

            int filas = statement.executeUpdate();
            if (filas > 0) {
                System.out.println("Categoria eliminada correctamente.");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
	
	public static boolean agregarCategoria(Categoria categoria) {
//    	boolean flag = true;
//    	for (Categoria elemento : categoria.mostrarCategoria()) {
//			if (elemento.getCorreo().equals(persona.getCorreo())) {
//				flag = false;
//				break;
//			}
//		}
//    	if (flag) {
//			descomentar para usar for para validar
	
        try {
            PreparedStatement statement = con.prepareStatement(
                "INSERT INTO categoria (tipo) VALUES (?)"
            );
            statement.setString(1, categoria.getNombre());

            int filas = statement.executeUpdate();
            if (filas > 0) {
                System.out.println("Categoria agregado correctamente.");
                return true;
            }
        } catch (MySQLIntegrityConstraintViolationException e) {
           	JOptionPane.showMessageDialog(null, "Categoria ya creada");
            return false;
        } catch (Exception e) {
            e.printStackTrace();
            return false;

        }
        return false;

//    	}else {
//    		JOptionPane.showMessageDialog(null, "Usuario con mail ya creado(con for)");
//		}
    }
	
	public static LinkedList<Categoria> mostrarCategorias() {
        LinkedList<Categoria> categorias = new LinkedList<Categoria>();
        try {
            PreparedStatement stmt = con.prepareStatement("SELECT * FROM categoria");
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                int id = rs.getInt("idcategoria");
                String nombre = rs.getString("tipo");

                categorias.add(new Categoria(id,nombre));
                }
      
        } catch (Exception e) {
            e.printStackTrace();
        }
        return categorias;
    }
	
}
