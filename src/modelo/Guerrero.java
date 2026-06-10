package modelo;

import enums.TipoPersonaje;

/**
 * Guerrero: Personaje de alta resistencia física y daño contundente.
 */
public class Guerrero extends Personaje {

	public Guerrero(String nombre) {
		super(nombre, TipoPersonaje.GUERRERO, 120, 18, 10, 8, 30);

		// Inicializa Habilidad de daño: costeMana=10, multDanio=1.6, cura=0,
		// aplicaAturdido=false
		agregarHabilidad(new Habilidad("Faconazo", "Causa daño físico incrementado.", 10, 1.6, 0, false));

	}

	@Override
	public void evolucionar() {
		setNombre(getNombre() + " (Gaucho Legendario)");
		setVidaMax(getVidaMax() + 20);
		setAtaque(getAtaque() + 4);
		setDefensa(getDefensa() + 2);
	}
}