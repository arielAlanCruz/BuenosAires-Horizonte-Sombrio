package vista;

import controlador.ControladorJuego;
import dto.EntidadDTO;
import dto.EstadoBatallaDTO;
import dto.ResultadoTurno;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.util.List;

public class PantallaBatalla extends JPanel {

	private static final Color COLOR_FONDO = new Color(24, 24, 28);
	private static final Color COLOR_CABECERA = new Color(30, 30, 36);
	private static final Color COLOR_BORDE_DORADO = new Color(241, 196, 15);
	private static final Color COLOR_BARRA_FONDO = new Color(40, 40, 40);

	private static final Color COLOR_HP_ALIADO = new Color(46, 204, 113);
	private static final Color COLOR_MP_ALIADO = new Color(52, 152, 219);
	private static final Color COLOR_HP_ENEMIGO = new Color(231, 76, 60);

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

	private List<EntidadDTO> aliadosActuales;
	private List<EntidadDTO> enemigosActuales;
	private List<String> nombresItemsActuales;

	public PantallaBatalla(ControladorJuego controlador) {
		this.controlador = controlador;
		setLayout(new BorderLayout(5, 5));
		setBackground(COLOR_FONDO);

		JPanel panelCabecera = new JPanel(new BorderLayout());
		panelCabecera.setBackground(COLOR_CABECERA);

		JLabel titulo = new JLabel("INTERFAZ DE COMBATE", SwingConstants.CENTER);
		titulo.setFont(new Font("Arial", Font.BOLD, 14));
		titulo.setForeground(Color.WHITE);
		panelCabecera.add(titulo, BorderLayout.NORTH);

		lblTurnoActual = new JLabel(" ", SwingConstants.CENTER);
		lblTurnoActual.setFont(new Font("Arial", Font.ITALIC, 12));
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

		estilarBoton(btnAtacar, Color.BLACK, Color.WHITE);
		estilarBoton(btnDefender, new Color(41, 128, 185), Color.WHITE);
		estilarBoton(btnHabilidad, new Color(142, 68, 173), Color.WHITE);
		estilarBoton(btnItem, new Color(230, 126, 34), Color.WHITE);
		estilarBoton(btnEstado, new Color(39, 174, 96), Color.WHITE);
		estilarBoton(btnGuardar, Color.DARK_GRAY, Color.WHITE);

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
		if (enemigosActuales == null || enemigosActuales.isEmpty()) {
			mostrarMensajeLocal("No hay enemigos disponibles.");
			return;
		}

		List<EntidadDTO> vivos = obtenerVivos(enemigosActuales);
		if (vivos.isEmpty()) {
			mostrarMensajeLocal("No hay enemigos vivos a los que atacar.");
			return;
		}

		String[] opciones = new String[vivos.size()];
		for (int i = 0; i < vivos.size(); i++) {
			opciones[i] = vivos.get(i).getNombre() + " (HP " + vivos.get(i).getVidaActual() + ")";
		}

		int idx = JOptionPane.showOptionDialog(this, "Elegí un objetivo para atacar:", "Atacar",
				JOptionPane.DEFAULT_OPTION, JOptionPane.QUESTION_MESSAGE, null, opciones, opciones[0]);

		if (idx >= 0) {
			controlador.procesarAtaque(vivos.get(idx).getNombre());
		}
	}

	private void gestionarClicHabilidad() {
		if (aliadosActuales == null || enemigosActuales == null) {
			return;
		}

		String nombreTurno = lblTurnoActual.getText().replace("Turno activo: ", "").trim();
		boolean esCuracion = nombreTurno.toLowerCase().contains("curandera");

		List<EntidadDTO> objetivosDisponibles = esCuracion
				? obtenerVivos(aliadosActuales)
				: obtenerVivos(enemigosActuales);

		if (objetivosDisponibles.isEmpty()) {
			mostrarMensajeLocal("No hay objetivos vivos disponibles.");
			return;
		}

		String[] objNombres = new String[objetivosDisponibles.size()];
		for (int i = 0; i < objetivosDisponibles.size(); i++) {
			objNombres[i] = objetivosDisponibles.get(i).getNombre() + " (HP "
					+ objetivosDisponibles.get(i).getVidaActual() + ")";
		}

		int idxObj = JOptionPane.showOptionDialog(this, "Elegí el objetivo de la habilidad especial:", "Habilidad",
				JOptionPane.DEFAULT_OPTION, JOptionPane.QUESTION_MESSAGE, null, objNombres, objNombres[0]);

		if (idxObj >= 0) {
			controlador.procesarHabilidad(0, objetivosDisponibles.get(idxObj).getNombre());
		}
	}

