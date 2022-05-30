package Controleur;

import Modele.*;
import Structures.Couple;
import Structures.Tour;

import java.util.ArrayList;

public class IAMinMax extends IA{

    Tour tour;
    int horizon;
    int cpt;

    public IAMinMax(int horizon) {
        tour = new Tour();
        this.horizon = horizon;
    }

    public int calculCoup(DeroulementJeu dj, int horizon, boolean joueur, Integer borneCut) {     // joueur = true <=> calculCoup_Joueur_A
        cpt++;
        CalculJeu c = new CalculJeu(dj);
        if (dj.getEtape() == ETAT.END || horizon == 0) {   // Si horizon atteint ou jeu terminé, on retourne l'évaluation de jeu
            if (joueur) {
                return calcul.Heuristique(dj, dj.getJoueurActuel());
            } else {
                return calcul.Heuristique(dj, dj.getJeu().getJoueur(dj.getJoueurActuel().getID() % 2));
            }
        } else {                                // Sinon, on descend dans l'arbre
            ArrayList<Couple<DeroulementJeu, Tour>> branchements = c.Branchements();
            Integer minMax = null;
            int valFils;
            Tour tourMinMax = null;
            boolean cut = false;
            for (Couple<DeroulementJeu, Tour> couple : branchements) {
                if (minMax == null) {
                    valFils = calculCoup(couple.getFirst(), horizon - 1, !joueur, null);
                } else {
                    valFils = calculCoup(couple.getFirst(), horizon - 1, !joueur, minMax);

                    if (borneCut != null) {
                        if ((joueur && borneCut <= valFils) || (!joueur && borneCut >= valFils)) {
                            cut = true;
                        }
                    }
                }
                if (minMax == null || (joueur && valFils > minMax) || (!joueur && valFils < minMax)) {
                    minMax = valFils;
                    tourMinMax = couple.getSecond();
                }
                if (cut) {
                    break;
                }
            }
            tour = tourMinMax;
            return minMax;
        }
    }

    @Override
    public PionBasique selectPion() {
        if (tour.getPionSelectionne() == null) {
            calculCoup(calcul.getDj(), horizon, true, null);
        }
        return tour.getPionSelectionne();
    }

    @Override
    public Couple<Integer, Emplacement> getCoup1() {
        if (tour.getCoup1() == null) {
            calculCoup(calcul.getDj(), horizon, true, null);
        }
        return new Couple(tour.getTypeCoup1(), tour.getCoup1());
    }

    @Override
    public Couple<Integer, Emplacement> getCoup2() {
        if (tour.getCoup2() == null) {
            calculCoup(calcul.getDj(), horizon, true, null);
        }
        return new Couple(tour.getTypeCoup2(), tour.getCoup2());
    }

    @Override
    public Integer choixFocus() {
        if (tour.getFocus() == null) {
            calculCoup(calcul.getDj(), horizon, true, null);
        }
        Integer res = tour.getFocus();
        tour = new Tour();  // Tour suivant, il faut recalculer
        return res;
    }
}
