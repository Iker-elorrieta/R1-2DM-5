package Vista;

import javax.swing.JPanel;
import Modelo.Ejercicio;

import javax.swing.JLabel;
import javax.swing.JButton;
import javax.swing.Timer;
import java.awt.Font;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

public class Ejercicios extends JPanel {
    private static final long serialVersionUID = 1L;
    private List<Ejercicio> ejercicios;
    private int currentIndex = 0;
    private JLabel nombreLabel;
    private JLabel descripcionLabel;
    private JLabel timerLabel;
    private Timer timer;
    private int elapsedTime = 0; // Tiempo transcurrido en segundos
    private boolean isTimerRunning = false;
    private boolean isWorkoutStarted = false;

    public Ejercicios(String user, String workoutName, List<Ejercicio> ejercicios) {
        this.ejercicios = ejercicios;
        setLayout(null);

        // Etiqueta para mostrar el nombre del workout
        JLabel workoutLabel = new JLabel("Workout: " + workoutName);
        workoutLabel.setBounds(594, 11, 300, 30);
        workoutLabel.setFont(new Font("Arial", Font.BOLD, 20));
        add(workoutLabel);

        // Etiquetas para mostrar el nombre y descripción del ejercicio actual
        nombreLabel = new JLabel("Nombre: " + ejercicios.get(currentIndex).getNombre());
        nombreLabel.setFont(new Font("Arial", Font.PLAIN, 16));
        nombreLabel.setBounds(309, 15, 258, 25);
        add(nombreLabel);

        descripcionLabel = new JLabel("Descripción: " + ejercicios.get(currentIndex).getDescripcion());
        descripcionLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        descripcionLabel.setBounds(283, 43, 600, 25);
        add(descripcionLabel);

        // Botones para navegar entre los ejercicios
        JButton siguienteButton = new JButton("Siguiente");
        siguienteButton.setBounds(408, 79, 100, 30);
        add(siguienteButton);

        JButton anteriorButton = new JButton("Anterior");
        anteriorButton.setBounds(268, 79, 100, 30);
        anteriorButton.setEnabled(false); // Desactivar al inicio, ya que estamos en el primer ejercicio
        add(anteriorButton);

        JButton iniciarButton = new JButton("Iniciar workout");
        iniciarButton.setBounds(310, 120, 150, 30);
        add(iniciarButton);

        timerLabel = new JLabel("Tiempo: 00:00");
        timerLabel.setBounds(50, 20, 150, 30);
        timerLabel.setFont(new Font("Arial", Font.BOLD, 20));
        add(timerLabel);

        JButton salirButton = new JButton("Salir");
        salirButton.setBounds(310, 160, 150, 30);
        add(salirButton);

        // Configurar el temporizador
        timer = new Timer(1000, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                elapsedTime++;
                updateTimerLabel();
            }
        });

        // Acción del botón "Siguiente"
        siguienteButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (currentIndex < ejercicios.size() - 1) {
                    currentIndex++;
                    actualizarEjercicio();
                    anteriorButton.setEnabled(true); // Habilitar el botón "Anterior"
                }
                if (currentIndex == ejercicios.size() - 1) {
                    siguienteButton.setEnabled(false); // Desactivar el botón "Siguiente" si es el último ejercicio
                    if (isTimerRunning) {
                        iniciarButton.setText("Terminar workout"); // Cambiar el texto al último ejercicio
                    }
                }
            }
        });

        // Acción del botón "Anterior"
        anteriorButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (currentIndex > 0) {
                    currentIndex--;
                    actualizarEjercicio();
                    siguienteButton.setEnabled(true); // Habilitar el botón "Siguiente"
                }
                if (currentIndex == 0) {
                    anteriorButton.setEnabled(false); // Desactivar el botón "Anterior" si es el primer ejercicio
                }
            }
        });

        // Acción del botón "Iniciar workout"
        iniciarButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (!isWorkoutStarted) {
                    isWorkoutStarted = true;
                    iniciarCrono();
                    iniciarButton.setText("Pausar workout");
                    iniciarButton.setBackground(Color.RED);
                    if (currentIndex == ejercicios.size() - 1) {
                        iniciarButton.setText("Terminar workout");
                    }
                } else {
                    if (isTimerRunning) {
                    	pausarCrono();
                        iniciarButton.setText("Reanudar workout");
                        iniciarButton.setBackground(Color.GREEN);
                    } else {
                    	reanudarCrono();
                        iniciarButton.setText("Pausar workout");
                        iniciarButton.setBackground(Color.RED);
                    }
                }
            }
        });

        salirButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (isTimerRunning) {
                	pararCrono();
                }
                Perfil w = new Perfil(user);
                w.setSize(950, 500);
                w.setLocation(0, 0);
                
                removeAll();
                add(w, BorderLayout.CENTER);
                revalidate();
                repaint();
            }
        });
    }

    private void iniciarCrono() {
        timer.start();
        isTimerRunning = true;
    }

    private void pausarCrono() {
        timer.stop();
        isTimerRunning = false;
    }

    private void reanudarCrono() {
        timer.start();
        isTimerRunning = true;
    }

    private void pararCrono() {
        timer.stop();
        isTimerRunning = false;
    }

    // Método para actualizar el nombre y la descripción del ejercicio actual
    private void actualizarEjercicio() {
        nombreLabel.setText("Nombre: " + ejercicios.get(currentIndex).getNombre());
        descripcionLabel.setText("Descripción: " + ejercicios.get(currentIndex).getDescripcion());
    }

    // Método para actualizar el cronómetro
    private void updateTimerLabel() {
        int minutes = elapsedTime / 60;
        int seconds = elapsedTime % 60;
        timerLabel.setText(String.format("Tiempo: %02d:%02d", minutes, seconds));
    }
}
