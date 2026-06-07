package modelo;

/**
 * Equipable básico. Slots permitidos (MVP): - "arma" - "accesorio"
 */
public class ItemEquipable extends Item {

	public static final String SLOT_ARMA = "arma";
	public static final String SLOT_ACCESORIO = "accesorio";

	private final String slot;

	private final int bonAtaque;
	private final int bonDefensa;
	private final int bonVelocidad;

	public ItemEquipable(String nombre, String descripcion, String slot, int bonAtaque, int bonDefensa,
			int bonVelocidad) {
		super(nombre, descripcion);
		if (SLOT_ARMA.equals(slot) || SLOT_ACCESORIO.equals(slot)) {
			this.slot = slot;
		} else {
			this.slot = SLOT_ACCESORIO; // Valor por defecto si el slot no es válido
		}
		this.bonAtaque = bonAtaque;
		this.bonDefensa = bonDefensa;
		this.bonVelocidad = bonVelocidad;
	}

	public String getSlot() {
		return slot;
	}

	public int getBonAtaque() {
		return bonAtaque;
	}

	public int getBonDefensa() {
		return bonDefensa;
	}

	public int getBonVelocidad() {
		return bonVelocidad;
	}

	// Ejemplos para Fogata o recompensas fijas
	public static ItemEquipable cuchilloCriollo() {
		return new ItemEquipable("Cuchillo criollo", "Arma simple (+3 ATQ).", SLOT_ARMA, 3, 0, 0);
	}

	public static ItemEquipable amuletoGauchito() {
		return new ItemEquipable("Amuleto del Gauchito", "Accesorio (+1 DEF, +1 VEL).", SLOT_ACCESORIO, 0, 1, 1);
	}
}