package principal;
 
public class Mago extends Personaje {
    public Mago(String nombre) {
        super(nombre, 70, 120, 35, 5, 10);
    }
 
    @Override
    public void atacar(Personaje objetivo) {
        if (getMana() >= 20) {
            System.out.println(getNombre() + " lanza un hechizo mágico!");
            setMana(getMana() - 20);
            objetivo.recibirDanio(getAtaque());
        } else {
            System.out.println(getNombre() + " no tiene maná suficiente. Ataca con bastón.");
            objetivo.recibirDanio(getAtaque() / 2);
        }
    }
}