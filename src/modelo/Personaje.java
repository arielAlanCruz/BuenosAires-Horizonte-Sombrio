package modelo;

import enums.TipoPersonaje;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Personaje jugable. - Mantiene Mana y habilidades. - NO tiene inventario
 * individual (inventario es compartido y vive en PartyPersonajes). -
 * Equipamiento individual (arma + accesorio).
 */
public abstract class Personaje extends Entidad {

	private int nivel;
	private int experiencia;

	private int manaActual;
	private int manaMax;

	private final TipoPersonaje clase;
	private final Equipamiento equipamiento;
	private final List<Habilidad> habilidades = new ArrayList<>();

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

	// ─────────────────────────────
	// Getters
	// ─────────────────────────────
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

	// ─────────────────────────────
	// Habilidades
	// ─────────────────────────────
	protected void agregarHabilidad(Habilidad h) {
		if (h != null)
			habilidades.add(h);
	}

	public boolean tieneMana(int cantidad) {
		return manaActual >= Math.max(0, cantidad);
	}

	public void usarMana(int cantidad) {
		int c = Math.max(0, cantidad);
		manaActual = Math.max(0, manaActual - c);
	}

	public void recuperarMana(int cantidad) {
		int c = Math.max(0, cantidad);
		manaActual = Math.min(manaMax, manaActual + c);
	}

	protected void setManaMax(int nuevoMax) {
		this.manaMax = Math.max(0, nuevoMax);
		if (manaActual > manaMax)
			manaActual = manaMax;
	}

	// ─────────────────────────────
	// Progresión (simple)
	// ─────────────────────────────
	public void ganarExperiencia(int cantidad) {
		if (cantidad <= 0)
			return;
		experiencia += cantidad;

		// MVP: nivel up cada 100 exp (simple y fácil de testear)
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

		// Subida simple de stats (pueden ajustar números)
		setVidaMax(getVidaMax() + 10);
		setAtaque(getAtaque() + 2);
		setDefensa(getDefensa() + 1);
		setManaMax(getManaMax() + 5);

		// Recupera vida y mana al subir nivel (simple)
		curar(999999);
		recuperarMana(999999);

		// Evolución simple (nombre + stats) si quieren a cierto nivel
		if (nivel == 3) {
			evolucionar();
		}
	}

	/**
	 * Evolución simple (MVP): cambia nombre y/o stats.
	 */
	public abstract void evolucionar();

	/**
	 * Ataque base + bonificación por equipamiento.
	 */
	public int calcularAtaqueBase() {
		return getAtaque() + equipamiento.getBonificacionAtaque();
	}

	@Override
	public int getDefensa() {
		// Retorna la defensa base heredada de Entidad más la bonificación del
		// equipamiento activo
		return super.getDefensa() + equipamiento.getBonificacionDefensa();
	}

	@Override
	public int getVelocidad() {
		// Retorna la velocidad base de Entidad más la bonificación del equipamiento
		// activo
		return super.getVelocidad() + equipamiento.getBonificacionVelocidad();
	}
}