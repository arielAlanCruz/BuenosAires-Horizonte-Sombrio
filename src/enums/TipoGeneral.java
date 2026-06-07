package enums;

/**
 * Enum multiuso unificado para controlar acciones de combate,
 * estados alterados y el ciclo de vida de la batalla.
 */
public enum TipoGeneral {
    // Acciones del flujo de turno
    ATACAR,
    DEFENDER,
    HABILIDAD,
    USAR_ITEM,

    // Estados alterados activos (MVP)
    ATURDIDO,
    ESCUDO,

    // Estados del ciclo de la batalla
    EN_CURSO,
    VICTORIA,
    DERROTA
}