package Modele;

import Structures.Point;

import java.util.ArrayList;
import java.util.Arrays;


public class Jeu implements Comparable {
    // Contenu du jeu
    ArrayList<Pion> pions;
    public Joueur[] joueurs = new Joueur[2];
    public final int NBPIONS =14;

    public Jeu() {
        init();
    }
    public Jeu(boolean real) {
        init();
    }

    public void init() {
        pions = new ArrayList<>();
        joueurs[0] = new Joueur(1,NBPIONS/2);
        joueurs[1] = new Joueur(2,NBPIONS/2);
        for(int i = 0; i < 3; i++) {
            PionBasique p1 = new PionBasique(new Emplacement(new Point(0, 0), i), joueurs[0],NBPIONS/2-joueurs[0].getNbPionsRestants(),false);
            pions.add(p1);
            PionBasique p2 = new PionBasique(new Emplacement(new Point(3, 3), i), joueurs[1],NBPIONS/2-joueurs[1].getNbPionsRestants(),false);
            pions.add(p2);
            joueurs[0].supprimerPion();
            joueurs[1].supprimerPion();
        }
        joueurs[0].setFocus(0);
        joueurs[1].setFocus(2);
    }

    // -------GETTER & SETTERS-------
    public Joueur getJoueur(int id) {
        return joueurs[id];
    }
    public ArrayList<Pion> getPions() {
        return pions;
    }

    public void SetPions(Pion[] t_pion)
    {
        pions.clear();
        for (Pion tt_pion : t_pion) {
            pions.add(tt_pion);
        }
    }

    public Joueur[] getJoueurs() {
        return joueurs;
    }
    public Pion getPion(Emplacement e) {
        Point p = e.getCoordonnees();
        if ((p.getL() <= 3 && p.getL() >= 0) && (p.getC() <= 3 && p.getC() >= 0)) {
            for (Pion pion : pions) {
                if (e.equals(pion.getEmplacement())) {
                    return pion;
                }
            }
        }
        return null;
    }

    // -------INFOS JEU-------
    public ArrayList<Emplacement> casesDispo(Joueur j, Pion p) {   // Retourne une liste des pions jouables pour le joueur cou
        ArrayList<Emplacement> casesDisponibles = new ArrayList<>();
        Point coo = p.getCoordonnees();
        int epoque = p.getEpoque();
        if (epoque - 1 >= EPOQUE.PASSE) {   // Si on peut aller vers le passé
            Emplacement e = new Emplacement(coo, epoque-1);
            if (getPion(e) == null && j.getNbPionsRestants() > 0) {
                casesDisponibles.add(e);
            }
        }
        if (epoque + 1 <= EPOQUE.FUTUR) {   // Si on peut aller vers le futur
            Emplacement e = new Emplacement(coo, epoque+1);
            if (getPion(e) == null) {
                casesDisponibles.add(e);
            }
        }

        int [] dX = {-1, 0, 0, 1};
        int [] dY = {0, -1, 1, 0};
        Point pt;
        for (int i = 0; i < 4; i++) {   // Vérification des cases autour du pion selectionné
            pt = new Point(coo.getL() + dX[i], coo.getC() + dY[i]);
            if (pt.getL() >=0 && pt.getL() <= 3 && pt.getC() >= 0  && pt.getC() <= 3) {
                Emplacement e = new Emplacement(pt, epoque);
                casesDisponibles.add(e);
            }
        }

        return casesDisponibles;
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
    public ArrayList<PionBasique> pionsFocusJoueur(int focus, Joueur j) {
        ArrayList<PionBasique> liste = new ArrayList<>();
        for (Pion pion : pions) {
            if (pion instanceof PionBasique) {
                PionBasique pb = (PionBasique) pion;
                if (pb.getJoueur().equals(j) && pb.getEpoque() == focus) {
                    liste.add(pb);
                }
            }
        }
        return liste;
    }
    public Jeu copy() {
        Jeu j = new Jeu(false);
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
