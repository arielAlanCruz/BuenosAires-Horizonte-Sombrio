package modelo;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Inventario implements Serializable {
	private static final long serialVersionUID = 1L;

	private final List<Item> items = new ArrayList<>();

	public List<Item> getItems() {
		return Collections.unmodifiableList(items);
	}

	public boolean tieneItems() {
		if (items.isEmpty() == true) {
			return false;
		} else {
			return true;
		}
	}

	public void agregar(Item item) {
		if (item != null) {
			items.add(item);
		}
	}

	public Item getItem(int indice) {
		if (indice < 0) {
			return null;
		}
		if (indice >= items.size()) {
			return null;
		}
		return items.get(indice);
	}

	public void eliminar(int indice) {
		if (indice < 0) {
			return;
		}
		if (indice >= items.size()) {
			return;
		}
		items.remove(indice);
	}

	public String usarConsumible(int indice, Personaje objetivo) {
		Item item = getItem(indice);

		if (item == null) {
			return "Ítem no encontrado.";
		}

		System.out.println("DEBUG: Inventario ordenando usar el ítem...");

		// Le pasamos la responsabilidad al ítem
		String resultadoUso = item.consumir(objetivo);

		// Evaluamos el resultado con un if simple
		if (resultadoUso != null) {
			eliminar(indice);
			System.out.println("DEBUG: Inventario eliminó el ítem consumido.");
			return resultadoUso;
		} else {
			return "Ese ítem no es consumible.";
		}
	}
}