package Modelo;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.CollectionReference;
import com.google.cloud.firestore.Firestore;
import com.google.cloud.firestore.QueryDocumentSnapshot;
import com.google.cloud.firestore.QuerySnapshot;

import Conexion.Conexion;

public class Workouts {

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


    
    public ArrayList<String> mObtenerEjerciciosIDs() throws IOException {
        ArrayList<String> listaIDs = new ArrayList<>();
        Conexion conexion = new Conexion();
        Firestore db = conexion.conectar();

        try {
            String workoutId = this.nombre.toLowerCase(); // utilizo .toLowerCase porque cogia el nombre en mayusculas.
            
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
        ArrayList<Integer> listaNiveles = new ArrayList<>();
        Conexion conexion = new Conexion();
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
            System.out.println("Error: Clase Workouts, metodo mObtenerNiveles");
            e.printStackTrace();
        } finally {
            conexion.cerrar(db);
        }

        return listaNiveles;
    }



}
