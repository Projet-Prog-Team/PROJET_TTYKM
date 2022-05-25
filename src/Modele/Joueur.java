package Modele;

public class Joueur {
    int ID;
    private boolean statuePlaced;
    private int nbPionsRestants;
    public int nbActionsRestantes;
    int focus;


    public Joueur(int id,int nbpions) {
        ID = id;
        nbPionsRestants = nbpions;
        statuePlaced = false;
        nbActionsRestantes = 2;
    }

    public boolean isStatuePlaced() {
        return statuePlaced;
    }

    public void setStatuePlaced(boolean statuePlaced) {
        this.statuePlaced = statuePlaced;
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
                ", statuePlaced=" + statuePlaced +
                '}';
    }
    public Joueur copy() {
        Joueur j = new Joueur(getID(),nbPionsRestants);
        j.nbActionsRestantes = getNbActionsRestantes();
        j.setFocus(getFocus());
        j.setStatuePlaced(statuePlaced);
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
