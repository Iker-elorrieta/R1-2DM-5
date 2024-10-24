package Conexion;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.firestore.Firestore;
import com.google.cloud.firestore.FirestoreOptions;

public class Conexion {

	private static String nombreJSON = "gymapp.json";
	private static String projectID = "grupo5-gymapp";

	public Firestore conectar() throws IOException {
		FileInputStream serviceAccount;
		Firestore fs = null;
		try {
			serviceAccount = new FileInputStream(nombreJSON);

			FirestoreOptions firestoreOptions = FirestoreOptions.getDefaultInstance().toBuilder()
					.setProjectId(projectID).setCredentials(GoogleCredentials.fromStream(serviceAccount)).build();
			fs = firestoreOptions.getService();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return fs;
	}
	
	public void cerrar(Firestore fs) {
	    if (fs != null) {
	        try {
				fs.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
	    }
	}



}
