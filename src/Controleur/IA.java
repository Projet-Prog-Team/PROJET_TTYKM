package Controleur;

import Modele.CalculJeu;
import Modele.DeroulementJeu;
import Modele.Jeu;
import Modele.Pion;

import java.lang.reflect.Method;

public abstract class IA {
    CalculJeu calcul;
    public static IA nouvelle(CalculJeu c, String type, String heuristique) {
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
                    method = CalculJeu.class.getMethod(heuristique);
                } catch (NoSuchMethodException e) {
                    throw new RuntimeException(e);
                }
                resultat = new IADifficile(method);
                break;
            default:
                System.out.println("IA non reconnue");
        }
        if (resultat != null) {
            resultat.calcul = c;
        }
        return resultat;
    }
    public abstract Pion selectPion();

    public abstract Pion jouerCoup();

    public abstract Integer choixFocus();

    public abstract int calculCoup(DeroulementJeu dj, int horizon, boolean joueur);
}
