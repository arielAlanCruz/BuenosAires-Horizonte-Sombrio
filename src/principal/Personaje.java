package principal;

import java.util.ArrayList;
import java.util.List;

public abstract class Personaje extends Entidad {
	private int nivel;
	private int experiencia;
	private int expParaSiguienteNivel;
	private int manaActual;
	private int manaMax;
	protected TipoPersonaje clase;
	private Inventario inventario;
	private Equipamiento equipamiento;
	protected List<Habilidad> habilidades;
	private List<Observador> observadores;
	
	//Constructor de Personaje
    public Personaje(String nombre, int vidaMax, int manaMax, int ataque, int defensa, int velocidad, TipoPersonaje clase) {
        // Los atributos de Entidad se setean via setters (Entidad no tiene constructor con params aun)
        this.nivel = 1;
        this.experiencia = 0;
        this.expParaSiguienteNivel = 100;
        this.manaMax = manaMax;
        this.manaActual = manaMax;
        this.clase = clase;
        this.inventario = new Inventario();
        this.equipamiento = new Equipamiento();
        this.habilidades = new ArrayList<>();
        this.observadores = new ArrayList<>();
    }
    
    public void ganarExperiencia(int cantidad) {
    	this.experiencia += cantidad;
    	System.out.println(getNombre() + " gano " + cantidad + " de experiencia! Total: " + experiencia);
    	
    	if (this.experiencia >= this.expParaSiguienteNivel) {
    		subirNivel();
    	}
    }
    
    //Funcion para subir de nivel
    public void subirNivel() {
    	this.nivel++;
    	this.experiencia = 0;
    	this.expParaSiguienteNivel = this.expParaSiguienteNivel + 50;
    	
    	switch (this.clase) {
    	case GUERRERO:
    	case GAUCHO:
    		setAtaque(getAtaque() + 5);
    		setDefensa(getDefensa() + 3);
    		setVidaMax(getVidaMax() + 20);
    		break;
    	case MAGO:
    	case PAYADOR:
    		this.manaMax += 15;
    		this.manaActual = this.manaMax;
    		setAtaque(getAtaque() + 7);
    		setVidaMax(getVidaMax() + 10);
    		break;
    	case ARQUERO:
    	case BOLEADOR:
    		setAtaque(getAtaque() + 6);
    		setVelocidad(getVelocidad() + 3);
    		setVidaMax(getVidaMax() + 15);
    		break;
    	case CURANDERA:
    	case CURANDERA_AVANZADA:
    		this.manaMax += 10;
    		this.manaActual = this.manaMax;
    		setDefensa(getDefensa() + 4);
    		setVidaMax(getVidaMax() + 12);
    		break;
    	}
    	System.out.println(getNombre() + " subio al nivel " + nivel + "!");
    	notifyObservers(new EventoCombate(TipoGeneral.VICTORIA));
    }
    
    public void evolucionar() {
    	
    }
	
	public int atacar(Entidad objetivo) {
		
	};
	
	public void defender() {
		
	};
	
	public void usarHabilidad(Habilidad h, List<Entidad> obj) {
		
	};
	
	public void usarItem(ItemConsumible item) {
		
	};
	
	public List<Habilidad> getHabilidadesDisponibles(){
		
	};
	
	public void attach(Observador o) {
		observadores.add(o);
	};
	
	public void notifyObservers(EventoCombate e) {
		for(Observador o : observadores) {
			o.actualizar(e);
		}
	};
	
	//Getters y Setters
    public TipoPersonaje getClase()      { return clase; }
    public int getNivel()     { return nivel; }
    public int getManaActual()      { return manaActual; }
    public int getManaMax()    { return manaMax; }
    public Inventario getInventario()   { return inventario; }
    public Equipamiento getEquipamiento() { return equipamiento; }
    public int getExperiencia(){ return experiencia; }
    
    public void setClase(TipoPersonaje clase) {this.clase = clase; };
    public void setNivel(int nivel) {this.nivel = nivel; };
    public void setManaActual(int manaActual) { this.manaActual = manaActual; };
    public void setManaMax(int manaMax) { this.manaMax = manaMax; };
    public void setExperiencia(int experiencia) { this.experiencia = exp; };
    
    public void setAtaque(int ataque) {};
    public void setDefensa(int defensa) {};
    public void setVelocidad(int velocidad) {};
    public void setVidaMax(int vidaMax) {};
    
    @Override
    public String toString() {
        return String.format("[%s] %s | Vida: %d/%d | Maná: %d/%d | Ataque: %d | Defensa: %d | Vel: %d | Niv: %d",
                clase, getNombre(), getVidaActual(), getVidaMax(),
                manaActual, manaMax,
                getAtaque(), getDefensa(), getVelocidad(), nivel);
    }
}