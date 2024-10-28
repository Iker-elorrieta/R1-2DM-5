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
		lblNewLabel.setBounds(10, 11, 914, 84);
		lblNewLabel.setFont(new Font("Arial Black", Font.PLAIN, 28));
		add(lblNewLabel);

		JButton btnAtras = new JButton("Atras");
        btnAtras.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                Login p = new Login();
                p.setSize(950, 500);
                p.setLocation(0, 0);

                removeAll();
                add(p, BorderLayout.CENTER);
                revalidate();
                repaint();
            }
        });
        btnAtras.setBounds(539, 435, 89, 23);
        add(btnAtras);
		JButton btnCerrarSesion = new JButton("Cerrar Sesión");
		btnCerrarSesion.setBounds(332, 264, 182, 46);
		btnCerrarSesion.addActionListener(new ActionListener() {
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
		btnCerrarSesion.setFont(new Font("Arial Black", Font.PLAIN, 17));
		add(btnCerrarSesion);

		JButton btnWorkouts = new JButton("Ver Workouts");
		btnWorkouts.setBounds(332, 106, 182, 46);
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
		add(btnWorkouts);
		
		JButton btnAcciones = new JButton("Modificar Perfil");
		btnAcciones.setBounds(332, 188, 182, 46);
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
		add(btnAcciones);
	}
}