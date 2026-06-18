package vista;

import controlador.ControladorJuego;
import java.awt.*;
import java.net.URL;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.MatteBorder;

/**
 * Vista pura de resultado de batalla.
 * No contiene lógica de negocio: solo renderiza lo que el controlador le entrega.
 */
public class PantallaResultado extends JPanel {

    // ── Paleta de colores ────────────────────────────────────────────────────────
    private static final Color FONDO            = new Color(18, 18, 22);
    private static final Color PANEL_BG         = new Color(28, 28, 34);
    private static final Color DORADO           = new Color(212, 175, 55);
    private static final Color DORADO_OSCURO    = new Color(140, 110, 20);
    private static final Color TEXTO_PRIMARIO   = new Color(230, 225, 210);
    private static final Color TEXTO_SECUNDARIO = new Color(140, 135, 120);
    private static final Color VERDE_EXP        = new Color(80, 180, 100);
    private static final Color ROJO_DERROTA     = new Color(180, 50, 50);
    private static final Color BORDE_SUTIL      = new Color(55, 55, 65);

    // ── Fuentes ──────────────────────────────────────────────────────────────────
    private static final Font FUENTE_TITULO  = new Font("Monospaced", Font.BOLD, 28);
    private static final Font FUENTE_SECCION = new Font("Monospaced", Font.BOLD, 12);
    private static final Font FUENTE_FILA    = new Font("Monospaced", Font.PLAIN, 13);
    private static final Font FUENTE_FILA_B  = new Font("Monospaced", Font.BOLD, 13);
    private static final Font FUENTE_BOTON   = new Font("Arial", Font.BOLD, 12);

    // ── Componentes ──────────────────────────────────────────────────────────────
    private final JLabel  lblTitulo;
    private final JPanel  panelTabla;
    private final PanelTablaFondo panelTablaFondo;
    private final JButton btnGuardar;
    private final JButton btnContinuar;
    private final JButton btnMenu;

    public PantallaResultado(ControladorJuego controlador) {
        setLayout(new BorderLayout(0, 0));
        setBackground(FONDO);

        // ── NORTE: título ────────────────────────────────────────────────────────
        JPanel norte = new JPanel(new BorderLayout());
        norte.setBackground(PANEL_BG);
        norte.setBorder(new MatteBorder(0, 0, 2, 0, DORADO_OSCURO));

        lblTitulo = new JLabel("RESULTADO", SwingConstants.CENTER);
        lblTitulo.setFont(FUENTE_TITULO);
        lblTitulo.setForeground(DORADO);
        lblTitulo.setBorder(new EmptyBorder(22, 0, 18, 0));
        norte.add(lblTitulo, BorderLayout.CENTER);
        add(norte, BorderLayout.NORTH);

        // ── CENTRO: tabla dinámica de información ────────────────────────────────
        // Se reconstruye en cada llamada a mostrar*()
        panelTablaFondo = new PanelTablaFondo("/img/Victoria-sprite.png");
        panelTablaFondo.setLayout(new BorderLayout());

        panelTabla = new JPanel();
        panelTabla.setLayout(new BoxLayout(panelTabla, BoxLayout.Y_AXIS));
        panelTabla.setOpaque(false);
        panelTabla.setBorder(new EmptyBorder(24, 60, 24, 60));
        panelTablaFondo.add(panelTabla, BorderLayout.CENTER);

        JScrollPane scroll = new JScrollPane(panelTablaFondo);
        scroll.setBorder(null);
        scroll.getViewport().setOpaque(false);
        scroll.setOpaque(false);
        add(scroll, BorderLayout.CENTER);

        // Seccion SUR: botones de accion 
        // btnContinuar es la acción principal, destacada en dorado
        JPanel sur = new JPanel(new GridLayout(1, 3, 12, 0));
        sur.setBackground(PANEL_BG);
        sur.setBorder(BorderFactory.createCompoundBorder(
                new MatteBorder(2, 0, 0, 0, DORADO_OSCURO),
                new EmptyBorder(14, 40, 14, 40)
            )
        );

        btnGuardar   = crearBoton("GUARDAR",        PANEL_BG, TEXTO_SECUNDARIO);
        btnContinuar = crearBoton("CONTINUAR A LA FOGATA",    DORADO,   new Color(18, 18, 22));
        btnMenu      = crearBoton("VOLVER AL MENÚ", PANEL_BG, TEXTO_SECUNDARIO);

        btnGuardar.addActionListener(e -> controlador.guardarPartida());
        btnContinuar.addActionListener(e -> controlador.onContinuarDesdeResultado());
        btnMenu.addActionListener(e -> controlador.volverAlMenu());

        sur.add(btnGuardar);
        sur.add(btnContinuar);
        sur.add(btnMenu);
        add(sur, BorderLayout.SOUTH);
    }

    //API publica (firmas identicas a la version original)

    /** Victoria normal: muestra la EXP ganada. */
    public void mostrarVictoria(int expGanada) {
        lblTitulo.setText("¡VICTORIA!");
        lblTitulo.setForeground(DORADO);
        btnContinuar.setText("CONTINUAR A LA FOGATA");
        btnGuardar.setEnabled(true);

        panelTabla.removeAll();
        agregarSeccion("RECOMPENSA");
        agregarFila("Experiencia obtenida", "+" + expGanada + " EXP", VERDE_EXP);
        refrescar();
    }

