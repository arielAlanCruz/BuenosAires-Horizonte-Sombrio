package modelo;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Inventario compartido, ilimitado. MVP: se usa desde
 * PartyPersonajes.getInventarioCompartido().
 */
public class Inventario implements Serializable {
	private static final long serialVersionUID = 1L;

	private final List<Item> items = new ArrayList<>();

	public List<Item> getItems() {
		return Collections.unmodifiableList(items);
	}

	public boolean tieneItems() {
		return !items.isEmpty();
	}

	public void agregar(Item item) {
		if (item != null)
			items.add(item);
	}

	public Item getItem(int indice) {
		if (indice < 0 || indice >= items.size())
			return null;
		return items.get(indice);
	}

	public void eliminar(int indice) {
		if (indice < 0 || indice >= items.size())
			return;
		items.remove(indice);
	}

	/**
	 * Usa un consumible por índice sobre un objetivo. - Valida objetivo vivo. -
	 * Aplica efecto. - Remueve el consumible del inventario.
	 *
	 * Devuelve un mensaje para que el MotorCombate/Controlador armen el
	 * ResultadoTurno.
	 */
	public String usarConsumible(int indice, Personaje objetivo) {
		Item item = getItem(indice);
		if (!(item instanceof ItemConsumible)) {
			return "Ese ítem no es consumible.";
		}

		if (objetivo == null)
			return "Objetivo inválido.";
		if (!objetivo.estaVivo())
			return "No se puede usar un ítem en un personaje muerto.";

		ItemConsumible consumible = (ItemConsumible) item;

		int vidaAntes = objetivo.getVidaActual();
		int manaAntes = objetivo.getManaActual();

		consumible.aplicarA(objetivo);

		int vidaGanada = Math.max(0, objetivo.getVidaActual() - vidaAntes);
		int manaGanado = Math.max(0, objetivo.getManaActual() - manaAntes);

		// Remover del inventario compartido
		eliminar(indice);

		StringBuilder sb = new StringBuilder();
		sb.append(objetivo.getNombre()).append(" usa ").append(consumible.getNombre()).append(". ");
		if (vidaGanada > 0)
			sb.append("+").append(vidaGanada).append(" HP ");
		if (manaGanado > 0)
			sb.append("+").append(manaGanado).append(" MP ");
		if (vidaGanada == 0 && manaGanado == 0)
			sb.append("Sin efecto.");
		return sb.toString().trim();

	}
}