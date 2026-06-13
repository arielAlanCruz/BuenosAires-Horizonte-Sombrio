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
		return !items.isEmpty();
	}

	public void agregar(Item item) {
		if (item != null) {
			items.add(item);
		}
	}

	public Item getItem(int indice) {
		if (indice < 0 || indice >= items.size()) {
			return null;
		}
		return items.get(indice);
	}

	public void eliminar(int indice) {
		if (indice >= 0 && indice < items.size()) {
			items.remove(indice);
		}
	}

	public String usarItem(int indice, Personaje objetivo) {
		Item item = getItem(indice);
		if (item == null) {
			return "Ítem no encontrado.";
		}

		String resultadoUso = item.usar(objetivo, this);

		if (resultadoUso != null) {
			eliminar(indice);
			return resultadoUso;
		} else {
			return "Este ítem no se puede usar.";
		}
	}
}