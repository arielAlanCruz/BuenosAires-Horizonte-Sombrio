package modelo;

public class ItemEquipable extends Item {

	public static final String SLOT_ARMA = "arma";
	public static final String SLOT_ACCESORIO = "accesorio";

	private final String slot;

	private final int bonAtaque;
	private final int bonDefensa;
	private final int bonVelocidad;

	public ItemEquipable(
			String nombre,
			String descripcion,
			String slot,
			int bonAtaque,
			int bonDefensa,
			int bonVelocidad) {
		super(nombre, descripcion);
		this.slot = slot != null ? slot : SLOT_ACCESORIO;
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

	// Ítems de Equipamiento Criollos/Temáticos
	public static ItemEquipable cuchilloCriollo() {
		return new ItemEquipable("Cuchillo criollo", "Arma simple (+3 ATQ).",
				SLOT_ARMA, 3, 0, 0);
	}

	public static ItemEquipable amuletoGauchito() {
		return new ItemEquipable("Amuleto del Gauchito", "Accesorio (+1 DEF, +1 VEL).",
				SLOT_ACCESORIO, 0, 1, 1);
	}

	public static ItemEquipable baculoMistico() {
		return new ItemEquipable("Báculo de Caranday", "Báculo mágico (+4 ATQ).",
				SLOT_ARMA, 4, 0, 0);
	}

	public static ItemEquipable arcoReforzado() {
		return new ItemEquipable("Arco de Guayacán", "Arco de madera dura (+3 ATQ, +1 VEL).",
				SLOT_ARMA, 3, 0, 1);
	}

	public static ItemEquipable talismanPlata() {
		return new ItemEquipable("Talismán de Plata", "Protección criolla (+2 DEF).",
				SLOT_ACCESORIO, 0, 2, 0);
	}
}