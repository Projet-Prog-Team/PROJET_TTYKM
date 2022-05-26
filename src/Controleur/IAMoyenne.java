package Controleur;

import Modele.DeroulementJeu;
import Modele.Jeu;
import Modele.Pion;

public class IAMoyenne extends IA{

    public IAMoyenne() {

    }

    @Override
    public Pion selectPion() {
        return null;
    }

    @Override
    public Pion jouerCoup() {
        return null;
    }

    @Override
    public Integer choixFocus() {
        return 0;
    }

    @Override
    public int calculCoup(DeroulementJeu dj, int horizon, boolean joueur) {
        return 0;
    }
}
