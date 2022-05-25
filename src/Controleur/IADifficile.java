package Controleur;

import Modele.*;
import Structures.Couple;
import Structures.Tour;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;
import java.lang.reflect.Method;

public class IADifficile extends IA{

    Tour tour;
    int horizon;
    int cpt;
    Method method;
    public IADifficile(Method method) {
        tour = new Tour();
        this.horizon = 15;
        this.method = method;
    }

    public int calculCoup(DeroulementJeu dj, int horizon, boolean joueur) {     // joueur = true <=> calculCoup_Joueur_A
        cpt++;
        CalculJeu c = new CalculJeu(dj);
        if (dj.getEtape() == ETAT.END || horizon == 0) {   // Si horizon atteint ou jeu terminé, on retourne l'évaluation de jeu
            try {
                return (int) method.invoke(c,null);
            } catch (IllegalAccessException e) {
                throw new RuntimeException(e);
            } catch (InvocationTargetException e) {
                throw new RuntimeException(e);
            }
        } else {                                // Sinon, on descend dans l'arbre
            ArrayList<Couple<Integer, Tour>> fils = new ArrayList<>();

            ArrayList<Couple<DeroulementJeu, Tour>> branchements = c.Branchements();
            for (Couple<DeroulementJeu, Tour> couple : branchements) {
                fils.add(new Couple<>(calculCoup(couple.getFirst(), horizon - 1, !joueur), couple.getSecond()));
            }
            int minMax = joueur ? Math.abs(Collections.min(fils).getFirst())+1 : Math.abs(Collections.max(fils).getFirst())+1;
            int somme = 0;
            for (int i = 0; i < fils.size(); i++) {
                int valeur = joueur ? (fils.get(i).getFirst() + minMax) : (-1 * fils.get(i).getFirst() + minMax);
                fils.get(i).setFirst(valeur);
                somme += fils.get(i).getFirst();
            }
            Collections.sort(fils);  // Valeurs des fils triées en ordre décroissant


            Random r = new Random();    // Tirage d'un coup aléatoirement
            int x = r.nextInt(somme);   // On tire entre borne inf et borne sup de fils[1]
            int index = 0;
            while (index < fils.size()-1 && x >= fils.get(index).getFirst()) {
                index++;
            }

            tour = fils.get(index).getSecond();
            return joueur ? (fils.get(index).getFirst() - minMax) : (fils.get(index).getFirst() - minMax) * -1;
        }
    }

    @Override
    public PionBasique selectPion() {
        if (tour.getPionSelectionne() == null) {
            calculCoup(calcul.getDj(), horizon, true);
        }
        return tour.getPionSelectionne();
    }

    @Override
    public Couple<Integer, Emplacement> jouerCoup() {
        if (calcul.getDj().getEtape() == ETAT.MOVE1) {
            if (tour.getCoup1() == null) {
                calculCoup(calcul.getDj(), horizon, true);
            }
            return new Couple(tour.getTypeCoup1(), tour.getCoup1());
        }
        if (calcul.getDj().getEtape() == ETAT.MOVE2) {
            if (tour.getCoup2() == null) {
                calculCoup(calcul.getDj(), horizon, true);
            }
            return new Couple(tour.getTypeCoup2(), tour.getCoup2());
        }
        return null;
    }
    @Override
    public Integer choixFocus() {
        if (tour.getFocus() == null) {
            calculCoup(calcul.getDj(), horizon, true);
        }
        Integer res = tour.getFocus();
        tour = new Tour();  // Tour suivant, il faut recalculer
        return res;
    }
}
