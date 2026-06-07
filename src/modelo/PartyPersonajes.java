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

		party.agregarMiembro(new Guerrero("Guerrero"));
		party.agregarMiembro(new Mago("Mago"));
		party.agregarMiembro(new Arquero("Arquero"));
		party.agregarMiembro(new Curandera("Curandera"));

		// Inventario compartido inicial ampliado
		party.getInventarioCompartido().agregar(ItemConsumible.tortaFrita());
		party.getInventarioCompartido().agregar(ItemConsumible.mate());
		party.getInventarioCompartido().agregar(ItemConsumible.pastelito()); // +45 MP para el Mago o Curandera
		party.getInventarioCompartido().agregar(ItemConsumible.alfajor()); // Consumible mixto

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

		// Bucle estructurado tradicional
		for (int i = 0; i < vivos.size(); i++) {
			int expAGanar = porPersona;
			if (i < resto) {
				expAGanar = porPersona + 1;
			}
			vivos.get(i).ganarExperiencia(expAGanar);
		}
	}
}