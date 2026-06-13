package dto;

import enums.TipoGeneral;

/**
 * Describe qué pasó en un único turno de combate (Acción transaccional).
 * Se usa para comunicar: Modelo -> Controlador -> Vista (Log de texto y
 * animaciones).
 */
public class ResultadoTurno {

	private final TipoGeneral accion;
	private final String nombreAtacante;
	private final String nombreObjetivo;

	private final int danio;
	private final int curacion;

	private final TipoGeneral efectoAplicado; // puede ser null (ej. ATURDIDO)
	private final boolean turnoSalteado;

	private final TipoGeneral estadoBatalla; // EN_CURSO / VICTORIA / DERROTA
	private final String mensaje;
	private final int experienciaGanada;

	public ResultadoTurno(TipoGeneral accion, String nombreAtacante, String nombreObjetivo, int danio, int curacion,
			TipoGeneral efectoAplicado, boolean turnoSalteado, TipoGeneral estadoBatalla, String mensaje,
			int experienciaGanada) {
		this.accion = accion;
		this.nombreAtacante = nombreAtacante;
		this.nombreObjetivo = nombreObjetivo;
		this.danio = danio;
		this.curacion = curacion;
		this.efectoAplicado = efectoAplicado;
		this.turnoSalteado = turnoSalteado;
		this.estadoBatalla = estadoBatalla;
		this.mensaje = mensaje;
		this.experienciaGanada = experienciaGanada;
	}

	public TipoGeneral getAccion() {
		return accion;
	}

	public String getNombreAtacante() {
		return nombreAtacante;
	}

	public String getNombreObjetivo() {
		return nombreObjetivo;
	}

	public int getDanio() {
		return danio;
	}

	public int getCuracion() {
		return curacion;
	}

	public TipoGeneral getEfectoAplicado() {
		return efectoAplicado;
	}

	public boolean isTurnoSalteado() {
		return turnoSalteado;
	}

	public TipoGeneral getEstadoBatalla() {
		return estadoBatalla;
	}

	public String getMensaje() {
		return mensaje;
	}

	public int getExperienciaGanada() {
		return experienciaGanada;
	}

	// Helper para mensajes informativos rápidos
	public static ResultadoTurno info(String mensaje) {
		return new ResultadoTurno(null, "", "", 0, 0, null, false, TipoGeneral.EN_CURSO, mensaje, 0);
	}
}