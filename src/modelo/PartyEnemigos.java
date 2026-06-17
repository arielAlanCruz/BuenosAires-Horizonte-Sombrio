package modelo;

import enums.TipoEnemigo;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class PartyEnemigos {

	private final List<Enemigo> enemigos = new ArrayList<>();
	private static final Random RNG = new Random();

	public PartyEnemigos() {
	}

	// Factory method para crear una party de enemigos adecuada al nivel actual
	public static PartyEnemigos crearParaNivel(int nivel) {
		PartyEnemigos party = new PartyEnemigos();
		int n = Math.max(1, nivel);

		// Para niveles 1-5, se generan 3 enemigos comunes con sufijos
		// A, B y C para diferenciarlos
		String[] letras = { " A", " B", " C" };

		if (n == 1 || n == 2) {
			for (int i = 0; i < 3; i++) {
				party.enemigos.add(party.crearEnemigo(TipoEnemigo.COMUN_NIVEL1, n, letras[i]));
			}
		} else if (n == 3 || n == 4) {
			for (int i = 0; i < 3; i++) {
				party.enemigos.add(party.crearEnemigo(TipoEnemigo.COMUN_NIVEL2, n, letras[i]));
			}
		} else if (n == 5) {
			for (int i = 0; i < 3; i++) {
				party.enemigos.add(party.crearEnemigo(TipoEnemigo.COMUN_NIVEL3, n, letras[i]));
			}
		} else if (n == 6) {
			party.enemigos.add(party.crearEnemigo(TipoEnemigo.POMBERITO_JEFE, n, ""));
		} else {
			party.enemigos.add(party.crearEnemigo(TipoEnemigo.JEFEFINAL, n, ""));
		}

		return party;
	}

	private Enemigo crearEnemigo(TipoEnemigo tipo, int nivel, String sufijo) {
		int n = Math.max(1, nivel);

		switch (tipo) {
			case COMUN_NIVEL1:
			default:
				// Duendes otorgan mas experiencia pero son más débiles
				return new Enemigo("Duende Sombrío" + sufijo, TipoEnemigo.COMUN_NIVEL1, n, 70 + (n * 8), 12 + (n * 2),
						6 + n, 10 + RNG.nextInt(6), 70);

			case COMUN_NIVEL2:// Bandidos otorgan más experiencia pero son más lentos pero más resistentes
				return new Enemigo("Villero Oscuro" + sufijo, TipoEnemigo.COMUN_NIVEL2, n, 90 + (n * 10), 14 + (n * 3),
						8 + n,
						12, 40 + (n * 5));

			case COMUN_NIVEL3: // Mutantes otorgan menos experiencia pero son más rápidos pero menos
								// resistentes
				return new Enemigo("Porteño Maligno" + sufijo, TipoEnemigo.COMUN_NIVEL3, n, 110 + (n * 12),
						16 + (n * 3),
						9 + n, 11, 55 + (n * 5));

			case POMBERITO_JEFE:
				// Pomberito es un enemigo jefe que aparece en el nivel 6, con estadísticas más
				// altas y una habilidad especial
				return new EnemigoJefe("Pomberito (Jefe)", TipoEnemigo.POMBERITO_JEFE, n, 220 + (n * 10), 18 + (n * 2),
						10 + n, 14, 120);

			case JEFEFINAL:
				// el jefe final es mucho más fuerte y otorga mucha experiencia, pero no es tan
				// rápido
				return new EnemigoJefe("DON AMADEO el Rezado(JEFE FINAL)", TipoEnemigo.JEFEFINAL, n, 300 + (n * 15),
						30 + (n * 3), 15 + n, 16, 200);

		}
	}

	public List<Enemigo> getEnemigos() {
		return Collections.unmodifiableList(enemigos);
	}

	public List<Enemigo> getVivos() {
		List<Enemigo> vivos = new ArrayList<>();
		for (int i = 0; i < enemigos.size(); i++) {
			Enemigo e = enemigos.get(i);
			if (e != null && e.estaVivo()) {
				vivos.add(e);
			}
		}
		return vivos;
	}

	public boolean todosDerrotados() {
		return getVivos().isEmpty();
	}

	public int calcularExpTotal() {
		int total = 0;
		for (int i = 0; i < enemigos.size(); i++) {
			Enemigo e = enemigos.get(i);
			if (e != null) {
				total += e.getExpOtorgada();
			}
		}
		return Math.max(0, total);
	}

}