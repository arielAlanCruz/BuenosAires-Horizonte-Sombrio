package modelo;

public class ItemConsumible extends Item {

	private final int vidaRecuperada;
	private final int manaRecuperado;

	public ItemConsumible(String nombre, String descripcion, int vidaRecuperada, int manaRecuperado) {
		super(nombre, descripcion);

		// Reemplazamos Math.max por if/else tradicional
		if (vidaRecuperada < 0) {
			this.vidaRecuperada = 0;
		} else {
			this.vidaRecuperada = vidaRecuperada;
		}

		if (manaRecuperado < 0) {
			this.manaRecuperado = 0;
		} else {
			this.manaRecuperado = manaRecuperado;
		}
	}

	public int getVidaRecuperada() {
		return vidaRecuperada;
	}

	public int getManaRecuperado() {
		return manaRecuperado;
	}

	public void aplicarA(Personaje objetivo) {
		if (objetivo == null) {
			return;
		}

		if (objetivo.estaVivo() == false) {
			return;
		}

		if (vidaRecuperada > 0) {
			objetivo.curar(vidaRecuperada);
		}

		if (manaRecuperado > 0) {
			objetivo.recuperarMana(manaRecuperado);
		}
	}

	@Override
	public String consumir(Personaje objetivo) {
		if (objetivo == null) {
			System.out.println("DEBUG: Falló. Objetivo es nulo.");
			return "No se puede usar en este objetivo.";
		}

		if (objetivo.estaVivo() == false) {
			System.out.println("DEBUG: Falló. Objetivo muerto.");
			return "No se puede usar en este objetivo.";
		}

		int vidaAntes = objetivo.getVidaActual();
		int manaAntes = objetivo.getManaActual();

		this.aplicarA(objetivo);

		// Calculamos cuánto ganó realmente sin usar Math.max()
		int vidaGanada = objetivo.getVidaActual() - vidaAntes;
		if (vidaGanada < 0) {
			vidaGanada = 0;
		}

		int manaGanado = objetivo.getManaActual() - manaAntes;
		if (manaGanado < 0) {
			manaGanado = 0;
		}

		// Armamos el texto sumando palabras de forma tradicional
		String resultado = objetivo.getNombre() + " usa " + getNombre() + ".";

		if (vidaGanada > 0) {
			resultado = resultado + " +" + vidaGanada + " HP.";
		}

		if (manaGanado > 0) {
			resultado = resultado + " +" + manaGanado + " MP.";
		}

		if (vidaGanada == 0 && manaGanado == 0) {
			resultado = resultado + " Sin efecto.";
		}

		System.out.println("DEBUG: Consumo exitoso -> " + resultado);

		return resultado;
	}

	// Consumibles fijos
	public static ItemConsumible tortaFrita() {
		return new ItemConsumible("Torta frita", "Recupera 25 HP.", 25, 0);
	}

	public static ItemConsumible mate() {
		return new ItemConsumible("Mate", "Recupera 25 MP.", 0, 25);
	}

	public static ItemConsumible guiso() {
		return new ItemConsumible("Guiso", "Recupera 20 HP y 10 MP.", 20, 10);
	}

	public static ItemConsumible pastelito() {
		return new ItemConsumible("Pastelito criollo", "Recupera 45 MP.", 0, 45);
	}

	public static ItemConsumible alfajor() {
		return new ItemConsumible("Alfajor de maicena", "Recupera 40 HP y 20 MP.", 40, 20);
	}

	public static ItemConsumible asado() {
		return new ItemConsumible("Asado de tira", "Recupera 80 HP y 20 MP.", 80, 20);
	}
}