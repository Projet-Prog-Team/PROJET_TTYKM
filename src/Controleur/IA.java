package Controleur;

import Modele.*;
import Structures.Couple;

public abstract class IA {
    CalculJeu calcul;
    public static IA nouvelle(CalculJeu c, String type) {
        IA resultat = null;
        // Méthode de fabrication pour l'IA, qui crée le bon objet selon la config
        switch (type) {
            case "facile":
                resultat = new IAMinMax(1);
                break;
            case "moyenne":
                resultat = new IAMinMax(2);
                break;
            case "difficile":
                resultat = new IAMinMax(4);
                break;
            default:
                System.out.println("IA non reconnue");
        }
        if (resultat != null) {
            resultat.calcul = c;
        }
        return resultat;
    }
    public abstract PionBasique selectPion();
    public abstract Couple<Integer, Emplacement> getCoup1();
    public abstract Couple<Integer, Emplacement> getCoup2();
    public abstract Integer choixFocus();
    public abstract int calculCoup(DeroulementJeu dj, int horizon, boolean joueur, Integer borneCut);
}
