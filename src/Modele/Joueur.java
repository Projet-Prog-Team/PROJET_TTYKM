package Modele;

public class Joueur {
    int ID;
    int nbPionsRestants = 4;
    int nbActionsRestantes = 2;

    private int focus;

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public Joueur(int id,int t_nbpions) {
        ID = id;
        nbPionsRestants = t_nbpions;
    }

    public boolean peutSupprimerPion() {
        return nbPionsRestants != 0;
    }

    public void supprimerPion() {
        nbPionsRestants--;
    }

    public int getFocus() {
        return focus;
    }
    public void setFocus(int f) {this.focus = f;}
    public int getNbActionsRestantes() { return nbActionsRestantes; }
    /*@Override
    public String toString() {
        return "\nJoueur{" +
                "ID=" + ID +
                ", nbPionsRestants=" + nbPionsRestants +
                ", nbActionsRestantes=" + nbActionsRestantes +
                ", pionActuel=" +  +
                ", focus=" + focus +
                '}';
    }*/
}
