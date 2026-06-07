package modelo;

import java.io.Serializable;

/**
 * Equipamiento básico (MVP): arma + accesorio. Las bonificaciones se usan al
 * calcular ataque/defensa/velocidad en el combate (si desean agregarlo luego).
 */
public class Equipamiento implements Serializable {
	private static final long serialVersionUID = 1L;

	private ItemEquipable arma;
	private ItemEquipable accesorio;

	public ItemEquipable getArma() {
		return arma;
	}

	public ItemEquipable getAccesorio() {
		return accesorio;
	}

	public void equiparArma(ItemEquipable item) {
		if (item == null)
			return;
		if (!ItemEquipable.SLOT_ARMA.equals(item.getSlot()))
			return;
		this.arma = item;
	}

	public void equiparAccesorio(ItemEquipable item) {
		if (item == null)
			return;
		if (!ItemEquipable.SLOT_ACCESORIO.equals(item.getSlot()))
			return;
		this.accesorio = item;
	}

	public int getBonificacionAtaque() {
		int bon = 0;
		if (arma != null)
			bon += arma.getBonAtaque();
		if (accesorio != null)
			bon += accesorio.getBonAtaque();
		return bon;
	}

	public int getBonificacionDefensa() {
		int bon = 0;
		if (arma != null)
			bon += arma.getBonDefensa();
		if (accesorio != null)
			bon += accesorio.getBonDefensa();
		return bon;
	}

	public int getBonificacionVelocidad() {
		int bon = 0;
		if (arma != null)
			bon += arma.getBonVelocidad();
		if (accesorio != null)
			bon += accesorio.getBonVelocidad();
		return bon;
	}
}