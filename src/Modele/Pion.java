package Modele;

public class Pion extends Cases{
    Joueur joueur;
    public Pion(Joueur j) {
        super(TYPE.PION);
        joueur = j;
    }

    @Override
    public String toString() {
        switch (type) {
            case VIDE:
                return " 0 ";
            case PION:
                return "" + joueur.getID();
            default:
                return "PROBLEME";
        }
    }
}
