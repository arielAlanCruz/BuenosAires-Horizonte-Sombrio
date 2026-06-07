package modelo;

import enums.TipoPersonaje;

/**
 * Curandera: Especialista en el soporte y la restauración de salud del equipo.
 */
public class Curandera extends Personaje {

	public Curandera(String nombre) {
		super(nombre, TipoPersonaje.CURANDERA, 85, 12, 8, 10, 90);

		// Inicializa Habilidad de curación: costeMana=18, multDanio=0.0, cura=35,
		// aplicaAturdido=false
		agregarHabilidad(new Habilidad("Sanar", "Restaura la salud de un compañero.", 18, 0.0, 35, false));
	}

	@Override
	public void evolucionar() {
		setNombre(getNombre() + " (Matriarca)");
		setDefensa(getDefensa() + 2);
		setManaMax(getManaMax() + 10);
	}
}