package controlador;

import dto.ResultadoTurno;
import enums.TipoGeneral;
import modelo.*;
import vista.*; // Importación de paquete unificada para evitar omisiones de pantallas

import java.io.IOException; // Requerido para la gestión de serialización
import java.util.List;

/**
 * GRASP Controller Puro.
 * No contiene imports de javax.swing.* ni interactúa directamente con
 * componentes gráficos.
 */
public class ControladorJuego {

	private final GameEngine engine;
	private VentanaPrincipal ventana;

	// Pantallas del paquete vista (importadas correctamente vía vista.*)
	private PantallaInicio pantallaInicio;
	private PantallaBatalla pantallaBatalla;
	private PantallaEstado pantallaEstado;
	private PantallaResultado pantallaResultado;
	private PantallaFogata pantallaFogata;

	private static final String SAVE_FILE = "partida.dat";
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

	public void iniciarNuevaPartida() {
		engine.iniciarNuevaPartida();
		engine.iniciarNuevoNivel();
		ventana.mostrarPantalla("BATALLA");
		refrescarPantallaBatalla();
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

	public void procesarAtaque(int indiceObjetivo) {
		MotorCombate motor = engine.getMotorCombate();
		ResultadoTurno r = motor.procesarTurnoJugador(TipoGeneral.ATACAR, indiceObjetivo);
		consumirResultadoYContinuar(r);
	}

	public void procesarDefensa() {
		MotorCombate motor = engine.getMotorCombate();
		ResultadoTurno r = motor.procesarTurnoJugador(TipoGeneral.DEFENDER, -1);
		consumirResultadoYContinuar(r);
	}

	public void procesarHabilidad(int idxHabilidad, int idxObjetivo) {
		MotorCombate motor = engine.getMotorCombate();
		ResultadoTurno r = motor.procesarHabilidadJugador(idxHabilidad, idxObjetivo);
		consumirResultadoYContinuar(r);
	}

	public void procesarItem(int idxItem, int idxObjetivo) {
		PartyPersonajes party = engine.getPartyPersonajes();
		Inventario inv = party.getInventarioCompartido();
		List<Personaje> vivos = party.getVivos();

		if (idxObjetivo >= 0 && idxObjetivo < vivos.size()) {
			Personaje objetivo = vivos.get(idxObjetivo);
			String msg = inv.usarConsumible(idxItem, objetivo);
			ResultadoTurno r = engine.getMotorCombate().consumirTurnoPorUsoItem(msg, objetivo.getNombre());
			consumirResultadoYContinuar(r);
		}
	}

	public void procesarVerEstado() {
		MotorCombate motor = engine.getMotorCombate();
		Entidad actual = motor.getEntidadEnTurnoActual();

		if (actual instanceof Personaje) {
			pantallaEstado.mostrarPersonaje((Personaje) actual);
			ventana.mostrarPantalla("ESTADO");
		}
	}

	public void onVolverDesdeEstado() {
		ventana.mostrarPantalla("BATALLA");
		refrescarPantallaBatalla();
	}

	public void ejecutarTurnoEnemigoAutomatico() {
		MotorCombate motor = engine.getMotorCombate();
		if (motor.getEstadoBatalla() == TipoGeneral.EN_CURSO && motor.esTurnoDeEnemigo()) {
			ResultadoTurno re = motor.procesarTurnoEnemigo();
			imprimirConsola(re);
			actualizarVista(re);

			// Animación visual delegada
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
		pantallaBatalla.habilitarBotonesAccion(false); // Congela interacción

		// Notificamos a la vista que inicie su temporizador de transición
		pantallaBatalla.iniciarTemporizadorTransicionFinBatalla();
	}

	public void cambiarAPantallaResultadoFinal() {
		MotorCombate motor = engine.getMotorCombate();
		if (ultimaBatallaGanada) {
			int exp = engine.getPartyPersonajes() != null ? motor.getPartyEnemigos().calcularExpTotal() : 0;
			if (engine.getPartyPersonajes() != null) {
				engine.getPartyPersonajes().distribuirExperiencia(exp);
			}

			// Si ganaron el nivel 7 (Combate final), el juego ha sido completado
			if (engine.getNivelActual() == 7) {
				pantallaResultado.mostrarVictoriaFinal();
			} else {
				// CORRECCIÓN: Mostramos la victoria del nivel actual, pero NO avanzamos de
				// nivel aquí
				pantallaResultado.mostrarVictoria(exp);
			}
		} else {
			pantallaResultado.mostrarDerrota();
		}
		ventana.mostrarPantalla("RESULTADO");
	}

	public void onContinuarDesdeResultado() {
		if (ultimaBatallaGanada) {
			// Si el nivel que acaban de GANAR en batalla era el 7, se termina el juego
			if (engine.getNivelActual() == 7) {
				volverAlMenu();
			} else {
				restablecerSaludYManaParty();
				ventana.mostrarPantalla("FOGATA");
				pantallaFogata.entregarItemsFijos();

				// CORRECCIÓN: El nivel avanza de forma segura aquí, al transicionar a la Fogata
				engine.avanzarNivel();
			}
		} else {
			reintentarNivel();
		}
	}

	private void refrescarPantallaBatalla() {
		MotorCombate motor = engine.getMotorCombate();
		pantallaBatalla.actualizarBarras(
				engine.getPartyPersonajes(),
				motor.getPartyEnemigos(),
				motor.getEntidadEnTurnoActual(),
				engine.getNivelActual());

		boolean esTurnoHéroe = motor.esTurnoDePersonaje();
		pantallaBatalla.habilitarBotonesAccion(esTurnoHéroe);

		// Si es turno de la IA, le indicamos a la vista que controle el retardo
		if (motor.getEstadoBatalla() == TipoGeneral.EN_CURSO && !esTurnoHéroe) {
			pantallaBatalla.iniciarTemporizadorTurnoEnemigo();
		}
	}

	private void actualizarVista(ResultadoTurno r) {
		pantallaBatalla.mostrarResultadoTurno(r);
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

	private void restablecerSaludYManaParty() {
		if (engine.getPartyPersonajes() != null) {
			for (int i = 0; i < engine.getPartyPersonajes().getMiembros().size(); i++) {
				Personaje p = engine.getPartyPersonajes().getMiembros().get(i);
				p.revivirYRestaurar(); // Levanta al personaje si estaba debilitado (HP=0)
				p.recuperarMana(99999);
				p.removerEfecto(TipoGeneral.ATURDIDO);
				p.removerEfecto(TipoGeneral.ESCUDO);
			}
		}
	}

	private void reintentarNivel() {
		if (engine.getPartyPersonajes() != null) {
			for (int i = 0; i < engine.getPartyPersonajes().getMiembros().size(); i++) {
				Personaje p = engine.getPartyPersonajes().getMiembros().get(i);
				p.revivirYRestaurar(); // Levanta al personaje si estaba debilitado (HP=0)
				p.recuperarMana(99999);
				p.removerEfecto(TipoGeneral.ATURDIDO);
				p.removerEfecto(TipoGeneral.ESCUDO);
			}
		}
		engine.iniciarNuevoNivel();
		ventana.mostrarPantalla("BATALLA");
		refrescarPantallaBatalla();
	}

	public void onContinuarDesdeFogata() {
		engine.iniciarNuevoNivel();
		ventana.mostrarPantalla("BATALLA");
		refrescarPantallaBatalla();
	}
}