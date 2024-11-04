package Controlador;

import java.awt.Desktop;
import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.CollectionReference;
import com.google.cloud.firestore.Firestore;
import com.google.cloud.firestore.QueryDocumentSnapshot;
import com.google.cloud.firestore.QuerySnapshot;

import Conexion.Conexion;
import Modelo.Workouts;


public class Metodos {
    // Métodos de validación

    public boolean contraComprobar(String contraseña, String repContraseña) {
        return repContraseña.equals(contraseña);
    }

    public boolean comprobarFecha(String fNac, String dateFormat) {
        SimpleDateFormat formato = new SimpleDateFormat(dateFormat);
        formato.setLenient(false);
        try {
            formato.parse(fNac);
            return true;
        } catch (ParseException e) {
            return false;
        }
    }

    public Boolean validarEmail(String email) {
        Pattern pattern = Pattern.compile("^([0-9a-zA-Z]+[-._+&])*[0-9a-zA-Z]+@([-0-9a-zA-Z]+[.])+[a-zA-Z]{2,6}$");
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }

    // Cargar niveles en el ComboBox

    public void cargarNiveles(JComboBox<Object> nivelComboBox, JPanel panelWorkouts) {
        try {
            ArrayList<Integer> niveles = mObtenerNiveles();
            for (Integer nivel : niveles) {
                nivelComboBox.addItem(nivel);
            }
        } catch (IOException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(panelWorkouts, "Error al obtener los niveles: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    // Mostrar workouts según el nivel seleccionado

    public void mostrarDatosWorkouts(JComboBox<Object> nivelComboBox, JPanel panelWorkouts) {
        try {
            ArrayList<Workouts> listaWorkouts = mObtenerWorkouts();
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
                    // Panel principal que contendrá el workout y los ejercicios
                    JPanel workoutPanel = new JPanel();
                    workoutPanel.setBorder(BorderFactory.createTitledBorder(workout.getNombre()));
                    workoutPanel.setLayout(new BoxLayout(workoutPanel, BoxLayout.Y_AXIS));

                    JLabel nivelLabel = new JLabel("Nivel: " + workout.getNivel());
                    JLabel ejerciciosLabel = new JLabel("Ejercicios: " + workout.getEjercicios());
                    JLabel videoLabel = new JLabel("Video: " + workout.getVideo());
                    

                    // Listener para abrir el video
                    videoLabel.addMouseListener(new java.awt.event.MouseAdapter() {
                        public void mouseClicked(java.awt.event.MouseEvent evt) {
                            abrirVideo(workout.getVideo());
                        }
                    });

                    // Añadimos los elementos de texto al workoutPanel
                    workoutPanel.add(nivelLabel);
                    workoutPanel.add(videoLabel);
                    workoutPanel.add(ejerciciosLabel);
                    
                    JButton verEjerciciosButton = new JButton("Iniciar workout");
                    workoutPanel.add(verEjerciciosButton);

                    // Panel para los ejercicios, que se mostrará al hacer clic en el nombre del workout
                    JPanel ejerciciosPanel = new JPanel();
                    ejerciciosPanel.setLayout(new BoxLayout(ejerciciosPanel, BoxLayout.Y_AXIS));
                    ejerciciosPanel.setVisible(false);
                    workoutPanel.add(ejerciciosPanel);

                    // Acción para mostrar/ocultar ejercicios cuando se hace clic en el nombre del workout
                    workoutPanel.addMouseListener(new java.awt.event.MouseAdapter() {
                        public void mouseClicked(java.awt.event.MouseEvent evt) {
                            if (!ejerciciosPanel.isVisible()) {
                                try {
                                    // Obtener y mostrar los ejercicios del workout
                                    ArrayList<String> ejercicios = mObtenerEjerciciosIDs(workout.getNombre());
                                    ejerciciosPanel.removeAll(); // Limpiar el panel antes de agregar los ejercicios

                                    for (String ejercicio : ejercicios) {
                                        JLabel ejercicioLabel = new JLabel(ejercicio);
                                        ejerciciosPanel.add(ejercicioLabel);
                                    }

                                    ejerciciosPanel.revalidate();
                                    ejerciciosPanel.repaint();
                                } catch (IOException ex) {
                                    ex.printStackTrace();
                                    JOptionPane.showMessageDialog(panelWorkouts, "Error al obtener los ejercicios: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                                }
                            }
                            // Mostrar/ocultar el panel
                            ejerciciosPanel.setVisible(!ejerciciosPanel.isVisible());
                        }
                    });

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



    private void abrirVideo(String videoID) {
        try {
            String baseURL = "https://www.youtube.com/watch?v=";

            if (videoID == null || videoID.isEmpty()) {
                JOptionPane.showMessageDialog(null, "La URL del video no es válida.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            String videoURL = baseURL + videoID;
            
            URI uri = new URI(videoURL);
            if (Desktop.isDesktopSupported()) {
                Desktop desktop = Desktop.getDesktop();
                if (desktop.isSupported(Desktop.Action.BROWSE)) {
                    desktop.browse(uri);
                }
            }
        } catch (IOException | URISyntaxException e) {
            e.printStackTrace();
        }
    }


    // Métodos para obtener workouts y niveles

    public ArrayList<Workouts> mObtenerWorkouts() throws IOException {
    	Conexion conexion = new Conexion();
        Firestore db = conexion.conectar();
        ArrayList<Workouts> listaWorkouts = new ArrayList<>();

        try {
            CollectionReference workouts = db.collection("workouts");
            ApiFuture<QuerySnapshot> querySnapshot = workouts.get();
            List<QueryDocumentSnapshot> entrenos = querySnapshot.get().getDocuments();

            for (QueryDocumentSnapshot entreno : entrenos) {
                Workouts work = new Workouts();
                work.setNombre(entreno.getString("nombre"));
                work.setVideo(entreno.getString("video"));

                Long ejercicios = entreno.getLong("numEjercicios");
                Long nivel = entreno.getLong("nivel");

                if (ejercicios != null) {
                    work.setEjercicios(ejercicios.intValue());
                }
                if (nivel != null) {
                    work.setNivel(nivel.intValue());
                }

                listaWorkouts.add(work);
            }
        } catch (InterruptedException | ExecutionException e) {
            System.out.println("Error: Clase Workouts, metodo mObtenerWorkouts");
            e.printStackTrace();
        } finally {
            conexion.cerrar(db);
        }

        return listaWorkouts;
    }
    
    public ArrayList<String> mObtenerEjerciciosIDs(String workoutNombre) throws IOException {
    	Conexion conexion = new Conexion();
        ArrayList<String> listaIDs = new ArrayList<>();
        Firestore db = conexion.conectar();

        try {
            // Convertir el nombre del workout a minúsculas para obtener el ID correspondiente
            String workoutId = workoutNombre.toLowerCase(); 

            CollectionReference ejerciciosRef = db.collection("workouts").document(workoutId).collection("ejercicios");
            ApiFuture<QuerySnapshot> ejerciciosSnapshot = ejerciciosRef.get();
            List<QueryDocumentSnapshot> ejerciciosDocs = ejerciciosSnapshot.get().getDocuments();

            for (QueryDocumentSnapshot ejercicio : ejerciciosDocs) {
                listaIDs.add(ejercicio.getId());
            }
        } catch (InterruptedException | ExecutionException e) {
            System.out.println("Error: Clase Workouts, metodo mObtenerEjerciciosIDs");
            e.printStackTrace();
        } finally {
            conexion.cerrar(db);
        }

        return listaIDs;
    }



    public ArrayList<Integer> mObtenerNiveles() throws IOException {
    	Conexion conexion = new Conexion();
        ArrayList<Integer> listaNiveles = new ArrayList<>();
        Firestore db = conexion.conectar();

        try {
            CollectionReference workoutsRef = db.collection("workouts");
            ApiFuture<QuerySnapshot> querySnapshot = workoutsRef.get();
            List<QueryDocumentSnapshot> entrenos = querySnapshot.get().getDocuments();

            for (QueryDocumentSnapshot entreno : entrenos) {
                Long nivel = entreno.getLong("nivel");
                if (nivel != null && !listaNiveles.contains(nivel.intValue())) {
                    listaNiveles.add(nivel.intValue());
                }
            }
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        } finally {
            conexion.cerrar(db);
        }

        return listaNiveles;
    }
    
    public void verificarYCrearArchivo() {
        File carpeta = new File("Backups");
        if (!carpeta.exists()) {
            if (carpeta.mkdirs()) {
                System.out.println("Carpeta 'Backups' creada.");
            } else {
                System.out.println("No se pudo crear la carpeta 'Backups'.");
            }
        }

        File archivo = new File(carpeta, "usuarios.dat");
        try {
            if (archivo.createNewFile()) {
                System.out.println("Archivo creado: " + archivo.getName());
            } else {
                System.out.println("El archivo ya existe.");
            }
        } catch (IOException e) {
            System.out.println("Ocurrió un error al crear el archivo.");
            e.printStackTrace();
        }
    }
    

    
    
}