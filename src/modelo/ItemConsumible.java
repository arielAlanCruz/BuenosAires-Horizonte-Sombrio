package modelo;

/**
 * Consumible simple. - Puede curar vida y/o recuperar mana. - NO remueve del
 * inventario: eso lo hace Inventario.usarConsumible().
 */
public class ItemConsumible extends Item {

	private final int vidaRecuperada;
	private final int manaRecuperado;

	public ItemConsumible(String nombre, String descripcion, int vidaRecuperada, int manaRecuperado) {
		super(nombre, descripcion);
		this.vidaRecuperada = Math.max(0, vidaRecuperada);
		this.manaRecuperado = Math.max(0, manaRecuperado);
	}

	public int getVidaRecuperada() {
		return vidaRecuperada;
	}

	public int getManaRecuperado() {
		return manaRecuperado;
	}

	public void aplicarA(Personaje objetivo) {
		if (objetivo == null)
			return;
		if (!objetivo.estaVivo())
			return;

		if (vidaRecuperada > 0)
			objetivo.curar(vidaRecuperada);
		if (manaRecuperado > 0)
			objetivo.recuperarMana(manaRecuperado);
	}

	// consumibles criollos de alto rendimiento en fogata
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