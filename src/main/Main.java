package main;

import javax.swing.SwingUtilities;

import controlador.ControladorJuego;
import vista.*;

/**
 * Punto de entrada del programa.
 * MVP: inicializa MVC y muestra la pantalla inicial.
 */

public class Main {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            // Controlador (GRASP Controller)
            ControladorJuego controlador = new ControladorJuego();

            // Ventana principal
            VentanaPrincipal ventana = new VentanaPrincipal(controlador);
            controlador.setVentanaPrincipal(ventana);

            // Pantallas
            PantallaInicio inicio = new PantallaInicio(controlador);
            PantallaBatalla batalla = new PantallaBatalla(controlador);
            PantallaEstado estado = new PantallaEstado(controlador);
            PantallaResultado resultado = new PantallaResultado(controlador);
            PantallaFogata fogata = new PantallaFogata(controlador);

            // Registrar pantallas
            ventana.agregarPantalla("INICIO", inicio);
            ventana.agregarPantalla("BATALLA", batalla);
            ventana.agregarPantalla("ESTADO", estado);
            ventana.agregarPantalla("RESULTADO", resultado);
            ventana.agregarPantalla("FOGATA", fogata);

            // Darle al controlador referencias a las pantallas
            controlador.setPantallas(inicio, batalla, estado, resultado, fogata);

            // Mostrar
            ventana.setVisible(true);
            ventana.mostrarPantalla("INICIO");
        });
    }
}