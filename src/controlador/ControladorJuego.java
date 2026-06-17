package controlador;

import dto.EntidadDTO;
import dto.EstadoBatallaDTO;
import dto.ResultadoTurno;
import enums.TipoGeneral;
import modelo.*;
import vista.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * GRASP Controller.
 * Traduce interacciones por nombres a índices lógicos del modelo y realiza el
 * mapeo de datos.
 */
public class ControladorJuego {

	private final GameEngine engine;
	private VentanaPrincipal ventana;

	private PantallaInicio pantallaInicio;
	private PantallaBatalla pantallaBatalla;
	private PantallaEstado pantallaEstado;
	private PantallaResultado pantallaResultado;
	private PantallaFogata pantallaFogata;

	private static final String SAVE_FILE = "partida.dat";
	private static final int NIVEL_FINAL = 7;
	private boolean ultimaBatallaGanada = false;

	public ControladorJuego() {
		this.engine = GameEngine.getInstance();
	}

	public void setVentanaPrincipal(VentanaPrincipal ventana) {
		this.ventana = ventana;
	}

	public void setPantallas(PantallaInicio inicio, PantallaBatalla batalla, PantallaEstado estado,
			PantallaResultado resultado, PantallaFogata fogata) {
		this.pantallaInicio = inicio;
		this.pantallaBatalla = batalla;
		this.pantallaEstado = estado;
		this.pantallaResultado = resultado;
		this.pantallaFogata = fogata;
	}

	// =========================================================
	// ============== FLUJO PRINCIPAL DEL JUEGO ================
	// =========================================================

	public void iniciarNuevaPartida() {
		engine.iniciarNuevaPartida();
		engine.iniciarNuevoNivel();
		ventana.mostrarPantalla("BATALLA");
		refrescarPantallaBatalla();// Para mostrar el estado inicial de la batalla
									// inmediatamente al iniciar una nueva partida
	}

	public void volverAlMenu() {
		if (pantallaResultado != null) {
			pantallaResultado.limpiar();
		}
		ventana.mostrarPantalla("INICIO");
	}

	public void cargarPartida() {
		try {
			engine.cargarPartida(SAVE_FILE);
			engine.iniciarNuevoNivel();
			ventana.mostrarPantalla("BATALLA");
			refrescarPantallaBatalla();
			mostrarMensaje("Partida cargada correctamente.");
		} catch (IOException | ClassNotFoundException ex) {
			mostrarMensaje("No se pudo cargar la partida: " + ex.getMessage());
		}
	}

	public void guardarPartida() {
		try {
			engine.guardarPartida(SAVE_FILE);
			mostrarMensaje("Partida guardada en " + SAVE_FILE);
		} catch (IOException | IllegalStateException ex) {
			mostrarMensaje("No se pudo guardar la partida: " + ex.getMessage());
		}
	}

	// =========================================================
	// =============== ACCIONES DEL JUGADOR ====================
	// =========================================================

	public void procesarAtaque(String nombreObjetivo) {
		int indice = buscarIndiceEnemigoPorNombre(nombreObjetivo);
		if (indice != -1) {
			MotorCombate motor = engine.getMotorCombate();
			ResultadoTurno r = motor.procesarTurnoJugador(TipoGeneral.ATACAR, indice);
			consumirResultadoYContinuar(r);
		} else {
			mostrarMensaje("Objetivo no válido para ataque.");
		}
	}

	public void procesarDefensa() {
		MotorCombate motor = engine.getMotorCombate();
		ResultadoTurno r = motor.procesarTurnoJugador(TipoGeneral.DEFENDER, -1);
		consumirResultadoYContinuar(r);
	}

