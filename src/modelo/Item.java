package modelo;

import java.io.Serializable;

/**
 * Item base (abstract). MVP: no usamos TipoGeneral acá para evitar mezclar
 * responsabilidades; la distinción la hacemos por herencia (Consumible /
 * Equipable).
 */
public abstract class Item implements Serializable {
	private static final long serialVersionUID = 1L;

	private final String nombre;
	private final String descripcion;

	protected Item(String nombre, String descripcion) {
		this.nombre = (nombre != null ? nombre : "");
		this.descripcion = (descripcion != null ? descripcion : "");
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