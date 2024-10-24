package Controlador;
import java.awt.Desktop;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import Modelo.Workouts;
import Vista.PantallaEjercicios;

public class Metodos {

	public boolean contraComprobar(String contraseña, String repContraseña) {
		boolean resultado = true;
		if(repContraseña.intern() != contraseña.intern()) {
			resultado = false;
		} 
		return resultado;
	}

	public boolean comprobarFecha(String fNac, String dateFormat) {
		SimpleDateFormat formato = new SimpleDateFormat(dateFormat);
		formato.setLenient(false);
		  try {
		    formato.parse(fNac);
		  } catch (ParseException e) {
		    return false;
		  }
		  return true;
	}

	public Boolean validarEmail (String email) {
		Pattern pattern = Pattern.compile("^([0-9a-zA-Z]+[-._+&])*[0-9a-zA-Z]+@([-0-9a-zA-Z]+[.])+[a-zA-Z]{2,6}$");
		Matcher matcher = pattern.matcher(email);
		return matcher.matches();
	}
	
	public void cargarNiveles(JComboBox<Object> nivelComboBox, JPanel panelWorkouts) {
        try {
            Workouts workouts = new Workouts();
            ArrayList<Integer> niveles = workouts.mObtenerNiveles();

            for (Integer nivel : niveles) {
                nivelComboBox.addItem(nivel);
            }
        } catch (IOException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(panelWorkouts, "Error al obtener los niveles: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
	
	public void mostrarDatosWorkouts(JComboBox<Object> nivelComboBox, JPanel panelWorkouts) {
        try {
            Workouts workouts = new Workouts();
            ArrayList<Workouts> listaWorkouts = workouts.mObtenerWorkouts();

            Object nivelSeleccionado = nivelComboBox.getSelectedItem();

            panelWorkouts.removeAll();

            for (Workouts workout : listaWorkouts) {
                boolean mostrarWorkout = false;

                if ("Todos".equals(nivelSeleccionado)) {
                    mostrarWorkout = true;
                } else if (nivelSeleccionado instanceof Integer) {
                    mostrarWorkout = workout.getNivel() == (Integer) nivelSeleccionado;
                }

                if (mostrarWorkout) {
                    JPanel workoutPanel = new JPanel();
                    workoutPanel.setBorder(BorderFactory.createTitledBorder(workout.getNombre()));
                    workoutPanel.setLayout(new BoxLayout(workoutPanel, BoxLayout.Y_AXIS));

                    JLabel nivelLabel = new JLabel("Nivel: " + workout.getNivel());
                    JLabel ejerciciosLabel = new JLabel("Ejercicios: " + workout.getEjercicios());
                    JLabel videoLabel = new JLabel("Video: " + workout.getVideo());

                    JPanel ejerciciosPanel = new JPanel();
                    ejerciciosPanel.setLayout(new BoxLayout(ejerciciosPanel, BoxLayout.Y_AXIS));
                    ejerciciosPanel.setVisible(false);

                    workoutPanel.addMouseListener(new java.awt.event.MouseAdapter() {
                        public void mouseClicked(java.awt.event.MouseEvent evt) {
                            ArrayList<String> ids = null;
                            try {
                                ids = workout.mObtenerEjerciciosIDs();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                            ejerciciosPanel.removeAll();
                            for (String id : ids) {
                                ejerciciosPanel.add(new JLabel("Ejercicio de este workout: " + id));
                            }
                            ejerciciosPanel.setVisible(!ejerciciosPanel.isVisible());
                            panelWorkouts.revalidate();
                            panelWorkouts.repaint();
                        }
                    });

                    videoLabel.addMouseListener(new java.awt.event.MouseAdapter() {
                        public void mouseClicked(java.awt.event.MouseEvent evt) {
                            abrirVideo(workout.getVideo());
                        }
                    });

                    JButton btnIniciarWorkout = new JButton("Iniciar Workout");
                    btnIniciarWorkout.addActionListener(e -> {
                    	panelWorkouts.setVisible(false);
                        try {
							new PantallaEjercicios(workout);
						} catch (IOException e1) {
							
							e1.printStackTrace();
						}
                    });

                    workoutPanel.add(nivelLabel);
                    workoutPanel.add(ejerciciosLabel);
                    workoutPanel.add(videoLabel);
                    workoutPanel.add(btnIniciarWorkout);
                    
                    workoutPanel.add(ejerciciosPanel);

                    panelWorkouts.add(workoutPanel);
                }
            }

            panelWorkouts.revalidate();
            panelWorkouts.repaint();
        } catch (IOException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(panelWorkouts, "Error al obtener los workouts: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
	private void abrirVideo(String videoURL) {
	        try {
	            URI uri = new URI(videoURL);
	            if (Desktop.isDesktopSupported()) {
	                Desktop desktop = Desktop.getDesktop();
	                if (desktop.isSupported(Desktop.Action.BROWSE)) {
	                    desktop.browse(uri);
	                } else {
	                    System.out.println("Desktop no soportado");
	                }
	            }
	        } catch (IOException | URISyntaxException e) {
	            e.printStackTrace();
	        }
	    }
	
}
