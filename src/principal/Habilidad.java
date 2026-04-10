package principal;

import java.util.List;

public class Habilidad {
	private String nombre;
	private String descripcion;
	private int costeMana;
	protected TipoGeneral tipoHabilidad;
	
	private double multiplicadorDanio;
	private int cantidadCuracion;
	private TipoGeneral efecto;
	private boolean esDeArea;
	
	//Constructor para HABILIDAD DE DAÑO
	public Habilidad(String nombre, String descripcion, int costeMana, double multiplicadorDanio, boolean esDeArea) {
		this.nombre = nombre;
		this.setDescripcion(descripcion);
		this.costeMana = costeMana;
		this.tipoHabilidad = TipoGeneral.HABILIDAD_DANIO;
		this.multiplicadorDanio = multiplicadorDanio;
		this.esDeArea = esDeArea;
	}
	
	//Constructor para HABILIDAD DE CURACION
	public Habilidad(String nombre, String descripcion, int costeMana, int cantidadCuracion, boolean esDeArea) {
		this.nombre = nombre;
		this.setDescripcion(descripcion);
		this.costeMana = costeMana;
		this.tipoHabilidad = TipoGeneral.HABILIDAD_CURACION;
		this.cantidadCuracion = cantidadCuracion;
		this.esDeArea = esDeArea;
	}
	
	//Constructor para HABILIDAD DE ESTADO
	public Habilidad(String nombre, String descripcion, int costeMana, TipoGeneral efecto, boolean esDeArea) {
		this.nombre = nombre;
		this.setDescripcion(descripcion);
		this.costeMana = costeMana;
		this.tipoHabilidad = TipoGeneral.HABILIDAD_ESTADO;
		this.efecto = efecto;
		this.esDeArea = esDeArea;
	}
	
	public void ejecutar(Personaje origen, List<Entidad> objetivos) {
		switch(tipoHabilidad) {
			case HABILIDAD_DANIO:
				int danioBase = origen.getAtaque();
				int danioFinal = (int) (danioBase * multiplicadorDanio);
				
				for (Entidad objetivo : objetivos) {
					objetivo.recibirDanio(danioFinal);
					System.out.println(origen.getNombre() + " uso " + nombre + " en " + objetivo.getNombre() + " por " + danioFinal + " de daño.");
					if(!esDeArea) break;
				}
				break;
			case HABILIDAD_CURACION:
				for (Entidad objetivo : objetivos) {
					objetivo.curar(cantidadCuracion);
					System.out.println(origen.getNombre() + " uso " + nombre + " en " + objetivo.getNombre() + " curando " + cantidadCuracion + " de vida.");
					if(!esDeArea) break;
				}
				break;
			case HABILIDAD_ESTADO:
				for(Entidad objetivo : objetivos) {
					objetivo.aplicarEfecto(efecto);
					System.out.println(origen.getNombre() + " uso " + nombre + " en " + objetivo.getNombre() + ". Efecto aplicado: " + efecto);
				}
			default:
				System.out.println("Tipo de habilidad desconocido: " + tipoHabilidad);
				break;
		}
			
	}
	
	public boolean puedeUsarse(Personaje p) {
		return p.getManaActual() >= this.costeMana;
	}
	
	public String getNombre() {
		return nombre;
	}
	
	public int getCosteMana() {
		return costeMana;
	}
	
	public TipoGeneral getTipoHabilidad() {
		return tipoHabilidad;
	}

	public String getDescripcion() {
		return descripcion;
	}

	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}
}