	public void procesarHabilidad(int idxHabilidad, String nombreObjetivo) {
		MotorCombate motor = engine.getMotorCombate();
		int indice = -1;

		Entidad actual = motor.getEntidadEnTurnoActual();
		if (!(actual instanceof Personaje)) {
			mostrarMensaje("No es el turno de un héroe.");
			return;
		}
		Personaje heroe = (Personaje) actual;

		Habilidad hab = heroe.getHabilidades().get(idxHabilidad);
		if (hab.getCantidadCuracion() > 0) {
			indice = buscarIndiceAliadoPorNombre(nombreObjetivo);
		} else {
			indice = buscarIndiceEnemigoPorNombre(nombreObjetivo);
		}

		if (indice != -1) {
			ResultadoTurno r = motor.procesarHabilidadJugador(idxHabilidad, indice);
			consumirResultadoYContinuar(r);
		} else {
			mostrarMensaje("Objetivo no válido para habilidad.");
		}
	}

	public void procesarItem(int idxItem, String nombreObjetivo) {
		PartyPersonajes party = engine.getPartyPersonajes();
		Inventario inv = party.getInventarioCompartido();
		int idxObjetivo = buscarIndiceAliadoPorNombre(nombreObjetivo);

		if (idxObjetivo != -1) {
			Personaje objetivo = party.getVivos().get(idxObjetivo);
			String msg = inv.usarItem(idxItem, objetivo);
			ResultadoTurno r = engine.getMotorCombate().consumirTurnoPorUsoItem(msg, objetivo.getNombre());
			consumirResultadoYContinuar(r);
		}
	}

	// =========================================================
	// =============== CONTROL DE VISTAS UI ====================
	// =========================================================

	public void procesarVerEstado() {
		MotorCombate motor = engine.getMotorCombate();
		Entidad actual = motor.getEntidadEnTurnoActual();

		if (actual instanceof Personaje) {
			Personaje p = (Personaje) actual;
			EntidadDTO dto = mapearSinglePersonajeADTO(p);
			pantallaEstado.mostrarPersonaje(dto);
			ventana.mostrarPantalla("ESTADO");
		}
	}

	public void onVolverDesdeEstado() {
		ventana.mostrarPantalla("BATALLA");
		refrescarPantallaBatalla();
	}
	// =========================================================
	// =============== FLUJO DE TURNOS / BATALLA ==============
	// =========================================================

	public void ejecutarTurnoEnemigoAutomatico() {
		MotorCombate motor = engine.getMotorCombate();
		if (motor.getEstadoBatalla() == TipoGeneral.EN_CURSO && motor.esTurnoDeEnemigo()) {
			ResultadoTurno re = motor.procesarTurnoEnemigo();
			imprimirConsola(re);
			actualizarVista(re);

			if (re.getAccion() == TipoGeneral.ATACAR || re.getAccion() == TipoGeneral.HABILIDAD) {
				pantallaBatalla.dispararAnimacionAtaque(re.getNombreAtacante());
			}

			if (re.getEstadoBatalla() == TipoGeneral.VICTORIA) {
				procesarFinBatalla(true);
			} else if (re.getEstadoBatalla() == TipoGeneral.DERROTA) {
				procesarFinBatalla(false);
			} else {
				refrescarPantallaBatalla();
			}
		}
	}

	private void consumirResultadoYContinuar(ResultadoTurno r) {
		imprimirConsola(r);
		actualizarVista(r);

		if (r.getAccion() == TipoGeneral.ATACAR || r.getAccion() == TipoGeneral.HABILIDAD) {
			pantallaBatalla.dispararAnimacionAtaque(r.getNombreAtacante());
		}

		if (r.getEstadoBatalla() == TipoGeneral.VICTORIA) {
			procesarFinBatalla(true);
		} else if (r.getEstadoBatalla() == TipoGeneral.DERROTA) {
			procesarFinBatalla(false);
		} else {
			refrescarPantallaBatalla();
		}
	}

	private void procesarFinBatalla(boolean victoria) {
		this.ultimaBatallaGanada = victoria;
		pantallaBatalla.habilitarBotonesAccion(false);
		pantallaBatalla.iniciarTemporizadorTransitionFinBatalla();
	}

