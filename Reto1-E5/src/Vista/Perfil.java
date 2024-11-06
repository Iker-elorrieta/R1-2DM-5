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
        
        JButton btnVerHistorial = new JButton("Ver Historial");
        btnVerHistorial.setBounds(627, 412, 204, 25);
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
                

                metodos.cargarNiveles(nivelComboBox, panelWorkouts);
                metodos.mostrarDatosWorkouts(nivelComboBox, panelWorkouts);
                
                		JLabel lblNewLabel = new JLabel(user);
                		lblNewLabel.setBounds(20, 30, 166, 40);
                		add(lblNewLabel);
                		lblNewLabel.setFont(new Font("Arial Black", Font.PLAIN, 28));
        
	}
}