package Modelo;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.ArrayList;

import Controlador.Metodos;

public class CrearBackups {

	public static void main(String[] args) throws IOException {
		Metodos metodos = new Metodos();
		metodos.verificarYCrearArchivo();
		escribirUsuariosEnArchivo(new Usuarios().bajarTodosLosUsuarios());
		escribirWorkoutsEnArchivo(new Workouts().mObtenerWorkouts());
	}
	
	private static void escribirUsuariosEnArchivo(ArrayList<Usuarios> usuarios) {
		try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream("Backups/usuarios.dat"))) {
			for(Usuarios usu: usuarios) {
				oos.writeObject(usu);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	private static void escribirWorkoutsEnArchivo(ArrayList<Workouts> workouts) {
		try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream("Backups/workouts.dat"))) {
			oos.writeObject(workouts);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}

