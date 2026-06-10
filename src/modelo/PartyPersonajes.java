package modelo;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.io.Serializable;

public class PartyPersonajes implements Serializable {

	private static final long serialVersionUID = 1L;

	private final List<Personaje> miembros = new ArrayList<>();
	private final Inventario inventarioCompartido = new Inventario();

	public PartyPersonajes() {
	}

	public static PartyPersonajes crearPartyInicial() {
		PartyPersonajes party = new PartyPersonajes();

		// 1. Crear Guerrero y equipar
		Guerrero guerrero = new Guerrero("Guerrero");
		guerrero.getEquipamiento().equiparArma(ItemEquipable.cuchilloCriollo());
		guerrero.getEquipamiento().equiparAccesorio(ItemEquipable.amuletoGauchito());
		party.agregarMiembro(guerrero);

		// 2. Crear Mago y equipar
		Mago mago = new Mago("Mago");
		mago.getEquipamiento().equiparArma(ItemEquipable.baculoMistico());
		mago.getEquipamiento().equiparAccesorio(ItemEquipable.amuletoGauchito());
		party.agregarMiembro(mago);

		// 3. Crear Arquero y equipar
		Arquero arquero = new Arquero("Arquero");
		arquero.getEquipamiento().equiparArma(ItemEquipable.arcoReforzado());
		arquero.getEquipamiento().equiparAccesorio(ItemEquipable.amuletoGauchito());
		party.agregarMiembro(arquero);

		// 4. Crear Curandera y equipar
		Curandera curandera = new Curandera("Curandera");
		curandera.getEquipamiento().equiparArma(ItemEquipable.cuchilloCriollo()); // Arma básica
		curandera.getEquipamiento().equiparAccesorio(ItemEquipable.talismanPlata());
		party.agregarMiembro(curandera);

		// Inventario compartido inicial
		party.getInventarioCompartido().agregar(ItemConsumible.tortaFrita());
		party.getInventarioCompartido().agregar(ItemConsumible.mate());
		party.getInventarioCompartido().agregar(ItemConsumible.pastelito());
		party.getInventarioCompartido().agregar(ItemConsumible.alfajor());

		return party;
	}

	public List<Personaje> getMiembros() {
		return Collections.unmodifiableList(miembros);
	}

	public Inventario getInventarioCompartido() {
		return inventarioCompartido;
	}

	public void agregarMiembro(Personaje personaje) {
		if (personaje != null) {
			miembros.add(personaje);
		}
	}

	public List<Personaje> getVivos() {
		List<Personaje> vivos = new ArrayList<>();
		for (int i = 0; i < miembros.size(); i++) {
			Personaje p = miembros.get(i);
			if (p != null) {
				if (p.estaVivo()) {
					vivos.add(p);
				}
			}
		}
		return vivos;
	}

	public boolean todosDerrotados() {
		return getVivos().isEmpty();
	}

	public void distribuirExperiencia(int expTotal) {
		int total = Math.max(0, expTotal);
		List<Personaje> vivos = getVivos();
		if (vivos.isEmpty()) {
			return;
		}

		int porPersona = total / vivos.size();
		int resto = total % vivos.size();

		for (int i = 0; i < vivos.size(); i++) {
			int expAGanar = porPersona;
			if (i < resto) {
				expAGanar = porPersona + 1;
			}
			vivos.get(i).ganarExperiencia(expAGanar);
		}
	}
}