package Modelo;

public class Ejercicio {
    private String nombre;
    private String descripcion;
    private int descanso;
    private int tiempo;
    private int series;

    public Ejercicio(String nombre, String descripcion, int tiempo, int tiempoDesc, int series) {
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.tiempo = tiempo;
        this.descanso = tiempoDesc;
        this.series = series;
    }

    public String getNombre() {
    	return nombre;
    }
    
    public void setNombre(String nombre) {
    	this.nombre = nombre;
    }

    public String getDescripcion() {
    	return descripcion;
    }
    public void setDescripcion(String descripcion) {
    	this.descripcion = descripcion;
    }

    public int getTiempo() {
    	return tiempo;
    }
    public void setTiempo(int tiempo) {
    	this.tiempo = tiempo;
    }

    public int getDescanso() {
    	return descanso;
    }
    public void setDescanso(int tiempo) {
    	this.descanso = tiempo;
    }

    public int getSeries() {
    	return series;
    }
    public void setSeries(int series) {
    	this.series = series;
    }

	public Ejercicio(String nombre, String descripcion) {
		super();
		this.nombre = nombre;
		this.descripcion = descripcion;
	}
}