	private void gestionarClicItem() {
		if (nombresItemsActuales == null || nombresItemsActuales.isEmpty()) {
			mostrarMensajeLocal("No hay ítems en el inventario compartido.");
			return;
		}

		// 1. Instanciamos un JList con las descripciones dinámicas
		JList<String> listaItems = new JList<>(nombresItemsActuales.toArray(new String[0]));
		listaItems.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		listaItems.setVisibleRowCount(5); // Altura de fila máxima sin barra

		// 2. Envolvemos la lista en un ScrollPane vertical
		JScrollPane scrollPane = new JScrollPane(listaItems);
		scrollPane.setPreferredSize(new Dimension(380, 120));

		// 3. Mostramos la lista vertical en un diálogo de confirmación
		int opcion = JOptionPane.showConfirmDialog(
				this,
				scrollPane,
				"Inventario Compartido (Pociones y Equipamiento)",
				JOptionPane.OK_CANCEL_OPTION,
				JOptionPane.PLAIN_MESSAGE);

		if (opcion == JOptionPane.OK_OPTION) {
			int idxItem = listaItems.getSelectedIndex();
			if (idxItem < 0) {
				mostrarMensajeLocal("Seleccioná un ítem de la lista para usar.");
				return;
			}

			List<EntidadDTO> vivos = obtenerVivos(aliadosActuales);
			if (vivos.isEmpty()) {
				mostrarMensajeLocal("No hay personajes vivos para utilizar el ítem.");
				return;
			}

			String[] opcionesObjetivo = new String[vivos.size()];
			for (int i = 0; i < vivos.size(); i++) {
				opcionesObjetivo[i] = vivos.get(i).getNombre() + " (HP " + vivos.get(i).getVidaActual() + ")";
			}

			int idxObj = JOptionPane.showOptionDialog(this, "Elegí el objetivo del ítem:", "Objetivo",
					JOptionPane.DEFAULT_OPTION, JOptionPane.QUESTION_MESSAGE, null, opcionesObjetivo,
					opcionesObjetivo[0]);

			if (idxObj >= 0) {
				controlador.procesarItem(idxItem, vivos.get(idxObj).getNombre());
			}
		}
	}

	private List<EntidadDTO> obtenerVivos(List<EntidadDTO> lista) {
		List<EntidadDTO> vivos = new java.util.ArrayList<>();
		for (int i = 0; i < lista.size(); i++) {
			if (lista.get(i).isEstaVivo()) {
				vivos.add(lista.get(i));
			}
		}
		return vivos;
	}

	public void iniciarTemporizadorTurnoEnemigo() {
		habilitarBotonesAccion(false);
		Timer timer = new Timer(1000, e -> {
			controlador.ejecutarTurnoEnemigoAutomatico();
		});
		timer.setRepeats(false);
		timer.start();
	}

	public void iniciarTemporizadorTransitionFinBatalla() {
		habilitarBotonesAccion(false);
		Timer timer = new Timer(1500, e -> {
			controlador.cambiarAPantallaResultadoFinal();
		});
		timer.setRepeats(false);
		timer.start();
	}

	public void actualizarBarras(EstadoBatallaDTO estado) {
		this.aliadosActuales = estado.getAliados();
		this.enemigosActuales = estado.getEnemigos();
		this.nombresItemsActuales = estado.getNombresItemsInventario();

		panelEscenario.actualizarEscenario(estado.getAliados(), estado.getEnemigos(), estado.getNivelActual());

		if (estado.getNombreEntidadTurnoActual() != null && !estado.getNombreEntidadTurnoActual().isEmpty()) {
			lblTurnoActual.setText("Turno activo: " + estado.getNombreEntidadTurnoActual());
		}

		panelAliados.removeAll();
		if (estado.getAliados() != null) {
			for (int i = 0; i < estado.getAliados().size(); i++) {
				panelAliados.add(crearFilaEstadoAliado(estado.getAliados().get(i)));
			}
		}

		panelEnemigos.removeAll();
		if (estado.getEnemigos() != null) {
			for (int i = 0; i < estado.getEnemigos().size(); i++) {
				panelEnemigos.add(crearFilaEstadoEnemigo(estado.getEnemigos().get(i)));
			}
		}

		panelAliados.revalidate();
		panelAliados.repaint();
		panelEnemigos.revalidate();
		panelEnemigos.repaint();
	}