	// =========================================================
	// =============== RESULTADOS Y PROGRESIÓN ================
	// =========================================================

	public void cambiarAPantallaResultadoFinal() {
		MotorCombate motor = engine.getMotorCombate();
		if (ultimaBatallaGanada) {
			int exp = engine.getPartyPersonajes() != null ? motor.getPartyEnemigos().calcularExpTotal() : 0;
			if (engine.getPartyPersonajes() != null) {
				engine.getPartyPersonajes().distribuirExperiencia(exp);
			}

			if (engine.getNivelActual() == NIVEL_FINAL) {
				pantallaResultado.mostrarVictoriaFinal();
			} else {
				pantallaResultado.mostrarVictoria(exp);
			}
		} else {
			pantallaResultado.mostrarDerrota();
		}
		ventana.mostrarPantalla("RESULTADO");
	}

	public void onContinuarDesdeResultado() {
		if (ultimaBatallaGanada) {
			if (engine.getNivelActual() == NIVEL_FINAL) {
				volverAlMenu();
			} else {
				restablecerSaludYManaParty();
				ventana.mostrarPantalla("FOGATA");
				pantallaFogata.entregarItemsFijos();
				engine.avanzarNivel();
			}
		} else {
			reintentarNivel();
		}
	}

	public void onContinuarDesdeFogata() {
		engine.iniciarNuevoNivel();
		ventana.mostrarPantalla("BATALLA");
		refrescarPantallaBatalla();
	}
	// =========================================================
	// ================== ACTUALIZACIÓN UI =====================
	// =========================================================

	private void refrescarPantallaBatalla() {
		MotorCombate motor = engine.getMotorCombate();
		// convertir personajes y enemigos del modelo a DTOs para la vista
		List<EntidadDTO> aliados = mapearListaPersonajes(engine.getPartyPersonajes().getMiembros());
		List<EntidadDTO> enemigos = mapearListaEnemigos(motor.getPartyEnemigos().getEnemigos());

		Entidad actual = motor.getEntidadEnTurnoActual();
		// String nombreTurno = actual != null ? actual.getNombre() : "";
		if (actual == null) {
			mostrarMensaje("Error: No hay entidad en turno actual.");
			return;
		}
		String nombreTurno = actual.getNombre();

		List<String> nombresItems = new ArrayList<>();
		Inventario inv = engine.getPartyPersonajes().getInventarioCompartido();
		for (int i = 0; i < inv.getItems().size(); i++) {
			Item item = inv.getItems().get(i);
			nombresItems.add(item.getNombre() + "  |  " + item.getDescripcion());
		}

		EstadoBatallaDTO estadoDTO = new EstadoBatallaDTO(aliados, enemigos, nombreTurno, engine.getNivelActual(),
				nombresItems);

		pantallaBatalla.actualizarBarras(estadoDTO);

		boolean esTurnoHeroe = motor.esTurnoDePersonaje();
		pantallaBatalla.habilitarBotonesAccion(esTurnoHeroe);

		if (motor.getEstadoBatalla() == TipoGeneral.EN_CURSO && !esTurnoHeroe) {
			pantallaBatalla.iniciarTemporizadorTurnoEnemigo();
		}
	}

	private void actualizarVista(ResultadoTurno r) {
		pantallaBatalla.mostrarResultadoTurno(r);
		refrescarPantallaBatalla();
	}
	// =========================================================
	// ===================== MAPEOS DTO ========================
	// =========================================================

	private List<EntidadDTO> mapearListaPersonajes(List<Personaje> personajes) {
		List<EntidadDTO> lista = new ArrayList<>();
		for (int i = 0; i < personajes.size(); i++) {
			lista.add(mapearSinglePersonajeADTO(personajes.get(i)));
		}
		return lista;
	}

