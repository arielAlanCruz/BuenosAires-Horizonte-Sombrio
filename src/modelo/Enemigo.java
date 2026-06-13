package modelo;

import enums.TipoEnemigo;
import enums.TipoGeneral;

public class Enemigo extends Entidad {

	private final TipoEnemigo tipo;
	private final int nivel;
	private final int expOtorgada;

	public Enemigo(String nombre, TipoEnemigo tipo, int nivel, int vidaMax, int ataque, int defensa, int velocidad,
			int expOtorgada) {
		super(nombre, vidaMax, ataque, defensa, velocidad);
		this.tipo = tipo;
		this.nivel = nivel;
		this.expOtorgada = Math.max(0, expOtorgada);
	}

	public TipoEnemigo getTipo() {
		return tipo;
	}

	public int getNivel() {
		return nivel;
	}

	public int getExpOtorgada() {
		return expOtorgada;
	}

	// Comportamiento por defecto del enemigo común
	public TipoGeneral elegirAccion() {
		return TipoGeneral.ATACAR;
	}
}