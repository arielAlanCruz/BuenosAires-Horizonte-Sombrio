package vista;

import dto.EntidadDTO;
import java.awt.*;
import java.io.File;
import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.imageio.ImageIO;
import javax.swing.*;

public class PanelEscenario extends JPanel {

	private Image imgFondo;

	// Antes esto era PartyPersonajes/PartyEnemigos (modelo). La vista no debe
	// tener una referencia al modelo real: solo recibe los EntidadDTO que el
	// controlador ya armó en refrescarPantallaBatalla().
	private List<EntidadDTO> aliadosActual;
	private List<EntidadDTO> enemigosActual;

	private String nombreAtacando = null;
	private String mensajeCombate = "¡Comienza el combate!";

	private final Map<String, Image> cacheImagenes = new HashMap<>();

	public PanelEscenario() {
		setPreferredSize(new Dimension(800, 320));
		setBackground(Color.DARK_GRAY);
	}

	public void actualizarEscenario(List<EntidadDTO> aliados, List<EntidadDTO> enemigos, int nivelActual) {
		this.aliadosActual = aliados;
		this.enemigosActual = enemigos;

		// Selección estricta del fondo en base a la secuencia oficial de combates (1 a
		// 7)
		String rutaFondo = "/img/Nivel1.png"; // Ombú seco por defecto (Zona 1: Combates 1 y 2)

		if (nivelActual == 3 || nivelActual == 4) {
			rutaFondo = "/img/Nivel2.png"; // Villa Miseria (Zona 2: Combates 3 y 4)
		} else if (nivelActual == 5 || nivelActual == 6) {
			rutaFondo = "/img/Nivel3.png"; // Obelisco lluvioso (Zona 3: Combates 5 y 6)
		} else if (nivelActual >= 7) {
			rutaFondo = "/img/NivelFinal.png"; // Casa Rosada (Zona Final: Combate 7)
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

		if (aliadosActual != null) {
			double[] xRatios = { 0.10, 0.15, 0.20, 0.25 };
			double[] yRatios = { 0.72, 0.78, 0.72, 0.78 };
			int index = 0;
			for (int i = 0; i < aliadosActual.size(); i++) {
				EntidadDTO p = aliadosActual.get(i);
				if (p.isEstaVivo() && index < yRatios.length) {
					int x = (int) (getWidth() * xRatios[index]);
					int y = (int) (getHeight() * yRatios[index]) - (size / 2);
					dibujarEntidad(g2d, p, x, y, size, true);
				}
				index++;
			}
		}

		if (enemigosActual != null) {
			double[] xRatios = { 0.90, 0.85, 0.80, 0.75 };
			double[] yRatios = { 0.78, 0.72, 0.78, 0.72 };
			int index = 0;
			for (int i = 0; i < enemigosActual.size(); i++) {
				EntidadDTO e = enemigosActual.get(i);
				if (e.isEstaVivo() && index < yRatios.length) {
					int x = (int) (getWidth() * xRatios[index]);
					int y = (int) (getHeight() * yRatios[index]) - (size / 2);
					dibujarEntidad(g2d, e, x, y, size, false);
				}
				index++;
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
			int paso = (int) (getWidth() * 0.06);
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
