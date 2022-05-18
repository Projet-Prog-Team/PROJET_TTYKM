package Controleur;

import Modele.DeroulementJeu;
import Modele.Jeu;
import Modele.Pion;

import java.util.ArrayList;
import java.util.Random;

public class IAFacile extends IA {
    Random r;

    public IAFacile() {
        r = new Random();
    }

    public Pion selectPion() {
        int epoque = calcul.getDj().getJoueurActuel().getFocus();
        ArrayList<Pion> pionsDispo = calcul.getDj().getJeu().pionsFocusJoueur(epoque, calcul.getDj().getJoueurActuel());
        int x = r.nextInt(pionsDispo.size());
        return pionsDispo.get(x);
    }

    public Pion jouerCoup() {
        ArrayList<Pion> casesDispo = calcul.getDj().getJeu().casesDispo(calcul.getDj().getJoueurActuel(), calcul.getDj().getPionActuel());
        int x = r.nextInt(casesDispo.size());
        return(casesDispo.get(x));
    }

    public Integer choixFocus() {
        int x = r.nextInt(3);
        while (!calcul.getDj().peutSelectionnerFocus(x, calcul.getDj().getJoueurActuel().getID())) {
            x = r.nextInt(3);
        }
        return x;
    }

    @Override
    public int calculCoup(DeroulementJeu dj, int horizon, boolean joueur) {
        return 0;
    }
}
