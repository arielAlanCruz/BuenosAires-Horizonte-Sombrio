package modelo;

import java.io.*;

/**
 * Snapshot serializable del estado mínimo de la partida.
 */
public class EstadoPartida implements Serializable {

	private static final long serialVersionUID = 1L;

	private final PartyPersonajes partyPersonajes;
	private final int nivelActual;

	public EstadoPartida(PartyPersonajes partyPersonajes, int nivelActual) {
		this.partyPersonajes = partyPersonajes;
		this.nivelActual = nivelActual;
	}

	public PartyPersonajes getPartyPersonajes() {
		return partyPersonajes;
	}

	public int getNivelActual() {
		return nivelActual;
	}

	public void guardar(String rutaArchivo) throws IOException {
		try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(rutaArchivo))) {
			oos.writeObject(this);
		}
	}

	public static EstadoPartida cargar(String rutaArchivo) throws IOException, ClassNotFoundException {
		try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(rutaArchivo))) {
			Object obj = ois.readObject();
			return (EstadoPartida) obj;
		}
	}
}