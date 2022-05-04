package Modele;

import java.util.Arrays;

public class Joueur {
    int ID;
    Pion[] inventaire = new Pion[4];
    int nbActionsRestantes = 2;
    EPOQUE Tableau;

    public Joueur(int id) {
        ID = id;
        for (int i = 0; i < 4; i++) {
            inventaire[i] = new Pion(this);
        }
    }

    public void supprimerPion(Pion pion) {

    }

    @Override
    public String toString() {
        return "Joueur{" +
                "ID=" + ID +
                ", inventaire=" + Arrays.toString(inventaire) +
                ", nbActionsRestantes=" + nbActionsRestantes +
                '}';
    }

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }
}
