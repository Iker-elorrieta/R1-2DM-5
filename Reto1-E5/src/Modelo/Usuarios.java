package Modelo;

import java.io.Serializable;

public class Usuarios implements Serializable {
    private static final long serialVersionUID = 1L;

    private String correo;
    private String contrasena;
    private String apellidos;
    private String fechaNacimiento;
    private int nivel;

    public Usuarios(String correo, String contrasena, String apellidos, String fechaNacimiento, int nivel) {
        this.correo = correo;
        this.contrasena = contrasena;
        this.apellidos = apellidos;
        this.fechaNacimiento = fechaNacimiento;
        this.nivel = nivel;
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

    public String getApellidos() {
        return apellidos;
    }

    public void setApellidos(String apellidos) {
        this.apellidos = apellidos;
    }

    public String getFechaNacimiento() {
        return fechaNacimiento;
    }

    public void setFechaNacimiento(String fechaNacimiento) {
        this.fechaNacimiento = fechaNacimiento;
    }

    public int getNivel() {
        return nivel;
    }

    public void setNivel(int nivel) {
        this.nivel = nivel;
    }

    @Override
    public String toString() {
        return "Usuario{" +
                "correo='" + correo + '\'' +
                ", contrasena='" + contrasena + '\'' +
                ", apellidos='" + apellidos + '\'' +
                ", fechaNacimiento='" + fechaNacimiento + '\'' +
                ", nivel=" + nivel +
                '}';
    }
}
