package vista;

import controlador.ControladorJuego;
import modelo.GameEngine;
import modelo.ItemConsumible;
import modelo.ItemEquipable;

import javax.swing.*;
import java.awt.*;
import java.net.URL;

public class PantallaFogata extends JPanel {

    private final ControladorJuego controlador;
    private final JLabel lblInfo;
    private Image imgFondo;

    public PantallaFogata(ControladorJuego controlador) {
        this.controlador = controlador;

        setLayout(new BorderLayout(10, 10));

        URL url = getClass().getResource("/img/Fogata.png");
        if (url != null) {
            imgFondo = new ImageIcon(url).getImage();
        }

        JLabel titulo = new JLabel("FOGATA", SwingConstants.CENTER);
        titulo.setFont(new Font("Arial", Font.BOLD, 32));
        titulo.setForeground(Color.WHITE);

        lblInfo = new JLabel(" ", SwingConstants.CENTER);
        lblInfo.setFont(new Font("Arial", Font.BOLD, 18));
        lblInfo.setForeground(Color.WHITE);

        JButton btnContinuar = new JButton("Continuar");
        btnContinuar.setFont(new Font("Arial", Font.BOLD, 16));
        btnContinuar.addActionListener(e -> controlador.onContinuarDesdeFogata());

        JPanel pnlCentro = new JPanel(new BorderLayout());
        pnlCentro.setOpaque(false);
        pnlCentro.add(lblInfo, BorderLayout.CENTER);

        JPanel pnlSur = new JPanel();
        pnlSur.setOpaque(false);
        pnlSur.add(btnContinuar);

        add(titulo, BorderLayout.NORTH);
        add(pnlCentro, BorderLayout.CENTER);
        add(pnlSur, BorderLayout.SOUTH);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (imgFondo != null) {
            g.drawImage(imgFondo, 0, 0, getWidth(), getHeight(), this);
        } else {
            g.setColor(Color.DARK_GRAY);
            g.fillRect(0, 0, getWidth(), getHeight());
        }
    }

    public void entregarItemsFijos() {
        GameEngine engine = GameEngine.getInstance();
        if (engine.getPartyPersonajes() == null)
            return;

        // Consumibles
        engine.getPartyPersonajes().getInventarioCompartido().agregar(ItemConsumible.tortaFrita());
        engine.getPartyPersonajes().getInventarioCompartido().agregar(ItemConsumible.asado());

        // ENTREGAS DE ARMAS: Agregamos equipamiento interactivo polimórfico al
        // inventario compartido
        engine.getPartyPersonajes().getInventarioCompartido().agregar(
                new ItemEquipable("Cuchillo de Plata", "Arma legendaria (+8 ATQ).", ItemEquipable.SLOT_ARMA, 8, 0, 0));
        engine.getPartyPersonajes().getInventarioCompartido().agregar(new ItemEquipable("Poncho de Alpaca",
                "Accesorio protector (+4 DEF, +2 VEL).", ItemEquipable.SLOT_ACCESORIO, 0, 4, 2));

        lblInfo.setText("Suministros recogidos: Torta Frita, Asado de Tira, Cuchillo de Plata y Poncho de Alpaca.");
    }
}