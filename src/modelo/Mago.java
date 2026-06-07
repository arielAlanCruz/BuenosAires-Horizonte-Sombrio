package modelo;

import enums.TipoPersonaje;

/**
 * Mago: más mana y habilidad de daño.
 */
public class Mago extends Personaje {

	public Mago(String nombre) {
		super(nombre, TipoPersonaje.MAGO, 80, // vida
				14, // ataque
				6, // defensa
				12, // velocidad
				80 // mana
		);

		agregarHabilidad(new Habilidad("Rayo", "Daño mágico concentrado.", 15, 1.8, 0, false));
	}

	@Override
	public void evolucionar() {
		setNombre(getNombre() + " (Hechicero)");
		setAtaque(getAtaque() + 5);
		setDefensa(getDefensa() + 1);
		setManaMax(getManaMax() + 15);
	}
}