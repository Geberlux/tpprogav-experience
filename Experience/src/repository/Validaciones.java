package repository;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.swing.JOptionPane;

public interface Validaciones {

		public static final Pattern VALID_EMAIL_ADDRESS_REGEX = 
		    Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$", Pattern.CASE_INSENSITIVE);

		public static boolean validate(String emailStr) {
		        Matcher matcher = VALID_EMAIL_ADDRESS_REGEX.matcher(emailStr);
		        return matcher.matches();
		}
		static String ValidarString(String mensaje) {
			
			String dato;
			do {
				dato = JOptionPane.showInputDialog(mensaje);
				if (dato.isEmpty()) {
					JOptionPane.showMessageDialog(null, "Error al ingresar.");
				}
			} while (dato.isEmpty());
			return dato;
			
			
		}
}
