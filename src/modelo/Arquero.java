package modelo;

import enums.TipoPersonaje;

/**
 * Arquero: buena velocidad para el orden inicial, habilidad de daño eficiente.
 */
public class Arquero extends Personaje {

	public Arquero(String nombre) {
		super(nombre, TipoPersonaje.ARQUERO, 90, // vida
				16, // ataque
				7, // defensa
				16, // velocidad
				40 // mana
				);

		agregarHabilidad(new Habilidad("Flecha Precisa", "Ataque certero de alto daño.", 12, 1.7, 0, false));
	}

	@Override
	public void evolucionar() {
		setNombre(getNombre() + " (Francotirador)");
		setAtaque(getAtaque() + 4);
		setVelocidad(getVelocidad() + 2);
	}
}