package vista;

import controlador.ControladorJuego;
import dto.EntidadDTO;

import javax.swing.*;
import java.awt.*;

public class PantallaEstado extends JPanel {

    private final ControladorJuego controlador;
    private final JTextArea area;
    private final JButton btnVolver;

    public PantallaEstado(ControladorJuego controlador) {
        this.controlador = controlador;
        setLayout(new BorderLayout(10, 10));

        JLabel titulo = new JLabel("ESTADO DEL PERSONAJE", SwingConstants.CENTER);
        titulo.setFont(new Font("Arial", Font.BOLD, 20));

        area = new JTextArea();
        area.setEditable(false);
        area.setFont(new Font("Monospaced", Font.PLAIN, 14));

        btnVolver = new JButton("Volver");
        btnVolver.addActionListener(e -> controlador.onVolverDesdeEstado());

        JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.CENTER));
        panelBotones.add(btnVolver);

        add(titulo, BorderLayout.NORTH);
        add(new JScrollPane(area), BorderLayout.CENTER);
        add(panelBotones, BorderLayout.SOUTH);
    }

    public void mostrarPersonaje(EntidadDTO p) {
        if (p == null) {
            area.setText("Sin personaje.");
            return;
        }

        String texto = ""
                + "Nombre: " + p.getNombre() + "\n"
                + "Clase: " + p.getClase() + "\n"
                + "Nivel: " + p.getNivel() + " (EXP: " + p.getExperiencia() + "/100)\n"
                + "\n"
                + "Vida: " + p.getVidaActual() + "/" + p.getVidaMax() + "\n"
                + "Mana: " + p.getManaActual() + "/" + p.getManaMax() + "\n"
                + "Ataque Total: " + p.getAtaqueTotal() + "\n"
                + "Defensa Total: " + p.getDefensaTotal() + "\n"
                + "Velocidad Total: " + p.getVelocidadTotal() + "\n"
                + "\n"
                + "Estados:\n"
                + " - ATURDIDO: " + (p.isTieneAturdido() ? "SI" : "NO") + "\n"
                + " - ESCUDO: " + (p.isTieneEscudo() ? "SI" : "NO") + "\n";

        area.setText(texto);
    }
}