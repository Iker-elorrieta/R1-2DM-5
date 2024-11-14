package Modelo;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.CollectionReference;
import com.google.cloud.firestore.DocumentReference;
import com.google.cloud.firestore.Firestore;
import com.google.cloud.firestore.QueryDocumentSnapshot;
import com.google.cloud.firestore.QuerySnapshot;

import Conexion.Conexion;


public class Workouts implements Serializable {
    private static final long serialVersionUID = 1L;
    
    private static final String WorkoutsCol = "workouts";
    private static final String Nombre = "nombre";
    private static final String Video = "video";
    private static final String EjerciciosNum = "numEjercicios";
    private static final String Nivel = "nivel";
    private String video;
    private String nombre;
    private int ejercicios;
    private int nivel;
    private int tiempoTotal;

    public Workouts() {
    }

    public Workouts(String video, String nombre, int ejercicios, int nivel) {
        this.video = video;
        this.nombre = nombre;
        this.ejercicios = ejercicios;
        this.nivel = nivel;
    }

    public String getVideo() {
        return video;
    }

    public void setVideo(String video) {
        this.video = video;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public int getEjercicios() {
        return ejercicios;
    }

    public void setEjercicios(int ejercicios) {
        this.ejercicios = ejercicios;
    }

    public int getNivel() {
        return nivel;
    }

    public void setNivel(int nivel) {
        this.nivel = nivel;
    }

    public ArrayList<Workouts> mObtenerWorkouts() throws IOException {
        Conexion conexion = new Conexion();
        Firestore db = conexion.conectar();
        ArrayList<Workouts> listaWorkouts = new ArrayList<>();

        try {
            CollectionReference workouts = db.collection(WorkoutsCol);
            ApiFuture<QuerySnapshot> querySnapshot = workouts.get();
            List<QueryDocumentSnapshot> entrenos = querySnapshot.get().getDocuments();

            for (QueryDocumentSnapshot entreno : entrenos) {
                Workouts work = new Workouts();
                work.setNombre(entreno.getString(Nombre));
                work.setVideo(entreno.getString(Video));
                
                Long ejercicios = entreno.getLong(EjerciciosNum);
                Long nivel = entreno.getLong(Nivel);
                
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
	
	public DocumentReference mObtenerWorkoutByID(String id) {
		Firestore co = null;
		DocumentReference ruta = null;
		try {
			Conexion conexion = new Conexion();
			co = conexion.conectar();
			ruta = co.collection(WorkoutsCol).document(id);
			System.out.println(ruta);

			co.close();

		} catch (InterruptedException | ExecutionException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}

		return ruta;
	}

    public ArrayList<Integer> mObtenerNiveles() throws IOException {
        ArrayList<Integer> listaNiveles = new ArrayList<>();
        Conexion conexion = new Conexion();
        Firestore db = conexion.conectar();

        try {
            CollectionReference workoutsRef = db.collection(WorkoutsCol);
            ApiFuture<QuerySnapshot> querySnapshot = workoutsRef.get();
            List<QueryDocumentSnapshot> entrenos = querySnapshot.get().getDocuments();

            for (QueryDocumentSnapshot entreno : entrenos) {
                Long nivel = entreno.getLong(Nivel);
                if (nivel != null && !listaNiveles.contains(nivel.intValue())) {
                    listaNiveles.add(nivel.intValue());
                }
            }
        } catch (InterruptedException | ExecutionException e) {
            System.out.println("Error: Clase Workouts, metodo mObtenerNiveles");
            e.printStackTrace();
        } finally {
            conexion.cerrar(db);
        }

        return listaNiveles;
    }

	public int getTiempoTotal() {
		return tiempoTotal;
	}

	public void setTiempoTotal(int tiempoTotal) {
		this.tiempoTotal = tiempoTotal;
	}

}
