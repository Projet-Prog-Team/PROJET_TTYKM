package Controleur;

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
    public int calculCoup(Jeu j, int horizon) {
        return 0;
    }
}
