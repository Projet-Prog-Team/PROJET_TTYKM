package Modele;

public class Joueur {
    int ID;
    private int nbPionsRestants;
    public int nbActionsRestantes = 2;
    int focus;

    public Joueur(int id,int nbpions) {
        ID = id;
        nbPionsRestants = nbpions;
    }

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
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
        Joueur j = new Joueur(getID(),nbPionsRestants);
        j.nbActionsRestantes = getNbActionsRestantes();
        j.setFocus(getFocus());
        return j;
    }
    public int getNbPionsRestants() {
        return nbPionsRestants;
    }

    public void SetNbPionsRestants(int t_nb)
    {
        nbPionsRestants= t_nb;
    }
}
