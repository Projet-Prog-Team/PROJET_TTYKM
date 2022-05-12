package Controleur;

import Modele.Jeu;
import Modele.Pion;
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
        this.horizon = 10;
        this.method = method;
    }

    public int calculCoup(Jeu j, int horizon) {
        cpt++;
        if (j.estTermine() || horizon == 0) {
            try {
                return (int) method.invoke(j,null );
            } catch (IllegalAccessException e) {
                throw new RuntimeException(e);
            } catch (InvocationTargetException e) {
                throw new RuntimeException(e);
            }
        } else {
            ArrayList<Couple<Integer, Tour>> fils = new ArrayList<>();

            ArrayList<Couple<Jeu, Tour>> branchements = j.Branchements();
            for (Couple<Jeu, Tour> couple : branchements) {
                fils.add(new Couple<>(calculCoup_B(couple.getFirst(), horizon - 1), couple.getSecond()));
            }
            int min = Math.abs(Collections.min(fils).getFirst())+1;
            int somme = 0;
            for (int i = 0; i < fils.size(); i++) {
                int valeur = fils.get(i).getFirst() + min;
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
            return fils.get(index).getFirst() - min;
        }
    }

    public int calculCoup_B(Jeu j, int horizon) {
        cpt++;
        if (j.estTermine() || horizon == 0) {
            try {
                return (int) method.invoke(j,null );
            } catch (IllegalAccessException e) {
                throw new RuntimeException(e);
            } catch (InvocationTargetException e) {
                throw new RuntimeException(e);
            }
        } else {
            ArrayList<Couple<Integer, Tour>> fils = new ArrayList<>();

            ArrayList<Couple<Jeu, Tour>> branchements = j.Branchements();
            for (Couple<Jeu, Tour> couple : branchements) {
                fils.add(new Couple<>(calculCoup(couple.getFirst(), horizon - 1), couple.getSecond()));
            }

            int max = Math.abs(Collections.max(fils).getFirst())+1;
            int somme = 0;
            for (int i = 0; i < fils.size(); i++) { // Inverser les signes
                fils.get(i).setFirst((-1 * fils.get(i).getFirst()) + max); // et tout level à > 0
                somme += fils.get(i).getFirst();
            }
            // Ici, la valeur maximale dans fils correspond à l'heuristique la plus faible (celle qu'on veut choisir)

            Collections.sort(fils);  // Valeurs des fils triées en ordre décroissant

            Random r = new Random();    // Tirage d'un coup aléatoirement
            int x = r.nextInt(somme);   // On tire entre borne inf et borne sup de fils[1]
            int index = 0;
            while (index < fils.size()-1 && x >= fils.get(index).getFirst()) {
                index++;
            }

            tour = fils.get(index).getSecond();
            return (fils.get(index).getFirst() - max) * -1;
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
