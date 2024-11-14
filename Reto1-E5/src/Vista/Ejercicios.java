package Vista;
import Modelo.Ejercicio;
import javax.swing.JPanel;
import javax.swing.JLabel;
import javax.swing.JButton;
import javax.swing.JOptionPane;
import java.awt.Font;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import Controlador.Metodos;

public class Ejercicios extends JPanel {
    private static final long serialVersionUID = 1L;
    private List<Ejercicio> ejercicios;
    private int currentIndex = 0;
    private JLabel nombreLabel;
    private JLabel descripcionLabel;
    private JLabel timerWorkoutLabel, timerEjerLabel, timerDescLabel;
    private double tiempoSerie, tiempoDesc;
    private double tiempoTotalInicial, tiempoInicial;
    private double tiempoWorkout, tiempoEjercicio;
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
        this.tiempoSerie = ejercicios.get(0).getTiempo();
        this.tiempoDesc = ejercicios.get(0).getDescanso();
        this.tiempoTotalInicial = tiempoTotalWorkout;
        this.tiempoInicial = tiempoTotalWorkout;
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

        JButton siguienteButton = new JButton("Siguiente ejercicio");
        siguienteButton.setBounds(309, 120, 151, 30);
        siguienteButton.setEnabled(false); // Desactivar el botón "Siguiente" al inicio
        add(siguienteButton);

        JButton iniciarButton = new JButton("Iniciar workout");
        iniciarButton.setBounds(189, 80, 150, 30);
        add(iniciarButton);
        
        timerWorkoutLabel = new JLabel("Tiempo Workout: " + formatTime(tiempoWorkout));
        timerWorkoutLabel.setBounds(21, 241, 318, 30);
        timerWorkoutLabel.setFont(new Font("Arial", Font.BOLD, 20));
        add(timerWorkoutLabel);

        timerEjerLabel = new JLabel("Tiempo Ejercicio: " + formatTime(tiempoEjercicio));
        timerEjerLabel.setFont(new Font("Arial", Font.BOLD, 20));
        timerEjerLabel.setBounds(21, 282, 318, 30);
        add(timerEjerLabel);
        
        timerDescLabel = new JLabel("Tiempo Descanso: " + formatTime(tiempoDesc));
        timerDescLabel.setFont(new Font("Arial", Font.BOLD, 20));
        timerDescLabel.setBounds(21, 323, 318, 30);
        add(timerDescLabel);
        
        JButton salirButton = new JButton("Salir");
        salirButton.setBounds(417, 80, 150, 30);
        add(salirButton);

        if (ejercicios.size() == 1) {
            siguienteButton.setVisible(false);
        }

        siguienteButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int respuesta = JOptionPane.showConfirmDialog(
                    Ejercicios.this,
                    "¿Has completado el ejercicio \"" + ejercicios.get(currentIndex).getNombre() + "\"?",
                    "Confirmar Ejercicio Completado",
                    JOptionPane.YES_NO_OPTION
                );

                if (respuesta == JOptionPane.YES_OPTION) {
                    ejerciciosRealizados.add(ejercicios.get(currentIndex).getNombre());

                    if (currentIndex < ejercicios.size() - 1) {
                        currentIndex++;
                        actualizarEjercicio();

                        metodos.detenerCrono(1);
                        metodos.iniciarCrono(Ejercicios.this, 1);
                        
                        metodos.detenerCrono(2);
                        metodos.detenerCrono(3);
                        metodos.iniciarCrono(Ejercicios.this, 2);
                    }

                    if (currentIndex == ejercicios.size() - 1) {
                        siguienteButton.setEnabled(false);
                    }
                }
            }
        });


        iniciarButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (!isWorkoutStarted) {
                    isWorkoutStarted = true;
                    siguienteButton.setEnabled(true);
                    metodos.iniciarCrono(Ejercicios.this, 0);
                    metodos.iniciarCrono(Ejercicios.this, 1);
                    metodos.iniciarCrono(Ejercicios.this, 2);
                    iniciarButton.setText("Pausar workout");
                    iniciarButton.setBackground(Color.RED);
                } else {
                    if (isTimerRunning) {
                    	metodos.pausarCrono(0);
                    	metodos.pausarCrono(1);
                        iniciarButton.setText("Reanudar workout");
                        iniciarButton.setBackground(Color.GREEN);
                        isTimerRunning = false;
                    } else {
                        metodos.reanudarCrono(Ejercicios.this, 0);
                        metodos.reanudarCrono(Ejercicios.this, 1);
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
                    metodos.detenerCrono(0);
                    metodos.detenerCrono(1);
                }
                
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
            }
        });

    }

    private void actualizarEjercicio() {
        nombreLabel.setText("Nombre: " + ejercicios.get(currentIndex).getNombre());
        descripcionLabel.setText("Descripción: " + ejercicios.get(currentIndex).getDescripcion());
    }

    private String formatTime(double seconds) {
        double minutes = seconds / 60;
        double remainingSeconds = seconds % 60;
        return String.format("%02d:%02d", (int) minutes, (int) remainingSeconds);
    }
    
    public double getTiempoEjer() {
        return tiempoSerie;
    }

    public double getTiempoDesc() {
        return tiempoDesc;
    }
    
    public void setTimerRunning(boolean running) {
        this.isTimerRunning = running;
    }
    
    public void decrementarTiempoEjer() {
    	tiempoSerie--;
        tiempoInicial--;
    }

    public void decrementarTiempoDesc() {
        tiempoDesc--;
        tiempoInicial--;
    }
    
    public void establecerTiempoWorkout(double valor) {
    	tiempoWorkout = valor;
    }
    
    public void establecerTiempoEjercicio(double valor) {
    	tiempoEjercicio = valor;
    }
    
    public void updateWorkoutTimerLabel() {
        timerWorkoutLabel.setText("Tiempo Workout: " + formatTime(tiempoWorkout));
    }

    public void updateEjerTimerLabel() {
        timerEjerLabel.setText("Tiempo Ejercicio: " + formatTime(tiempoEjercicio));
    }

    public void updateDescTimerLabel() {
        timerDescLabel.setText("Tiempo Descanso: " + formatTime(tiempoDesc));
    }
    
    public double getTiempoTotalInicial() {
        return tiempoTotalInicial;
    }

    public double getTiempoRestante() {
    	return tiempoTotalInicial - tiempoInicial;
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
    
    public Ejercicio getCurrentEjericio() {
    	return ejercicios.get(currentIndex);
    }
    
    public boolean esUltimoEjercicio() {
    	return currentIndex == ejercicios.size() - 1;
    }
    
    public List<Map<String, Object>> getWorkoutRealizado() {
        return workoutRealizado;
    }

    public void reestablecerTiempoSerie() {
    	tiempoSerie = getCurrentEjericio().getTiempo();
    }

    public void reestablecerTiempoDescanso() {
    	tiempoDesc = getCurrentEjericio().getDescanso();
    }
}