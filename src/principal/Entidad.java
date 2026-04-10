package principal;

import java.util.List;

public class Entidad {
	private String nombre;
	private int vidaMax;
	private int vidaActual;
	private int ataque;
	private int defensa;
	private int velocidad;
	private List<TipoGeneral> efectosActivos;
	
	public void recibirDanio(int cantidad) {
		
	}
	
	public void curar(int cantidad) {
		
	}
	
	public boolean estaVivo() {
		return false;
		
	}
	
	public void aplicarEfecto(TipoGeneral e) {
		
	}
	
	public boolean tieneEfecto(TipoGeneral e) {
		return false;
		
	}
	
	public void removerEfecto(TipoGeneral e) {
		
	}
	
	public void procesarEfectosDeEstado() {
		
	}
	
	public String getNombre() {
		return nombre;
		
	} 
	
	public int getVidaActual() {
		return vidaActual;
		
	}
	
	public int getVidaMax() {
		return vidaMax;
		
	}
	
	public int getAtaque() {
		return ataque;
		
	}
	
	public int getDefensa() {
		return defensa;
		
	}
	
	public int getVelocidad() {
		return velocidad;
		
	}

	public List<TipoGeneral> getEfectosActivos() {
		return efectosActivos;
	}

	public void setEfectosActivos(List<TipoGeneral> efectosActivos) {
		this.efectosActivos = efectosActivos;
	}
}
