package dto;

/**
 * Snapshot inmutable que contiene datos descriptivos y de rendimiento de una
 * entidad.
 * Protege al modelo de accesos directos por parte de la vista.
 */
public class EntidadDTO {

    private final String nombre;
    private final int vidaActual;
    private final int vidaMax;
    private final int manaActual;
    private final int manaMax;
    private final boolean estaVivo;
    private final boolean tieneEscudo;
    private final boolean tieneAturdido;

    // Campos de uso para la PantallaEstado
    private final int nivel;
    private final int experiencia;
    private final String clase;
    private final int ataqueTotal;
    private final int defensaTotal;
    private final int velocidadTotal;

    // NUEVO: Nombres de los equipamientos activos
    private final String nombreArma;
    private final String nombreAccesorio;

    public EntidadDTO(String nombre, int vidaActual, int vidaMax, int manaActual, int manaMax,
            boolean estaVivo, boolean tieneEscudo, boolean tieneAturdido,
            int nivel, int experiencia, String clase, int ataqueTotal, int defensaTotal, int velocidadTotal,
            String nombreArma, String nombreAccesorio) {
        this.nombre = nombre;
        this.vidaActual = vidaActual;
        this.vidaMax = vidaMax;
        this.manaActual = manaActual;
        this.manaMax = manaMax;
        this.estaVivo = estaVivo;
        this.tieneEscudo = tieneEscudo;
        this.tieneAturdido = tieneAturdido;
        this.nivel = nivel;
        this.experiencia = experiencia;
        this.clase = clase;
        this.ataqueTotal = ataqueTotal;
        this.defensaTotal = defensaTotal;
        this.velocidadTotal = velocidadTotal;
        this.nombreArma = nombreArma;
        this.nombreAccesorio = nombreAccesorio;
    }

    public String getNombre() {
        return nombre;
    }

    public int getVidaActual() {
        return vidaActual;
    }

    public int getVidaMax() {
        return vidaMax;
    }

    public int getManaActual() {
        return manaActual;
    }

    public int getManaMax() {
        return manaMax;
    }

    public boolean isEstaVivo() {
        return estaVivo;
    }

    public boolean isTieneEscudo() {
        return tieneEscudo;
    }

    public boolean isTieneAturdido() {
        return tieneAturdido;
    }

    public int getNivel() {
        return nivel;
    }

    public int getExperiencia() {
        return experiencia;
    }

    public String getClase() {
        return clase;
    }

    public int getAtaqueTotal() {
        return ataqueTotal;
    }

    public int getDefensaTotal() {
        return defensaTotal;
    }

    public int getVelocidadTotal() {
        return velocidadTotal;
    }

    public String getNombreArma() {
        return nombreArma;
    }

    public String getNombreAccesorio() {
        return nombreAccesorio;
    }
}