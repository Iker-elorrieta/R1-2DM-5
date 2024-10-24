package Vista;

import java.awt.BorderLayout;
import java.awt.EventQueue;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import Modelo.CrearBackups;

public class Principal extends JFrame {

    private static final long serialVersionUID = 1L;
    private JPanel contentPane;

    /**
     * Launch the application.
     */
    public static void main(String[] args) {
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    CrearBackups backups = new CrearBackups();
                    backups.verificarYCrearArchivo();
                    backups.bajarDatosUsuarios();

                    Principal frame = new Principal();
                    frame.setVisible(true);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    /**
     * Create the frame.
     */
    public Principal() {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(100, 100, 950, 500);
        contentPane = new JPanel();
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));

        setContentPane(contentPane);
        contentPane.setLayout(null);
        
        Login l = new Login();
        l.setSize(950, 500);
        l.setLocation(0, 0);
        
        contentPane.removeAll();
        contentPane.add(l, BorderLayout.CENTER);
        contentPane.revalidate();
        contentPane.repaint();
    }
}
