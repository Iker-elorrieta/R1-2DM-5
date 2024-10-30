package Modelo;

import java.io.IOException;
import java.io.Serializable; // Importa Serializable
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.CollectionReference;
import com.google.cloud.firestore.Firestore;
import com.google.cloud.firestore.QueryDocumentSnapshot;
import com.google.cloud.firestore.QuerySnapshot;

import Controlador.Metodos;


public class Workouts implements Serializable {
    private static final long serialVersionUID = 1L;
    
    private String video;
    private String nombre;
    private int ejercicios;
    private int nivel;

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
        Metodos metodos = new Metodos();
        Firestore db = metodos.conectar();
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
            metodos.cerrar(db);
        }

        return listaWorkouts;
    }

    public ArrayList<Integer> mObtenerNiveles() throws IOException {
        ArrayList<Integer> listaNiveles = new ArrayList<>();
        Metodos metodos = new Metodos();
        Firestore db = metodos.conectar();

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
            System.out.println("Error: Clase Workouts, metodo mObtenerNiveles");
            e.printStackTrace();
        } finally {
            metodos.cerrar(db);
        }

        return listaNiveles;
    }

}
