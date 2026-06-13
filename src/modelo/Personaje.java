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

		if (nivel == 2) {
			evolucionar();
		}
	}

	public abstract void evolucionar();

	public int calcularAtaqueBase() {
		return getAtaque() + equipamiento.getBonificacionAtaque();
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