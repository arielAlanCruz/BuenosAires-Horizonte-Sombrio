package modelo;

import java.io.Serializable;

public abstract class Item implements Serializable {
	private static final long serialVersionUID = 1L;

	private final String nombre;
	private final String descripcion;

	protected Item(String nombre, String descripcion) {
		if (nombre != null) {
			this.nombre = nombre;
		} else {
			this.nombre = "";
		}

		if (descripcion != null) {
			this.descripcion = descripcion;
		} else {
			this.descripcion = "";
		}
	}

	// Método polimórfico universal de uso
	public abstract String usar(Personaje objetivo, Inventario inv);

	public String getNombre() {
		return nombre;
	}

	public String getDescripcion() {
		return descripcion;
	}

	@Override
	public String toString() {
		return nombre;
	}
}