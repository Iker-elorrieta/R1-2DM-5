package Vista;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.FileInputStream;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JRadioButton;
import javax.swing.JTextField;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.firestore.CollectionReference;
import com.google.cloud.firestore.Firestore;
import com.google.cloud.firestore.FirestoreOptions;
import com.toedter.calendar.JDateChooser;
import com.toedter.calendar.JTextFieldDateEditor;

import Controlador.Metodos;

public class ModificarPerfil extends JPanel {

	private static final long serialVersionUID = 1L;

	/**
	 * Create the panel.
	 */
	private JTextField textFieldNombre;
	private JTextField textFieldApellido;
	private JTextField textFieldMail;
	private JPasswordField textFieldRepContraseña;
	private JPasswordField textFieldContraseña;
	public ModificarPerfil(String user) {

		Metodos metodos = new Metodos();

		setLayout(null);

		JLabel lblModificarPerfil = new JLabel("Modificar Perfil");
		lblModificarPerfil.setFont(new Font("Arial Black", Font.BOLD, 28));
		lblModificarPerfil.setBounds(238, 10, 367, 76);
		add(lblModificarPerfil);

		JLabel lblNombre = new JLabel("Nombre");
		lblNombre.setFont(new Font("Arial Black", Font.PLAIN, 17));
		lblNombre.setBounds(25, 100, 95, 25);
		add(lblNombre);

		JLabel lblApellido = new JLabel("Apellido");
		lblApellido.setFont(new Font("Arial Black", Font.PLAIN, 17));
		lblApellido.setBounds(25, 168, 95, 17);
		add(lblApellido);

		textFieldNombre = new JTextField();
		textFieldNombre.setBounds(140, 106, 161, 20);
		add(textFieldNombre);
		textFieldNombre.setColumns(10);

		textFieldApellido = new JTextField();
		textFieldApellido.setBounds(140, 170, 161, 20);
		add(textFieldApellido);
		textFieldApellido.setColumns(10);

		JLabel lblContraseña = new JLabel("Contraseña");
		lblContraseña.setFont(new Font("Arial Black", Font.PLAIN, 17));
		lblContraseña.setBounds(25, 299, 122, 14);
		add(lblContraseña);

		textFieldMail = new JTextField();
		textFieldMail.setBounds(140, 354, 161, 20);
		add(textFieldMail);
		textFieldMail.setColumns(10);

		JLabel lblMail = new JLabel("Mail");
		lblMail.setFont(new Font("Arial Black", Font.PLAIN, 17));
		lblMail.setBounds(25, 352, 122, 17);
		add(lblMail);

		JLabel lblRepetirContrasea = new JLabel("Repetir Contraseña");
		lblRepetirContrasea.setFont(new Font("Arial Black", Font.PLAIN, 17));
		lblRepetirContrasea.setBounds(332, 299, 194, 14);
		add(lblRepetirContrasea);

		JLabel lblFNacimiento = new JLabel("F. Nacimiento");
		lblFNacimiento.setFont(new Font("Arial Black", Font.PLAIN, 17));
		lblFNacimiento.setBounds(332, 105, 194, 14);
		add(lblFNacimiento);

		JDateChooser fechaNacimientoCalendar = new JDateChooser();
        fechaNacimientoCalendar.setBounds(474, 105, 200, 20);
        add(fechaNacimientoCalendar);

        JTextFieldDateEditor editor = (JTextFieldDateEditor) fechaNacimientoCalendar.getDateEditor();
        editor.setEditable(false);

        Calendar ahoraMismo = Calendar.getInstance();
        int ano = ahoraMismo.get(Calendar.YEAR);
        int mes = ahoraMismo.get(Calendar.MONTH) + 1;
        int dia = ahoraMismo.get(Calendar.DATE);
        String maxString = ano + "-" + mes + "-" + dia;
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        
        try {
            fechaNacimientoCalendar.setMaxSelectableDate(dateFormat.parse(maxString));
            fechaNacimientoCalendar.setDate(dateFormat.parse(maxString));
        } catch (ParseException e) {
            e.printStackTrace();
        }


		JLabel lblError = new JLabel("");
		lblError.setForeground(new Color(255, 0, 0));
		lblError.setFont(new Font("Arial Black", Font.PLAIN, 14));
		lblError.setBounds(123, 426, 390, 30);
		add(lblError);

		JButton btnConfirmar = new JButton("Confirmar Cambios");
		btnConfirmar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				char[] arrayContra = textFieldContraseña.getPassword();
				char[] arrayRepContra = textFieldRepContraseña.getPassword();
				String Contraseña = new String(arrayContra);
				String repContraseña = new String(arrayRepContra);
				String mail = textFieldMail.getText();
				String apellido = textFieldApellido.getText();
				String nombre = textFieldNombre.getText();

				
				
				if (nombre.intern() == "" || apellido.intern() == ""  || mail.intern() == ""
						 || Contraseña.intern() == "" || repContraseña.intern() == "") {
					lblError.setText("Rellena todos los campos");
				} else if (!metodos.validarEmail(mail)) {
					lblError.setText("Mail no válido");
				} else if (!metodos.contraComprobar(Contraseña, repContraseña)) {
					lblError.setText("Las contraseñas no coinciden");
				} else {
					lblError.setText("Registro realizado correctamente");
					updateUI();
				
					FileInputStream serviceAccount;
					try {
						serviceAccount = new FileInputStream("gymapp.json");
						FirestoreOptions firestoreOptions = FirestoreOptions.getDefaultInstance().toBuilder()
								.setProjectId("grupo5-gymapp")
								.setCredentials(GoogleCredentials.fromStream(serviceAccount)).build();
						Firestore db = firestoreOptions.getService();

						CollectionReference usuarios = db.collection("usuarios");

						Map<String, Object> usuario1 = new HashMap<>();
						usuario1.put("nombre", nombre);
						usuario1.put("apellido", apellido);
						usuario1.put("contrasena", Contraseña);
						usuario1.put("correo", mail);
						usuario1.put("fecha de nacimiento", fechaNacimientoCalendar.getDate());
						

					} catch (IOException e1) {

						e1.printStackTrace();
					}

				}

				updateUI();
			}
		});
		btnConfirmar.setFont(new Font("Arial Black", Font.PLAIN, 17));
		btnConfirmar.setBounds(112, 392, 235, 23);
		add(btnConfirmar);

		textFieldRepContraseña = new JPasswordField();
		textFieldRepContraseña.setBounds(521, 300, 109, 20);
		add(textFieldRepContraseña);

		textFieldContraseña = new JPasswordField();
		textFieldContraseña.setBounds(140, 300, 161, 20);
		add(textFieldContraseña);
		
		JButton btnAtras = new JButton("Atras");
		btnAtras.setFont(new Font("Arial Black", Font.BOLD, 17));
		btnAtras.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Perfil r = new Perfil(user);
				r.setSize(950, 500);
				r.setLocation(0, 0);
				
				removeAll();
				add(r, BorderLayout.CENTER);
				revalidate();
				repaint();
			}
		});
		btnAtras.setBounds(383, 395, 222, 23);
		add(btnAtras);

	
	}

}
