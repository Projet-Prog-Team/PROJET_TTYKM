package Modele;

import Patterns.Observable;
import Structures.Couple;
import Structures.Point;
import Structures.Tour;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;


public class Jeu implements Comparable {
    // Contenu du jeu
    ArrayList<Pion> pions;
    public Joueur[] joueurs = new Joueur[2];
    Joueur joueurActuel;
    Pion pionActuel;
    int aGagne;

    public Jeu() {
        init();
    }

    // Reste
    public Joueur getJoueur(int id) {
        return joueurs[id];
    }
    public int distancePionBord(Pion p) {
        int d = 1;
        int l = p.getCoordonnees().getL();
        int c = p.getCoordonnees().getC();
        if (c == 0 || l == 0 || c == 3 || l == 3) { // Si pion au bord
            d = 0;
        }
        return d;
    }
    public ArrayList<Pion> casesDispo(Joueur j, Pion p) {   // Retourne une liste des pions jouables pour le joueur cou
        ArrayList<Pion> casesDisponibles = new ArrayList<>();
        Point coo = p.getCoordonnees();
        int epoque = p.getEpoque();
        if (epoque - 1 >= EPOQUE.PASSE) {   // Si on peut aller vers le passé
            if (getPion(coo, epoque-1) == null && j.getNbPionsRestants() > 0) {
                casesDisponibles.add(new Pion(coo, epoque-1, j));
            }
        }
        if (epoque + 1 <= EPOQUE.FUTUR) {   // Si on peut aller vers le futur
            if (getPion(coo, epoque+1) == null) {
                casesDisponibles.add(new Pion(coo, epoque+1, j));
            }
        }

        int [] dX = {-1, 0, 0, 1};
        int [] dY = {0, -1, 1, 0};
        Point pt;
        for (int i = 0; i < 4; i++) {   // Vérification des cases autour du pion selectionné
            pt = new Point(coo.getL() + dX[i], coo.getC() + dY[i]);
            if (pt.getL() >=0 && pt.getL() <= 3 && pt.getC() >= 0  && pt.getC() <= 3) {
                casesDisponibles.add(new Pion(pt, epoque, j));
            }
        }

        return casesDisponibles;
    }
    public void init() {
        pions = new ArrayList<>();
        joueurs[0] = new Joueur(1);
        joueurs[1] = new Joueur(2);
        for(int i = 0; i < 3; i++) {
            Pion p1 = new Pion(new Point(0, 0), i, joueurs[0]);
            pions.add(p1);
            Pion p2 = new Pion(new Point(3, 3), i, joueurs[1]);
            pions.add(p2);
        }
        joueurs[0].setFocus(0);
        joueurs[1].setFocus(2);
    }
    public ArrayList<Pion> getPions() {
        return pions;
    }
    public Joueur[] getJoueurs() {
        return joueurs;
    }
    public Pion getPion(Point p, int e) {
        if ((p.getL() <= 3 && p.getL() >= 0) && (p.getC() <= 3 && p.getC() >= 0)) {
            for (Pion pion : pions) {
                if (p.equals(pion.getCoordonnees()) && e == pion.getEpoque()) {
                    return pion;
                }
            }
        }
        return null;
    }
    public boolean joueurAGagne(Joueur joueur) {
        Joueur adversaire = joueurs[(joueur.getID()) % 2];
        int nbPlateauxVides = 0;
        for (int i = 0; i < 3; i++) {
            if (pionsFocusJoueur(i, adversaire).size() == 0) {
                nbPlateauxVides++;
            }
        }
        return nbPlateauxVides >= 2;
    }
    public ArrayList<Pion> pionsFocusJoueur(int focus, Joueur j) {
        ArrayList<Pion> liste = new ArrayList<>();
        for (Pion pion : pions) {
            if (pion.getJoueur().equals(j) && pion.getEpoque() == focus) {
                liste.add(pion);
            }
        }
        return liste;
    }
    public Jeu copy() {
        Jeu j = new Jeu();
        j.joueurs[0] = joueurs[0].copy();
        j.joueurs[1] = joueurs[1].copy();
        j.pions.clear();
        for (Pion pion : pions) {
            Pion p = pion.copy(j.joueurs[pion.getJoueur().getID()-1]);
            j.pions.add(p);
        }
        return j;
    }
    @Override
    public String toString() {
        return "Jeu{" +
                "pions=" + pions +
                ", joueurs=" + Arrays.toString(joueurs) +
                '}';
    }
    @Override
    public int compareTo(Object o) {
        return 0;
    }
}
