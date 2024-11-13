package Vista;

import Modelo.Ejercicio;
import javax.swing.JPanel;
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
import Controlador.Metodos;

public class Ejercicios extends JPanel {
    private static final long serialVersionUID = 1L;
    private List<Ejercicio> ejercicios;
    private int currentIndex = 0;
    private JLabel nombreLabel;
    private JLabel descripcionLabel;
    private JLabel timerLabel;
    private int tiempoInicial;
    private int tiempoTotalInicial;
    private boolean isTimerRunning = false;
    private boolean isWorkoutStarted = false;  // para verificar si el workout ha comenzado
    private List<String> ejerciciosRealizados = new ArrayList<>();
    private List<Map<String, Object>> workoutRealizado = new ArrayList<>();
    private Metodos metodos;

    private String user;
    private String workoutName;

    public Ejercicios(String user, String workoutName, List<Ejercicio> ejercicios, int tiempoTotalWorkout) {
        this.user = user;
        this.workoutName = workoutName;
        this.ejercicios = ejercicios;
        this.tiempoInicial = tiempoTotalWorkout;
        this.tiempoTotalInicial = tiempoTotalWorkout;
        this.metodos = new Metodos();

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

        JButton iniciarButton = new JButton("Iniciar workout");
        iniciarButton.setBounds(189, 80, 150, 30);
        add(iniciarButton);

        timerLabel = new JLabel("Tiempo: " + formatTime(tiempoInicial));
        timerLabel.setBounds(50, 20, 150, 30);
        timerLabel.setFont(new Font("Arial", Font.BOLD, 20));
        add(timerLabel);

        JButton salirButton = new JButton("Salir");
        salirButton.setBounds(417, 80, 150, 30);
        add(salirButton);
        
        iniciarButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (!isWorkoutStarted) {
                    isWorkoutStarted = true;
                    metodos.iniciarCrono(Ejercicios.this);
                    iniciarButton.setText("Pausar workout");
                    iniciarButton.setBackground(Color.RED);
                } else {
                    if (isTimerRunning) {
                        metodos.pausarCrono();
                        iniciarButton.setText("Reanudar workout");
                        iniciarButton.setBackground(Color.GREEN);
                        isTimerRunning = false;
                    } else {
                        metodos.reanudarCrono(Ejercicios.this);
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
                    metodos.detenerCrono();
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
                
                if (isWorkoutStarted) {
                    metodos.finalizarWorkout(user, workoutName, ejercicios, ejerciciosRealizados, tiempoTotalInicial - tiempoInicial, workoutRealizado, Ejercicios.this);

                } else {
                    Perfil p = new Perfil(user);
                    p.setSize(950, 500);
                    p.setLocation(0, 0);

                    removeAll();
                    add(p, BorderLayout.CENTER);
                    revalidate();
                    repaint();
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

    }

    private String formatTime(int seconds) {
        int minutes = seconds / 60;
        int remainingSeconds = seconds % 60;
        return String.format("%02d:%02d", minutes, remainingSeconds);
    }

    public int getTiempoInicial() {
        return tiempoInicial;
    }

    public void setTimerRunning(boolean running) {
        this.isTimerRunning = running;
    }

    public void decrementarTiempoInicial() {
        tiempoInicial--;
    }

    public void updateTimerLabel() {
        timerLabel.setText(formatTime(tiempoInicial));
    }

    public int getTiempoTotalInicial() {
        return tiempoTotalInicial;
    }

    public String getUser() {
        return user;
    }

    public String getWorkoutName() {
        return workoutName;
    }
    
    public int getCurrentIndex() {
        return currentIndex;
    }

    public List<Ejercicio> getEjercicios() {
        return ejercicios;
    }

    public List<String> getEjerciciosRealizados() {
        return ejerciciosRealizados;
    }

    public List<Map<String, Object>> getWorkoutRealizado() {
        return workoutRealizado;
    }
}
