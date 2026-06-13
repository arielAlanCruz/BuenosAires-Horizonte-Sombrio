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

        // Selección de la biografía/historia temática según la clase
        String historia;
        switch (p.getClase().toUpperCase()) {
            case "GUERRERO":
                historia = "Un gaucho recio de las pampas bonaerenses. Defensor de la tradición, su\n" +
                        "facón criollo y su poncho son su único resguardo contra las sombras que\n" +
                        "brotan del Obelisco lluvioso.";
                break;
            case "MAGO":
                historia = "Hechicero pampeano que aprendió los misterios rúnicos de los montes de\n" +
                        "Caranday. Canaliza el misticismo del viento Pampero para azotar a las\n" +
                        "bestias oscuras del horizonte.";
                break;
            case "ARQUERO":
                historia = "Rastreador silencioso originario de los humedales del Paraná. Con su\n" +
                        "arco reforzado tallado en madera dura de Guayacán, caza desde las\n" +
                        "sombras con precisión letal.";
                break;
            case "CURANDERA":
                historia = "Matriarca y médica yuyera de las afueras rurales. Utiliza la sabiduría\n" +
                        "de la plata criolla y hierbas medicinales pampeanas para remendar y\n" +
                        "resguardar el alma de sus compañeros.";
                break;
            default:
                historia = "Un valiente combatiente de Buenos Aires que resiste con entereza en el\n" +
                        "Horizonte Sombrío.";
                break;
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
                + "Equipamiento Activo:\n"
                + " - [Arma]:      " + p.getNombreArma() + "\n"
                + " - [Accesorio]: " + p.getNombreAccesorio() + "\n"
                + "\n"
                + "Estados Activos:\n"
                + " - ATURDIDO: " + (p.isTieneAturdido() ? "SI" : "NO") + "\n"
                + " - ESCUDO:    " + (p.isTieneEscudo() ? "SI" : "NO") + "\n"
                + "\n"
                + "─────────────────────────────────────────────────────────────────────────────\n"
                + "Trasfondo Histórico:\n"
                + historia + "\n"
                + "─────────────────────────────────────────────────────────────────────────────\n";

        area.setText(texto);
    }
}