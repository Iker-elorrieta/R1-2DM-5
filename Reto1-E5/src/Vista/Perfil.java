package Vista;

import javax.swing.JPanel;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;


import javax.swing.JButton;
import javax.swing.JLabel;
import java.awt.Font;

public class Perfil extends JPanel {
	
	private static final long serialVersionUID = 1L;

	/**
	 * Create the panel.
	 * @param queryMail 
	 */
	public Perfil(String user) {
		setLayout(null);
		
		JLabel lblNewLabel = new JLabel(user);
		lblNewLabel.setFont(new Font("Arial Black", Font.PLAIN, 28));
		lblNewLabel.setBounds(10, 11, 914, 84);
		add(lblNewLabel);

		JButton btnAtras = new JButton("Cerrar Sesión");
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
		btnAtras.setFont(new Font("Arial Black", Font.PLAIN, 17));
		btnAtras.setBounds(570, 297, 182, 46);
		add(btnAtras);
		
		JButton btnWorkouts = new JButton("Ver Workouts");
		btnWorkouts.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Workouts r = new Workouts(user);
				r.setSize(950, 500);
				r.setLocation(0, 0);
				
				removeAll();
				add(r, BorderLayout.CENTER);
				revalidate();
				repaint();
			}
		});
		btnWorkouts.setFont(new Font("Arial Black", Font.PLAIN, 17));
		btnWorkouts.setBounds(570, 107, 182, 46);
		add(btnWorkouts);
		
		JButton btnAcciones = new JButton("Modificar Perfil");
		btnAcciones.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Workouts r = new Workouts(user);
				r.setSize(950, 500);
				r.setLocation(0, 0);
				
				removeAll();
				add(r, BorderLayout.CENTER);
				revalidate();
				repaint();
			}
		});
		btnAcciones.setFont(new Font("Arial Black", Font.PLAIN, 17));
		btnAcciones.setBounds(570, 200, 182, 46);
		add(btnAcciones);
	}
}
