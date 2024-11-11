package Vista;

import javax.swing.JPanel;
import Modelo.Ejercicio;
import javax.swing.JLabel;
import javax.swing.JButton;
import javax.swing.Timer;

import Controlador.Metodos;

import java.awt.Font;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import javax.swing.JOptionPane;

public class Ejercicios extends JPanel {
    private static final long serialVersionUID = 1L;
    private List<Ejercicio> ejercicios;
    private int currentIndex = 0;
    private JLabel nombreLabel;
    private JLabel descripcionLabel;
    private JLabel timerLabel;
    private Timer timer;
    private int elapsedTime = 0;
    private boolean isTimerRunning = false;
    private boolean isWorkoutStarted = false;
    private List<String> ejerciciosRealizados = new ArrayList<>();
    private List<Map<String, Object>> workoutRealizado = new ArrayList<>();

    public Ejercicios(String user, String workoutName, List<Ejercicio> ejercicios) {
        this.ejercicios = ejercicios;
        setLayout(null);

        JLabel workoutLabel = new JLabel("Workout: " + workoutName);
        workoutLabel.setBounds(594, 11, 300, 30);
        workoutLabel.setFont(new Font("Arial", Font.BOLD, 20));
        add(workoutLabel);

        nombreLabel = new JLabel("Nombre: " + ejercicios.get(currentIndex).getNombre());
        nombreLabel.setFont(new Font("Arial", Font.PLAIN, 16));
        nombreLabel.setBounds(309, 15, 258, 25);
        add(nombreLabel);

        descripcionLabel = new JLabel("Descripción: " + ejercicios.get(currentIndex).getDescripcion());
        descripcionLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        descripcionLabel.setBounds(283, 43, 600, 25);
        add(descripcionLabel);

        JButton siguienteButton = new JButton("Siguiente");
        siguienteButton.setBounds(408, 79, 100, 30);
        add(siguienteButton);

        JButton anteriorButton = new JButton("Anterior");
        anteriorButton.setBounds(268, 79, 100, 30);
        anteriorButton.setEnabled(false);
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

        timer = new Timer(1000, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                elapsedTime++;
                updateTimerLabel();
            }
        });

        siguienteButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (currentIndex < ejercicios.size() - 1) {
                    ejerciciosRealizados.add(ejercicios.get(currentIndex).getNombre());
                    currentIndex++;
                    actualizarEjercicio();
                    anteriorButton.setEnabled(true);
                }
                if (currentIndex == ejercicios.size() - 1) {
                    siguienteButton.setEnabled(false);
                }
            }
        });

        anteriorButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (currentIndex > 0) {
                    currentIndex--;
                    actualizarEjercicio();
                    siguienteButton.setEnabled(true);
                }
                if (currentIndex == 0) {
                    anteriorButton.setEnabled(false);
                }
            }
        });

        iniciarButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (!isWorkoutStarted) {
                    isWorkoutStarted = true;
                    iniciarCrono();
                    iniciarButton.setText("Pausar workout");
                    iniciarButton.setBackground(Color.RED);
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
                if (isWorkoutStarted && !ejerciciosRealizados.contains(ejercicios.get(currentIndex).getNombre())) {
                    ejerciciosRealizados.add(ejercicios.get(currentIndex).getNombre());
                }
                
                Map<String, Object> workoutData = new HashMap<>();
                workoutData.put("nombreWorkout", workoutName);
                workoutData.put("ejerciciosRealizados", new ArrayList<>(ejerciciosRealizados));
                workoutData.put("tiempoTotal", elapsedTime);

                workoutRealizado.add(workoutData); // Agregar el workout realizado a la lista

                int ejerciciosCompletados = ejerciciosRealizados.size();
                int totalEjercicios = ejercicios.size();
                double porcentajeCompletado = ((double) ejerciciosCompletados / totalEjercicios) * 100;

                String mensajeMotivacional;
                if (porcentajeCompletado == 100) {
                    mensajeMotivacional = "¡Felicidades! Completaste todo el workout. ¡Sigue así!";
                } else if (porcentajeCompletado == 50) {
                    mensajeMotivacional = "¡Buen trabajo! Has completado la mitad del workout.";
                } else if (porcentajeCompletado > 50) {
                    mensajeMotivacional = "¡Bien hecho! Has completado más de la mitad del workout.";
                } else {
                    mensajeMotivacional = "No te desanimes, a la próxima lo conseguirás";
                }

                Metodos.guardarDatosWorkout(porcentajeCompletado, elapsedTime, workoutName);
                
                String resumen = String.format(
                        "Tiempo total: %02d:%02d\nEjercicios completados: %d de %d\nPorcentaje completado: %.2f%%\n\n%s",
                        elapsedTime / 60, elapsedTime % 60,
                        ejerciciosCompletados, totalEjercicios,
                        porcentajeCompletado,
                        mensajeMotivacional
                );

                JOptionPane.showMessageDialog(null, resumen, "Resumen del Workout", JOptionPane.INFORMATION_MESSAGE);

                Perfil w = new Perfil(user);
                w.setSize(950, 500);
                w.setLocation(0, 0);
                removeAll();
                add(w, BorderLayout.CENTER);
                revalidate();
                repaint();

                System.out.println(workoutRealizado); // Puedes revisar el estado de la lista aquí
            }
        });

        if (ejercicios.size() == 1) {
            siguienteButton.setEnabled(false);
        }
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

    private void actualizarEjercicio() {
        nombreLabel.setText("Nombre: " + ejercicios.get(currentIndex).getNombre());
        descripcionLabel.setText("Descripción: " + ejercicios.get(currentIndex).getDescripcion());
    }

    private void updateTimerLabel() {
        int minutes = elapsedTime / 60;
        int seconds = elapsedTime % 60;
        timerLabel.setText(String.format("Tiempo: %02d:%02d", minutes, seconds));
    }
}
