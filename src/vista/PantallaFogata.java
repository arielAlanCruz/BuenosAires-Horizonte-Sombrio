package vista;

import controlador.ControladorJuego;
import java.awt.*;
import java.net.URL;
import javax.swing.*;

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

        setLayout(new BorderLayout(0, 0));

        // Cargar imagen de fondo
        URL url = getClass().getResource("/img/Fogata.png");
        if (url != null) {
            imgFondo = new ImageIcon(url).getImage();
        }

        //Titulo en la parte superior
        JLabel titulo = EstiloUI.labelTitulo("Fogata");
        titulo.setBorder(BorderFactory.createEmptyBorder(18, 0, 0, 0));

        //Info + Boton sobre overlay oscuro
        //Panel semitransparente para mejorar la legibilidad del texto sobre la imagen
        JPanel panelSur = new JPanel(new BorderLayout(0, 8)){
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                g.setColor(new Color(0, 0, 0, 180)); //Negro semitransparente
                g.fillRect(0, 0, getWidth(), getHeight());
            }
        };

        panelSur.setOpaque(false);
        panelSur.setBorder(BorderFactory.createEmptyBorder(14, 30, 14, 30));

        lblInfo = new JLabel(" ", SwingConstants.CENTER);
        lblInfo.setFont(EstiloUI.FUENTE_TEXTO);
        lblInfo.setForeground(EstiloUI.COLOR_TEXTO_PRIMARIO);

        JButton btnContinuar = EstiloUI.botonPrimario("Continuar");
        btnContinuar.addActionListener(e -> controlador.onContinuarDesdeFogata());

        JPanel panelBoton = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 0));
        panelBoton.setOpaque(false);
        panelBoton.add(btnContinuar);

        panelSur.add(lblInfo, BorderLayout.CENTER);
        panelSur.add(panelBoton, BorderLayout.SOUTH);

        add(titulo, BorderLayout.NORTH);
        add(panelSur, BorderLayout.SOUTH);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (imgFondo != null) {
            g.drawImage(imgFondo, 0, 0, getWidth(), getHeight(), this);
        } else {
            g.setColor(EstiloUI.COLOR_FONDO);
            g.fillRect(0, 0, getWidth(), getHeight());
        }
    }

    /**
     * Llamado por el controlador para mostrar los suministros entregados.
     * La vista solo actualiza el label con el texto recibido.
     */
    public void mostrarSuministros(String descripcion) {
        lblInfo.setText("Suministros recogidos: " + descripcion);
    }
}