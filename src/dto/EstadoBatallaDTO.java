package dto;

import java.util.List;

/**
 * Concentra el estado completo del escenario de combate en un único objeto de
 * transporte,
 * incluyendo los ítems disponibles en el inventario de manera desacoplada.
 */
public class EstadoBatallaDTO {

    private final List<EntidadDTO> aliados;
    private final List<EntidadDTO> enemigos;
    private final String nombreEntidadTurnoActual;
    private final int nivelActual;
    private final List<String> nombresItemsInventario;

    public EstadoBatallaDTO(List<EntidadDTO> aliados, List<EntidadDTO> enemigos,
            String nombreEntidadTurnoActual, int nivelActual,
            List<String> nombresItemsInventario) {
        this.aliados = aliados;
        this.enemigos = enemigos;
        this.nombreEntidadTurnoActual = nombreEntidadTurnoActual;
        this.nivelActual = nivelActual;
        this.nombresItemsInventario = nombresItemsInventario;
    }

    public List<EntidadDTO> getAliados() {
        return aliados;
    }

    public List<EntidadDTO> getEnemigos() {
        return enemigos;
    }

    public String getNombreEntidadTurnoActual() {
        return nombreEntidadTurnoActual;
    }

    public int getNivelActual() {
        return nivelActual;
    }

    public List<String> getNombresItemsInventario() {
        return nombresItemsInventario;
    }
}