    /** Victoria final: campaña completada. */
    public void mostrarVictoriaFinal() {
        lblTitulo.setText("¡BUENOS AIRES SALVADA!");
        lblTitulo.setForeground(DORADO);
        btnContinuar.setText("FIN DE PARTIDA");
        btnGuardar.setEnabled(false);

        panelTabla.removeAll();
        agregarSeccion("CAMPAÑA COMPLETADA");
        agregarSeparador();
        agregarMensajeCentrado("Derrotaste al Nigromante en la Casa Rosada.");
        agregarMensajeCentrado("La niebla se disipa. Buenos Aires respira.");
        refrescar();
    }

    /** Derrota: invita a reintentar el nivel. */
    public void mostrarDerrota() {
        lblTitulo.setText("DERROTA");
        lblTitulo.setForeground(ROJO_DERROTA);
        btnContinuar.setText("REINTENTAR");
        btnGuardar.setEnabled(false);

        panelTabla.removeAll();
        agregarSeparador();
        agregarMensajeCentrado("Tu party ha sido derrotada.");
        agregarMensajeCentrado("Podés reintentar el nivel desde el principio.");
        refrescar();
    }

    /** Limpia el contenido dinámico al volver al menú. */
    public void limpiar() {
        lblTitulo.setText("RESULTADO");
        lblTitulo.setForeground(DORADO);
        panelTabla.removeAll();
        refrescar();
    }

    // ── Helpers de construcción visual (solo renderizado) ────────────────────────

    /** Encabezado de sección en gris claro. */
    private void agregarSeccion(String titulo) {
        JLabel lbl = new JLabel(titulo);
        lbl.setFont(FUENTE_SECCION);
        lbl.setForeground(TEXTO_SECUNDARIO);
        lbl.setAlignmentX(Component.LEFT_ALIGNMENT);
        lbl.setBorder(new EmptyBorder(10, 0, 4, 0));
        panelTabla.add(lbl);
    }

    /** Fila con etiqueta a la izquierda y valor coloreado a la derecha. */
    private void agregarFila(String etiqueta, String valor, Color colorValor) {
        JPanel fila = new JPanel(new BorderLayout(12, 0));
        fila.setBackground(PANEL_BG);
        fila.setMaximumSize(new Dimension(Integer.MAX_VALUE, 34));
        fila.setAlignmentX(Component.LEFT_ALIGNMENT);
        fila.setBorder(BorderFactory.createCompoundBorder(
                new MatteBorder(0, 0, 1, 0, BORDE_SUTIL),
                new EmptyBorder(6, 12, 6, 12)));

        JLabel lblEtiqueta = new JLabel(etiqueta);
        lblEtiqueta.setFont(FUENTE_FILA);
        lblEtiqueta.setForeground(TEXTO_PRIMARIO);

        JLabel lblValor = new JLabel(valor, SwingConstants.RIGHT);
        lblValor.setFont(FUENTE_FILA_B);
        lblValor.setForeground(colorValor);

        fila.add(lblEtiqueta, BorderLayout.WEST);
        fila.add(lblValor, BorderLayout.EAST);
        panelTabla.add(fila);
    }

    /** Espacio vertical entre secciones. */
    private void agregarSeparador() {
        JPanel sep = new JPanel();
        sep.setBackground(FONDO);
        sep.setMaximumSize(new Dimension(Integer.MAX_VALUE, 10));
        sep.setAlignmentX(Component.LEFT_ALIGNMENT);
        panelTabla.add(sep);
    }

    /** Texto centrado para mensajes narrativos. */
    private void agregarMensajeCentrado(String texto) {
        JLabel lbl = new JLabel(texto, SwingConstants.CENTER);
        lbl.setFont(FUENTE_FILA);
        lbl.setForeground(TEXTO_PRIMARIO);
        lbl.setAlignmentX(Component.CENTER_ALIGNMENT);
        lbl.setBorder(new EmptyBorder(6, 0, 6, 0));
        panelTabla.add(lbl);
    }

    /** Botón con estilo unificado. */
    private JButton crearBoton(String texto, Color fondo, Color colorTexto) {
        JButton btn = new JButton(texto);
        btn.setFont(FUENTE_BOTON);
        btn.setBackground(fondo);
        btn.setForeground(colorTexto);
        btn.setFocusPainted(false);
        btn.setBorder(BorderFactory.createLineBorder(DORADO_OSCURO, 1));
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        return btn;
    }

    /** Fuerza el repintado del panel central tras modificarlo. */
    private void refrescar() {
        panelTabla.revalidate();
        panelTabla.repaint();
    }

    private static class PanelTablaFondo extends JPanel {
        private final Image imagen;

        public PanelTablaFondo(String ruta) {
            URL url = getClass().getResource(ruta);
            this.imagen = (url != null) ? new ImageIcon(url).getImage() : null;
            setOpaque(true);
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            if (imagen != null){
                g.drawImage(imagen, 0, 0, getWidth(), getHeight(), this);
            } else {
                g.setColor(FONDO);
                g.fillRect(0, 0, getWidth(), getHeight());
            }
            g.setColor(new Color(0, 0, 0, 100)); // Capa de oscurecimiento para mejorar legibilidad
            g.fillRect(0, 0, getWidth(), getHeight());
        }
    }
}
