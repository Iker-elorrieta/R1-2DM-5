package Modelo;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

import com.google.cloud.Timestamp;
import com.google.cloud.firestore.CollectionReference;
import com.google.cloud.firestore.DocumentReference;
import com.google.cloud.firestore.DocumentSnapshot;
import com.google.cloud.firestore.Firestore;
import com.google.cloud.firestore.QueryDocumentSnapshot;

import Conexion.Conexion;
import Modelo.Historial;

public class Historial implements Serializable{
	private static final long serialVersionUID = 1L;

	// Atributos
	// nombre del historico sera el nombre del usuario, nivel y tiempo estimado
	private Workouts workout;
	private double tiempoRealizacion; // no usamos para calcular hace falta que sea double?
	private Date fecha;
	private double porcentajeCompletado;

	// Nombres de campos en Firestore
	private static final String COLLECTION_NAME = "historico";
	private static final String FIELD_WORKOUT = "Workout";

	private static final String FIELD_TIEMPO_REALIZACION = "tiempoRealizacion";
	private static final String FIELD_FECHA = "fecha";
	private static final String FIELD_PORCENTAJE_COMPLETADO = "porcentajeCompletado";

	// Constructores

	public Historial(Workouts workout, Date fecha, double porcentajeCompletado, String tiempoRealizacion) {
		this.workout = workout;
		this.fecha = fecha;
		this.porcentajeCompletado = porcentajeCompletado;
		this.tiempoRealizacion = Double.parseDouble(tiempoRealizacion.split(":")[0]) * 60
				+ Double.parseDouble(tiempoRealizacion.split(":")[1]);
	}

	// Constructor adicional que coincide con la firma usada en guardarEnHistorial
	public Historial(String workoutName, int totalEjercicios, int tiempoTotalInicial, double tiempoRealizado, Date fecha, int porcentajeCompletado) {
	    this.workout = new Workouts(); // Creamos un objeto Workouts con el nombre
	    this.workout.setNombre(workoutName);
	    this.workout.setTiempoTotal(tiempoTotalInicial);
	    this.fecha = fecha;
	    this.porcentajeCompletado = porcentajeCompletado;
	    this.tiempoRealizacion = tiempoRealizado;
	}

	// Métodos Getters y Setters


	public Historial() {
		// TODO Auto-generated constructor stub
	}

	public double getTiempoRealizacion() {
		return tiempoRealizacion;
	}

	public Workouts getWorkout() {
		return workout;
	}

	public void setWorkout(Workouts workout) {
		this.workout = workout;
	}

	public void setTiempoRealizacion(double tiempoRealizacion) {
		this.tiempoRealizacion = tiempoRealizacion;
	}

	public Date getFecha() {
		return fecha;
	}

	public void setFecha(Date fecha) {
		this.fecha = fecha;
	}

	public double getPorcentajeCompletado() {
		return porcentajeCompletado;
	}

	public void setPorcentajeCompletado(double porcentajeCompletado) {
		this.porcentajeCompletado = porcentajeCompletado;
	}

	public void mIngresarHistorico(String emailUsuario, Workouts workout) throws Exception {
	    Firestore co = null;
	    try {
	        Conexion conexion = new Conexion();
	        co = conexion.conectar();
	        DocumentReference historialCo = co.collection(new Usuarios().getUsuarioscol()).document(emailUsuario);
	        CollectionReference coleccionHistorico = historialCo.collection(COLLECTION_NAME);

	        // Crear el mapa de datos a guardar
	        Map<String, Object> historialData = new HashMap<>();
	        historialData.put(FIELD_TIEMPO_REALIZACION, this.tiempoRealizacion);
	        historialData.put(FIELD_FECHA, this.fecha);
	        historialData.put(FIELD_PORCENTAJE_COMPLETADO, this.porcentajeCompletado);
	        historialData.put(FIELD_WORKOUT, workout.mObtenerWorkoutByID(workout.getNombre()));

	        // Guardar en la colección de históricos
	        coleccionHistorico.document().set(historialData);

	        co.close();
	    } catch (IOException | InterruptedException | ExecutionException e) {
	        e.printStackTrace();
	    }
	}

	// CRUD: obtenerHistorico
	public ArrayList<Historial> mObtenerHistorico(String emailUsuario) throws Exception {
	    Firestore co = null;
	    ArrayList<Historial> listaHistorial = new ArrayList<>();
	    try {
	        Conexion conexion = new Conexion();
	        co = conexion.conectar();
	        DocumentReference usuarioDoc = co.collection(new Usuarios().getUsuarioscol()).document(emailUsuario);
	        CollectionReference coleccionHistorico = usuarioDoc.collection(COLLECTION_NAME);
	        List<QueryDocumentSnapshot> documentosHistorico = coleccionHistorico.get().get().getDocuments();

	        for (QueryDocumentSnapshot doc : documentosHistorico) {
	            // Obtener el nombre del workout (puede ser DocumentReference o String)
	            Object workoutField = doc.get(FIELD_WORKOUT);
	            String workoutName = null;

	            if (workoutField instanceof DocumentReference) {
	                workoutName = ((DocumentReference) workoutField).getId();
	            } else if (workoutField instanceof String) {
	                workoutName = (String) workoutField;
	            }

	            double tiempoRealizacion = doc.getDouble(FIELD_TIEMPO_REALIZACION);
	            Date fecha = doc.getDate(FIELD_FECHA);
	            double porcentajeCompletado = doc.getDouble(FIELD_PORCENTAJE_COMPLETADO);

	            Workouts workout = new Workouts();
	            workout.setNombre(workoutName);

	            Historial historial = new Historial(workout, fecha, porcentajeCompletado, 
	                String.format("%02d:%02d", (int)(tiempoRealizacion / 60), (int)(tiempoRealizacion % 60)));

	            listaHistorial.add(historial);
	        }
	        co.close();
	    } catch (IOException | InterruptedException | ExecutionException e) {
	        e.printStackTrace();
	    }
	    return listaHistorial;
	}



	public static Date obtenerFechaDate(DocumentSnapshot documentSnapshot, String fieldName) {
		Timestamp timestamp = documentSnapshot.getTimestamp(fieldName);
		return (timestamp != null) ? timestamp.toDate() : null;
	}
}
