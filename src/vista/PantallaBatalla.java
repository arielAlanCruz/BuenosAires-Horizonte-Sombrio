package vista;

import controlador.ControladorJuego;
import dto.EntidadDTO;
import dto.EstadoBatallaDTO;
import dto.ResultadoTurno;
import java.awt.*;
import javax.swing.*;
import javax.swing.border.TitledBorder;

public class PantallaBatalla extends JPanel {

	// Constantes de diseño centralizadas
	private static final Color COLOR_FONDO = new Color(24, 24, 28);
	private static final Color COLOR_CABECERA = new Color(30, 30, 36);
	private static final Color COLOR_BORDE_DORADO = new Color(241, 196, 15);
	private static final Color COLOR_BARRA_FONDO = new Color(40, 40, 40);

	private static final Color COLOR_HP_ALIADO = new Color(46, 204, 113);
	private static final Color COLOR_MP_ALIADO = new Color(52, 152, 219);
	private static final Color COLOR_HP_ENEMIGO = new Color(231, 76, 60);

	private static final Color COLOR_BTN_ATAQUE = Color.BLACK;
	private static final Color COLOR_BTN_DEFENSA = new Color(41, 128, 185);
	private static final Color COLOR_BTN_HABILIDAD = new Color(142, 68, 173);
	private static final Color COLOR_BTN_ITEM = new Color(230, 126, 34);
	private static final Color COLOR_BTN_ESTADO = new Color(39, 174, 96);
	private static final Color COLOR_BTN_GUARDAR = Color.DARK_GRAY;

	private static final int ANCHO_NOMBRE = 90; // ajustá según el nombre más largo de tu juego

	private final ControladorJuego controlador;
	private final PanelEscenario panelEscenario;
	private final JLabel lblTurnoActual;

	private final JPanel panelAliados;
	private final JPanel panelEnemigos;

	private final JButton btnAtacar;
	private final JButton btnDefender;
	private final JButton btnHabilidad;
	private final JButton btnItem;
	private final JButton btnEstado;
	private final JButton btnGuardar;

	public PantallaBatalla(ControladorJuego controlador) {
		this.controlador = controlador;
		setLayout(new BorderLayout(5, 5));
		setBackground(COLOR_FONDO);

		JPanel panelCabecera = new JPanel(new BorderLayout());
		panelCabecera.setBackground(COLOR_CABECERA);

		lblTurnoActual = new JLabel(" ", SwingConstants.CENTER);
		lblTurnoActual.setFont(new Font("Monospaced", Font.BOLD, 12));
		lblTurnoActual.setForeground(Color.YELLOW);
		panelCabecera.add(lblTurnoActual, BorderLayout.SOUTH);

		add(panelCabecera, BorderLayout.NORTH);

		panelEscenario = new PanelEscenario();
		add(panelEscenario, BorderLayout.CENTER);

		JPanel panelComandos = new JPanel(new GridLayout(1, 3, 10, 10));
		panelComandos.setBackground(COLOR_FONDO);
		panelComandos.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

		panelAliados = new JPanel(new GridLayout(4, 1, 5, 5));
		panelAliados.setOpaque(false);
		panelAliados.setBorder(crearBordeEstilizado("ALIADOS"));

		panelEnemigos = new JPanel(new GridLayout(4, 1, 5, 5));
		panelEnemigos.setOpaque(false);
		panelEnemigos.setBorder(crearBordeEstilizado("ENEMIGOS"));

		JPanel panelAcciones = new JPanel(new GridLayout(3, 2, 6, 6));
		panelAcciones.setOpaque(false);
		panelAcciones.setBorder(crearBordeEstilizado("ACCIONES"));

		btnAtacar = new JButton("ATAQUE");
		btnDefender = new JButton("DEFENSA");
		btnHabilidad = new JButton("HABILIDAD");
		btnItem = new JButton("USAR ÍTEM");
		btnEstado = new JButton("VER ESTADO");
		btnGuardar = new JButton("GUARDAR");

		estilarBoton(btnAtacar, COLOR_BTN_ATAQUE, Color.WHITE);
		estilarBoton(btnDefender, COLOR_BTN_DEFENSA, Color.WHITE);
		estilarBoton(btnHabilidad, COLOR_BTN_HABILIDAD, Color.WHITE);
		estilarBoton(btnItem, COLOR_BTN_ITEM, Color.WHITE);
		estilarBoton(btnEstado, COLOR_BTN_ESTADO, Color.WHITE);
		estilarBoton(btnGuardar, COLOR_BTN_GUARDAR, Color.WHITE);

		// Los Listeners capturan el evento físico de la UI y gestionan la delegación
		btnAtacar.addActionListener(e -> gestionarClicAtaque());
		btnDefender.addActionListener(e -> controlador.procesarDefensa());
		btnHabilidad.addActionListener(e -> gestionarClicHabilidad());
		btnItem.addActionListener(e -> gestionarClicItem());
		btnEstado.addActionListener(e -> controlador.procesarVerEstado());
		btnGuardar.addActionListener(e -> controlador.guardarPartida());

		panelAcciones.add(btnAtacar);
		panelAcciones.add(btnDefender);
		panelAcciones.add(btnHabilidad);
		panelAcciones.add(btnItem);
		panelAcciones.add(btnEstado);
		panelAcciones.add(btnGuardar);

		panelComandos.add(panelAliados);
		panelComandos.add(panelEnemigos);
		panelComandos.add(panelAcciones);

		add(panelComandos, BorderLayout.SOUTH);
	}

