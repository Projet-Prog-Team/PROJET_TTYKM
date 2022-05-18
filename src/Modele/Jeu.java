package Modele;

import Structures.Point;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Arrays;


public class Jeu implements Comparable {
    // Contenu du jeu
    ArrayList<Pion> pions;
    public Joueur[] joueurs = new Joueur[2];
    /*private boolean real =false;
    Joueur joueurActuel;
    private Pion pionActuel;
    private ArrayList<Pion> preview;
    public ManageFiles MemoryManager;
    public final int NBPIONS = 14;
    int previewFocus1;
    int previewFocus2;
    int aGagne;

    Pion suggestionSource, suggestionDest;
    int suggestionFocus;*/

    public Jeu() {
        real=true;
        init();
    }
    public Jeu(boolean real) {
        init();
        real=real;
    }

    public void init() {
        pions = new ArrayList<>();
        joueurs[0] = new Joueur(1,NBPIONS/2);
        joueurs[1] = new Joueur(2,NBPIONS/2);
        preview = null;
        for(int i = 0; i < 3; i++) {
            Pion p1 = new Pion(new Point(0, 0), i, joueurs[0],NBPIONS/2-joueurs[0].getNbPionsRestants(),false);
            pions.add(p1);
            Pion p2 = new Pion(new Point(3, 3), i, joueurs[1],NBPIONS-joueurs[1].getNbPionsRestants(),false);
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

    // -------INFOS JEU-------
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
