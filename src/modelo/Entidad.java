package modelo;

import enums.TipoGeneral;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public abstract class Entidad implements Serializable {
	private static final long serialVersionUID = 1L;
	private String nombre;

	private int vidaMax;
	private int vidaActual;

	private int ataque;
	private int defensa;
	private int velocidad;

	private final List<TipoGeneral> efectosActivos = new ArrayList<>();

	protected Entidad(String nombre, int vidaMax, int ataque, int defensa, int velocidad) {
		this.nombre = nombre;
		this.vidaMax = Math.max(1, vidaMax);
		this.vidaActual = this.vidaMax;
		this.ataque = Math.max(0, ataque);
		this.defensa = Math.max(0, defensa);
		this.velocidad = velocidad;
	}

	public String getNombre() {
		return nombre;
	}

	public int getVidaMax() {
		return vidaMax;
	}

	public int getVidaActual() {
		return vidaActual;
	}

	public int getAtaque() {
		return ataque;
	}

	/**
	 * Retorna el ataque base de la entidad.
	 * Puede ser sobreescrito polimórficamente por subclases para añadir
	 * bonificaciones.
	 */
	public int getAtaqueEfectivo() {
		return getAtaque();
	}

	public int getDefensa() {
		return defensa;
	}

	public int getVelocidad() {
		return velocidad;
	}

	public List<TipoGeneral> getEfectosActivos() {
		return Collections.unmodifiableList(efectosActivos);
	}

	protected void setNombre(String nombre) {
		if (nombre != null && !nombre.trim().isEmpty())
			this.nombre = nombre;
	}

	protected void setVidaMax(int vidaMax) {
		this.vidaMax = Math.max(1, vidaMax);
		if (vidaActual > this.vidaMax)
			vidaActual = this.vidaMax;
	}

	protected void setAtaque(int ataque) {
		this.ataque = Math.max(0, ataque);
	}

	protected void setDefensa(int defensa) {
		this.defensa = Math.max(0, defensa);
	}

	protected void setVelocidad(int velocidad) {
		this.velocidad = velocidad;
	}

	public boolean estaVivo() {
		return vidaActual > 0;
	}

	public void recibirDanio(int cantidad) {
		if (!estaVivo())
			return;
		int danio = Math.max(0, cantidad);
		vidaActual = Math.max(0, vidaActual - danio);
	}

	public void curar(int cantidad) {
		if (!estaVivo())
			return;
		int cura = Math.max(0, cantidad);
		vidaActual = Math.min(vidaMax, vidaActual + cura);
	}

	public void revivirYRestaurar() {
		this.vidaActual = this.vidaMax;
	}

	public void aplicarEfecto(TipoGeneral efecto) {
		if (efecto == null)
			return;
		if (efecto != TipoGeneral.ATURDIDO && efecto != TipoGeneral.ESCUDO)
			return;

		if (!efectosActivos.contains(efecto)) {
			efectosActivos.add(efecto);
		}
	}

	public boolean tieneEfecto(TipoGeneral efecto) {
		return efecto != null && efectosActivos.contains(efecto);
	}

	public void removerEfecto(TipoGeneral efecto) {
		if (efecto == null)
			return;
		efectosActivos.remove(efecto);
	}

	public boolean consumirAturdidoSiExiste() {
		if (tieneEfecto(TipoGeneral.ATURDIDO)) {
			removerEfecto(TipoGeneral.ATURDIDO);
			return true;
		}
		return false;
	}

	public void limpiarEscudoAlInicioDeTurno() {
		removerEfecto(TipoGeneral.ESCUDO);
	}
}