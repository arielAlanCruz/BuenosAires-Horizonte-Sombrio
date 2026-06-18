package vista;

import controlador.ControladorJuego;
import java.awt.*;
import javax.swing.*;

/**
 * Pantalla inicial: nueva partida / cargar partida.
 */
public class PantallaInicio extends JPanel {

    private final ControladorJuego controlador;
    private Image imagenFondo;

    public PantallaInicio(ControladorJuego controlador) {
        this.controlador = controlador;

        setLayout(new BorderLayout());

        ImageIcon icon = new ImageIcon(getClass().getResource("/img/Imagen-Inicio.png"));
        imagenFondo = icon.getImage();

        JLabel titulo = EstiloUI.labelTitulo("Buenos Aires: Horizonte Sombrío");

        JButton btnNueva = EstiloUI.botonPrimario("Nueva Partida");
        JButton btnCargar = EstiloUI.botonSecundario("Cargar Partida");
        JButton btnSalir = EstiloUI.botonSecundario("Salir");

        btnNueva.addActionListener(e -> controlador.iniciarNuevaPartida());
        btnCargar.addActionListener(e -> controlador.cargarPartida());
        btnSalir.addActionListener(e -> System.exit(0));

        JPanel panelBotones = new JPanel(new GridLayout(3, 1, 0, 10));
        panelBotones.setOpaque(false);
        panelBotones.add(btnNueva);
        panelBotones.add(btnCargar);
        panelBotones.add(btnSalir);

        JPanel panelCentro = new JPanel(new GridLayout(2, 1, 0, 20));
        panelCentro.setOpaque(false);
        panelCentro.add(titulo);
        panelCentro.add(panelBotones);

        JPanel panelMedio = new JPanel(new GridBagLayout());
        panelMedio.setOpaque(false);
        panelMedio.add(panelCentro);

        add(panelMedio, BorderLayout.CENTER);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.drawImage(imagenFondo, 0, 0, getWidth(), getHeight(), this);
    }
}