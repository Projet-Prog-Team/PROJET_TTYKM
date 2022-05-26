package Controleur;

import Modele.*;
import Structures.Couple;

public class IAMoyenne extends IA{

    public IAMoyenne() {

    }

    @Override
    public PionBasique selectPion() {
        return null;
    }

    @Override
    public Couple<Integer, Emplacement> getCoup1() {
        return null;
    }

    @Override
    public Couple<Integer, Emplacement> getCoup2() {
        return null;
    }

    @Override
    public Integer choixFocus() {
        return 0;
    }

    @Override
    public int calculCoup(DeroulementJeu dj, int horizon, boolean joueur, Integer borneCut) {
        return 0;
    }
}
