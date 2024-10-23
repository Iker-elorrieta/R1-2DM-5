package Vista;

import javax.swing.JPanel;
import javax.swing.JLabel;
import java.awt.Font;
import javax.swing.JTextField;
import javax.swing.JPasswordField;
import javax.swing.JButton;

public class Login extends JPanel {

	private static final long serialVersionUID = 1L;
	private JTextField textFieldUsuario;
	private JPasswordField passwordField;

	/**
	 * Create the panel.
	 */
	public Login() {
		setLayout(null);
		
		JLabel lblLogin = new JLabel("Login");
		lblLogin.setFont(new Font("Arial Black", Font.PLAIN, 17));
		lblLogin.setBounds(155, 114, 314, 66);
		add(lblLogin);
		
		JLabel lblUsuario = new JLabel("Usuario");
		lblUsuario.setBounds(164, 202, 46, 14);
		add(lblUsuario);
		
		textFieldUsuario = new JTextField();
		textFieldUsuario.setBounds(304, 199, 86, 20);
		add(textFieldUsuario);
		textFieldUsuario.setColumns(10);
		
		JLabel lblContraseña = new JLabel("Contraseña");
		lblContraseña.setBounds(164, 261, 74, 14);
		add(lblContraseña);
		
		passwordField = new JPasswordField();
		passwordField.setBounds(304, 258, 86, 20);
		add(passwordField);
		
		JButton btnLogin = new JButton("Iniciar Sesion");
		btnLogin.setBounds(218, 324, 128, 23);
		add(btnLogin);

	}
}
