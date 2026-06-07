package vista;

import controlador.ControladorJuego;
import modelo.GameEngine;
import modelo.ItemConsumible;

import javax.swing.*;
import java.awt.*;
import java.net.URL;

/**
 * Fogata (MVP):
 * - Entrega 2-3 items fijos al inventario compartido.
 * - Luego continuar al siguiente nivel.
 */
public class PantallaFogata extends JPanel {

    private final ControladorJuego controlador;
    private final JLabel lblInfo;
    private Image imgFondo;

    public PantallaFogata(ControladorJuego controlador) {
        this.controlador = controlador;

        setLayout(new BorderLayout(10, 10));

        // Cargar imagen de fondo
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

        // Entrega de suministros mejorada en el campamento
        engine.getPartyPersonajes().getInventarioCompartido().agregar(ItemConsumible.tortaFrita());
        engine.getPartyPersonajes().getInventarioCompartido().agregar(ItemConsumible.mate());
        engine.getPartyPersonajes().getInventarioCompartido().agregar(ItemConsumible.guiso());
        engine.getPartyPersonajes().getInventarioCompartido().agregar(ItemConsumible.pastelito());
        engine.getPartyPersonajes().getInventarioCompartido().agregar(ItemConsumible.asado()); // Suministro de curación
                                                                                               // alta

        lblInfo.setText("Suministros recogidos: Torta frita, Mate, Guiso, Pastelito y Asado de tira.");
    }
}