	private void gestionarClicAtaque() {
		String[] opciones = controlador.getOpcionesEnemigos();
		if (opciones.length == 0) {
			mostrarMensajeLocal("No hay enemigos vivos a los que atacar.");
			return;
		}

		int idx = JOptionPane.showOptionDialog(this, "Elegí un objetivo para atacar:", "Atacar",
				JOptionPane.DEFAULT_OPTION, JOptionPane.QUESTION_MESSAGE, null, opciones, opciones[0]);

		// El controlador trabaja con nombres (String), no con índices: le pasamos
		// el nombre elegido, no la posición dentro del diálogo.
		if (idx >= 0) {
			controlador.procesarAtaque(opciones[idx]);
		}
	}

	private void gestionarClicHabilidad() {
		String[] habilidades = controlador.getOpcionesHabilidades();

		if (habilidades.length == 0) {
			mostrarMensajeLocal("No hay habilidades disponibles.");
			return;
		}
		int idxHab = JOptionPane.showOptionDialog(this, "Elegí una habilidad:", "Habilidad",
				JOptionPane.DEFAULT_OPTION, JOptionPane.QUESTION_MESSAGE, null, habilidades, habilidades[0]);

		if (idxHab < 0) {
			return;
		}

		// El controlador se encarga de distinguir entre habilidades de ataque
		String[] objetivos;
		String mensajeObjetivo;

		if (controlador.habilidadEsCuracion(idxHab)) {
			objetivos = controlador.getOpcionesAliados();
			mensajeObjetivo = "Elegi un aliado a curar!";
		} else {
			objetivos = controlador.getOpcionesEnemigos();
			mensajeObjetivo = "Elegi un enemigo a atacar!";
		}

		if (objetivos.length == 0) {
			mostrarMensajeLocal("No hay objetivos vivos disponibles.");
			return;
		}

		int idxObj = JOptionPane.showOptionDialog(this, mensajeObjetivo, "Objetivo", JOptionPane.DEFAULT_OPTION,
				JOptionPane.QUESTION_MESSAGE, null, objetivos, objetivos[0]);

		// idxHabilidad sí es un índice (así lo espera el motor); el objetivo
		// se identifica por nombre, igual que en el ataque.
		if (idxObj >= 0) {
			controlador.procesarHabilidad(idxHab, objetivos[idxObj]);
		}
	}

	private void gestionarClicItem() {
		String[] items = controlador.getOpcionesItems();
		if (items.length == 0) {
			mostrarMensajeLocal("No hay ítems en el inventario compartido.");
			return;
		}
		int idxItem = mostrarListaSeleccion("Elegí un ítem para consumir", items);
		if (idxItem < 0) {
			return;
		}

		String[] aliados = controlador.getOpcionesAliados();
		if (aliados.length == 0) {
			mostrarMensajeLocal("No hay personajes vivos para utilizar el ítem.");
			return;
		}
		int idxObj = JOptionPane.showOptionDialog(this, "Elegí el objetivo del ítem:", "Objetivo",
				JOptionPane.DEFAULT_OPTION, JOptionPane.QUESTION_MESSAGE, null, aliados, aliados[0]);

		// idxItem sí es índice dentro del inventario; el objetivo va por nombre.
		if (idxObj >= 0) {
			controlador.procesarItem(idxItem, aliados[idxObj]);
		}
	}

