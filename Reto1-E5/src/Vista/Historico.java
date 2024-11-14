package Vista;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.JScrollPane;
import javax.swing.table.DefaultTableModel;

import Modelo.Historial;

import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.awt.event.ActionEvent;

public class Historico extends JPanel {

    private static final long serialVersionUID = 1L;
    private JTable table;
    private DefaultTableModel tableModel;

    public Historico(String user) {
        setBackground(new Color(255, 255, 255));
        setBounds(288, 11, 895, 541);
        setLayout(null);

        JLabel lblNewLabel = new JLabel("HISTÓRICO DE WORKOUTS");
        lblNewLabel.setFont(new Font("Tahoma", Font.PLAIN, 24));
        lblNewLabel.setBounds(256, 0, 326, 40);
        add(lblNewLabel);

        // Configuración del modelo de la tabla
        String[] columnNames = { "Workout", "Fecha", "Duración", "Completado (%)" };
        tableModel = new DefaultTableModel(columnNames, 0);

        // Crear la tabla para mostrar el historial
        table = new JTable(tableModel);
        table.setFillsViewportHeight(true);
        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBounds(85, 49, 705, 360);
        add(scrollPane);

        // Obtener historial del usuario desde Firestore y llenar la tabla
        cargarHistorial(user);

        JButton salirButton = new JButton("Salir");
        salirButton.setBounds(382, 419, 150, 30);
        salirButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                Perfil r = new Perfil(user);
                r.setSize(950, 500);
                r.setLocation(0, 0);

                removeAll();
                add(r, BorderLayout.CENTER);
                revalidate();
                repaint();
            }
        });
        add(salirButton);
    }

    private void cargarHistorial(String user) {
        try {
            // Obtener historial del usuario desde Firestore
            Historial historialModel = new Historial();
            ArrayList<Historial> historialList = historialModel.mObtenerHistorico(user);

            // Llenar la tabla con los datos del historial
            for (Historial historial : historialList) {
                String workoutNombre = historial.getWorkout().getNombre();
                String fecha = historial.getFecha() != null ? historial.getFecha().toString() : "N/A";
                String duracion = String.format("%02d:%02d",
                        (int) (historial.getTiempoRealizacion() / 60),
                        (int) (historial.getTiempoRealizacion() % 60));
                String porcentajeCompletado = String.valueOf(historial.getPorcentajeCompletado());

                // Añadir fila al modelo de la tabla
                tableModel.addRow(new Object[] { workoutNombre, fecha, duracion, porcentajeCompletado });
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
