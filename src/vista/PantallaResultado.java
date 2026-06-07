package vista;

import controlador.ControladorJuego;

import javax.swing.*;
import java.awt.*;

public class PantallaResultado extends JPanel {

    private final ControladorJuego controlador;
    private final JLabel lblTitulo;
    private final JLabel lblInfo;
    private final JButton btnContinuar;
    private final JButton btnGuardar;

    public PantallaResultado(ControladorJuego controlador) {
        this.controlador = controlador;

        setLayout(new BorderLayout(10, 10));

        lblTitulo = new JLabel("RESULTADO", SwingConstants.CENTER);
        lblTitulo.setFont(new Font("Arial", Font.BOLD, 24));

        lblInfo = new JLabel(" ", SwingConstants.CENTER);
        lblInfo.setFont(new Font("Arial", Font.PLAIN, 16));

        JPanel botones = new JPanel(new GridLayout(1, 3, 10, 10));
        btnGuardar = new JButton("Guardar");
        btnContinuar = new JButton("Continuar");
        JButton btnMenu = new JButton("Volver al Menú");

        btnGuardar.addActionListener(e -> controlador.guardarPartida());
        btnContinuar.addActionListener(e -> controlador.onContinuarDesdeResultado());
        btnMenu.addActionListener(e -> controlador.volverAlMenu());

        botones.add(btnGuardar);
        botones.add(btnContinuar);
        botones.add(btnMenu);

        add(lblTitulo, BorderLayout.NORTH);
        add(lblInfo, BorderLayout.CENTER);
        add(botones, BorderLayout.SOUTH);
    }

    public void mostrarVictoria(int expGanada) {
        lblTitulo.setText("¡VICTORIA!");
        lblInfo.setText("Experiencia ganada: " + expGanada);
        btnContinuar.setText("Avanzar a Fogata");
        btnContinuar.setVisible(true);
        btnGuardar.setEnabled(true);
    }

    public void mostrarVictoriaFinal() {
        lblTitulo.setText("¡FELICITACIONES!");
        // Uso de etiquetas HTML simples en Swing para estructurar el salto de línea
        // elegante
        lblInfo.setText("<html><center>¡Has derrotado al Nigromante en la Casa Rosada!<br>"
                + "Salvaste a Buenos Aires del Horizonte Sombrío.<br>"
                + "Gracias por jugar la campaña.</center></html>");
        btnContinuar.setText("Fin de Partida");
        btnContinuar.setVisible(true);
        btnGuardar.setEnabled(false); // No es necesario guardar tras terminar el juego
    }

    public void mostrarDerrota() {
        lblTitulo.setText("DERROTA");
        lblInfo.setText("Tu party ha sido derrotada.");
        btnContinuar.setText("Reintentar Nivel");
        btnContinuar.setVisible(true);
        btnGuardar.setEnabled(false);
    }

    public void limpiar() {
        lblTitulo.setText("RESULTADO");
        lblInfo.setText(" ");
    }
}