package Vista;

import Modelo.Workouts;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

public class PantallaEjercicios {

    private JFrame frame;
    private JPanel panelEjercicios;
    private JLabel lblNombreEjercicio;
    private JLabel lblDescripcionEjercicio;
    private JLabel lblNombreWorkout;
    private JButton btnIniciarPausar;
    private Timer timer;
    private int tiempoTranscurrido = 0;

    public PantallaEjercicios(Workouts workout) throws IOException {
        initialize(workout);
    }

    private void initialize(Workouts workout) throws IOException {
        frame = new JFrame();
        frame.setBounds(100, 100, 600, 400);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.getContentPane().setLayout(new BorderLayout());

        panelEjercicios = new JPanel();
        panelEjercicios.setLayout(null);

        JPanel panelSuperior = new JPanel();
        panelSuperior.setBounds(0, 0, 582, 315);

        lblNombreEjercicio = new JLabel("Ejercicio: " + workout.mObtenerEjerciciosIDs());
        lblNombreEjercicio.setBounds(0, 0, 181, 102);
        lblDescripcionEjercicio = new JLabel("Descripción: Aquí va la descripción del ejercicio.");
        lblDescripcionEjercicio.setBounds(156, 0, 204, 102);
        panelSuperior.setLayout(null);
        panelSuperior.add(lblNombreEjercicio);
        
                lblNombreWorkout = new JLabel("Workout: " + workout.getNombre());
                lblNombreWorkout.setBounds(433, 0, 149, 102);
                panelSuperior.add(lblNombreWorkout);
        panelSuperior.add(lblDescripcionEjercicio);

        panelEjercicios.add(panelSuperior);

        JLabel lblCronometro = new JLabel("Cronómetro: 00:00");
        lblCronometro.setBounds(243, 318, 92, 14);
        panelEjercicios.add(lblCronometro);

        btnIniciarPausar = new JButton("Iniciar");
        btnIniciarPausar.setBounds(243, 333, 92, 23);
        panelEjercicios.add(btnIniciarPausar);

        btnIniciarPausar.addActionListener(new ActionListener() {
            private boolean enFuncionamiento = false;

            @Override
            public void actionPerformed(ActionEvent e) {
                if (enFuncionamiento) {
                    timer.stop();
                    btnIniciarPausar.setText("Iniciar");
                    enFuncionamiento = false;
                } else {
                    iniciarCronometro(lblCronometro);
                    btnIniciarPausar.setText("Pausar");
                    enFuncionamiento = true;
                }
            }
        });

        frame.getContentPane().add(new JScrollPane(panelEjercicios), BorderLayout.CENTER);
        
        JButton btnVolver = new JButton("Volver");
        btnVolver.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent e) {
        		//metodo para volver a pantalla workouts
        	}
        });
        btnVolver.setBounds(0, 333, 89, 23);
        panelEjercicios.add(btnVolver);
        frame.setVisible(true);
    }

    private void iniciarCronometro(JLabel lblCronometro) {
        timer = new Timer(1000, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                tiempoTranscurrido++;
                int minutos = tiempoTranscurrido / 60;
                int segundos = tiempoTranscurrido % 60;
                lblCronometro.setText(String.format("Cronómetro: %02d:%02d", minutos, segundos));
            }
        });
        timer.start();
    }
}
