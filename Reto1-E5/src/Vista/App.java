package Vista;

import java.awt.BorderLayout;
import java.awt.EventQueue;


import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class App extends JFrame {

	private static final long serialVersionUID = 1L;
	private JPanel contentPane;

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
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 911, 536);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));

		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		
		
		JButton btnLogin = new JButton("Login");
		btnLogin.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Login l = new Login();
				l.setSize(950, 500);
				l.setLocation(0, 0);
				
				contentPane.removeAll();
				contentPane.add(l, BorderLayout.CENTER);
				contentPane.revalidate();
				contentPane.repaint();
			}
		});
		btnLogin.setBounds(149, 325, 89, 23);
		contentPane.add(btnLogin);
		
		JButton btnRegistro = new JButton("Registro");
		btnRegistro.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {

				Registro1 r = new Registro1();
				r.setSize(950, 500);
				r.setLocation(0, 0);
				
				contentPane.removeAll();
				contentPane.add(r, BorderLayout.CENTER);
				contentPane.revalidate();
				contentPane.repaint();
			}
		});
		btnRegistro.setBounds(367, 325, 89, 23);
		contentPane.add(btnRegistro);
	}

}
