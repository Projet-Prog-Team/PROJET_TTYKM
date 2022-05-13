package Controleur;

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
        int epoque = jeu.getJoueurActuel().getFocus();
        ArrayList<Pion> pionsDispo = jeu.pionsFocusJoueur(epoque, jeu.getJoueurActuel());
        int x = r.nextInt(pionsDispo.size());
        return pionsDispo.get(x);
    }

    public Pion jouerCoup() {
        ArrayList<Pion> casesDispo = jeu.casesDispo();
        int x = r.nextInt(casesDispo.size());
        return(casesDispo.get(x));
    }

    public Integer choixFocus() {
        int x = r.nextInt(3);
        while (!jeu.peutSelectionnerFocus(x, jeu.getJoueurActuel().getID())) {
            x = r.nextInt(3);
        }
        return x;
    }

    @Override
    public int calculCoup(Jeu j, int horizon) {
        return 0;
    }
}
