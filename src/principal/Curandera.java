package principal;

public class Curandera extends Personaje {
    public Curandera(String nombre) {
        super(nombre, 100, 110, 10, 12, 12);
    }
 
    @Override
    public void atacar(Personaje objetivo) {
        System.out.println(getNombre() + " golpea con su báculo.");
        objetivo.recibirDanio(getAtaque());
    }
 
    // Habilidad especial de la Curandera
    public void curar(Personaje aliado) {
        if (getMana() >= 25) {
            int curacion = 30;
            aliado.setVida(aliado.getVida() + curacion);
            setMana(getMana() - 25);
            System.out.println(getNombre() + " cura a " + aliado.getNombre() + " por " + curacion + " puntos.");
        } else {
            System.out.println(getNombre() + " no tiene maná para curar.");
        }
    }
}