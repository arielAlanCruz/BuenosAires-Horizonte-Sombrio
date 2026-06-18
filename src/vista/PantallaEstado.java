package vista;

import controlador.ControladorJuego;
import dto.EntidadDTO;
import java.awt.*;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.MatteBorder;

/**
 * Pantalla de estado del personaje en turno.
 * Muestra stats, mana y efectos activos. Solo renderiza, no toca el modelo.
 */
public class PantallaEstado extends JPanel {

    private final ControladorJuego controlador;
    private final JPanel panelDatos;

    public PantallaEstado(ControladorJuego controlador) {
        this.controlador = controlador;

        setLayout(new BorderLayout(0, 0));
        setBackground(EstiloUI.COLOR_FONDO);

        // ── NORTE: título ────────────────────────────────────────────────────────
        JPanel norte = new JPanel(new BorderLayout());
        norte.setBackground(EstiloUI.COLOR_PANEL);
        norte.setBorder(new MatteBorder(0, 0, 2, 0, EstiloUI.COLOR_DORADO_OSCURO));
        norte.add(EstiloUI.labelTitulo("ESTADO DEL PERSONAJE"), BorderLayout.CENTER);
        add(norte, BorderLayout.NORTH);

        // ── CENTRO: filas de datos ───────────────────────────────────────────────
        panelDatos = new JPanel();
        panelDatos.setLayout(new BoxLayout(panelDatos, BoxLayout.Y_AXIS));
        panelDatos.setBackground(EstiloUI.COLOR_FONDO);
        panelDatos.setBorder(new EmptyBorder(24, 80, 24, 80));

        JScrollPane scroll = new JScrollPane(panelDatos);
        scroll.setBorder(null);
        scroll.setBackground(EstiloUI.COLOR_FONDO);
        scroll.getViewport().setBackground(EstiloUI.COLOR_FONDO);
        add(scroll, BorderLayout.CENTER);

        // ── SUR: botón volver ────────────────────────────────────────────────────
        JPanel sur = new JPanel(new FlowLayout(FlowLayout.CENTER));
        sur.setBackground(EstiloUI.COLOR_PANEL);
        sur.setBorder(new MatteBorder(2, 0, 0, 0, EstiloUI.COLOR_DORADO_OSCURO));

        JButton btnVolver = EstiloUI.botonSecundario("← Volver");
        btnVolver.addActionListener(e -> controlador.onVolverDesdeEstado());
        sur.add(btnVolver);

        add(sur, BorderLayout.SOUTH);
    }

    /**
     * Recibe el EntidadDTO que armó el controlador y construye las filas de
     * stats. Antes recibía un Personaje del modelo directamente; ahora solo
     * ve el snapshot inmutable, igual que el resto de las pantallas.
     * habilidadesInfo trae, por cada habilidad, el texto ya armado por el
     * controlador con nombre, costo de maná y descripción.
     */
    public void mostrarPersonaje(EntidadDTO p, String[] habilidadesInfo) {
        panelDatos.removeAll();

        if (p == null) {
            panelDatos.add(EstiloUI.labelSeccion("Sin personaje seleccionado."));
            refrescar();
            return;
        }

        // Sección: identidad
        agregarSeccion("PERSONAJE");
        agregarFila("Nombre",  p.getNombre(),       EstiloUI.COLOR_DORADO);
        agregarFila("Clase",   p.getClase(),        EstiloUI.COLOR_TEXTO_PRIMARIO);
        agregarFila("Nivel",   "Nv." + p.getNivel(), EstiloUI.COLOR_TEXTO_PRIMARIO);

        // Sección: vida y mana
        agregarSeparador();
        agregarSeccion("RECURSOS");
        agregarFila("Vida",  p.getVidaActual()  + " / " + p.getVidaMax(),  EstiloUI.COLOR_HP);
        agregarFila("Mana",  p.getManaActual()  + " / " + p.getManaMax(),  EstiloUI.COLOR_MP);

        // Sección: stats de combate (ya incluyen el bono de equipamiento,
        // tal como lo calcula Personaje.calcularAtaqueBase()/getDefensa())
        agregarSeparador();
        agregarSeccion("ESTADÍSTICAS");
        agregarFila("Ataque",   String.valueOf(p.getAtaqueTotal()),   EstiloUI.COLOR_TEXTO_PRIMARIO);
        agregarFila("Defensa",  String.valueOf(p.getDefensaTotal()),  EstiloUI.COLOR_TEXTO_PRIMARIO);

        // Sección: equipamiento
        agregarSeparador();
        agregarSeccion("EQUIPAMIENTO");
        agregarFila("Arma",       p.getNombreArma(),       EstiloUI.COLOR_TEXTO_PRIMARIO);
        agregarFila("Accesorio",  p.getNombreAccesorio(),  EstiloUI.COLOR_TEXTO_PRIMARIO);

        // Sección: efectos activos
        agregarSeparador();
        agregarSeccion("EFECTOS ACTIVOS");
        agregarFila("Aturdido", p.isTieneAturdido() ? "SÍ" : "NO",
                p.isTieneAturdido() ? EstiloUI.COLOR_ENEMIGO : EstiloUI.COLOR_TEXTO_SECUNDARIO);
        agregarFila("Escudo",   p.isTieneEscudo()   ? "SÍ" : "NO",
                p.isTieneEscudo()   ? EstiloUI.COLOR_MP     : EstiloUI.COLOR_TEXTO_SECUNDARIO);

        // Sección: habilidades (nombre, costo de maná y descripción)
        agregarSeparador();
        agregarSeccion("HABILIDADES");
        if (habilidadesInfo == null || habilidadesInfo.length == 0) {
            agregarFilaHabilidad("Sin habilidades disponibles.");
        } else {
            for (int i = 0; i < habilidadesInfo.length; i++) {
                agregarFilaHabilidad(habilidadesInfo[i]);
            }
        }

        refrescar();
    }

