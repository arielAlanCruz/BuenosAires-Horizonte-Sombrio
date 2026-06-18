package vista;

import java.awt.*;
import javax.swing.*;
import javax.swing.border.TitledBorder;

/**
 * Clase utilitaria de estilos visuales compartidos.
 * Centraliza colores, fuentes, bordes y factory methods de componentes
 * para mantener homogeneidad en todas las pantallas del juego.
 * No contiene lógica de negocio.
 */

public class EstiloUI {

    //Colores
    public static final Color COLOR_FONDO           = new Color(24, 24, 28);
    public static final Color COLOR_PANEL           = new Color(30, 30, 36);
    public static final Color COLOR_DORADO          = new Color(212, 175, 55);
    public static final Color COLOR_DORADO_OSCURO   = new Color(140, 110, 20);
    public static final Color COLOR_TEXTO_PRIMARIO  = new Color(230, 225, 210);
    public static final Color COLOR_TEXTO_SECUNDARIO= new Color(140, 135, 120);
    public static final Color COLOR_HP              = new Color(46, 204, 113);
    public static final Color COLOR_MP              = new Color(52, 152, 219);
    public static final Color COLOR_ENEMIGO         = new Color(231, 76, 60);
    public static final Color COLOR_BARRA_FONDO     = new Color(40, 40, 40);

    //Fuentes
    public static final Font FUENTE_TITULO    = new Font("Monospaced", Font.BOLD, 28);
    public static final Font FUENTE_SUBTITULO = new Font("Monospaced", Font.BOLD, 12);
    public static final Font FUENTE_TEXTO     = new Font("Monospaced", Font.PLAIN, 13);
    public static final Font FUENTE_BOTON     = new Font("Arial", Font.BOLD, 12);
    public static final Font FUENTE_LOG       = new Font("Monospaced", Font.BOLD, 13);

    // Constructor privado: solo se usan sus miembros estáticos
    private EstiloUI() {}

    //Factory methods de botones

    //Botón primario: acción principal de la pantalla (continuar, iniciar, etc).
    //Fondo dorado, texto oscuro.
    public static JButton botonPrimario(String texto) {
        JButton btn = new JButton(texto);
        btn.setFont(FUENTE_BOTON);
        btn.setBackground(COLOR_DORADO);
        btn.setForeground(COLOR_FONDO);
        btn.setFocusPainted(false);
        btn.setBorder(BorderFactory.createLineBorder(COLOR_DORADO_OSCURO, 1));
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        return btn;
    }

    //Botón secundario: acciones de menor peso (guardar, volver, etc).
    //Fondo oscuro, texto claro.
    public static JButton botonSecundario(String texto) {
        JButton btn = new JButton(texto);
        btn.setFont(FUENTE_BOTON);
        btn.setBackground(COLOR_PANEL);
        btn.setForeground(COLOR_TEXTO_PRIMARIO);
        btn.setFocusPainted(false);
        btn.setBorder(BorderFactory.createLineBorder(COLOR_DORADO_OSCURO, 1));
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        return btn;
    }

    //Botón de acción de combate: para los botones de la pantalla de batalla.
    //Permite especificar color de fondo propio manteniendo el estilo general.
    public static JButton botonCombate(String texto, Color colorFondo) {
        JButton btn = new JButton(texto);
        btn.setFont(FUENTE_BOTON);
        btn.setBackground(colorFondo);
        btn.setForeground(Color.WHITE);
        btn.setFocusPainted(false);
        btn.setBorder(BorderFactory.createLineBorder(Color.BLACK, 1));
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        return btn;
    }

    //Factory methods de labels

    //Label de titulo centrado: usado en el encabezado de cada pantalla.
    public static JLabel labelTitulo(String texto) {
        JLabel lbl = new JLabel(texto, SwingConstants.CENTER);
        lbl.setFont(FUENTE_TITULO);
        lbl.setForeground(COLOR_DORADO);
        lbl.setBorder(BorderFactory.createEmptyBorder(22, 0, 18, 0));
        return lbl;
    }

    //Label de sección: encabezado de grupo dentro de una pantalla.
    public static JLabel labelSeccion(String texto) {
        JLabel lbl = new JLabel(texto);
        lbl.setFont(FUENTE_SUBTITULO);
        lbl.setForeground(COLOR_TEXTO_SECUNDARIO);
        return lbl;
    }

    //Factory methods de bordes

    //Borde dorado estilizado con título: usado en los paneles de PantallaBatalla.
    public static TitledBorder bordeDorado(String titulo) {
        TitledBorder border = BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(COLOR_DORADO, 1), titulo);
        border.setTitleColor(COLOR_DORADO);
        border.setTitleFont(FUENTE_BOTON);
        return border;
    }
}