	public void iniciarTemporizadorTurnoEnemigo() {
		habilitarBotonesAccion(false);
		Timer timer = new Timer(1000, e -> {
			controlador.ejecutarTurnoEnemigoAutomatico();
		});
		timer.setRepeats(false);
		timer.start();
	}

	// Nombre alineado exactamente con el que invoca ControladorJuego.
	// (El controlador la llama "Transition", no "Transición" — se mantiene
	// así a propósito para no tocar el controlador ya validado.)
	public void iniciarTemporizadorTransitionFinBatalla() {
		habilitarBotonesAccion(false);
		Timer timer = new Timer(1500, e -> {
			controlador.cambiarAPantallaResultadoFinal();
		});
		timer.setRepeats(false);
		timer.start();
	}

	// Antes recibía PartyPersonajes/PartyEnemigos/Entidad (modelo) directo.
	// Ahora recibe el EstadoBatallaDTO que ya armó el controlador: la vista
	// nunca vuelve a tener una referencia al modelo real.
	public void actualizarBarras(EstadoBatallaDTO estado) {
		if (estado == null) {
			return;
		}

		panelEscenario.actualizarEscenario(estado.getAliados(), estado.getEnemigos(), estado.getNivelActual());

		String nombreTurno = estado.getNombreEntidadTurnoActual();
		if (nombreTurno != null && !nombreTurno.isEmpty()) {
			lblTurnoActual.setText("Turno activo: " + nombreTurno);
		}

		panelAliados.removeAll();
		for (EntidadDTO p : estado.getAliados()) {
			panelAliados.add(crearFilaEstadoAliado(p));
		}

		panelEnemigos.removeAll();
		for (EntidadDTO e : estado.getEnemigos()) {
			panelEnemigos.add(crearFilaEstadoEnemigo(e));
		}

		panelAliados.revalidate();
		panelAliados.repaint();
		panelEnemigos.revalidate();
		panelEnemigos.repaint();
	}

	private JPanel crearFilaEstadoAliado(EntidadDTO p) {
		JPanel panel = new JPanel(new BorderLayout(6, 0));
		panel.setOpaque(false);

		JLabel lblNombre = new JLabel(p.getNombre());
		lblNombre.setForeground(Color.WHITE);
		lblNombre.setFont(new Font("Monospaced", Font.BOLD, 11));
		lblNombre.setPreferredSize(new Dimension(ANCHO_NOMBRE, lblNombre.getPreferredSize().height));

		JPanel barras = new JPanel();
		barras.setLayout(new BoxLayout(barras, BoxLayout.Y_AXIS));
		barras.setOpaque(false);
		barras.add(crearFilaBarra("HP", p.getVidaActual(), p.getVidaMax(), COLOR_HP_ALIADO));
		barras.add(crearFilaBarra("MP", p.getManaActual(), p.getManaMax(), COLOR_MP_ALIADO));

		panel.add(lblNombre, BorderLayout.WEST);
		panel.add(barras, BorderLayout.CENTER);

		return panel;
	}

	private JPanel crearFilaEstadoEnemigo(EntidadDTO e) {
		JPanel panel = new JPanel(new BorderLayout(6, 0));
		panel.setOpaque(false);

		JLabel lblNombre = new JLabel(e.getNombre());
		lblNombre.setForeground(new Color(230, 126, 34));
		lblNombre.setFont(new Font("Monospaced", Font.BOLD, 11));
		lblNombre.setPreferredSize(new Dimension(ANCHO_NOMBRE, lblNombre.getPreferredSize().height));

		panel.add(lblNombre, BorderLayout.WEST);
		panel.add(crearFilaBarra("HP", e.getVidaActual(), e.getVidaMax(), COLOR_HP_ENEMIGO));
		return panel;
	}

