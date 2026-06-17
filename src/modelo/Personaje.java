package modelo;

import enums.TipoPersonaje;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public abstract class Personaje extends Entidad {

	private int nivel;
	private int experiencia;
	private int manaActual;
	private int manaMax;

	private final TipoPersonaje clase;
	private final Equipamiento equipamiento;
	private final List<Habilidad> habilidades = new ArrayList<>();
	private static final int NIVEL_EVOLUCION = 2;

	protected Personaje(String nombre, TipoPersonaje clase, int vidaMax, int ataque, int defensa, int velocidad,
			int manaMax) {
		super(nombre, vidaMax, ataque, defensa, velocidad);
		this.clase = clase;
		this.nivel = 1;
		this.experiencia = 0;
		this.manaMax = Math.max(0, manaMax);
		this.manaActual = this.manaMax;
		this.equipamiento = new Equipamiento();
	}

	public int getNivel() {
		return nivel;
	}

	public int getExperiencia() {
		return experiencia;
	}

	public int getManaActual() {
		return manaActual;
	}

	public int getManaMax() {
		return manaMax;
	}

	public TipoPersonaje getClase() {
		return clase;
	}

	public Equipamiento getEquipamiento() {
		return equipamiento;
	}

	public List<Habilidad> getHabilidades() {
		return Collections.unmodifiableList(habilidades);
	}

	protected void agregarHabilidad(Habilidad h) {
		if (h != null)
			habilidades.add(h);
	}

	public boolean tieneMana(int cantidad) {
		return manaActual >= Math.max(0, cantidad);
		// si manaActual es 50 y cantidad es -10, devuelve true
		// (no se penaliza por habilidades que recuperan mana)
	}

	public void usarMana(int cantidad) {
		int c = Math.max(0, cantidad);
		manaActual = Math.max(0, manaActual - c);
	}// Si manaActual es 50 y cantidad es -10, no se resta nada
		// (no se penaliza por habilidades que recuperan mana)

	public void recuperarMana(int cantidad) {
		int c = Math.max(0, cantidad);
		manaActual = Math.min(manaMax, manaActual + c);
	}// Si manaActual es 50, manaMax es 100 y cantidad es -10, no se suma nada
		// (no se penaliza por habilidades que consumen mana)

	protected void setManaMax(int nuevoMax) {
		this.manaMax = Math.max(0, nuevoMax);
		if (manaActual > manaMax)
			manaActual = manaMax;
	}// Si el nuevo manaMax es menor que el manaActual,
		// se ajusta manaActual al nuevo máximo

	public void ganarExperiencia(int cantidad) {
		if (cantidad <= 0)
			return;
		experiencia += cantidad;

		while (experiencia >= expParaSiguienteNivel()) {
			experiencia -= expParaSiguienteNivel();
			subirNivel();
		}
	}

	protected int expParaSiguienteNivel() {
		return 100;
	}

	protected void subirNivel() {
		nivel++;
		setVidaMax(getVidaMax() + 10);
		setAtaque(getAtaque() + 2);
		setDefensa(getDefensa() + 1);
		setManaMax(getManaMax() + 5);

		curar(999999);
		recuperarMana(999999);

		if (nivel == NIVEL_EVOLUCION) {
			evolucionar();
		}
	}

	public abstract void evolucionar();

	public int calcularAtaqueBase() {
		return getAtaque() + equipamiento.getBonificacionAtaque();
	}

	@Override
	public int getAtaqueEfectivo() {
		return calcularAtaqueBase();
	}

	@Override
	public int getDefensa() {
		return super.getDefensa() + equipamiento.getBonificacionDefensa();
	}

	@Override
	public int getVelocidad() {
		return super.getVelocidad() + equipamiento.getBonificacionVelocidad();
	}
}