package Modele;

public class Joueur {
    int ID;
    int nbPionsRestants = 4;
    int nbActionsRestantes = 2;
    Pion pionActuel;
    int focus;

    public Joueur(int id) {
        ID = id;
    }

    public boolean peutSupprimerPion() {
        return nbPionsRestants == 0;
    }

    public void supprimerPion() {
        nbPionsRestants--;
    }
    public Pion getPionActuel() { return pionActuel; }
    public int getFocus() {
        return focus;
    }

    @Override
    public String toString() {
        return "Joueur{" +
                "ID=" + ID +
                ", nbPionsRestants=" + nbPionsRestants +
                ", nbActionsRestantes=" + nbActionsRestantes +
                ", pionActuel=" + pionActuel +
                ", focus=" + focus +
                '}';
    }
}
