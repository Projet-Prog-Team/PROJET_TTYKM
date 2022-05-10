package Modele;

public class Joueur {
    int ID;
    int nbPionsRestants = 4;
    public int nbActionsRestantes = 2;
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
    public int getFocus() {
        return focus;
    }
    public void setFocus(int f) {this.focus = f;}
    public int getNbActionsRestantes() { return nbActionsRestantes; }

    @Override
    public boolean equals(Object obj) {
        Joueur j = (Joueur) obj;
        return j.getID() == getID();
    }

    @Override
    public String toString() {
        return "\nJoueur{" +
                "ID=" + ID +
                ", nbPionsRestants=" + nbPionsRestants +
                ", nbActionsRestantes=" + nbActionsRestantes +
                ", focus=" + focus +
                '}';
    }
    public Joueur copy() {
        Joueur j = new Joueur(getID());
        j.nbPionsRestants = getNbPionsRestants();
        j.nbActionsRestantes = getNbActionsRestantes();
        j.setFocus(getFocus());
        return j;
    }
    public int getNbPionsRestants() {
        return nbPionsRestants;
    }
}
