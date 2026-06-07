package modelo;

import enums.TipoGeneral;
import java.io.Serializable;
import java.util.Objects;

public class Habilidad implements Serializable {
	private static final long serialVersionUID = 1L;

	private final String nombre;
	private final String descripcion;
	private final int costeMana;
	private final double multiplicadorDanio;
	private final int cantidadCuracion;
	private final boolean aplicaAturdido;

	public Habilidad(String nombre, String descripcion, int costeMana, double multiplicadorDanio, int cantidadCuracion,
			boolean aplicaAturdido) {
		this.nombre = Objects.requireNonNull(nombre, "nombre");
		this.descripcion = Objects.requireNonNull(descripcion, "descripcion");
		this.costeMana = Math.max(0, costeMana);
		this.multiplicadorDanio = Math.max(0.0, multiplicadorDanio);
		this.cantidadCuracion = Math.max(0, cantidadCuracion);
		this.aplicaAturdido = aplicaAturdido;
	}

	public String getNombre() {
		return nombre;
	}

	public String getDescripcion() {
		return descripcion;
	}

	public int getCosteMana() {
		return costeMana;
	}

	public double getMultiplicadorDanio() {
		return multiplicadorDanio;
	}

	public int getCantidadCuracion() {
		return cantidadCuracion;
	}

	public boolean isAplicaAturdido() {
		return aplicaAturdido;
	}

	public boolean puedeUsarse(Personaje p) {
		if (p != null) {
			if (p.estaVivo()) {
				if (p.tieneMana(costeMana)) {
					return true;
				}
			}
		}
		return false;
	}

	public dto.ResultadoTurno ejecutar(Personaje origen, Entidad objetivo) {
		// Estructuras de validación explícitas mediante condicionales estructurados
		if (origen == null || objetivo == null) {
			String nombreOri = "";
			String nombreObj = "";
			if (origen != null) {
				nombreOri = origen.getNombre();
			}
			if (objetivo != null) {
				nombreObj = objetivo.getNombre();
			}
			return new dto.ResultadoTurno(TipoGeneral.HABILIDAD, nombreOri, nombreObj, 0, 0, null, false,
					TipoGeneral.EN_CURSO, "Habilidad inválida: faltan datos.", 0);
		}

		if (!puedeUsarse(origen)) {
			return new dto.ResultadoTurno(TipoGeneral.HABILIDAD, origen.getNombre(), objetivo.getNombre(), 0, 0, null,
					false, TipoGeneral.EN_CURSO, origen.getNombre() + " no tiene mana suficiente.", 0);
		}

		if (!objetivo.estaVivo()) {
			return new dto.ResultadoTurno(TipoGeneral.HABILIDAD, origen.getNombre(), objetivo.getNombre(), 0, 0, null,
					false, TipoGeneral.EN_CURSO, "El objetivo ya está derrotado.", 0);
		}

		// Consumir maná
		origen.usarMana(costeMana);

		// Caso 1: Curación
		if (cantidadCuracion > 0) {
			int antes = objetivo.getVidaActual();
			objetivo.curar(cantidadCuracion);
			int curado = Math.max(0, objetivo.getVidaActual() - antes);

			return new dto.ResultadoTurno(TipoGeneral.HABILIDAD, origen.getNombre(), objetivo.getNombre(), 0, curado,
					null, false, TipoGeneral.EN_CURSO, origen.getNombre() + " usa " + nombre + " y cura a "
							+ objetivo.getNombre() + " por " + curado + " HP.",
					0);
		}

		// Caso 2: Daño
		int danioBase = (int) Math.round(origen.calcularAtaqueBase() * multiplicadorDanio);
		objetivo.recibirDanio(danioBase);

		TipoGeneral efecto = null;
		if (aplicaAturdido) {
			if (objetivo.estaVivo()) {
				objetivo.aplicarEfecto(TipoGeneral.ATURDIDO);
				efecto = TipoGeneral.ATURDIDO;
			}
		}

		return new dto.ResultadoTurno(TipoGeneral.HABILIDAD, origen.getNombre(), objetivo.getNombre(),
				Math.max(0, danioBase), 0, efecto, false, TipoGeneral.EN_CURSO, origen.getNombre() + " usa " + nombre
						+ " sobre " + objetivo.getNombre() + " y causa " + Math.max(0, danioBase) + " de daño.",
				0);
	}
}