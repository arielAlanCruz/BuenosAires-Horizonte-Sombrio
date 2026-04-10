package principal;
 
public class Arquero extends Personaje{
    public Arquero(String nombre) {
        super(nombre, 90, 50, 20, 10, 18);
    }
 
    @Override
    public void atacar(Personaje objetivo) {
        System.out.println(getNombre() + " dispara una flecha!");
        // Bonus de daño por alta velocidad
        int danio = getAtaque() + (getVelocidad() / 3);
        objetivo.recibirDanio(danio);
    }
}
 