	private List<EntidadDTO> mapearListaEnemigos(List<Enemigo> enemigos) {
		List<EntidadDTO> lista = new ArrayList<>();
		for (int i = 0; i < enemigos.size(); i++) {
			Enemigo e = enemigos.get(i);
			lista.add(new EntidadDTO(
					e.getNombre(), e.getVidaActual(), e.getVidaMax(), 0, 0,
					e.estaVivo(), e.tieneEfecto(TipoGeneral.ESCUDO), e.tieneEfecto(TipoGeneral.ATURDIDO),
					e.getNivel(), 0, "ENEMIGO", e.getAtaque(), e.getDefensa(), e.getVelocidad(),
					"Ninguno", "Ninguno")); // Los enemigos no tienen slots de arma/accesorio visibles
		}
		return lista;
	}

	private EntidadDTO mapearSinglePersonajeADTO(Personaje p) {
		String armaNom = "Ninguna";
		String accNom = "Ninguno";

		// Validamos el equipamiento de forma clara
		if (p.getEquipamiento() != null) {
			if (p.getEquipamiento().getArma() != null) {
				armaNom = p.getEquipamiento().getArma().getNombre();
			}
			if (p.getEquipamiento().getAccesorio() != null) {
				accNom = p.getEquipamiento().getAccesorio().getNombre();
			}
		}

		// Retornamos el DTO con los parámetros ordenados visualmente
		EntidadDTO Heroe_dto = new EntidadDTO(
				p.getNombre(), p.getVidaActual(), p.getVidaMax(), p.getManaActual(), p.getManaMax(),
				p.estaVivo(), p.tieneEfecto(TipoGeneral.ESCUDO), p.tieneEfecto(TipoGeneral.ATURDIDO),
				p.getNivel(), p.getExperiencia(), p.getClase().toString(), p.calcularAtaqueBase(),
				p.getDefensa(), p.getVelocidad(), armaNom, accNom);

		return Heroe_dto;
	}

	// =========================================================
	// ===================== BÚSQUEDAS =========================
	// =========================================================

	private int buscarIndiceEnemigoPorNombre(String nombre) {
		List<Enemigo> vivos = engine.getMotorCombate().getPartyEnemigos().getVivos();
		for (int i = 0; i < vivos.size(); i++) {
			if (vivos.get(i).getNombre().equals(nombre)) {
				return i;
			}
		}
		return -1;
	}

	private int buscarIndiceAliadoPorNombre(String nombre) {
		List<Personaje> vivos = engine.getPartyPersonajes().getVivos();
		for (int i = 0; i < vivos.size(); i++) {
			if (vivos.get(i).getNombre().equals(nombre)) {
				return i;
			}
		}
		return -1;
	}

	// =========================================================
	// ==================== UTILIDADES =========================
	// =========================================================

	private void restablecerSaludYManaParty() {
		if (engine.getPartyPersonajes() != null) {
			for (int i = 0; i < engine.getPartyPersonajes().getMiembros().size(); i++) {
				Personaje p = engine.getPartyPersonajes().getMiembros().get(i);
				p.revivirYRestaurar();
				p.recuperarMana(99999);
				p.removerEfecto(TipoGeneral.ATURDIDO);
				p.removerEfecto(TipoGeneral.ESCUDO);
			}
		}
	}

	private void reintentarNivel() {
		restablecerSaludYManaParty();
		engine.iniciarNuevoNivel();
		ventana.mostrarPantalla("BATALLA");
		refrescarPantallaBatalla();
	}

	private void imprimirConsola(ResultadoTurno r) {
		if (r == null)
			return;
		if (r.getMensaje() != null && !r.getMensaje().isEmpty()) {
			System.out.println("CONSOLE LOG: " + r.getMensaje());
		}
	}

	public void mostrarMensaje(String msg) {
		if (ventana != null) {
			ventana.mostrarMensaje(msg);
		} else {
			System.out.println(msg);
		}
	}
}