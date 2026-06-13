package modelo;

import dto.ResultadoTurno;
import enums.TipoGeneral;
import java.util.ArrayList;
import java.util.List;

public class MotorCombate {

	private PartyPersonajes partyPersonajes;
	private PartyEnemigos partyEnemigos;
	private final List<Entidad> ordenTurnos = new ArrayList<>();
	private int indiceTurno = 0;
	private TipoGeneral estadoBatalla = TipoGeneral.EN_CURSO;

	public void iniciarBatalla(PartyPersonajes party, PartyEnemigos enemigos) {
		this.partyPersonajes = party;
		this.partyEnemigos = enemigos;
		this.estadoBatalla = TipoGeneral.EN_CURSO;

		ordenTurnos.clear();
		indiceTurno = 0;

		List<Personaje> heroes = new ArrayList<>(partyPersonajes.getMiembros());
		List<Enemigo> listaEnemigos = new ArrayList<>(partyEnemigos.getEnemigos());

		heroes.sort((a, b) -> Integer.compare(b.getVelocidad(), a.getVelocidad()));
		listaEnemigos.sort((a, b) -> Integer.compare(b.getVelocidad(), a.getVelocidad()));

		boolean heroesGananIniciativa = true;
		if (!listaEnemigos.isEmpty() && !heroes.isEmpty()) {
			heroesGananIniciativa = heroes.get(0).getVelocidad() >= listaEnemigos.get(0).getVelocidad();
		}

		List<Entidad> vanguardiaHeroes = extraerSubListaSegura(heroes, 0, 2);
		List<Entidad> retaguardiaHeroes = extraerSubListaSegura(heroes, 2, heroes.size());

		List<Entidad> vanguardiaEnemigos = extraerSubListaSegura(listaEnemigos, 0, 2);
		List<Entidad> retaguardiaEnemigos = extraerSubListaSegura(listaEnemigos, 2, listaEnemigos.size());

		if (heroesGananIniciativa) {
			ordenTurnos.addAll(vanguardiaHeroes);
			ordenTurnos.addAll(vanguardiaEnemigos);
			ordenTurnos.addAll(retaguardiaHeroes);
			ordenTurnos.addAll(retaguardiaEnemigos);
		} else {
			ordenTurnos.addAll(vanguardiaEnemigos);
			ordenTurnos.addAll(vanguardiaHeroes);
			ordenTurnos.addAll(retaguardiaEnemigos);
			ordenTurnos.addAll(retaguardiaHeroes);
		}

		avanzarHastaEntidadViva();
	}

	private List<Entidad> extraerSubListaSegura(List<? extends Entidad> lista, int desde, int hasta) {
		List<Entidad> subLista = new ArrayList<>();
		for (int i = desde; i < hasta && i < lista.size(); i++) {
			subLista.add(lista.get(i));
		}
		return subLista;
	}

	public TipoGeneral getEstadoBatalla() {
		return estadoBatalla;
	}

	public Entidad getEntidadEnTurnoActual() {
		if (ordenTurnos.isEmpty()) {
			return null;
		}
		return ordenTurnos.get(indiceTurno);
	}

	public PartyPersonajes getPartyPersonajes() {
		return partyPersonajes;
	}

	public PartyEnemigos getPartyEnemigos() {
		return partyEnemigos;
	}

	public ResultadoTurno procesarTurnoJugador(TipoGeneral accion, int indiceObjetivo) {
		if (estadoBatalla != TipoGeneral.EN_CURSO) {
			return new ResultadoTurno(accion, "", "", 0, 0, null, false, estadoBatalla, "La batalla ya terminó.", 0);
		}

		Entidad actual = getEntidadEnTurnoActual();
		if (!(actual instanceof Personaje)) {
			return new ResultadoTurno(accion, "", "", 0, 0, null, false, estadoBatalla,
					"No es el turno de un personaje.", 0);
		}

		return procesarTurnoDeEntidad(actual, accion, -1, indiceObjetivo);
	}

	public ResultadoTurno procesarTurnoEnemigo() {
		if (estadoBatalla != TipoGeneral.EN_CURSO) {
			return new ResultadoTurno(null, "", "", 0, 0, null, false, estadoBatalla, "La batalla ya terminó.", 0);
		}

		Entidad actual = getEntidadEnTurnoActual();
		if (!(actual instanceof Enemigo)) {
			return new ResultadoTurno(null, "", "", 0, 0, null, false, estadoBatalla, "No es el turno de un enemigo.",
					0);
		}

		Enemigo enemigo = (Enemigo) actual;
		TipoGeneral accion = enemigo.elegirAccion();
		return procesarTurnoDeEntidad(enemigo, accion, -1, -1);
	}

