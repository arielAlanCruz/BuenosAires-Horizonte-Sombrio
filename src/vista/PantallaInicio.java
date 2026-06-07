package vista;

import controlador.ControladorJuego;

import javax.swing.*;
import java.awt.*;

/**
 * Pantalla inicial: nueva partida / cargar partida.
 */
public class PantallaInicio extends JPanel {

    private final ControladorJuego controlador;

    public PantallaInicio(ControladorJuego controlador) {
        this.controlador = controlador;

        setLayout(new BorderLayout());

        JLabel titulo = new JLabel("Buenos Aires: Horizonte Sombrío", SwingConstants.CENTER);
        titulo.setFont(new Font("Arial", Font.BOLD, 26));

        JPanel centro = new JPanel();
        centro.setLayout(new GridLayout(3, 1, 10, 10));

        JButton btnNueva = new JButton("Nueva Partida");
        JButton btnCargar = new JButton("Cargar Partida");
        JButton btnSalir = new JButton("Salir");

        btnNueva.addActionListener(e -> controlador.iniciarNuevaPartida());
        btnCargar.addActionListener(e -> controlador.cargarPartida());
        btnSalir.addActionListener(e -> System.exit(0));

        centro.add(btnNueva);
        centro.add(btnCargar);
        centro.add(btnSalir);

        add(titulo, BorderLayout.NORTH);
        add(centro, BorderLayout.CENTER);
    }
}