package Vista;

import javax.swing.JPanel;
import javax.swing.JLabel;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.FileInputStream;
import java.util.List;

import javax.swing.JTextField;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.firestore.Firestore;
import com.google.cloud.firestore.FirestoreOptions;
import com.google.cloud.firestore.QueryDocumentSnapshot;

import javax.swing.JPasswordField;
import javax.swing.JButton;

public class Login extends JPanel {

	private static final String credArchivo = "gymapp.json";
	private static final String proyectoID = "grupo5-gymapp";
	
	private static final String usuariosColl = "usuarios";
	private static final String correoField = "correo";
	private static final String contraField = "contrasena";
	
	private static final long serialVersionUID = 1L;
	private JTextField textFieldUsuario;
	private JPasswordField passwordField;

	/**
	 * Create the panel.
	 */
	public Login() {
		
		
		setLayout(null);
		
		JLabel lblLogin = new JLabel("Login");
		lblLogin.setFont(new Font("Arial Black", Font.BOLD, 28));
		lblLogin.setBounds(32, 11, 314, 66);
		add(lblLogin);
		
		JLabel lblMail = new JLabel("Email");
		lblMail.setFont(new Font("Arial Black", Font.BOLD, 17));
		lblMail.setBounds(32, 103, 103, 14);
		add(lblMail);
		
		textFieldUsuario = new JTextField();
		textFieldUsuario.setBounds(220, 103, 126, 20);
		add(textFieldUsuario);
		textFieldUsuario.setColumns(10);
		
		JLabel lblContraseña = new JLabel("Contraseña");
		lblContraseña.setFont(new Font("Arial Black", Font.BOLD, 17));
		lblContraseña.setBounds(32, 178, 165, 17);
		add(lblContraseña);
		
		passwordField = new JPasswordField();
		passwordField.setBounds(220, 179, 126, 20);
		add(passwordField);
		
		JButton btnLogin = new JButton("Iniciar Sesion");
		btnLogin.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {

				String correo = textFieldUsuario.getText();
				char[] arrayContra = passwordField.getPassword();
				String contrasena = new String(arrayContra);
				
				try {
					// conexion con la base de datos
					FileInputStream serviceAccount = new FileInputStream(credArchivo);
					FirestoreOptions options = FirestoreOptions.getDefaultInstance().toBuilder()
							.setProjectId(proyectoID).setCredentials(GoogleCredentials.fromStream(serviceAccount)).build();
					Firestore db = options.getService();
					
					QueryDocumentSnapshot loginUser = null;
					System.out.println("login attempt on "+correo);
					
					// obtener coleccion de usuarios
					List<QueryDocumentSnapshot> usuarios = db.collection(usuariosColl).get().get().getDocuments();
					for (QueryDocumentSnapshot usuarioSnapshot : usuarios) {
						String queryMail = usuarioSnapshot.getString(correoField);
						String queryPass = usuarioSnapshot.getString(contraField);
						
						if (queryMail.contentEquals(correo) && queryPass.contentEquals(contrasena)) {
							loginUser = usuarioSnapshot;
							break;
						}
					}
					
					// comprobar resultado login
					if (loginUser != null) {
						System.out.println("login success");
					} else {
						System.out.println("login failed");
					}
				} catch (Exception e1) { e1.printStackTrace(); }

			
			}
		});
		btnLogin.setFont(new Font("Arial Black", Font.PLAIN, 17));
		btnLogin.setBounds(98, 255, 178, 23);
		add(btnLogin);
		
		JButton btnAtras = new JButton("Atras");
		btnAtras.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
			}
		});
		btnAtras.setBounds(822, 444, 89, 23);
		add(btnAtras);
	}
}
