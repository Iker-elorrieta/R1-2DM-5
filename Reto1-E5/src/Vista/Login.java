package Vista;

import java.awt.BorderLayout;
import java.awt.Color;

import java.awt.Font;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.FileInputStream;
import java.util.List;

import javax.swing.JButton;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.ImageIcon;


import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.firestore.Firestore;
import com.google.cloud.firestore.FirestoreOptions;
import com.google.cloud.firestore.QueryDocumentSnapshot;

import Conexion.Conexion;


public class Login extends JPanel {

	private static final long serialVersionUID = 1L;
	private JTextField textFieldUsuario;
	private JPasswordField passwordField;
	private static final String credArchivo = "gymapp.json";
	private static final String proyectoID = "grupo5-gymapp";
	
	private static final String usuariosColl = "usuarios";
	private static final String correoField = "correo";
	private static final String contraField = "contrasena";
	public String user = "";

	/**
	 * Create the frame.
	 */
	public Login() {
		setLayout(null);
		
		ImageIcon iconoOriginal = new ImageIcon("./img/gymapp.png");
        Image imagenEscalada = iconoOriginal.getImage().getScaledInstance(300, 250, Image.SCALE_SMOOTH);
        ImageIcon iconoEscalado = new ImageIcon(imagenEscalada);
        
        JLabel lblImagen = new JLabel(iconoEscalado);
        lblImagen.setBounds(167, 41, 295, 241);
        add(lblImagen);
		
		
		JLabel lblLogin = new JLabel("Login");
		lblLogin.setFont(new Font("Arial Black", Font.BOLD, 28));
		lblLogin.setBounds(32, 11, 314, 66);
		add(lblLogin);
		
		JLabel lblMail = new JLabel("Email");
		lblMail.setFont(new Font("Arial Black", Font.BOLD, 17));
		lblMail.setBounds(157, 292, 103, 14);
		add(lblMail);
		
		textFieldUsuario = new JTextField();
		textFieldUsuario.setBounds(335, 293, 141, 20);
		add(textFieldUsuario);
		textFieldUsuario.setColumns(10);
		
		JLabel lblContraseña = new JLabel("Contraseña");
		lblContraseña.setFont(new Font("Arial Black", Font.BOLD, 17));
		lblContraseña.setBounds(157, 344, 165, 17);
		add(lblContraseña);
		
		passwordField = new JPasswordField();
		passwordField.setBounds(335, 346, 141, 20);
		add(passwordField);
		
		JLabel lblError = new JLabel("");
		lblError.setForeground(new Color(255, 0, 0));
		lblError.setFont(new Font("Arial Black", Font.PLAIN, 14));
		lblError.setBounds(181, 379, 281, 30);
		add(lblError);
		
		JButton btnLogin = new JButton("Iniciar Sesion");
		btnLogin.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {

				String correo = textFieldUsuario.getText();
				char[] arrayContra = passwordField.getPassword();
				String contrasena = new String(arrayContra);
				
				try {
					FileInputStream serviceAccount = new FileInputStream(credArchivo);
					FirestoreOptions options = FirestoreOptions.getDefaultInstance().toBuilder()
							.setProjectId(proyectoID).setCredentials(GoogleCredentials.fromStream(serviceAccount)).build();
					Firestore db = options.getService();
					
					QueryDocumentSnapshot loginUser = null;
					System.out.println("login attempt on "+correo);
					
					Conexion conexion = new Conexion();
					boolean conectar =conexion.verificarConexion(db);
					if(!conectar) {
						System.out.println("Error en la conexión, utilizando datos desde el backup");
					}
					
					List<QueryDocumentSnapshot> usuarios = db.collection(usuariosColl).get().get().getDocuments();
					for (QueryDocumentSnapshot usuarioSnapshot : usuarios) {
						String queryMail = usuarioSnapshot.getString(correoField);
						String queryPass = usuarioSnapshot.getString(contraField);
						
						if (queryMail.contentEquals(correo) && queryPass.contentEquals(contrasena)) {
							loginUser = usuarioSnapshot;
							user = usuarioSnapshot.getId();
							break;
						}else if(!queryMail.contentEquals(correo) && !queryPass.contentEquals(contrasena)) {
							lblError.setText("Introduce los datos correctamente");
						}else if(!queryMail.contentEquals(correo)) {
							lblError.setText("Mail no válido");
						}else if(!queryPass.contentEquals(contrasena)) {
							lblError.setText("contraseña incorrecta");
						}
					}
					
					if (loginUser != null) {
						System.out.println("login success");
						
						try {
		                    ProcessBuilder builder = new ProcessBuilder("java", "-jar", "CrearBackups.jar");
		                    builder.start();
		                } catch (Exception e1) {
		                    e1.printStackTrace();
		                }
						Perfil p = new Perfil(user);
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
		btnLogin.setBounds(95, 419, 197, 33);
		add(btnLogin);
		
		JButton btnRegistro = new JButton("Registro");
		btnRegistro.setFont(new Font("Arial Black", Font.PLAIN, 17));
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
		btnRegistro.setBounds(335, 419, 197, 33);
		add(btnRegistro);
	}
	

}