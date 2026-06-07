package modelo;

import enums.TipoEnemigo;
import enums.TipoGeneral;

public class Enemigo extends Entidad {

	private final TipoEnemigo tipo;
	private final int nivel;
	private final int expOtorgada;
	private final boolean jefe;

	public Enemigo(String nombre, TipoEnemigo tipo, int nivel, int vidaMax, int ataque, int defensa, int velocidad,
			int expOtorgada, boolean esJefe) {
		super(nombre, vidaMax, ataque, defensa, velocidad);
		this.tipo = tipo;
		this.nivel = nivel;
		this.expOtorgada = Math.max(0, expOtorgada);
		this.jefe = esJefe;
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

	public boolean isJefe() {
		return jefe;
	}

	/**
	 * IA de Combate:
	 * - Enemigos comunes: Atacan siempre físicamente.
	 * - Jefes: Tienen 40% de probabilidad de aturdir y 60% de atacar para ser
	 * equilibrados y desafiantes.
	 */
	public TipoGeneral elegirAccion() {
		if (jefe) {
			// Genera un número aleatorio entre 0.0 y 1.0
			if (Math.random() < 0.40) {
				return TipoGeneral.HABILIDAD; // Aturde a un héroe
			} else {
				return TipoGeneral.ATACAR; // Ataca físicamente con daño alto
			}
		} else {
			return TipoGeneral.ATACAR;
		}
	}
}