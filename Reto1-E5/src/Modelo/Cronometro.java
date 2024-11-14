package Modelo;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Cronometro extends Thread {
	
	private boolean activo = false;
	private boolean iniciado = false;
	
	private int tiempo = 0;
	private ActionListener onUpdate;
	
	public Cronometro(ActionListener onUpdate) { this.onUpdate = onUpdate; }
	
	@Override
	public void run() {
		if (!iniciado) {
			tiempo = 0;
			iniciado = true;
			activo = true;
			
			int deci = 0;
			while (iniciado) {
				try {
					Cronometro.sleep(100);
				} catch (Exception e) { e.printStackTrace(); }
				
				if (activo) {
					deci++;
					if (deci >= 10) {
						deci = 0;
						tiempo++;
						onUpdate.actionPerformed(new ActionEvent(tiempo, ActionEvent.ACTION_PERFORMED, "eventoTiempo"));
					}
				}
			}
		}
	}
	
	public void pausar() {
		if (iniciado && activo) activo = false;
	}

	public void reanudar() {
		if (iniciado && !activo) activo = true;
	}
	
	public void detener() {
		if (iniciado) {
			activo = false;
			iniciado = false;
		}
	}
	
	public double obtenerTiempo() { return tiempo; }
}
