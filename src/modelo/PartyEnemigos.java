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

	public static PartyEnemigos crearParaNivel(int nivel) {
		PartyEnemigos party = new PartyEnemigos();
		int n = Math.max(1, nivel);

		String[] letras = { " A", " B", " C" };

		// Algoritmo de generación lineal de enemigos por combate
		if (n == 1 || n == 2) {
			// Zona 1: Ombú (3 Duendes comunes)
			for (int i = 0; i < 3; i++) {
				party.enemigos.add(party.crearEnemigo(TipoEnemigo.COMUN, n, letras[i]));
			}
		} else if (n == 3 || n == 4) {
			// Zona 2: Villa Miseria (3 Bandidos comunes)
			for (int i = 0; i < 3; i++) {
				party.enemigos.add(party.crearEnemigo(TipoEnemigo.COMUN_NIVEL2, n, letras[i]));
			}
		} else if (n == 5) {
			// Zona 3: Obelisco lluvioso (3 Mutantes comunes)
			for (int i = 0; i < 3; i++) {
				party.enemigos.add(party.crearEnemigo(TipoEnemigo.COMUN_NIVEL3, n, letras[i]));
			}
		} else if (n == 6) {
			// Zona 3 Jefe: Pomberito (Enemigo único)
			party.enemigos.add(party.crearEnemigo(TipoEnemigo.POMBERITO, n, ""));
		} else {
			// Zona Final: Casa Rosada (Jefe Final)
			party.enemigos.add(party.crearEnemigo(TipoEnemigo.JEFEFINAL, n, ""));
		}

		return party;
	}

	public List<Enemigo> getEnemigos() {
		return Collections.unmodifiableList(enemigos);
	}

	public List<Enemigo> getVivos() {
		List<Enemigo> vivos = new ArrayList<>();
		for (int i = 0; i < enemigos.size(); i++) {
			Enemigo e = enemigos.get(i);
			if (e != null) {
				if (e.estaVivo()) {
					vivos.add(e);
				}
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

	private Enemigo crearEnemigo(TipoEnemigo tipo, int nivel, String sufijo) {
		int n = Math.max(1, nivel);

		switch (tipo) {
			case POMBERITO:
				return new Enemigo("Pomberito (Jefe)", TipoEnemigo.POMBERITO, n, 220 + (n * 10), 18 + (n * 2), 10 + n,
						14, 120, true);

			case JEFEFINAL:
				return new Enemigo("Nigromante (Jefe Final)", TipoEnemigo.JEFEFINAL, n, 300 + (n * 15), 22 + (n * 3),
						12 + n, 16, 200, true);

			case COMUN_NIVEL2:
				return new Enemigo("Bandido" + sufijo, TipoEnemigo.COMUN_NIVEL2, n, 90 + (n * 10), 14 + (n * 3), 8 + n,
						12, 40 + (n * 5), false);

			case COMUN_NIVEL3:
				return new Enemigo("Mutante Ombú" + sufijo, TipoEnemigo.COMUN_NIVEL3, n, 110 + (n * 12), 16 + (n * 3),
						9 + n, 11, 55 + (n * 5), false);

			case COMUN:
			default:
				// Duendes otorgan 20 EXP cada uno, asegurando subir a nivel 2 en el Combate 2
				// (60 + 60 = 120 EXP total)
				return new Enemigo("Duende Sombrío" + sufijo, TipoEnemigo.COMUN, n, 70 + (n * 8), 12 + (n * 2), 6 + n,
						10 + RNG.nextInt(6), 20, false);
		}
	}
}