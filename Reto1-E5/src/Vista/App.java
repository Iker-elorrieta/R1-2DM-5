package Vista;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.firestore.CollectionReference;
import com.google.cloud.firestore.DocumentReference;
import com.google.cloud.firestore.Firestore;
import com.google.cloud.firestore.FirestoreOptions;

import Controlador.Metodos;

import javax.swing.JLabel;
import javax.swing.JTextField;
import java.awt.Font;
import javax.swing.JRadioButton;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.awt.event.ActionEvent;
import java.awt.Color;
import javax.swing.JPasswordField;

public class App extends JFrame {

	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private JTextField textFieldNombre;
	private JTextField textFieldApellido;
	private JTextField textFieldUsuario;
	private JTextField textFieldMail;
	private JTextField textFieldFNac;
	private JPasswordField textFieldRepContraseña;
	private JPasswordField textFieldContraseña;

	/**
	 * Launch the application.  
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					
					App frame = new App();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public App() {
		Metodos metodos = new Metodos();

		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 881, 532);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));

		setContentPane(contentPane);
		contentPane.setLayout(null);

		JLabel lblRegistro = new JLabel("Registro");
		lblRegistro.setFont(new Font("Arial Black", Font.BOLD, 28));
		lblRegistro.setBounds(332, 11, 161, 76);
		contentPane.add(lblRegistro);

		JLabel lblNombre = new JLabel("Nombre");
		lblNombre.setFont(new Font("Arial Black", Font.PLAIN, 17));
		lblNombre.setBounds(25, 100, 95, 25);
		contentPane.add(lblNombre);

		JLabel lblApellido = new JLabel("Apellido");
		lblApellido.setFont(new Font("Arial Black", Font.PLAIN, 17));
		lblApellido.setBounds(25, 182, 95, 17);
		contentPane.add(lblApellido);

		JLabel lblUsuario = new JLabel("Usuario");
		lblUsuario.setFont(new Font("Arial Black", Font.PLAIN, 17));
		lblUsuario.setBounds(25, 256, 95, 14);
		contentPane.add(lblUsuario);

		textFieldNombre = new JTextField();
		textFieldNombre.setBounds(169, 105, 161, 20);
		contentPane.add(textFieldNombre);
		textFieldNombre.setColumns(10);
		

		textFieldApellido = new JTextField();
		textFieldApellido.setBounds(169, 179, 161, 20);
		contentPane.add(textFieldApellido);
		textFieldApellido.setColumns(10);
		

		JLabel lblContraseña = new JLabel("Contraseña");
		lblContraseña.setFont(new Font("Arial Black", Font.PLAIN, 17));
		lblContraseña.setBounds(25, 332, 122, 14);
		contentPane.add(lblContraseña);

		textFieldUsuario = new JTextField();
		textFieldUsuario.setBounds(169, 253, 161, 20);
		contentPane.add(textFieldUsuario);
		textFieldUsuario.setColumns(10);
		

		textFieldMail = new JTextField();
		textFieldMail.setBounds(169, 407, 161, 20);
		contentPane.add(textFieldMail);
		textFieldMail.setColumns(10);
		

		JLabel lblMail = new JLabel("Mail");
		lblMail.setFont(new Font("Arial Black", Font.PLAIN, 17));
		lblMail.setBounds(25, 410, 122, 17);
		contentPane.add(lblMail);

		JLabel lblRepetirContrasea = new JLabel("Repetir Contraseña");
		lblRepetirContrasea.setFont(new Font("Arial Black", Font.PLAIN, 17));
		lblRepetirContrasea.setBounds(456, 332, 194, 14);
		contentPane.add(lblRepetirContrasea);

		JLabel lblFNacimiento = new JLabel("F. Nacimiento");
		lblFNacimiento.setFont(new Font("Arial Black", Font.PLAIN, 17));
		lblFNacimiento.setBounds(456, 108, 194, 14);
		contentPane.add(lblFNacimiento);

		textFieldFNac = new JTextField();
		textFieldFNac.setColumns(10);
		textFieldFNac.setBounds(667, 105, 161, 20);
		contentPane.add(textFieldFNac);
		

		JRadioButton rdbtnEntrenador = new JRadioButton("Entrenador");
		rdbtnEntrenador.setFont(new Font("Arial", Font.BOLD, 17));
		rdbtnEntrenador.setBounds(456, 180, 161, 23);
		contentPane.add(rdbtnEntrenador);

		JRadioButton rdbtnCliente = new JRadioButton("Cliente");
		rdbtnCliente.setFont(new Font("Arial", Font.BOLD, 17));
		rdbtnCliente.setBounds(456, 255, 161, 23);
		contentPane.add(rdbtnCliente);

		ButtonGroup grupo1 = new ButtonGroup();
		grupo1.add(rdbtnEntrenador);
		grupo1.add(rdbtnCliente);
		
		JLabel lblError = new JLabel("xd");
		lblError.setForeground(new Color(255, 0, 0));
		lblError.setFont(new Font("Arial Black", Font.PLAIN, 14));
		lblError.setBounds(412, 437, 390, 30);
		contentPane.add(lblError);

		JButton btnRegistrarse = new JButton("Registrarse");
		btnRegistrarse.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				char[] arrayContra = textFieldContraseña.getPassword();
				char[] arrayRepContra = textFieldRepContraseña.getPassword();
				String Contraseña = new String(arrayContra);
				String repContraseña = new String(arrayRepContra);
				String fNac = textFieldFNac.getText();
				String mail = textFieldMail.getText();
				String usuario = textFieldUsuario.getText();
				String apellido = textFieldApellido.getText();
				String nombre = textFieldNombre.getText();

				if (nombre.intern() == "" || apellido.intern() == "" || usuario.intern() == "" || mail.intern() == "" || fNac.intern() == "" || Contraseña.intern() == "" || repContraseña.intern() == "") {
					lblError.setText("Rellena todos los campos");
					
				}else if(!metodos.comprobarFecha(fNac, "dd/MM/yyyy")) {
					lblError.setText("Formato de fehca de nacimiento no válida('dd/MM/yyyy')");
				}else if(!metodos.validarEmail(mail)) {
					lblError.setText("Mail no válido");
				}else if(!rdbtnEntrenador.isSelected() && !rdbtnCliente.isSelected()) {
					lblError.setText("Selecciona tipo de usuario");
				}else if(!metodos.contraComprobar(Contraseña, repContraseña)) {
					lblError.setText("Las contraseñas no coinciden");
				}
				else {
					lblError.setText("Registro realizado correctamente");
					contentPane.updateUI();
					FileInputStream serviceAccount;
					try {
						serviceAccount = new FileInputStream("gymapp.json");
						FirestoreOptions firestoreOptions = FirestoreOptions.getDefaultInstance().toBuilder()
							.setProjectId("grupo5-gymapp").setCredentials(GoogleCredentials.fromStream(serviceAccount)).build();
					Firestore db = firestoreOptions.getService();
					
					CollectionReference usuarios = db.collection("usuarios");
					
					Map<String,Object> usuario1 = new HashMap<>();
					usuario1.put("nombre", nombre);
					usuario1.put("apellido", apellido);
					usuario1.put("contrasena", Contraseña);
					usuario1.put("mail", mail);
					if(rdbtnCliente.isSelected()) {
						usuario1.put("nivel", 0);
					}
					
					//fecha nacimiento
					SimpleDateFormat formato = new SimpleDateFormat("dd/MM/yyyy");
					try {
						Date fecha = formato.parse(fNac);
						usuario1.put("fecha de nacimiento", fecha);
					} catch (ParseException e1) {
						
						e1.printStackTrace();
					}
					usuarios.document(usuario).set(usuario1);
				
					} catch (IOException e1) {
						
						e1.printStackTrace();
					}
					
				}

				contentPane.updateUI();
			}
		});
		btnRegistrarse.setFont(new Font("Arial Black", Font.PLAIN, 17));
		btnRegistrarse.setBounds(537, 403, 152, 23);
		contentPane.add(btnRegistrarse);

		textFieldRepContraseña = new JPasswordField();
		textFieldRepContraseña.setBounds(667, 332, 161, 20);
		contentPane.add(textFieldRepContraseña);

		textFieldContraseña = new JPasswordField();
		textFieldContraseña.setBounds(169, 332, 161, 20);
		contentPane.add(textFieldContraseña);

	}
}
