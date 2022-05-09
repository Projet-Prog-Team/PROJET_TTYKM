package Modele;

public class Joueur {
    int ID;
    int nbPionsRestants = 4;
    public int nbActionsRestantes = 2;
    Pion pionActuel;
    int focus;

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public Joueur(int id) {
        ID = id;
    }

    public boolean peutSupprimerPion() {
        return nbPionsRestants != 0;
    }

    public void supprimerPion() {
        nbPionsRestants--;
    }
    public Pion getPionActuel() { return pionActuel; }
    public void setPionActuel(Pion p) {this.pionActuel = p;}
    public int getFocus() {
        return focus;
    }
    public void setFocus(int f) {this.focus = f;}
    public int getNbActionsRestantes() { return nbActionsRestantes; }
    @Override
    public String toString() {
        return "\nJoueur{" +
                "ID=" + ID +
                ", nbPionsRestants=" + nbPionsRestants +
                ", nbActionsRestantes=" + nbActionsRestantes +
                ", pionActuel=" + pionActuel +
                ", focus=" + focus +
                '}';
    }
}