    // ── Helpers de construcción visual ───────────────────────────────────────────

    private void agregarSeccion(String titulo) {
        JLabel lbl = EstiloUI.labelSeccion(titulo);
        lbl.setAlignmentX(Component.LEFT_ALIGNMENT);
        lbl.setBorder(new EmptyBorder(10, 0, 4, 0));
        panelDatos.add(lbl);
    }

    private void agregarFila(String etiqueta, String valor, Color colorValor) {
        JPanel fila = new JPanel(new BorderLayout(12, 0));
        fila.setBackground(EstiloUI.COLOR_PANEL);
        fila.setMaximumSize(new Dimension(Integer.MAX_VALUE, 34));
        fila.setAlignmentX(Component.LEFT_ALIGNMENT);
        fila.setBorder(BorderFactory.createCompoundBorder(
                new MatteBorder(0, 0, 1, 0, new Color(55, 55, 65)),
                new EmptyBorder(6, 12, 6, 12)));

        JLabel lblEtiqueta = new JLabel(etiqueta);
        lblEtiqueta.setFont(EstiloUI.FUENTE_TEXTO);
        lblEtiqueta.setForeground(EstiloUI.COLOR_TEXTO_PRIMARIO);

        JLabel lblValor = new JLabel(valor, SwingConstants.RIGHT);
        lblValor.setFont(EstiloUI.FUENTE_LOG);
        lblValor.setForeground(colorValor);

        fila.add(lblEtiqueta, BorderLayout.WEST);
        fila.add(lblValor, BorderLayout.EAST);
        panelDatos.add(fila);
    }

    private void agregarFilaHabilidad(String texto) {
        JLabel lbl = new JLabel("<html><body style='width:600px'>" + texto + "</body></html>");
        lbl.setFont(EstiloUI.FUENTE_TEXTO);
        lbl.setForeground(EstiloUI.COLOR_TEXTO_PRIMARIO);
        lbl.setAlignmentX(Component.LEFT_ALIGNMENT);
        lbl.setBorder(BorderFactory.createCompoundBorder(
                new MatteBorder(0, 0, 1, 0, new Color(55, 55, 65)),
                new EmptyBorder(8, 12, 8, 12)));
        panelDatos.add(lbl);
    }

    private void agregarSeparador() {
        JPanel sep = new JPanel();
        sep.setBackground(EstiloUI.COLOR_FONDO);
        sep.setMaximumSize(new Dimension(Integer.MAX_VALUE, 10));
        sep.setAlignmentX(Component.LEFT_ALIGNMENT);
        panelDatos.add(sep);
    }

    private void refrescar() {
        panelDatos.revalidate();
        panelDatos.repaint();
    }
}
