package Vista;

import java.awt.BorderLayout;
import java.awt.Color;

import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.FileInputStream;
import java.util.List;

import javax.swing.JButton;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;


import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.firestore.Firestore;
import com.google.cloud.firestore.FirestoreOptions;
import com.google.cloud.firestore.QueryDocumentSnapshot;

import Modelo.CrearBackups;

public class Login extends JPanel {

	private static final long serialVersionUID = 1L;
	private JTextField textFieldUsuario;
	private JPasswordField passwordField;
	private static final String credArchivo = "gymapp.json";
	private static final String proyectoID = "grupo5-gymapp";
	
	private static final String usuariosColl = "usuarios";
	private static final String correoField = "correo";
	private static final String contraField = "contrasena";


	/**
	 * Create the frame.
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
		
		JLabel lblError = new JLabel("");
		lblError.setForeground(new Color(255, 0, 0));
		lblError.setFont(new Font("Arial Black", Font.PLAIN, 14));
		lblError.setBounds(412, 437, 390, 30);
		add(lblError);
		
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
						}else if(!queryMail.contentEquals(correo) && !queryPass.contentEquals(contrasena)) {
							lblError.setText("ambos campos incorrectos");
						}else if(!queryMail.contentEquals(correo)) {
							lblError.setText("Mail no válido");
						}else if(!queryPass.contentEquals(contrasena)) {
							lblError.setText("contraseña incorrecta");
						}
					}
					
					// comprobar resultado login
					if (loginUser != null) {
						System.out.println("login success");
						
						CrearBackups.main(null);
						Perfil p = new Perfil(); // Pasar el contentPane al perfil
						p.setSize(950, 500);
						p.setLocation(0, 0);
						
						removeAll();
						add(p, BorderLayout.CENTER);
						revalidate();
						repaint();
					} else {
						System.out.println("login failed");
					}
				} catch (Exception e1) { e1.printStackTrace(); }

			
			}
		});
		btnLogin.setFont(new Font("Arial Black", Font.PLAIN, 17));
		btnLogin.setBounds(94, 269, 197, 33);
		add(btnLogin);
		
		JButton btnRegistro = new JButton("Registro");
		btnRegistro.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Registro1 r = new Registro1();
				r.setSize(950, 500);
				r.setLocation(0, 0);
				
				removeAll();
				add(r, BorderLayout.CENTER);
				revalidate();
				repaint();
			}
		});
		btnRegistro.setBounds(367, 325, 89, 23);
		add(btnRegistro);
	}
	

}