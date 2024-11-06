package Vista;

import javax.swing.JPanel;

import Controlador.Metodos;
import javax.swing.JLabel;
import java.awt.Font;

public class Ejercicios extends JPanel {
    Metodos metodos = new Metodos();
    
    private static final long serialVersionUID = 1L;

    public Ejercicios(String user) {
    	setLayout(null);
    	
    	JLabel lblNombreEjer = new JLabel("");
    	lblNombreEjer.setFont(new Font("Tahoma", Font.PLAIN, 14));
    	lblNombreEjer.setBounds(304, 11, 156, 21);
    	add(lblNombreEjer);
    	
    	JLabel lblNombreWorkout = new JLabel("");
    	lblNombreWorkout.setFont(new Font("Tahoma", Font.PLAIN, 14));
    	lblNombreWorkout.setBounds(514, 11, 156, 21);
    	add(lblNombreWorkout);
    	
    }
}