	private ResultadoTurno procesarTurnoDeEntidad(Entidad entidad, TipoGeneral accion, int indiceHabilidad,
			int indiceObjetivo) {
		if (entidad == null) {
			return new ResultadoTurno(accion, "", "", 0, 0, null, false, estadoBatalla, "Turno inválido.", 0);
		}

		entidad.limpiarEscudoAlInicioDeTurno();

		if (entidad.consumirAturdidoSiExiste()) {
			ResultadoTurno r = new ResultadoTurno(accion, entidad.getNombre(), "", 0, 0, TipoGeneral.ATURDIDO, true,
					estadoBatalla, entidad.getNombre() + " está ATURDIDO y pierde el turno.", 0);
			avanzarTurno();
			return r;
		}

		ResultadoTurno resultado;
		switch (accion) {
			case ATACAR:
				resultado = resolverAtaque(entidad, indiceObjetivo);
				break;
			case DEFENDER:
				resultado = resolverDefensa(entidad);
				break;
			case HABILIDAD:
				if (entidad instanceof Enemigo) {
					resultado = resolverHabilidadJefe((Enemigo) entidad);
				} else {
					resultado = resolverHabilidadPersonaje((Personaje) entidad, indiceHabilidad, indiceObjetivo);
				}
				break;
			default:
				resultado = new ResultadoTurno(null, entidad.getNombre(), "", 0, 0, null, false, estadoBatalla,
						"Acción no soportada.", 0);
		}

		this.estadoBatalla = verificarFinBatalla();

		if (this.estadoBatalla == TipoGeneral.EN_CURSO) {
			avanzarTurno();
		}

		return new ResultadoTurno(
				resultado.getAccion(),
				resultado.getNombreAtacante(),
				resultado.getNombreObjetivo(),
				resultado.getDanio(),
				resultado.getCuracion(),
				resultado.getEfectoAplicado(),
				resultado.isTurnoSalteado(),
				this.estadoBatalla,
				resultado.getMensaje(),
				resultado.getExperienciaGanada());
	}

	private ResultadoTurno resolverAtaque(Entidad atacante, int indiceObjetivo) {
		Entidad objetivo = seleccionarObjetivoParaAtaque(atacante, indiceObjetivo);
		if (objetivo == null) {
			return new ResultadoTurno(TipoGeneral.ATACAR, atacante.getNombre(), "", 0, 0, null, false, estadoBatalla,
					"No hay objetivos disponibles.", 0);
		}

		int danio = calcularDanio(atacante, objetivo);
		objetivo.recibirDanio(danio);

		String msg = atacante.getNombre() + " ataca a " + objetivo.getNombre() + " por " + danio + " HP.";
		if (!objetivo.estaVivo()) {
			msg += " ¡" + objetivo.getNombre() + " ha caído!";
		}

		return new ResultadoTurno(TipoGeneral.ATACAR, atacante.getNombre(), objetivo.getNombre(), danio, 0, null, false,
				TipoGeneral.EN_CURSO, msg, 0);
	}

	private ResultadoTurno resolverDefensa(Entidad defensor) {
		defensor.aplicarEfecto(TipoGeneral.ESCUDO);
		return new ResultadoTurno(TipoGeneral.DEFENDER, defensor.getNombre(), defensor.getNombre(), 0, 0,
				TipoGeneral.ESCUDO, false, TipoGeneral.EN_CURSO,
				defensor.getNombre() + " activa ESCUDO (Defensa x1.5).", 0);
	}

	private ResultadoTurno resolverHabilidadJefe(Enemigo jefe) {
		List<Personaje> vivos = partyPersonajes.getVivos();
		if (vivos.isEmpty()) {
			return new ResultadoTurno(TipoGeneral.HABILIDAD, jefe.getNombre(), "", 0, 0, null, false, estadoBatalla,
					"No hay objetivos vivos.", 0);
		}

		int idx = (int) (Math.random() * vivos.size());
		Personaje victima = vivos.get(idx);
		victima.aplicarEfecto(TipoGeneral.ATURDIDO);

		String msg = jefe.getNombre() + " lanza un grito espectral y ATURDE a " + victima.getNombre() + " por 1 turno.";
		return new ResultadoTurno(TipoGeneral.HABILIDAD, jefe.getNombre(), victima.getNombre(), 0, 0,
				TipoGeneral.ATURDIDO, false, TipoGeneral.EN_CURSO, msg, 0);
	}

	private ResultadoTurno resolverHabilidadPersonaje(Personaje origen, int indiceHabilidad, int indiceObjetivo) {
		if (origen.getHabilidades().isEmpty()) {
			return new ResultadoTurno(TipoGeneral.HABILIDAD, origen.getNombre(), "", 0, 0, null, false,
					TipoGeneral.EN_CURSO, "Sin habilidades.", 0);
		}

		int idx = Math.max(0, indiceHabilidad);
		if (idx >= origen.getHabilidades().size()) {
			idx = 0;
		}
		Habilidad h = origen.getHabilidades().get(idx);

		Entidad objetivo = seleccionarObjetivoParaHabilidad(origen, h, indiceObjetivo);
		if (objetivo == null) {
			return new ResultadoTurno(TipoGeneral.HABILIDAD, origen.getNombre(), "", 0, 0, null, false,
					TipoGeneral.EN_CURSO, "Objetivo inválido.", 0);
		}

		return h.ejecutar(origen, objetivo);
	}

