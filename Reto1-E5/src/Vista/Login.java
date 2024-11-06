package Vista;

import java.awt.BorderLayout;
import java.awt.Color;

import java.awt.Font;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.ImageIcon;


import com.google.cloud.firestore.QueryDocumentSnapshot;

import Controlador.Metodos;


public class Login extends JPanel {

	private static final long serialVersionUID = 1L;
	private JTextField textFieldUsuario;
	private JPasswordField passwordField;
	public String user = "";

	/**
	 * Create the frame.
	 */
	public Login() {
		Metodos metodos = new Metodos();
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
		            QueryDocumentSnapshot loginUser = metodos.iniciarSesion(correo, contrasena, lblError);
		            
		            if (loginUser != null) {
		                try {
		                    ProcessBuilder builder = new ProcessBuilder("java", "-jar", "CrearBackups.jar");
		                    builder.start();
		                } catch (Exception ex) {
		                    ex.printStackTrace();
		                }

		                Perfil w = new Perfil(user);
		                w.setSize(950, 500);
		                w.setLocation(0, 0);
		                
		                removeAll();
		                add(w, BorderLayout.CENTER);
		                revalidate();
		                repaint();
		            }
		        } catch (Exception ex) {
		            lblError.setText("Error al iniciar sesión.");
		            ex.printStackTrace();
		        }
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