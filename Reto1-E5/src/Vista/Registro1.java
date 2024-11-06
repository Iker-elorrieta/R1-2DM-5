package Vista;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Date;

import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JRadioButton;
import javax.swing.JTextField;


import com.toedter.calendar.JDateChooser;
import com.toedter.calendar.JTextFieldDateEditor;

import Controlador.Metodos;

public class Registro1 extends JPanel {

	private static final long serialVersionUID = 1L;

	
	private JTextField textFieldNombre;
	private JTextField textFieldApellido;
	private JTextField textFieldUsuario;
	private JTextField textFieldMail;
	private JPasswordField textFieldRepContraseña;
	private JPasswordField textFieldContraseña;
	/**
	 * Create the panel.
	 */
	public Registro1() {

		Metodos metodos = new Metodos();

		setLayout(null);

		JLabel lblRegistro = new JLabel("Registro");
		lblRegistro.setFont(new Font("Arial Black", Font.BOLD, 28));
		lblRegistro.setBounds(238, 10, 161, 76);
		add(lblRegistro);

		JLabel lblNombre = new JLabel("Nombre");
		lblNombre.setFont(new Font("Arial Black", Font.PLAIN, 17));
		lblNombre.setBounds(25, 100, 95, 25);
		add(lblNombre);

		JLabel lblApellido = new JLabel("Apellido");
		lblApellido.setFont(new Font("Arial Black", Font.PLAIN, 17));
		lblApellido.setBounds(25, 168, 95, 17);
		add(lblApellido);

		JLabel lblUsuario = new JLabel("Usuario");
		lblUsuario.setFont(new Font("Arial Black", Font.PLAIN, 17));
		lblUsuario.setBounds(25, 237, 95, 14);
		add(lblUsuario);

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

		textFieldUsuario = new JTextField();
		textFieldUsuario.setBounds(140, 238, 161, 20);
		add(textFieldUsuario);
		textFieldUsuario.setColumns(10);

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

        metodos.configurarFechaNacimiento(fechaNacimientoCalendar);

		JRadioButton rdbtnEntrenador = new JRadioButton("Entrenador");
		rdbtnEntrenador.setFont(new Font("Arial", Font.BOLD, 17));
		rdbtnEntrenador.setBounds(332, 166, 161, 23);
		add(rdbtnEntrenador);

		JRadioButton rdbtnCliente = new JRadioButton("Cliente");
		rdbtnCliente.setFont(new Font("Arial", Font.BOLD, 17));
		rdbtnCliente.setBounds(332, 234, 161, 23);
		add(rdbtnCliente);

		ButtonGroup grupo1 = new ButtonGroup();
		grupo1.add(rdbtnEntrenador);
		grupo1.add(rdbtnCliente);

		JLabel lblError = new JLabel("");
		lblError.setForeground(new Color(255, 0, 0));
		lblError.setFont(new Font("Arial Black", Font.PLAIN, 14));
		lblError.setBounds(123, 426, 390, 30);
		add(lblError);

		JButton btnRegistrarse = new JButton("Registrarse");
		btnRegistrarse.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String nombre = textFieldNombre.getText();
                String apellido = textFieldApellido.getText();
                String usuario = textFieldUsuario.getText();
                String mail = textFieldMail.getText();
                String contrasena = new String(textFieldContraseña.getPassword());
                String repContrasena = new String(textFieldRepContraseña.getPassword());
                Date fechaNacimiento = fechaNacimientoCalendar.getDate();
                
                if (nombre.isEmpty() || apellido.isEmpty() || usuario.isEmpty() || mail.isEmpty() 
                        || contrasena.isEmpty() || repContrasena.isEmpty()) {
                    lblError.setText("Rellena todos los campos");
                } else if (!metodos.validarEmail(mail)) {
                    lblError.setText("Mail no válido");
                } else if (!rdbtnEntrenador.isSelected() && !rdbtnCliente.isSelected()) {
                    lblError.setText("Selecciona tipo de usuario");
                } else if (!metodos.contraComprobar(contrasena, repContrasena)) {
                    lblError.setText("Las contraseñas no coinciden");
                } else {
                    boolean esCliente = rdbtnCliente.isSelected();
                    metodos.registrarUsuario(nombre, apellido, usuario, mail, contrasena, fechaNacimiento, esCliente, lblError);
                }
			}
		});
		btnRegistrarse.setFont(new Font("Arial Black", Font.PLAIN, 17));
		btnRegistrarse.setBounds(238, 395, 152, 23);
		add(btnRegistrarse);

		textFieldRepContraseña = new JPasswordField();
		textFieldRepContraseña.setBounds(521, 300, 109, 20);
		add(textFieldRepContraseña);

		textFieldContraseña = new JPasswordField();
		textFieldContraseña.setBounds(140, 300, 161, 20);
		add(textFieldContraseña);
		
		JButton btnAtras = new JButton("Atras");
		btnAtras.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Login r = new Login();
				r.setSize(950, 500);
				r.setLocation(0, 0);
				
				removeAll();
				add(r, BorderLayout.CENTER);
				revalidate();
				repaint();
			}
		});
		btnAtras.setBounds(544, 433, 89, 23);
		add(btnAtras);

	
	}
}
