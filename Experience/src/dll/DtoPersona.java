package dll;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

import javax.swing.JOptionPane;

import com.mysql.jdbc.exceptions.jdbc4.MySQLIntegrityConstraintViolationException;

import bll.Persona;
import repository.Encriptador;

public class DtoPersona {
	private static Connection con = Conexion.getInstance().getConnection();

	 public static Persona login(String correo, String contrasenia) {	
		 Persona persona = null;
	        try {
	            PreparedStatement stmt = con.prepareStatement(
	                "SELECT * FROM persona WHERE correo = ? AND contrasenia = ?"
	            );
	            stmt.setString(1, correo);
	            stmt.setString(2, Encriptador.encriptar(contrasenia));
	            //executequery se utiliza cuando no hay cambios en la bdd
	            ResultSet rs = stmt.executeQuery();

	            if (rs.next()) {
	                int id = rs.getInt("idpersona");
	                String nombre = rs.getString("nombre");
	                String rol = rs.getString("rol");

	                 persona = new Persona(id,nombre,correo,contrasenia,rol);
	                }
	       
	        } catch (Exception e) {
	            e.printStackTrace();
	        }
	        return persona;
	    }
	    public static Persona buscarPorID() {
	    	

	    	int id = 0;
	    	
	    	List<Persona> personas = DtoPersona.mostrarPersonas();
	    			
	    	
	    	String[] personasArray = new String[personas.size()];
	    	for (int i = 0; i < personasArray.length; i++) {
	    		personasArray[i] = personas.get(i).getCorreo() +"/"+personas.get(i).getId();
			}
	    	String elegido = (String)JOptionPane.showInputDialog(null,
	    			"Elija usuario", null, 0, null, personasArray, personasArray[0]);
	    	id = Integer.parseInt( elegido.split("/")[1]);
	    	Persona persona = null;
	        try {
	            PreparedStatement stmt = con.prepareStatement(
	                "SELECT * FROM persona WHERE idpersona = ?"
	            );
	            stmt.setInt(1, id);
	  
	            //executequery se utiliza cuando no hay cambios en la bdd
	            ResultSet rs = stmt.executeQuery();

	            if (rs.next()) {
	                String nombre = rs.getString("nombre");
	                String correo = rs.getString("correo");
	                String contrasenia = rs.getString("contrasenia");
	                String rol = rs.getString("rol");

	                persona = new Persona(id,nombre,correo,contrasenia,rol);
	                }
	       
	        } catch (Exception e) {
	            e.printStackTrace();
	        }
	        return persona;
	    }
	    public static Persona buscarPorIDConFiltro() {
	    	String[] roles= {"admin","cliente"}; 
			String rolElegido = (String) JOptionPane.showInputDialog(null, "Ingrese rol","",0,null,
					roles
					,roles[0]);

	    	int id = 0;
	    	
	    	List<Persona> personas = DtoPersona.mostrarPersonas().
	    			stream()
	    			.filter(persona->persona.getRol().equals(rolElegido))
	    			.collect(Collectors.toList());
	    	
	    	String[] personasArray = new String[personas.size()];
	    	for (int i = 0; i < personasArray.length; i++) {
	    		personasArray[i] = personas.get(i).getCorreo() +"/"+personas.get(i).getId();
			}
	    	String elegido = (String)JOptionPane.showInputDialog(null,
	    			"Elija usuario", null, 0, null, personasArray, personasArray[0]);
	    	id = Integer.parseInt( elegido.split("/")[1]);
	    	Persona persona = null;
	        try {
	            PreparedStatement stmt = con.prepareStatement(
	                "SELECT * FROM persona WHERE idpersona = ?"
	            );
	            stmt.setInt(1, id);
	  
	            //executequery se utiliza cuando no hay cambios en la bdd
	            ResultSet rs = stmt.executeQuery();

	            if (rs.next()) {
	                String nombre = rs.getString("nombre");
	                String correo = rs.getString("correo");
	                String contrasenia = rs.getString("contrasenia");
	                String rol = rs.getString("rol");

	                persona = new Persona(id,nombre,correo,contrasenia,rol);
	                }
	       
	        } catch (Exception e) {
	            e.printStackTrace();
	        }
	        return persona;
	    }
	    
	    public static boolean EditarPersona(Persona persona) {
	    	System.out.println(persona);
	        try {
	            PreparedStatement statement = con.prepareStatement(
	                "UPDATE `persona` SET nombre=?,rol=?,correo=?,contrasenia=? WHERE idpersona=?"
	            );
	            statement.setString(1, persona.getNombre());
	            statement.setString(2, persona.getRol());
	            statement.setString(3, persona.getCorreo());
	            statement.setString(4, persona.getContrasenia());
	            statement.setInt(5, persona.getId());

	            int filas = statement.executeUpdate();
	            if (filas > 0) {
	                System.out.println("Usuario editado correctamente.");
	                return true;
	            }
	        } catch (Exception e) {
	            e.printStackTrace();
	            return false;
            }
	        return false;
	        }
	    
	    
	    public static void EliminarPersona(int id) {
	        try {
	            PreparedStatement statement = con.prepareStatement(
	                "DELETE FROM `persona` WHERE idpersona = ?"
	            );
	           
	            statement.setInt(1, id);

	            int filas = statement.executeUpdate();
	            if (filas > 0) {
	                System.out.println("Usuario eliminado correctamente.");
	            }
	        } catch (Exception e) {
	            e.printStackTrace();
	        }
	    }
	    public static boolean agregarPersona(Persona persona) {
//	    	boolean flag = true;
//	    	for (Persona elemento : persona.mostrarPersona()) {
//				if (elemento.getCorreo().equals(persona.getCorreo())) {
//					flag = false;
//					break;
//				}
//			}
//	    	if (flag) {
//				descomentar para usar for para validar
		
	        try {
	            PreparedStatement statement = con.prepareStatement(
	                "INSERT INTO persona (nombre, correo, rol, contrasenia) VALUES (?, ?, ?, ?)"
	            );
	            statement.setString(1, persona.getNombre());
	            statement.setString(2, persona.getCorreo());
	            statement.setString(3, persona.getRol());
	            statement.setString(4, Encriptador.encriptar(persona.getContrasenia()));

	            int filas = statement.executeUpdate();
	            if (filas > 0) {
	                System.out.println("Usuario agregado correctamente.");
	                return true;
	            }
	        } catch (MySQLIntegrityConstraintViolationException e) {
	           	JOptionPane.showMessageDialog(null, "Usuario con mail ya creado");
	            return false;
	        } catch (Exception e) {
	            e.printStackTrace();
	            return false;

	        }
            return false;

//	    	}else {
//	    		JOptionPane.showMessageDialog(null, "Usuario con mail ya creado(con for)");
//			}
	    }

	    public static LinkedList<Persona> mostrarPersonas() {
	        LinkedList<Persona> personas = new LinkedList<Persona>();
	        try {
	            PreparedStatement stmt = con.prepareStatement("SELECT * FROM persona");
	            ResultSet rs = stmt.executeQuery();

	            while (rs.next()) {
	                int id = rs.getInt("idpersona");
	                String nombre = rs.getString("nombre");
	                String correo = rs.getString("correo");
	                String rol = rs.getString("rol");
	                String contrasenia = rs.getString("contrasenia");

	                personas.add(new Persona(id,nombre,correo,contrasenia,rol));
	                }
	      
	        } catch (Exception e) {
	            e.printStackTrace();
	        }
	        return personas;
	    }
}
