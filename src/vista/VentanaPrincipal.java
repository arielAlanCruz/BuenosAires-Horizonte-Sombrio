package vista;

import controlador.ControladorJuego;

import javax.swing.*;
import java.awt.*;

public class VentanaPrincipal extends JFrame {

    private final JPanel contenedor;
    private final CardLayout layout;
    private final ControladorJuego controlador;

    public VentanaPrincipal(ControladorJuego controlador) {
        super("Buenos Aires: Horizonte Sombrío");
        this.controlador = controlador;

        this.layout = new CardLayout();
        this.contenedor = new JPanel(layout);

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(900, 550);
        setLocationRelativeTo(null);

        setContentPane(contenedor);
    }

    public void agregarPantalla(String key, JPanel pantalla) {
        contenedor.add(pantalla, key);
    }

    public void mostrarPantalla(String key) {
        layout.show(contenedor, key);
        revalidate();
        repaint();
    }

    public void mostrarMensaje(String msg) {
        // La ventana gestiona el componente de UI física, aislando la lógica del
        // controlador
        JOptionPane.showMessageDialog(this, msg);
    }

    public ControladorJuego getControlador() {
        return controlador;
    }
}