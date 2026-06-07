package modelo;

import java.io.IOException;

/**
 * Singleton que mantiene el estado global de la partida.
 * - partyPersonajes
 * - motorCombate
 * - nivelActual
 */
public class GameEngine {

    private static GameEngine instancia;

    private PartyPersonajes partyPersonajes;
    private MotorCombate motorCombate;
    private int nivelActual;

    private GameEngine() {
        motorCombate = new MotorCombate();
        nivelActual = 1;
    }

    public static synchronized GameEngine getInstance() {
        if (instancia == null) instancia = new GameEngine();
        return instancia;
    }

    public PartyPersonajes getPartyPersonajes() {
        return partyPersonajes;
    }

    public MotorCombate getMotorCombate() {
        return motorCombate;
    }

    public int getNivelActual() {
        return nivelActual;
    }

    public void iniciarNuevaPartida() {
        partyPersonajes = PartyPersonajes.crearPartyInicial();
        motorCombate = new MotorCombate();
        nivelActual = 1;
    }

    public void iniciarNuevoNivel() {
        PartyEnemigos enemigos = PartyEnemigos.crearParaNivel(nivelActual);
        motorCombate.iniciarBatalla(partyPersonajes, enemigos);
    }

    public void avanzarNivel() {
        nivelActual++;
    }

    // ─────────────────────────────
    // Guardar / Cargar (simple)
    // ─────────────────────────────

    public void guardarPartida(String rutaArchivo) throws IOException {
        if (partyPersonajes == null) throw new IllegalStateException("No hay partida iniciada.");
        EstadoPartida estado = new EstadoPartida(partyPersonajes, nivelActual);
        estado.guardar(rutaArchivo);
    }

    public void cargarPartida(String rutaArchivo) throws IOException, ClassNotFoundException {
        EstadoPartida estado = EstadoPartida.cargar(rutaArchivo);
        this.partyPersonajes = estado.getPartyPersonajes();
        this.nivelActual = estado.getNivelActual();
        this.motorCombate = new MotorCombate(); // motor nuevo (se inicia al comenzar combate)
    }
}