	private Entidad seleccionarObjetivoParaAtaque(Entidad atacante, int indiceObjetivo) {
		if (atacante instanceof Personaje) {
			List<Enemigo> vivos = partyEnemigos.getVivos();
			if (indiceObjetivo >= 0 && indiceObjetivo < vivos.size()) {
				return vivos.get(indiceObjetivo);
			}
			return seleccionarEnemigoConMenosVida();
		}
		return seleccionarAliadoConMenosVida();
	}

	private Entidad seleccionarObjetivoParaHabilidad(Personaje origen, Habilidad h, int indiceObjetivo) {
		if (h.getCantidadCuracion() > 0) {
			List<Personaje> vivos = partyPersonajes.getVivos();
			if (indiceObjetivo >= 0 && indiceObjetivo < vivos.size()) {
				return vivos.get(indiceObjetivo);
			}
			return seleccionarAliadoConMenosVida();
		} else {
			List<Enemigo> vivos = partyEnemigos.getVivos();
			if (indiceObjetivo >= 0 && indiceObjetivo < vivos.size()) {
				return vivos.get(indiceObjetivo);
			}
			return seleccionarEnemigoConMenosVida();
		}
	}

	private Enemigo seleccionarEnemigoConMenosVida() {
		Enemigo mejor = null;
		List<Enemigo> vivos = partyEnemigos.getVivos();
		for (int i = 0; i < vivos.size(); i++) {
			Enemigo e = vivos.get(i);
			if (mejor == null || e.getVidaActual() < mejor.getVidaActual()) {
				mejor = e;
			}
		}
		return mejor;
	}

	private Personaje seleccionarAliadoConMenosVida() {
		Personaje mejor = null;
		List<Personaje> vivos = partyPersonajes.getVivos();
		for (int i = 0; i < vivos.size(); i++) {
			Personaje p = vivos.get(i);
			if (mejor == null || p.getVidaActual() < mejor.getVidaActual()) {
				mejor = p;
			}
		}
		return mejor;
	}

	private int calcularDanio(Entidad atacante, Entidad objetivo) {
		// Reemplazo polimórfico del condicional manual por getAtaqueEfectivo()
		int ataqueTotal = atacante.getAtaqueEfectivo();

		int defensaFinal = objetivo.getDefensa();

		if (objetivo.tieneEfecto(TipoGeneral.ESCUDO)) {
			defensaFinal = (int) Math.round(defensaFinal * 1.5);
		}
		return Math.max(1, ataqueTotal - defensaFinal);
	}

	private void avanzarTurno() {
		if (ordenTurnos.isEmpty()) {
			return;
		}
		indiceTurno = (indiceTurno + 1) % ordenTurnos.size();
		avanzarHastaEntidadViva();
	}

	private void avanzarHastaEntidadViva() {
		if (ordenTurnos.isEmpty()) {
			return;
		}
		int intentos = 0;
		while (intentos < ordenTurnos.size()) {
			Entidad e = ordenTurnos.get(indiceTurno);
			if (e != null && e.estaVivo()) {
				return;
			}
			indiceTurno = (indiceTurno + 1) % ordenTurnos.size();
			intentos++;
		}
	}

	private TipoGeneral verificarFinBatalla() {
		if (partyPersonajes.todosDerrotados()) {
			return TipoGeneral.DERROTA;
		}
		if (partyEnemigos.todosDerrotados()) {
			return TipoGeneral.VICTORIA;
		}
		return TipoGeneral.EN_CURSO;
	}

	public boolean esTurnoDePersonaje() {
		return getEntidadEnTurnoActual() instanceof Personaje;
	}

	public boolean esTurnoDeEnemigo() {
		return getEntidadEnTurnoActual() instanceof Enemigo;
	}

	public ResultadoTurno procesarHabilidadJugador(int indiceHabilidad, int indiceObjetivo) {
		Entidad actual = getEntidadEnTurnoActual();
		if (!(actual instanceof Personaje)) {
			return new ResultadoTurno(TipoGeneral.HABILIDAD, "", "", 0, 0, null, false, estadoBatalla,
					"No es el turno de un héroe.", 0);
		}
		return procesarTurnoDeEntidad(actual, TipoGeneral.HABILIDAD, indiceHabilidad, indiceObjetivo);
	}

	public ResultadoTurno consumirTurnoPorUsoItem(String mensaje, String nombreObjetivo) {
		Entidad actual = getEntidadEnTurnoActual();
		if (actual == null) {
			return new ResultadoTurno(TipoGeneral.USAR_ITEM, "", nombreObjetivo, 0, 0, null, false, estadoBatalla,
					"Acción inválida.", 0);
		}

		ResultadoTurno r = new ResultadoTurno(TipoGeneral.USAR_ITEM, actual.getNombre(), nombreObjetivo, 0, 0, null,
				false, TipoGeneral.EN_CURSO, mensaje, 0);

		estadoBatalla = verificarFinBatalla();
		if (estadoBatalla == TipoGeneral.EN_CURSO) {
			avanzarTurno();
		}
		return r;
	}
}