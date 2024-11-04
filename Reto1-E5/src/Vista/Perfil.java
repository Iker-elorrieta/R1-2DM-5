package Vista;

import javax.swing.JPanel;
import javax.swing.JScrollPane;

import Controlador.Metodos;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import java.awt.Font;

public class Perfil extends JPanel {

	private static final long serialVersionUID = 1L;

	/**
	 * Create the panel.
	 * @param queryMail 
	 */
	
	private JComboBox<Object> nivelComboBox;
    public JPanel panelWorkouts;
	public Perfil(String user) {
		Metodos metodos = new Metodos();
		
		setLayout(null);

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
        btnAtras.setBounds(20, 310, 89, 23);
        add(btnAtras);
		JButton btnCerrarSesion = new JButton("Cerrar Sesión");
		btnCerrarSesion.setBounds(20, 225, 182, 46);
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
		btnWorkouts.setBounds(20, 86, 182, 46);
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
		btnAcciones.setBounds(20, 154, 182, 46);
		btnAcciones.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				ModificarPerfil r = new ModificarPerfil(user);
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

        nivelComboBox = new JComboBox<>();
        nivelComboBox.addItem("Todos");
        nivelComboBox.setBounds(428, 412, 85, 25);
        add(nivelComboBox);

        JButton btnVerWorkouts = new JButton("Ver Workouts");
        btnVerWorkouts.setBounds(523, 412, 150, 25);
        add(btnVerWorkouts);
        
        JButton btnVerHistorial = new JButton("Ver Historial");
        btnVerHistorial.setBounds(683, 412, 148, 25);
        add(btnVerHistorial);
        
        JLabel lblFiltrar = new JLabel("Filtrar por nivel: ");
        lblFiltrar.setBounds(318, 417, 100, 14);
        add(lblFiltrar);
        
                JScrollPane scrollPane = new JScrollPane();
                scrollPane.setBounds(306, 72, 568, 314);
                add(scrollPane);
                
                panelWorkouts = new JPanel();
                scrollPane.setViewportView(panelWorkouts);
                panelWorkouts.setLayout(new BoxLayout(panelWorkouts, BoxLayout.Y_AXIS));
                btnVerWorkouts.addActionListener(e -> metodos.mostrarDatosWorkouts(nivelComboBox, panelWorkouts));
                

                metodos.cargarNiveles(nivelComboBox, panelWorkouts);
                
                		JLabel lblNewLabel = new JLabel(user);
                		lblNewLabel.setBounds(20, 30, 166, 40);
                		add(lblNewLabel);
                		lblNewLabel.setFont(new Font("Arial Black", Font.PLAIN, 28));
        
	}
}