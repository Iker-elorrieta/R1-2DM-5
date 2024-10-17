package Vista;


import java.io.FileInputStream;
import java.util.List;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.firestore.Firestore;
import com.google.cloud.firestore.FirestoreOptions;
import com.google.cloud.firestore.QueryDocumentSnapshot;

public class LoginCodigo {

	private static final String credArchivo = "credentials.json";
	private static final String proyectoID = "grupo5-gymapp";
	
	private static final String usuariosColl = "usuarios";
	private static final String correoField = "correo";
	private static final String contraField = "contrasena";
	
	public static void main(String[] args) {
		final String correo = "admin2@gmail.com";
		final String contrasena = "cliente1234";
		
		try {
			// conexion con la base de datos
			FileInputStream serviceAccount = new FileInputStream(credArchivo);
			FirestoreOptions options = FirestoreOptions.getDefaultInstance().toBuilder()
					.setProjectId(proyectoID).setCredentials(GoogleCredentials.fromStream(serviceAccount)).build();
			Firestore db = options.getService();
			
			QueryDocumentSnapshot loginUser = null;
			System.out.println("login attempt on "+correo);
			
			// obtener coleccion de usuarios
			List<QueryDocumentSnapshot> usuarios = db.collection(usuariosColl).get().get().getDocuments();
			for (QueryDocumentSnapshot usuarioSnapshot : usuarios) {
				String queryMail = usuarioSnapshot.getString(correoField);
				String queryPass = usuarioSnapshot.getString(contraField);
				
				if (queryMail.contentEquals(correo) && queryPass.contentEquals(contrasena)) {
					loginUser = usuarioSnapshot;
					break;
				}
			}
			
			// comprobar resultado login
			if (loginUser != null) {
				System.out.println("login success");
			} else {
				System.out.println("login failed");
			}
		} catch (Exception e) { e.printStackTrace(); }

	}

}