	private JPanel crearFilaBarra(String etiqueta, int valorActual, int valorMax, Color color) {
		JPanel fila = new JPanel(new BorderLayout(3, 0));
		fila.setOpaque(false);

		JLabel lbl = new JLabel(etiqueta);
		lbl.setForeground(color);
		lbl.setFont(new Font("Monospaced", Font.BOLD, 9));
		lbl.setPreferredSize(new Dimension(20, 12));

		JProgressBar barra = new JProgressBar(0, valorMax);
		barra.setValue(valorActual);
		barra.setForeground(color);
		barra.setBackground(COLOR_BARRA_FONDO);
		barra.setBorder(BorderFactory.createLineBorder(Color.BLACK));
		barra.setStringPainted(true);
		barra.setFont(new Font("Monospaced", Font.BOLD, 8));
		barra.setUI(new javax.swing.plaf.basic.BasicProgressBarUI() {
			@Override
			protected Color getSelectionBackground() {
				return Color.WHITE;
			}

			@Override
			protected Color getSelectionForeground() {
				return Color.WHITE;
			}
		});

		fila.add(lbl, BorderLayout.WEST);
		fila.add(barra, BorderLayout.CENTER);
		return fila;
	}

	public void mostrarResultadoTurno(ResultadoTurno resultado) {
		if (resultado == null)
			return;
		String msg = resultado.getMensaje();
		if (msg != null && !msg.trim().isEmpty()) {
			panelEscenario.setMensajeCombate(msg);
		}
	}

	public void dispararAnimacionAtaque(String nombreAtacante) {
		panelEscenario.dispararAnimacionAtaque(nombreAtacante);
	}

	public void habilitarBotonesAccion(boolean habilitar) {
		btnAtacar.setEnabled(habilitar);
		btnDefender.setEnabled(habilitar);
		btnHabilidad.setEnabled(habilitar);
		btnItem.setEnabled(habilitar);
		btnEstado.setEnabled(true);
		btnGuardar.setEnabled(true);
	}

	// Selector en forma de lista con scroll. Se usa para ítems porque, con
	// nombre + descripción concatenados, una fila de botones
	// (JOptionPane.showOptionDialog) terminaba ocupando todo el ancho de la
	// pantalla y se volvía imposible elegir. Habilidad, enemigos y aliados
	// siguen usando el diálogo de botones de siempre.
	private int mostrarListaSeleccion(String titulo, String[] opciones) {
		JList<String> lista = new JList<>(opciones);
		lista.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		lista.setSelectedIndex(0);
		lista.setFont(new Font("Monospaced", Font.PLAIN, 12));
		lista.setBackground(COLOR_CABECERA);
		lista.setForeground(Color.WHITE);
		lista.setSelectionBackground(COLOR_BORDE_DORADO);
		lista.setSelectionForeground(Color.BLACK);
		lista.setVisibleRowCount(Math.min(opciones.length, 6));
		lista.setBorder(BorderFactory.createEmptyBorder(4, 8, 4, 8));

		JScrollPane scroll = new JScrollPane(lista);
		scroll.getViewport().setBackground(COLOR_CABECERA);
		Dimension preferido = scroll.getPreferredSize();
		scroll.setPreferredSize(new Dimension(420, preferido.height));

		int resultado = JOptionPane.showConfirmDialog(this, scroll, titulo,
				JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);

		if (resultado == JOptionPane.OK_OPTION) {
			return lista.getSelectedIndex();
		}
		return -1;
	}

	private void mostrarMensajeLocal(String msg) {
		JOptionPane.showMessageDialog(this, msg);
	}

	private TitledBorder crearBordeEstilizado(String titulo) {
		TitledBorder border = BorderFactory.createTitledBorder(BorderFactory.createLineBorder(COLOR_BORDE_DORADO, 1),
				titulo);
		border.setTitleColor(COLOR_BORDE_DORADO);
		border.setTitleFont(new Font("Arial", Font.BOLD, 11));
		return border;
	}

	private void estilarBoton(JButton boton, Color fondo, Color texto) {
		boton.setBackground(fondo);
		boton.setForeground(texto);
		boton.setFocusPainted(false);
		boton.setFont(new Font("Arial", Font.BOLD, 12));
		boton.setBorder(BorderFactory.createLineBorder(Color.BLACK, 1));
	}
}