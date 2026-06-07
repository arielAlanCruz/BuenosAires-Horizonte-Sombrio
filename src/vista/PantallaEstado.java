package vista;

import controlador.ControladorJuego;
import modelo.Personaje;
import enums.TipoGeneral;

import javax.swing.*;
import java.awt.*;

/**
 * Pantalla de estado (MVP): vida, ataque, defensa y estados (aturdido/escudo).
 */
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

        add(titulo, BorderLayout.NORTH);
        add(new JScrollPane(area), BorderLayout.CENTER);
        add(btnVolver, BorderLayout.SOUTH);
    }

    public void mostrarPersonaje(Personaje p) {
        if (p == null) {
            area.setText("Sin personaje.");
            return;
        }

        boolean aturdido = p.tieneEfecto(TipoGeneral.ATURDIDO);
        boolean escudo = p.tieneEfecto(TipoGeneral.ESCUDO);

        String texto = ""
                + "Nombre: " + p.getNombre() + "\n"
                + "Clase: " + p.getClase() + "\n"
                + "Nivel: " + p.getNivel() + "\n"
                + "\n"
                + "Vida: " + p.getVidaActual() + "/" + p.getVidaMax() + "\n"
                + "Mana: " + p.getManaActual() + "/" + p.getManaMax() + "\n"
                + "Ataque: " + p.getAtaque() + "\n"
                + "Defensa: " + p.getDefensa() + "\n"
                + "\n"
                + "Estados:\n"
                + " - ATURDIDO: " + (aturdido ? "SI" : "NO") + "\n"
                + " - ESCUDO: " + (escudo ? "SI" : "NO") + "\n";

        area.setText(texto);
    }
}