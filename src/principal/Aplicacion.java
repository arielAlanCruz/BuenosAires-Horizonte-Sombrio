package principal;
 
public class Aplicacion {
 
    public static void main(String[] args) {
        Personaje guerrero = new Guerrero("Aragorn");
        Personaje mago     = new Mago("Gandalf");
        Personaje arquero  = new Arquero("Legolas");
        Curandera curandera  = new Curandera("Elrond");
 
        System.out.println("=== Party ===");
        System.out.println(guerrero);
        System.out.println(mago);
        System.out.println(arquero);
        System.out.println(curandera);
 
        System.out.println("\n=== Combate de prueba ===");
        guerrero.atacar(mago);
        mago.atacar(guerrero);
        curandera.curar(guerrero);
    }
 
}