	private JPanel crearFilaEstadoAliado(EntidadDTO p) {
		JPanel panel = new JPanel(new GridBagLayout());
		panel.setOpaque(false);
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.insets = new Insets(1, 3, 1, 3);
		gbc.fill = GridBagConstraints.HORIZONTAL;

		JLabel lblNombre = new JLabel(p.getNombre());
		lblNombre.setForeground(Color.WHITE);
		lblNombre.setFont(new Font("Monospaced", Font.BOLD, 11));
		gbc.gridx = 0;
		gbc.gridy = 0;
		gbc.gridheight = 2;
		gbc.weightx = 0.3;
		panel.add(lblNombre, gbc);

		JLabel lblHP = new JLabel("HP");
		lblHP.setForeground(COLOR_HP_ALIADO);
		lblHP.setFont(new Font("Arial", Font.BOLD, 9));
		gbc.gridx = 1;
		gbc.gridy = 0;
		gbc.gridheight = 1;
		gbc.weightx = 0.05;
		panel.add(lblHP, gbc);

		JProgressBar barHP = new JProgressBar(0, p.getVidaMax());
		barHP.setValue(p.getVidaActual());
		barHP.setForeground(COLOR_HP_ALIADO);
		barHP.setBackground(COLOR_BARRA_FONDO);
		barHP.setBorder(BorderFactory.createLineBorder(Color.BLACK));
		barHP.setStringPainted(true);
		barHP.setFont(new Font("Arial", Font.BOLD, 8));
		barHP.setUI(new javax.swing.plaf.basic.BasicProgressBarUI() {
			@Override
			protected Color getSelectionBackground() {
				return Color.WHITE;
			}

			@Override
			protected Color getSelectionForeground() {
				return Color.WHITE;
			}
		});
		barHP.setPreferredSize(new Dimension(100, 10));
		gbc.gridx = 2;
		gbc.gridy = 0;
		gbc.weightx = 0.65;
		panel.add(barHP, gbc);

		JLabel lblMP = new JLabel("MP");
		lblMP.setForeground(COLOR_MP_ALIADO);
		lblMP.setFont(new Font("Arial", Font.BOLD, 9));
		gbc.gridx = 1;
		gbc.gridy = 1;
		gbc.weightx = 0.05;
		panel.add(lblMP, gbc);

		JProgressBar barMP = new JProgressBar(0, p.getManaMax());
		barMP.setValue(p.getManaActual());
		barMP.setForeground(COLOR_MP_ALIADO);
		barMP.setBackground(COLOR_BARRA_FONDO);
		barMP.setBorder(BorderFactory.createLineBorder(Color.BLACK));
		barMP.setStringPainted(true);
		barMP.setFont(new Font("Arial", Font.BOLD, 8));
		barMP.setUI(new javax.swing.plaf.basic.BasicProgressBarUI() {
			@Override
			protected Color getSelectionBackground() {
				return Color.WHITE;
			}

			@Override
			protected Color getSelectionForeground() {
				return Color.WHITE;
			}
		});
		barMP.setPreferredSize(new Dimension(100, 10));
		gbc.gridx = 2;
		gbc.gridy = 1;
		gbc.weightx = 0.65;
		panel.add(barMP, gbc);

		return panel;
	}

	private JPanel crearFilaEstadoEnemigo(EntidadDTO e) {
		JPanel panel = new JPanel(new GridBagLayout());
		panel.setOpaque(false);
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.insets = new Insets(3, 3, 3, 3);
		gbc.fill = GridBagConstraints.HORIZONTAL;

		JLabel lblNombre = new JLabel(e.getNombre());
		lblNombre.setForeground(new Color(230, 126, 34));
		lblNombre.setFont(new Font("Monospaced", Font.BOLD, 11));
		gbc.gridx = 0;
		gbc.gridy = 0;
		gbc.weightx = 0.3;
		panel.add(lblNombre, gbc);

		JLabel lblHP = new JLabel("HP");
		lblHP.setForeground(COLOR_HP_ENEMIGO);
		lblHP.setFont(new Font("Arial", Font.BOLD, 9));
		gbc.gridx = 1;
		gbc.gridy = 0;
		gbc.weightx = 0.05;
		panel.add(lblHP, gbc);

		JProgressBar barHP = new JProgressBar(0, e.getVidaMax());
		barHP.setValue(e.getVidaActual());
		barHP.setForeground(COLOR_HP_ENEMIGO);
		barHP.setBackground(COLOR_BARRA_FONDO);
		barHP.setBorder(BorderFactory.createLineBorder(Color.BLACK));
		barHP.setStringPainted(true);
		barHP.setFont(new Font("Arial", Font.BOLD, 8));
		barHP.setUI(new javax.swing.plaf.basic.BasicProgressBarUI() {
			@Override
			protected Color getSelectionBackground() {
				return Color.WHITE;
			}

			@Override
			protected Color getSelectionForeground() {
				return Color.WHITE;
			}
		});
		barHP.setPreferredSize(new Dimension(100, 11));
		gbc.gridx = 2;
		gbc.gridy = 0;
		gbc.weightx = 0.65;
		panel.add(barHP, gbc);

		return panel;
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