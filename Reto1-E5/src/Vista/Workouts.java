package Vista;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import Controlador.Metodos;
import java.awt.event.ActionListener;
import java.awt.BorderLayout;
import java.awt.event.ActionEvent;

public class Workouts extends JPanel {
	Metodos metodos = new Metodos();
	private JComboBox<Object> nivelComboBox;
	private static final long serialVersionUID = 1L;
	public JPanel panelWorkouts;
	/**
	 * Create the panel.
	 */
	public Workouts() {
        setLayout(null);
        panelWorkouts = new JPanel();
        panelWorkouts.setLayout(new BoxLayout(panelWorkouts, BoxLayout.Y_AXIS));
        
        JScrollPane scrollPane = new JScrollPane();
        scrollPane.setBounds(30, 30, 500, 250);
        add(scrollPane);

        nivelComboBox = new JComboBox<>();
        nivelComboBox.addItem("Todos");
        nivelComboBox.setBounds(127, 291, 85, 25);
        add(nivelComboBox);

        JButton btnVerWorkouts = new JButton("Ver Workouts");
        btnVerWorkouts.addActionListener(e -> metodos.mostrarDatosWorkouts(nivelComboBox, panelWorkouts));
        btnVerWorkouts.setBounds(222, 290, 150, 25);
        add(btnVerWorkouts);
        
        JButton btnVerHistorial = new JButton("Ver Historial");
        btnVerHistorial.setBounds(382, 291, 148, 25);
        add(btnVerHistorial);
        
        JLabel lblFiltrar = new JLabel("Filtrar por nivel: ");
        lblFiltrar.setBounds(30, 296, 100, 14);
        add(lblFiltrar);

        metodos.cargarNiveles(nivelComboBox, panelWorkouts);
        
        JButton btnAtras = new JButton("Atras");
        btnAtras.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent e) {
        		Perfil p = new Perfil(); // Pasar el contentPane al perfil
				p.setSize(950, 500);
				p.setLocation(0, 0);
				
				removeAll();
				add(p, BorderLayout.CENTER);
				revalidate();
				repaint();
        	}
        });
        btnAtras.setBounds(540, 159, 89, 23);
        add(btnAtras);
    
	}

}
