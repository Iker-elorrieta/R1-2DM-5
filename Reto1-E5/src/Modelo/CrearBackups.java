package Modelo;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.CollectionReference;
import com.google.cloud.firestore.Firestore;
import com.google.cloud.firestore.QueryDocumentSnapshot;
import com.google.cloud.firestore.QuerySnapshot;

import Conexion.Conexion;

public class CrearBackups {

    private static final String COLLECTION_NAME = "usuarios";

    public void verificarYCrearArchivo() {
        File carpeta = new File("Archivos");
        if (!carpeta.exists()) {
            if (carpeta.mkdirs()) {
                System.out.println("Carpeta 'Archivos' creada.");
            } else {
                System.out.println("No se pudo crear la carpeta 'Archivos'.");
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
    public void bajarDatosUsuarios() {
        Conexion conexion = new Conexion();
        Firestore firestore = null;

        try {
            firestore = conexion.conectar();
            CollectionReference collection = firestore.collection(COLLECTION_NAME);
            ApiFuture<QuerySnapshot> query = collection.get();
            List<QueryDocumentSnapshot> documentos = query.get().getDocuments();

            try (FileWriter writer = new FileWriter("Archivos/usuarios.dat", false)) {
                for (QueryDocumentSnapshot documento : documentos) {
                    String correo = documento.getString("correo");
                    String contrasena = documento.getString("contrasena");
                    if (correo != null && contrasena != null) {
                        writer.write(correo + ", " + contrasena + "\n");
                    }
                }
                System.out.println("Datos de usuarios guardados en usuarios.dat");
            } catch (IOException e) {
                System.out.println("Ocurrió un error al escribir en el archivo.");
                e.printStackTrace();
            }

        } catch (Exception e) {
            System.out.println("Error al conectar con Firestore o al obtener datos.");
            e.printStackTrace();
        } finally {
            conexion.cerrar(firestore);
        }
    }
}
