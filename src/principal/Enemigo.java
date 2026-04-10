package principal;

import java.util.List;

public class Enemigo extends Entidad {
	private TipoEnemigo tipo;
	private int nivel;
	private int expOtorgada;
	private List<Item> lootPosible;
	private int faseActual;
	private boolean esJefe;
	
	public Enemigo crear(TipoEnemigo tipo, int nivel) {
		
	}
	
	public TipoGeneral elegirAccion(List<Personaje> party) {
		
	}
	
	public void cambiarFase() {
		
	}
	
	public List<Item> soltarLoot(){
		
	}
	
	public int getExpOtorgada() {
		
	}
	
	public TipoEnemigo getTipo() {
		
	}
	
	public boolean esJefe() {
		
	}
	
	public int getFaseActual() {
		
	}
}
