package vista;

import controlador.ControladorJuego;
import dto.ResultadoTurno;
import modelo.PartyEnemigos;
import modelo.PartyPersonajes;
import modelo.Personaje;
import modelo.Enemigo;
import modelo.Habilidad;
import modelo.Item;
import modelo.Inventario;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;

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
		modelo.MotorCombate motor = modelo.GameEngine.getInstance().getMotorCombate();
		java.util.List<Enemigo> vivos = motor.getPartyEnemigos().getVivos();
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
			controlador.procesarAtaque(idx);
		}
	}

	private void gestionarClicHabilidad() {
		modelo.MotorCombate motor = modelo.GameEngine.getInstance().getMotorCombate();
		modelo.Entidad actual = motor.getEntidadEnTurnoActual();

		if (!(actual instanceof Personaje)) {
			mostrarMensajeLocal("No es el turno de un personaje.");
			return;
		}

		Personaje p = (Personaje) actual;
		java.util.List<Habilidad> habilidades = p.getHabilidades();
		if (habilidades.isEmpty()) {
			mostrarMensajeLocal("No hay habilidades disponibles.");
			return;
		}

		String[] opciones = new String[habilidades.size()];
		for (int i = 0; i < habilidades.size(); i++) {
			Habilidad h = habilidades.get(i);
			opciones[i] = h.getNombre() + " (MP " + h.getCosteMana() + ")";
		}

		int idxHab = JOptionPane.showOptionDialog(this, "Elegí una habilidad:", "Habilidad", JOptionPane.DEFAULT_OPTION,
				JOptionPane.QUESTION_MESSAGE, null, opciones, opciones[0]);

		if (idxHab < 0)
			return;

		Habilidad habSel = habilidades.get(idxHab);
		int objetivoIdx = -1;

		if (habSel.getCantidadCuracion() > 0) {
			java.util.List<Personaje> vivos = motor.getPartyPersonajes().getVivos();
			if (vivos.isEmpty()) {
				mostrarMensajeLocal("No hay aliados vivos para curar.");
				return;
			}
			String[] objOpciones = new String[vivos.size()];
			for (int i = 0; i < vivos.size(); i++) {
				objOpciones[i] = vivos.get(i).getNombre() + " (HP " + vivos.get(i).getVidaActual() + ")";
			}
			objetivoIdx = JOptionPane.showOptionDialog(this, "Elegí un aliado a curar:", "Objetivo",
					JOptionPane.DEFAULT_OPTION, JOptionPane.QUESTION_MESSAGE, null, objOpciones, objOpciones[0]);
		} else {
			java.util.List<Enemigo> vivos = motor.getPartyEnemigos().getVivos();
			if (vivos.isEmpty()) {
				mostrarMensajeLocal("No hay enemigos vivos para atacar.");
				return;
			}
			String[] objOpciones = new String[vivos.size()];
			for (int i = 0; i < vivos.size(); i++) {
				objOpciones[i] = vivos.get(i).getNombre() + " (HP " + vivos.get(i).getVidaActual() + ")";
			}
			objetivoIdx = JOptionPane.showOptionDialog(this, "Elegí un enemigo a atacar:", "Objetivo",
					JOptionPane.DEFAULT_OPTION, JOptionPane.QUESTION_MESSAGE, null, objOpciones, objOpciones[0]);
		}

		if (objetivoIdx >= 0) {
			controlador.procesarHabilidad(idxHab, objetivoIdx);
		}
	}

	private void gestionarClicItem() {
		PartyPersonajes party = modelo.GameEngine.getInstance().getPartyPersonajes();
		Inventario inv = party.getInventarioCompartido();

		if (!inv.tieneItems()) {
			mostrarMensajeLocal("No hay ítems en el inventario compartido.");
			return;
		}

		java.util.List<Item> items = inv.getItems();
		String[] opcionesItems = new String[items.size()];
		for (int i = 0; i < items.size(); i++) {
			opcionesItems[i] = items.get(i).getNombre() + " - " + items.get(i).getDescripcion();
		}

		// CORRECCIÓN: Se cambió JOptionPane.OPTION_TYPE_DEFAULT por
		// JOptionPane.DEFAULT_OPTION
		int idxItem = JOptionPane.showOptionDialog(this, "Elegí un ítem para consumir:", "Inventario Compartido",
				JOptionPane.DEFAULT_OPTION, JOptionPane.QUESTION_MESSAGE, null, opcionesItems, opcionesItems[0]);

		if (idxItem < 0)
			return;

		java.util.List<Personaje> vivos = party.getVivos();
		if (vivos.isEmpty()) {
			mostrarMensajeLocal("No hay personajes vivos para utilizar el ítem.");
			return;
		}

		String[] opcionesObjetivo = new String[vivos.size()];
		for (int i = 0; i < vivos.size(); i++) {
			opcionesObjetivo[i] = vivos.get(i).getNombre() + " (HP " + vivos.get(i).getVidaActual() + ")";
		}

		int idxObj = JOptionPane.showOptionDialog(this, "Elegí el objetivo del ítem:", "Objetivo",
				JOptionPane.DEFAULT_OPTION, JOptionPane.QUESTION_MESSAGE, null, opcionesObjetivo, opcionesObjetivo[0]);

		if (idxObj >= 0) {
			controlador.procesarItem(idxItem, idxObj);
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

	public void iniciarTemporizadorTransicionFinBatalla() {
		habilitarBotonesAccion(false);
		Timer timer = new Timer(1500, e -> {
			controlador.cambiarAPantallaResultadoFinal();
		});
		timer.setRepeats(false);
		timer.start();
	}

	public void actualizarBarras(PartyPersonajes party, PartyEnemigos enemigos, modelo.Entidad entidadEnTurno,
			int nivelActual) {
		panelEscenario.actualizarEscenario(party, enemigos, nivelActual);

		if (entidadEnTurno != null) {
			lblTurnoActual.setText("Turno activo: " + entidadEnTurno.getNombre());
		}

		panelAliados.removeAll();
		if (party != null) {
			for (int i = 0; i < party.getMiembros().size(); i++) {
				panelAliados.add(crearFilaEstadoAliado(party.getMiembros().get(i)));
			}
		}

		panelEnemigos.removeAll();
		if (enemigos != null) {
			for (int i = 0; i < enemigos.getEnemigos().size(); i++) {
				panelEnemigos.add(crearFilaEstadoEnemigo(enemigos.getEnemigos().get(i)));
			}
		}

		panelAliados.revalidate();
		panelAliados.repaint();
		panelEnemigos.revalidate();
		panelEnemigos.repaint();
	}

	private JPanel crearFilaEstadoAliado(Personaje p) {
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

	private JPanel crearFilaEstadoEnemigo(Enemigo e) {
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