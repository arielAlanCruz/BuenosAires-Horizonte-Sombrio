package principal;

import java.util.ArrayList;

public class Guerrero extends Personaje{
	
	private int bonusCuerpoACuerpo;
	
    public Guerrero(String nombre) {
        super(nombre, 120, 30, 35, 25, 10, TipoPersonaje.GUERRERO);
        this.bonusCuerpoACuerpo = 5;
        
        this.habilidades = new ArrayList<>();
        this.habilidades.add(new Habilidad("Golpe Fuerte", "Un golpe devastador cuerpo a cuerpo", 10, 1.8, false));
        this.habilidades.add(new Habilidad("Intimidar", "Reduce el ataque del enemigo", 8, TipoGeneral.DEBILITADO, false));
    }

	@Override
	public int atacar(Entidad objetivo) {
		// TODO Auto-generated method stub
		int danio = getAtaque() + bonusCuerpoACuerpo;
		objetivo.recibirDanio(danio);
        System.out.println(getNombre() + " ataca a " + objetivo.getNombre() + "con su espada por " + danio + "de daño!");
        return danio;
	}

	@Override
	public void evolucionar() {
		// TODO Auto-generated method stub
        // Solo puede evolucionar si es Guerrero base
        if (this.clase != TipoPersonaje.GUERRERO) {
            System.out.println(getNombre() + " ya evolucionó, no puede volver a hacerlo.");
            return;
        }

        this.clase = TipoPersonaje.GAUCHO;

        // Multiplica ataque y defensa por 1.3
        setAtaque((int)(getAtaque() * 1.3));
        setDefensa((int)(getDefensa() * 1.3));
        this.bonusCuerpoACuerpo = (int)(this.bonusCuerpoACuerpo * 1.3);

        System.out.println(getNombre() + " evolucionó a GAUCHO.");
        System.out.println("  Ataque:  " + getAtaque());
        System.out.println("  Defensa: " + getDefensa());
	}
	
	public int getBonusCuerpoACuerpo() {return bonusCuerpoACuerpo;};
	public void setBonusCuerpoACuerpo(int bonus) {this.bonusCuerpoACuerpo = bonus;};
}