package vista;

import dto.EntidadDTO;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import javax.imageio.ImageIO;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

public class PanelEscenario extends JPanel {

	private Image imgFondo;
	private java.util.List<EntidadDTO> partyActual;
	private java.util.List<EntidadDTO> enemigosActual;

	private String nombreAtacando = null;
	private String mensajeCombate = "¡Comienza el combate!";

	private final Map<String, Image> cacheImagenes = new HashMap<>();

	public PanelEscenario() {
		setPreferredSize(new Dimension(800, 320));
		setBackground(Color.DARK_GRAY);
	}

	public void actualizarEscenario(java.util.List<EntidadDTO> party, java.util.List<EntidadDTO> enemigos,
			int nivelActual) {
		this.partyActual = party;
		this.enemigosActual = enemigos;

		String rutaFondo = "/img/Nivel1.png";

		if (nivelActual == 3 || nivelActual == 4) {
			rutaFondo = "/img/Nivel2.png";
		} else if (nivelActual == 5 || nivelActual == 6) {
			rutaFondo = "/img/Nivel3.png";
		} else if (nivelActual >= 7) {
			rutaFondo = "/img/NivelFinal.png";
		}

		this.imgFondo = cargarRecurso(rutaFondo);
		repaint();
	}

	public void setMensajeCombate(String mensaje) {
		this.mensajeCombate = mensaje;
		repaint();
	}

	public void dispararAnimacionAtaque(String nombreAtacante) {
		this.nombreAtacando = nombreAtacante;
		repaint();

		Timer timer = new Timer(300, e -> {
			this.nombreAtacando = null;
			repaint();
		});
		timer.setRepeats(false);
		timer.start();
	}

	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		Graphics2D g2d = (Graphics2D) g;

		g2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

		if (imgFondo != null) {
			g2d.drawImage(imgFondo, 0, 0, getWidth(), getHeight(), this);
		} else {
			g2d.setColor(Color.BLACK);
			g2d.fillRect(0, 0, getWidth(), getHeight());
		}

		int size = Math.max(50, Math.min(110, (int) (getHeight() * 0.22)));

		if (partyActual != null) {
			double[] xRatios = { 0.08, 0.15, 0.08, 0.15 };
			double[] yRatios = { 0.40, 0.52, 0.64, 0.76 };
			for (int i = 0; i < partyActual.size(); i++) {
				EntidadDTO p = partyActual.get(i);
				if (p.isEstaVivo() && i < yRatios.length) {
					int x = (int) (getWidth() * xRatios[i]);
					int y = (int) (getHeight() * yRatios[i]) - (size / 2);
					dibujarEntidad(g2d, p, x, y, size, true);
				}
			}
		}

		if (enemigosActual != null) {
			double[] xRatios = { 0.82, 0.75, 0.82, 0.75 };
			double[] yRatios = { 0.40, 0.52, 0.64, 0.76 };
			for (int i = 0; i < enemigosActual.size(); i++) {
				EntidadDTO e = enemigosActual.get(i);
				if (e.isEstaVivo() && i < yRatios.length) {
					int x = (int) (getWidth() * xRatios[i]);
					int y = (int) (getHeight() * yRatios[i]) - (size / 2);
					dibujarEntidad(g2d, e, x, y, size, false);
				}
			}
		}

		if (mensajeCombate != null && !mensajeCombate.trim().isEmpty()) {
			g2d.setColor(new Color(0, 0, 0, 190));
			g2d.fillRect(0, getHeight() - 35, getWidth(), 35);
			g2d.setColor(Color.YELLOW);
			g2d.setFont(new Font("Monospaced", Font.BOLD, 13));
			g2d.drawString("LOG: " + mensajeCombate, 20, getHeight() - 13);
		}
	}

	private void dibujarEntidad(Graphics2D g2d, EntidadDTO entidad, int x, int y, int size, boolean esAliado) {
		String nombreLower = entidad.getNombre().toLowerCase();
		String archivoSprite = determinarRutaSprite(nombreLower, esAliado);
		Image sprite = cargarRecurso("/img/sprites/" + archivoSprite);

		int xActual = x;
		if (entidad.getNombre().equals(nombreAtacando)) {
			int paso = (int) (getWidth() * 0.04);
			xActual += esAliado ? paso : -paso;
		}

		if (sprite != null) {
			g2d.drawImage(sprite, xActual, y, size, size, this);
		} else {
			g2d.setColor(esAliado ? Color.BLUE : Color.RED);
			g2d.fillRect(xActual, y, size, size);
			g2d.setColor(Color.WHITE);
			g2d.setFont(new Font("Arial", Font.BOLD, 9));
			g2d.drawString(entidad.getNombre(), xActual + 5, y + (size / 2));
		}

		if (entidad.isTieneEscudo()) {
			g2d.setColor(new Color(30, 144, 255, 130));
			g2d.setStroke(new BasicStroke(3));
			g2d.drawOval(xActual - 5, y - 5, size + 10, size + 10);
		}
	}

	private String determinarRutaSprite(String nombre, boolean esAliado) {
		if (nombre.contains("guerrero"))
			return "guerrero.png";
		if (nombre.contains("mago"))
			return "mago.png";
		if (nombre.contains("arquero"))
			return "arquero.png";
		if (nombre.contains("curandera"))
			return "curandera.png";
		if (nombre.contains("pombero"))
			return "pombero.png";
		if (nombre.contains("jefe"))
			return "jefe_final.png";
		return esAliado ? "heroe_generico.png" : "enemigo_comun1.png";
	}

	private Image cargarRecurso(String ruta) {
		if (cacheImagenes.containsKey(ruta)) {
			return cacheImagenes.get(ruta);
		}
		URL url = getClass().getResource(ruta);
		if (url != null) {
			Image img = new ImageIcon(url).getImage();
			cacheImagenes.put(ruta, img);
			return img;
		}
		try {
			File archivo = new File("src" + ruta);
			if (archivo.exists()) {
				Image img = ImageIO.read(archivo);
				cacheImagenes.put(ruta, img);
				return img;
			}
		} catch (Exception e) {
			// Manejado
		}
		return null;
	}
}