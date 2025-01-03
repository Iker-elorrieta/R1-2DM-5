package Controlador;

import java.awt.BorderLayout;
import java.awt.Desktop;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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
import javax.swing.Timer;

import com.google.api.core.ApiFuture;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.firestore.CollectionReference;
import com.google.cloud.firestore.DocumentReference;
import com.google.cloud.firestore.Firestore;
import com.google.cloud.firestore.FirestoreOptions;
import com.google.cloud.firestore.QueryDocumentSnapshot;
import com.google.cloud.firestore.QuerySnapshot;
import com.toedter.calendar.JDateChooser;

import Conexion.Conexion;
import Modelo.Cronometro;
import Modelo.Ejercicio;
import Modelo.Workouts;
import Vista.Ejercicios;
import Vista.Perfil;


public class Metodos {
	private static final String credArchivo = "gymapp.json";
    private static final String proyectoID = "grupo5-gymapp";
    private static final String usuariosColl = "usuarios";
    private static final String correoField = "correo";
    private static final String contraField = "contrasena";
	private Timer timerSerie, timerDesc;
	private Cronometro timerWorkout, timerEjer;

	public static DocumentReference usuarioReferencia;
	
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

    public void mostrarDatosWorkouts(String user, JComboBox<Object> nivelComboBox, JPanel panelWorkouts) {
        try {
            ArrayList<Workouts> listaWorkouts = mObtenerWorkouts();
            Object nivelSeleccionado = nivelComboBox.getSelectedItem();

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
                    JLabel videoLabel = new JLabel("Video: " + workout.getVideo());

                    ArrayList<Ejercicio> ejercicios = mObtenerEjerciciosDescripcion(workout.getNombre());
                    JLabel ejerciciosCountLabel = new JLabel("Número de Ejercicios: " + ejercicios.size());
                    workoutPanel.add(ejerciciosCountLabel);

                    StringBuilder nombresEjercicios = new StringBuilder("<html>");
                    for (Ejercicio ejercicio : ejercicios) {
                        nombresEjercicios.append(ejercicio.getNombre()).append("<br>");
                    }
                    nombresEjercicios.append("</html>");
                    JLabel ejerciciosNamesLabel = new JLabel(nombresEjercicios.toString());
                    workoutPanel.add(ejerciciosNamesLabel);

                    videoLabel.addMouseListener(new java.awt.event.MouseAdapter() {
                        public void mouseClicked(java.awt.event.MouseEvent evt) {
                            abrirVideo(workout.getVideo());
                        }
                    });

                    workoutPanel.add(nivelLabel);
                    workoutPanel.add(videoLabel);

                    JButton verEjerciciosButton = new JButton("Iniciar workout");
                    workoutPanel.add(verEjerciciosButton);
                    verEjerciciosButton.addActionListener(new ActionListener() {
                        public void actionPerformed(ActionEvent e) {
                            String nombreWorkout = workout.getNombre();
                            int tiempoTotalWorkout = workout.getTiempoTotal();
							
							// Crear el panel Ejercicios pasando el nombre del workout y la lista de ejercicios
							Ejercicios ej = new Ejercicios(user, nombreWorkout, ejercicios, tiempoTotalWorkout);
							ej.setSize(950, 500);
							ej.setLocation(0, 0);

							// Reemplazar el contenido del panel principal con el panel de ejercicios
							JPanel perfilPanel = (JPanel) panelWorkouts.getParent().getParent().getParent(); // Obtén el panel Perfil
							perfilPanel.removeAll();
							perfilPanel.setLayout(new BorderLayout());
							perfilPanel.add(ej, BorderLayout.CENTER);

							perfilPanel.revalidate();
							perfilPanel.repaint();
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
                Long tiempoTotal = entreno.getLong("tiempoTotal");

                if (ejercicios != null) {
                    work.setEjercicios(ejercicios.intValue());
                }
                if (nivel != null) {
                    work.setNivel(nivel.intValue());
                }
                if (tiempoTotal != null) {
                    work.setTiempoTotal(tiempoTotal.intValue());
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
    
    
    public ArrayList<Ejercicio> mObtenerEjerciciosDescripcion(String workoutNombre) throws IOException {
        Conexion conexion = new Conexion();
        ArrayList<Ejercicio> listaEjercicios = new ArrayList<>();
        Firestore db = conexion.conectar();

        try {
            String workoutId = workoutNombre.toLowerCase();

            CollectionReference ejerciciosRef = db.collection("workouts").document(workoutId).collection("ejercicios");
            ApiFuture<QuerySnapshot> ejerciciosSnapshot = ejerciciosRef.get();
            List<QueryDocumentSnapshot> ejerciciosDocs = ejerciciosSnapshot.get().getDocuments();

            for (QueryDocumentSnapshot ejercicioDoc : ejerciciosDocs) {
                String nombre = ejercicioDoc.getId();
                String descripcion = ejercicioDoc.getString("descripcion");
                
                Long tiempo = ejercicioDoc.getLong("duracion");
                int tiempoEjercicio = (tiempo != null) ? tiempo.intValue() : 0;

                Long descanso = ejercicioDoc.getLong("descanso");
                int tiempoDescanso = (descanso != null) ? descanso.intValue() : 0;

                CollectionReference seriesRef = ejercicioDoc.getReference().collection("series");
                ApiFuture<QuerySnapshot> seriesSnapshot = seriesRef.get();
                List<QueryDocumentSnapshot> seriesDocs = seriesSnapshot.get().getDocuments();

                int numSeries = seriesDocs.size();
                
                listaEjercicios.add(new Ejercicio(nombre, descripcion, tiempoEjercicio, tiempoDescanso, numSeries));
            }
        } catch (InterruptedException | ExecutionException e) {
            System.out.println("Error: Clase Workouts, metodo mObtenerEjerciciosDescripcion");
            e.printStackTrace();
        } finally {
            conexion.cerrar(db);
        }

        return listaEjercicios;
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
    

        

        public QueryDocumentSnapshot iniciarSesion(String correo, String contrasena, JLabel lblError) throws Exception {
            FileInputStream serviceAccount = new FileInputStream(credArchivo);
            FirestoreOptions options = FirestoreOptions.getDefaultInstance().toBuilder()
                    .setProjectId(proyectoID)
                    .setCredentials(GoogleCredentials.fromStream(serviceAccount))
                    .build();
            Firestore db = options.getService();
            
            Conexion conexion = new Conexion();
            boolean conectar = conexion.verificarConexion(db);
            if (!conectar) {
                System.out.println("Error en la conexión, utilizando datos desde el backup");
            }
            
            List<QueryDocumentSnapshot> usuarios = db.collection(usuariosColl).get().get().getDocuments();
            
            QueryDocumentSnapshot loginUser = null;
            for (QueryDocumentSnapshot usuarioSnapshot : usuarios) {
                String queryMail = usuarioSnapshot.getString(correoField);
                String queryPass = usuarioSnapshot.getString(contraField);

                if (queryMail.equals(correo) && queryPass.equals(contrasena)) {
                    loginUser = usuarioSnapshot;
                    break;
                } else if (!queryMail.equals(correo) && !queryPass.equals(contrasena)) {
                    lblError.setText("Introduce los datos correctamente");
                } else if (!queryMail.equals(correo)) {
                    lblError.setText("Mail no válido");
                } else if (!queryPass.equals(contrasena)) {
                    lblError.setText("Contraseña incorrecta");
                }
            }

            return loginUser;
        }

        
        public String actualizarUsuario(String nombre, String apellido, String mail, String contrasena, String repContrasena, JLabel lblError) {
            if (nombre.isEmpty() || apellido.isEmpty() || mail.isEmpty() || contrasena.isEmpty() || repContrasena.isEmpty()) {
                lblError.setText("Rellena todos los campos");
                return "Campos incompletos";
            }
            
            if (!validarEmailReg(mail)) {
                lblError.setText("Mail no válido");
                return "Email no válido";
            }

            if (!contraComprobarReg(contrasena, repContrasena)) {
                lblError.setText("Las contraseñas no coinciden");
                return "Contraseñas no coinciden";
            }

            try (FileInputStream serviceAccount = new FileInputStream("gymapp.json")) {
                FirestoreOptions firestoreOptions = FirestoreOptions.getDefaultInstance().toBuilder()
                        .setProjectId("grupo5-gymapp")
                        .setCredentials(GoogleCredentials.fromStream(serviceAccount))
                        .build();
                Firestore db = firestoreOptions.getService();

                Map<String, Object> usuarioData = new HashMap<>();
                usuarioData.put("nombre", nombre);
                usuarioData.put("apellido", apellido);
                usuarioData.put("contrasena", contrasena);
                usuarioData.put("correo", mail);

                db.collection("usuarios").document(mail).set(usuarioData);

                lblError.setText("Registro realizado correctamente");
                return "Registro exitoso";

            } catch (IOException e) {
                lblError.setText("Error al conectar con Firestore");
                e.printStackTrace();
                return "Error de conexión";
            }
        }

        public boolean validarEmailReg(String email) {
            return email.contains("@") && email.contains(".");
        }

        public boolean contraComprobarReg(String contrasena, String repContrasena) {
            return contrasena.equals(repContrasena);
        }
        
        
        public void configurarFechaNacimiento(JDateChooser fechaNacimientoCalendar) {
            Calendar ahoraMismo = Calendar.getInstance();
            int ano = ahoraMismo.get(Calendar.YEAR);
            int mes = ahoraMismo.get(Calendar.MONTH) + 1;
            int dia = ahoraMismo.get(Calendar.DATE);
            String maxString = ano + "-" + mes + "-" + dia;
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

            try {
                fechaNacimientoCalendar.setMaxSelectableDate(dateFormat.parse(maxString));
                fechaNacimientoCalendar.setDate(dateFormat.parse(maxString));
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        
        public String registrarUsuario(String nombre, String apellido, String usuario, String mail, String contrasena,
                Date fechaNacimiento, boolean esCliente, JLabel lblError) {
        	try (FileInputStream serviceAccount = new FileInputStream("gymapp.json")) {
        		FirestoreOptions firestoreOptions = FirestoreOptions.getDefaultInstance().toBuilder()
        				.setProjectId("grupo5-gymapp")
        				.setCredentials(GoogleCredentials.fromStream(serviceAccount))
        				.build();
        		Firestore db = firestoreOptions.getService();
        		CollectionReference usuarios = db.collection("usuarios");
        		
        		Map<String, Object> usuarioData = new HashMap<>();
        		usuarioData.put("nombre", nombre);
        		usuarioData.put("apellido", apellido);
        		usuarioData.put("contrasena", contrasena);
        		usuarioData.put("correo", mail);
        		usuarioData.put("fecha de nacimiento", fechaNacimiento);
        		if (esCliente) {
        			usuarioData.put("nivel", 0);
        			}

        		usuarios.document(usuario).set(usuarioData);
        		lblError.setText("Registro realizado correctamente");
        		return "Registro realizado correctamente";
        		} catch (IOException e) {
        			e.printStackTrace();
        		lblError.setText("Error al conectar con Firestore");
        		return "Error al conectar con Firestore";
        		}
        	}
        

        public static void guardarDatosWorkout(double porcentaje, double timepo, String workoutName) {
        	try (FileInputStream serviceAccount = new FileInputStream("gymapp.json")) {
        		FirestoreOptions firestoreOptions = FirestoreOptions.getDefaultInstance().toBuilder()
        				.setProjectId("grupo5-gymapp")
        				.setCredentials(GoogleCredentials.fromStream(serviceAccount))
        				.build();
        		Firestore db = firestoreOptions.getService();

        		DocumentReference workout = db.collection("workouts").document(workoutName);
        		DocumentReference workoutRef = usuarioReferencia.collection("workouts").document(workoutName);
        		
    			Map<String, Object> data = new HashMap<String, Object>();
    			data.put("completados", porcentaje);
    			data.put("fecha", new Date());
    			data.put("tiempo total", timepo);
    			data.put("workout", workout);
    			workoutRef.set(data);
    		} catch (IOException e) { e.printStackTrace(); }
        }

        int serieIndex = 0;
        public void reiniciarSerieIndex() {
        	serieIndex = 0;
        }
        
        public void iniciarCrono(Ejercicios ejerciciosPanel, int timerIndex) {
            // Verificamos si el timer ya está configurado; si no, lo configuramos
        	if (timerIndex == 0 || timerIndex == 1) {
        		if (timerIndex == 1) serieIndex = 0;
        		if ((timerIndex == 0 ? timerWorkout : timerEjer) == null) {
        			Cronometro crono = new Cronometro( timerIndex == 0 ?
        				new ActionListener() {
	                        @Override
	                        public void actionPerformed(ActionEvent e) {
	                        	ejerciciosPanel.establecerTiempoWorkout(Double.valueOf((int) e.getSource()));
	                        	ejerciciosPanel.updateWorkoutTimerLabel();
	                        }
                		} :
        				new ActionListener() {
	                        @Override
	                        public void actionPerformed(ActionEvent e) {
	                        	ejerciciosPanel.establecerTiempoEjercicio(Double.valueOf((int) e.getSource()));
	                        	ejerciciosPanel.updateEjerTimerLabel();
	                        }
                    	}
        			);
        			if (timerIndex == 0) timerWorkout = crono;
        			else timerEjer = crono;
        		}
        		(timerIndex == 0 ? timerWorkout : timerEjer).start();
        	} else {
                if ((timerIndex == 2 ? timerSerie : timerDesc) == null) {
                    Timer timer = new Timer(1000, new ActionListener() {
                        @Override
                        public void actionPerformed(ActionEvent e) {
                            if (timerIndex == 2 ? ejerciciosPanel.getTiempoEjer() > 0 : ejerciciosPanel.getTiempoDesc() > 0) {
                                switch (timerIndex) {
    	                            case 2:
    	                            	ejerciciosPanel.decrementarTiempoEjer();
    	                            	ejerciciosPanel.updateEjerTimerLabel();
    	                            	break;
    	                            case 3:
    	                            	ejerciciosPanel.decrementarTiempoDesc();
    	                            	ejerciciosPanel.updateDescTimerLabel();
    	                            	break;
                                }
                            } else {
                                detenerCrono(timerIndex); // Detenemos el cronómetro cuando llegue a 0
                                if (timerIndex == 2) {
                                	Ejercicio current = ejerciciosPanel.getCurrentEjericio();
                                	System.out.println("current: "+serieIndex);
                                	System.out.println(current.getSeries() - 1);
                                	if (serieIndex < current.getSeries() - 1) {
                                        JOptionPane.showMessageDialog(null, "¡Tiempo completado para la serie!", "Tiempo finalizado", JOptionPane.INFORMATION_MESSAGE);
                                    	iniciarCrono(ejerciciosPanel, 3);
                                	} else {
                                        JOptionPane.showMessageDialog(null, "¡Se a finalizado todas las series!", "Ejercicio finalizado", JOptionPane.INFORMATION_MESSAGE);
                                		if (ejerciciosPanel.esUltimoEjercicio()) {
	                                        finalizarWorkout(
	                                                ejerciciosPanel.getUser(),
	                                                ejerciciosPanel.getWorkoutName(),
	                                                ejerciciosPanel.getEjercicios(),
	                                                ejerciciosPanel.getEjerciciosRealizados(),
	                                                ejerciciosPanel.getTiempoRestante(),
	                                                ejerciciosPanel.getWorkoutRealizado(),
	                                                ejerciciosPanel
	                                            );
                                    	}
                                	}
                                } else if (timerIndex == 3) {
                                    JOptionPane.showMessageDialog(null, "¡El descanso a finalizado!", "Tiempo finalizado", JOptionPane.INFORMATION_MESSAGE);
                                	serieIndex++;
                                	ejerciciosPanel.reestablecerTiempoSerie();
                                	ejerciciosPanel.reestablecerTiempoDescanso();
	                            	ejerciciosPanel.updateDescTimerLabel();
                                	iniciarCrono(ejerciciosPanel, 2);
                                }
                            }
                        }
                    });
                    if (timerIndex == 2) timerSerie = timer;
                    else timerDesc = timer;
                }
                if (timerIndex == 2) timerSerie.start();
                else timerDesc.start();
        	}
            ejerciciosPanel.setTimerRunning(true);
        }

        public void pausarCrono(int timerIndex) {
        	switch (timerIndex) {
	        	case 0:
	        		timerWorkout.pausar();
	        		break;
	        	case 1:
	        		timerEjer.pausar();
	        		break;
	        	case 2:
	        		if (timerSerie != null && timerSerie.isRunning()) {
	        			timerSerie.stop();
	                }
	        		break;
	        	case 3:
	        		if (timerDesc != null && timerDesc.isRunning()) {
	        			timerDesc.stop();
	                }
	        		break;
        	}
        }

        public void reanudarCrono(Ejercicios ejerciciosPanel, int timerIndex) {
        	switch (timerIndex) {
	        	case 0:
	        		timerWorkout.reanudar();
	                ejerciciosPanel.setTimerRunning(true);
	        		break;
	        	case 1:
	        		timerEjer.reanudar();
	                ejerciciosPanel.setTimerRunning(true);
	        		break;
	        	case 2:
	        		if (timerSerie != null && !timerSerie.isRunning()) {
	        			timerSerie.start();
	                    ejerciciosPanel.setTimerRunning(true);
	                }
	        		break;
	        	case 3:
	        		if (timerDesc != null && !timerDesc.isRunning()) {
	        			timerDesc.start();
	                    ejerciciosPanel.setTimerRunning(true);
	                }
	        		break;
        	}
        }

        public void detenerCrono(int timerIndex) {
        	switch (timerIndex) {
	        	case 0:
	        		if (timerWorkout != null) {
	        			timerWorkout.detener();
	        		}
	        		break;
	        	case 1:
	        		if (timerEjer != null) {
	        			timerEjer.detener();
	        			timerEjer = null;
	        		}
	        		break;
	        	case 2:
	                if (timerSerie != null) {
	                	timerSerie.stop();
	                    timerSerie = null;
	                }
	        		break;
	        	case 3:
	                if (timerDesc != null) {
	                	timerDesc.stop();
	                	timerDesc = null;
	                }
	        		break;
        	}
        }
        
        public void finalizarWorkout(String user, String workoutName, List<Ejercicio> ejercicios, 
                List<String> ejerciciosRealizados, double tiempoInicial, 
                List<Map<String, Object>> workoutRealizado, JPanel panelEjercicios) {

        if (timerWorkout != null) timerWorkout.detener();
        if (timerEjer != null) timerEjer.detener();

        // Obtener currentIndex utilizando el getter de Ejercicios
        int currentIndex = panelEjercicios instanceof Ejercicios ? ((Ejercicios) panelEjercicios).getCurrentIndex() : 0;

        // Asegurarse de que todos los ejercicios realizados estén añadidos
        if (!ejerciciosRealizados.contains(ejercicios.get(currentIndex).getNombre())) {
            ejerciciosRealizados.add(ejercicios.get(currentIndex).getNombre());
        }

        double tiempoTotal = timerWorkout.obtenerTiempo();

        // Crear el mapa con los datos del workout
        Map<String, Object> workoutData = new HashMap<>();
        workoutData.put("nombreWorkout", workoutName);
        workoutData.put("ejerciciosRealizados", new ArrayList<>(ejerciciosRealizados));
        workoutData.put("tiempoTotal", tiempoTotal);

        // Añadir la información del workout al listado de workouts realizados
        workoutRealizado.add(workoutData);
        System.out.println(workoutRealizado);

        // Calcular el porcentaje completado
        int ejerciciosCompletados = ejerciciosRealizados.size();
        int totalEjercicios = ejercicios.size();
        double porcentajeCompletado = ((double) ejerciciosCompletados / totalEjercicios) * 100;

        // Guardar datos del workout realizado
        guardarDatosWorkout(porcentajeCompletado, tiempoTotal, workoutName);

        // Determinar el mensaje motivacional
        String mensajeMotivacional = obtenerMensajeMotivacional(porcentajeCompletado);

        String resumen = String.format("Tiempo total: %02d:%02d\nEjercicios completados: %d de %d\nPorcentaje completado: %.2f%%\n\n%s",
                (int) tiempoTotal / 60, (int) tiempoTotal % 60, ejerciciosCompletados, totalEjercicios, porcentajeCompletado, mensajeMotivacional
        );

        JOptionPane.showMessageDialog(null, resumen, "Resumen del Workout", JOptionPane.INFORMATION_MESSAGE);

        Perfil p = new Perfil(user);
        p.setSize(950, 500);
        p.setLocation(0, 0);

        panelEjercicios.removeAll();
        panelEjercicios.add(p, BorderLayout.CENTER);
        panelEjercicios.revalidate();
        panelEjercicios.repaint();
    }

        private String obtenerMensajeMotivacional(double porcentajeCompletado) {
            if (porcentajeCompletado == 100) {
                return "¡Felicidades! Completaste todo el workout. ¡Sigue así!";
            } else if (porcentajeCompletado == 50) {
                return "¡Buen trabajo! Has completado la mitad del workout.";
            } else if (porcentajeCompletado > 50) {
                return "¡Bien hecho! Has completado más de la mitad del workout.";
            } else {
                return "No te desanimes, a la próxima lo conseguirás";
            }
        }

}