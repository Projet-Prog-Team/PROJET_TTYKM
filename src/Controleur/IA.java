package Controleur;

import Modele.Jeu;
import Modele.Pion;

import java.lang.reflect.Method;

public abstract class IA {
    Jeu jeu;
    public static IA nouvelle(Jeu j, String type, String heuristique) {
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
                Method method = null;
                try {
                    method = Jeu.class.getMethod(heuristique);
                } catch (NoSuchMethodException e) {
                    throw new RuntimeException(e);
                }
                resultat = new IADifficile(method);
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

    public abstract Integer choixFocus();

    public abstract int calculCoup(Jeu j, int horizon);
}
