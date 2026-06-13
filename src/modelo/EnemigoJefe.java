package modelo;

import enums.TipoEnemigo;
import enums.TipoGeneral;

/**
 * Representa un oponente de alto rango (Jefe) con comportamiento e IA
 * probabilística.
 */
public class EnemigoJefe extends Enemigo {

    public EnemigoJefe(String nombre, TipoEnemigo tipo, int nivel, int vidaMax, int ataque, int defensa, int velocidad,
            int expOtorgada) {
        super(nombre, tipo, nivel, vidaMax, ataque, defensa, velocidad, expOtorgada);
    }

    @Override
    public TipoGeneral elegirAccion() {
        // IA probabilística de Jefe (40% lanzar habilidad de aturdimiento, 60% ataque)
        if (Math.random() < 0.40) {
            return TipoGeneral.HABILIDAD;
        } else {
            return TipoGeneral.ATACAR;
        }
    }
}