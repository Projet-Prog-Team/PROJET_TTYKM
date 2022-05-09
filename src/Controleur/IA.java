package Controleur;

import Modele.Jeu;
import Modele.Pion;

public abstract class IA {
    Jeu jeu;
    public static IA nouvelle(Jeu j, String type) {
        IA resultat = null;
        // Méthode de fabrication pour l'IA, qui crée le bon objet selon la config
        switch (type) {
            case "facile":
                resultat = new IAFacile();
                break;
            case "moyenne":
                resultat = new IAMoyenne();
                break;
            case "difficile":
                resultat = new IADifficile();
                break;
            default:
                System.out.println("IA non reconnue");
        }
        if (resultat != null) {
            resultat.jeu = j;
        }
        return resultat;
    }
    public abstract Pion selectPion();

    public abstract Pion jouerCoup();

    public abstract int choixFocus();
}
