package Controleur;

import Modele.*;
import Structures.Couple;

import java.util.ArrayList;
import java.util.Random;

public class IAFacile extends IA {
    Random r;

    public IAFacile() {
        r = new Random();
    }

    public PionBasique selectPion() {
        int epoque = calcul.getDj().getJoueurActuel().getFocus();
        ArrayList<PionBasique> pionsDispo = calcul.getDj().getJeu().pionsFocusJoueur(epoque, calcul.getDj().getJoueurActuel());
        int x = r.nextInt(pionsDispo.size());
        return pionsDispo.get(x);
    }

    @Override
    public Couple<Integer, Emplacement> getCoup1() {
        ArrayList<Emplacement> casesDispo = calcul.getDj().getJeu().casesDispo(calcul.getDj().getJoueurActuel(), calcul.getDj().getPionActuel());
        int x = r.nextInt(casesDispo.size());
        return (new Couple(1, casesDispo.get(x)));
    }

    @Override
    public Couple<Integer, Emplacement> getCoup2() {
        return getCoup1();
    }

    public Integer choixFocus() {
        int x = r.nextInt(3);
        while (!calcul.getDj().peutSelectionnerFocus(x, calcul.getDj().getJoueurActuel().getID())) {
            x = r.nextInt(3);
        }
        return x;
    }

    @Override
    public int calculCoup(DeroulementJeu dj, int horizon, boolean joueur, Integer borneCut) {
        return 0;
    }
}
