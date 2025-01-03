package Modelo;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ExecutionException;

import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.Firestore;
import com.google.cloud.firestore.QueryDocumentSnapshot;
import com.google.cloud.firestore.QuerySnapshot;

import Conexion.Conexion;


public class Usuarios implements Serializable {
    private static final long serialVersionUID = 1L;
    
    private static final String UsuariosCol = "usuarios";
    private static final String Correo = "correo";
    private static final String Contrasena = "contrasena";
    private static final String Apellido = "apellido";
    private static final String FechaNac = "fechaNacimiento";
    private static final String Nivel = "nivel";
    private static final String Nombre = "nombre";
    

    private String correo;
    private String contrasena;
    private String apellido;
    private Date fechaNacimiento;
    private Double nivel;
    private String nombre;
    
    public Usuarios(String correo, String contrasena, String apellido, Date fechaNacimiento, String nombre) {
        this.correo = correo;
        this.contrasena = contrasena;
        this.apellido = apellido;
        this.fechaNacimiento = fechaNacimiento;
        this.nivel = (double) 0;
        this.nombre = nombre;
    }

    public Usuarios(String correo, String contrasena, String apellido, Date fechaNacimiento, Double nivel, String nombre) {
        this.correo = correo;
        this.contrasena = contrasena;
        this.apellido = apellido;
        this.fechaNacimiento = fechaNacimiento;
        this.nivel = nivel;
        this.nombre = nombre;
    }

    public Usuarios() {
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public String getCorreo() {
        return correo;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }

    public String getContrasena() {
        return contrasena;
    }

    public void setContrasena(String contrasena) {
        this.contrasena = contrasena;
    }

    public String getApellido() {
        return apellido;
    }

    public void setApellido(String apellido) {
        this.apellido = apellido;
    }

    public Date getFechaNacimiento() {
        return fechaNacimiento;
    }

    public void setFechaNacimiento(Date fechaNacimiento) {
        this.fechaNacimiento = fechaNacimiento;
    }

    public Double getNivel() {
        return nivel;
    }

    public void setNivel(Double nivel) {
        this.nivel = nivel;
    }
    

    public String getUsuarioscol() {
		return UsuariosCol;
	}

	@Override
    public String toString() {
        return "Usuario{" +
                "correo='" + correo + '\'' +
                ", contrasena='" + contrasena + '\'' +
                ", apellido='" + apellido + '\'' +
                ", fechaNacimiento='" + fechaNacimiento + '\'' +
                ", nivel=" + nivel +
                '}';
    }
    
    
    public ArrayList<Usuarios> bajarTodosLosUsuarios() {
    	Conexion conexion = new Conexion();
		Firestore co = null;

		ArrayList<Usuarios> listaUsuarios = new ArrayList<Usuarios>();

		try {
			co = conexion.conectar();

			ApiFuture<QuerySnapshot> query = co.collection(UsuariosCol).get();

			QuerySnapshot querySnapshot = query.get();
			List<QueryDocumentSnapshot> usuariosFireBase = querySnapshot.getDocuments();
			for (QueryDocumentSnapshot usuarioFireBase : usuariosFireBase) {

				Usuarios usuario = new Usuarios(usuarioFireBase.getString(Correo),
						usuarioFireBase.getString(Contrasena), usuarioFireBase.getString(Apellido),
						usuarioFireBase.getDate(FechaNac), usuarioFireBase.getDouble(Nivel),
						usuarioFireBase.getString(Nombre));

				listaUsuarios.add(usuario);
			}
		} catch (InterruptedException | ExecutionException e) {
			System.out.println("Error: Clase Contacto, metodo mObtenerContactos");
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return listaUsuarios;
	}
}
