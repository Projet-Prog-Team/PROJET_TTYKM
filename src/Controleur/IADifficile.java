package Controleur;

import Modele.Jeu;
import Modele.Pion;
import Structures.Couple;
import Structures.Tour;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

public class IADifficile extends IA{

    Tour tour;
    int horizon;
    int cpt;
    
    public IADifficile() {
        tour = new Tour();
        Random r = new Random();
        this.horizon = 10;
    }

    public int calculCoup(Jeu j, int horizon) {
        cpt++;
        if (j.estTermine() || horizon == 0) {
            return j.Heuristique();
        } else {
            ArrayList<Integer> valeursFils = new ArrayList<>();
            ArrayList<Tour> toursFils = new ArrayList<>();

            ArrayList<Couple<Jeu, Tour>> branchements = j.Branchements();
            for (Couple<Jeu, Tour> couple : branchements) {
                toursFils.add(couple.getSecond());
                valeursFils.add(calculCoup_B(couple.getFirst(), horizon - 1));
            }

            int index = valeursFils.indexOf(Collections.max(valeursFils));
            tour = toursFils.get(index);
            return valeursFils.get(index);
        }
    }

    public int calculCoup_B(Jeu j, int horizon) {
        cpt++;
        if (j.estTermine() || horizon == 0) {
            return j.Heuristique();
        } else {
            ArrayList<Integer> valeursFils = new ArrayList<>();
            ArrayList<Tour> toursFils = new ArrayList<>();

            ArrayList<Couple<Jeu, Tour>> branchements = j.Branchements();
            for (Couple<Jeu, Tour> couple : branchements) {
                toursFils.add(couple.getSecond());
                valeursFils.add(calculCoup(couple.getFirst(), horizon - 1));
            }

            int index = valeursFils.indexOf(Collections.min(valeursFils));
            tour = toursFils.get(index);
            return valeursFils.get(index);
        }
    }

    @Override
    public Pion selectPion() {
        if (tour.getPionSelectionne() == null) {
            calculCoup(jeu, horizon);
        }
        return tour.getPionSelectionne();
    }

    @Override
    public Pion jouerCoup() {
        if (jeu.getJoueurActuel().getNbActionsRestantes() == 2) {
            if (tour.getCoup1() == null) {
                calculCoup(jeu, horizon);
            }
            return tour.getCoup1();
        } else {
            if (tour.getCoup2() == null) {
                calculCoup(jeu, horizon);
            }
            return tour.getCoup2();
        }
    }
    @Override
    public Integer choixFocus() {
        if (tour.getFocus() == null) {
            calculCoup(jeu, horizon);
        }
        Integer res = tour.getFocus();
        tour = new Tour();  // Tour suivant, il faut recalculer
        return res;
    }
}
