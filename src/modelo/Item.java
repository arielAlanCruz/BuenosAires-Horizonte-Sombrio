package modelo;

import java.io.Serializable;

public abstract class Item implements Serializable {
	private static final long serialVersionUID = 1L;

	private final String nombre;
	private final String descripcion;

	protected Item(String nombre, String descripcion) {
		// Validación con if clásico sin operadores ternarios
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

	// Por defecto, un ítem no se puede consumir
	public String consumir(Personaje objetivo) {
		System.out.println("DEBUG: Intentando consumir " + this.nombre + " -> Falló, no es consumible.");
		return null;
	}

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