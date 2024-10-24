package Vista;

import javax.swing.*;
import Modelo.Workouts;
import Vista.PantallaEjercicios;
import Vista.PantallaWorkouts;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.awt.Desktop;

public class PantallaWorkouts {

	private JFrame frame;
	private JPanel panelWorkouts;
    private JComboBox<Object> nivelComboBox;

	/**
	 * Launch the application.
	 */
    public static void main(String[] args) {
        java.awt.EventQueue.invokeLater(() -> {
            try {
                PantallaWorkouts window = new PantallaWorkouts();
                window.frame.setVisible(true);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

	/**
	 * Create the application.
	 */
	public PantallaWorkouts() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	 private void initialize() {
	        frame = new JFrame();
	        frame.setBounds(100, 100, 600, 400);
	        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	        frame.getContentPane().setLayout(null);

	        panelWorkouts = new JPanel();
	        panelWorkouts.setLayout(new BoxLayout(panelWorkouts, BoxLayout.Y_AXIS));
	        
	        JScrollPane scrollPane = new JScrollPane(panelWorkouts);
	        scrollPane.setBounds(30, 30, 500, 250);
	        frame.getContentPane().add(scrollPane);

	        nivelComboBox = new JComboBox<>();
	        nivelComboBox.addItem("Todos");
	        nivelComboBox.setBounds(127, 291, 85, 25);
	        frame.getContentPane().add(nivelComboBox);

	        JButton btnVerWorkouts = new JButton("Ver Workouts");
	        btnVerWorkouts.addActionListener(e -> mostrarDatosWorkouts());
	        btnVerWorkouts.setBounds(222, 290, 150, 25);
	        frame.getContentPane().add(btnVerWorkouts);
	        
	        JButton btnVerHistorial = new JButton("Ver Historial");
	        btnVerHistorial.setBounds(382, 291, 148, 25);
	        frame.getContentPane().add(btnVerHistorial);
	        
	        JLabel lblFiltrar = new JLabel("Filtrar por nivel: ");
	        lblFiltrar.setBounds(30, 296, 100, 14);
	        frame.getContentPane().add(lblFiltrar);

	        cargarNiveles();
	    }

	    private void cargarNiveles() {
	        try {
	            Workouts workouts = new Workouts();
	            ArrayList<Integer> niveles = workouts.mObtenerNiveles();

	            for (Integer nivel : niveles) {
	                nivelComboBox.addItem(nivel);
	            }
	        } catch (IOException e) {
	            e.printStackTrace();
	            JOptionPane.showMessageDialog(frame, "Error al obtener los niveles: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
	        }
	    }

	    private void mostrarDatosWorkouts() {
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
	                        frame.setVisible(false);
	                        try {
								new PantallaEjercicios(workout);
							} catch (IOException e1) {
								// TODO Auto-generated catch block
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
	            JOptionPane.showMessageDialog(frame, "Error al obtener los workouts: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
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
