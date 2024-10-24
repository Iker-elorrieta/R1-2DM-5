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
	 */
	public Perfil() {
		setLayout(null);
		
		JLabel lblNewLabel = new JLabel("Perfil");
		lblNewLabel.setFont(new Font("Arial Black", Font.PLAIN, 28));
		lblNewLabel.setBounds(10, 11, 135, 84);
		add(lblNewLabel);

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
		btnAtras.setBounds(822, 444, 89, 23);
		add(btnAtras);
		
		JButton btnWorkouts = new JButton("Ver Workouts");
		btnWorkouts.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Workouts r = new Workouts();
				r.setSize(950, 500);
				r.setLocation(0, 0);
				
				removeAll();
				add(r, BorderLayout.CENTER);
				revalidate();
				repaint();
			}
		});
		btnWorkouts.setFont(new Font("Arial Black", Font.PLAIN, 17));
		btnWorkouts.setBounds(525, 49, 182, 46);
		add(btnWorkouts);
	}
}
