package Modele;

import Patterns.Observable;
import Structures.Point;
import java.util.ArrayList;
import java.util.Arrays;


public class Jeu extends Observable {
    private final ArrayList<Pion> pions = new ArrayList<>();
    public Joueur[] joueurs = new Joueur[2];
    Joueur joueurActuel;
    int aGagne;

    public Jeu() {
        init();
    }


    public void init() {
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
        joueurs[0].setPionActuel(getPion(new Point(0, 0), 0));
        joueurs[1].setPionActuel(getPion(new Point(3, 3), 2));
        aGagne = 0;

    }



    // ---------------Getter & setters---------------
    public ArrayList<Pion> getPions() {
        return pions;
    }
    public Joueur[] getJoueurs() {
        return joueurs;
    }
    public Joueur getJoueurActuel() {
        return joueurActuel;
    }
    public void setJoueurActuel(Joueur j) {
        joueurActuel = j;
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
    public int getEtape() {
        if (aGagne != 0) {
            System.out.println("Joueur qui a gagné : " + aGagne);
            return 4;
        } else {
            if (joueurActuel.getPionActuel() == null) {
                return 1;
            } else {
                if (joueurActuel.getNbActionsRestantes() == 0) {
                    return 3;
                } else {
                    return 2;
                }
            }
        }
    }



    // ---------------Infos sur le jeu---------------
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
    public ArrayList<Pion> casesDispo() {   // Retourne une liste des pions jouables pour le joueur cou
        Pion pionActuel = joueurActuel.getPionActuel();
        ArrayList<Pion> casesDisponibles = new ArrayList<>();
        Point coo = pionActuel.getCoordonnees();
        int epoque = pionActuel.getEpoque();
        if (epoque - 1 >= EPOQUE.PASSE) {   // Si on peut aller vers le passé
            if (getPion(coo, epoque-1) == null) {
                casesDisponibles.add(new Pion(coo, epoque-1, joueurActuel));
            }
        }
        if (epoque + 1 <= EPOQUE.FUTUR) {   // Si on peut aller vers le futur
            if (getPion(coo, epoque+1) == null) {
                casesDisponibles.add(new Pion(coo, epoque+1, joueurActuel));
            }
        }

        int [] dX = {-1, 0, 0, 1};
        int [] dY = {0, -1, 1, 0};
        Point p;
        for (int i = 0; i < 4; i++) {   // Vérification des cases autour du pion selectionné
            p = new Point(coo.getL() + dX[i], coo.getC() + dY[i]);
            if (p.getL() >=0 && p.getL() <= 3 && p.getC() >= 0  && p.getC() <= 3) {
                casesDisponibles.add(new Pion(p, epoque, joueurActuel));
            }
        }

        return casesDisponibles;
    }
    public ArrayList<Pion> pionsFocusJoueur(int focus, Joueur j) {
        ArrayList<Pion> liste = new ArrayList<>();
        for (Pion pion : pions) {
            if (pion.getJoueur() == j && pion.getEpoque() == focus) {
                liste.add(pion);
            }
        }
        return liste;
    }
    public boolean peutSelectionnerFocus(int epoque, int j) {
        return j == joueurActuel.getID()
                && epoque != joueurActuel.getFocus()
                && pionsFocusJoueur(epoque, joueurActuel).size() != 0;
    }

    public String getState(){
        String state = "";
        if(getEtape()==1){
            state="selection";
        }else if(getEtape()==2){
            if(joueurActuel.getNbActionsRestantes()==2){
                state="action1";
            }else{
                state="action2";
            }
        }else if(getEtape()==3){
            state = "focus";
        }else if(getEtape()==4){
            if(aGagne==1){
                state = "j1gagne";
            }else{
                state = "j2gagne";
            }
        }

        return state;
    }


    // ---------------Actions modifiants le jeu---------------
    public void selectPion(int l, int c, int epoque) {
        Pion selected = getPion(new Point(l, c), epoque);
        if (selected != null && epoque == joueurActuel.getFocus() && selected.getJoueur() == joueurActuel) {
            joueurActuel.setPionActuel(selected);
        }
        miseAJour();
    }
    public void jouerCoup(int l, int c, int epoque) {
        Pion clic = new Pion(new Point(l, c), epoque, joueurActuel);
        ArrayList<Pion> cases = casesDispo();

        if (cases.contains(clic)) {    // Si coup jouable
            joueurActuel.nbActionsRestantes--;
            if (epoque != joueurActuel.getPionActuel().getEpoque()) {
                changerEpoque(epoque);
            } else {
                move(joueurActuel.getPionActuel(), new Point(l, c));
                if (getPion(joueurActuel.pionActuel.getCoordonnees(), joueurActuel.getFocus()) == null) {   // Si le joueur a tué son propre pion
                    joueurActuel.nbActionsRestantes = 0;
                }
            }
            if (joueurAGagne(joueurActuel)) {
                aGagne = joueurActuel.getID();
            } else if (joueurAGagne(joueurs[(joueurActuel.getID()) % 2]) && joueurActuel.nbActionsRestantes == 0) {
                aGagne = joueurs[(joueurActuel.getID()) % 2].getID();
            }
        }
        System.out.println("nbActionsRestantes  : " + joueurActuel.getNbActionsRestantes());
        System.out.println("nbPionsRestants  : " + joueurActuel.nbPionsRestants);
        miseAJour();
    }
    public void changerEpoque(int epoque) { // Si possible, va déplacer le pion selectionné dans epoque et faire les copies éventuelles
        int PionEpoque = joueurActuel.getPionActuel().getEpoque();
        if(Math.abs(PionEpoque-epoque)==1) { // Si l'époque visée est accessible +- 1
            Pion pionActuel = joueurActuel.getPionActuel();
            if (getPion(pionActuel.coordonnees, epoque) == null) {
                pionActuel.epoque = epoque;
                if (epoque < PionEpoque) { // Si l'époque visée est plus loin (dans le futur) que l'époque du pion.
                    if (joueurActuel.peutSupprimerPion()) {
                        pions.add(new Pion(pionActuel.getCoordonnees(), PionEpoque, joueurActuel));
                        //ne supprime pas un pion du plateau mais du nombre total encore disponible à placer
                        joueurActuel.supprimerPion();
                    } else {
                        pionActuel.epoque = PionEpoque;
                    }
                }
            }
        }
    }
    public void switchPlayer() {
        if(joueurActuel == joueurs[0]) {
            joueurActuel=joueurs[1];
        }
        else {
            joueurActuel=joueurs[0];
        }
        ArrayList<Pion> pionInFocus = pionsFocusJoueur(joueurActuel.getFocus(), joueurActuel);
        joueurActuel.nbActionsRestantes=2;
        if (pionInFocus.size() == 1) {
            // forcer la sélection
            joueurActuel.setPionActuel(pionInFocus.get(0));
        } else if (pionInFocus.size() == 0){
            joueurActuel.setPionActuel(new Pion(new Point(-1, -1), joueurActuel.getFocus(), joueurActuel));
            joueurActuel.nbActionsRestantes=0;
        } else {
            joueurActuel.setPionActuel(null);
        }
        miseAJour();
    }
    public void move(Pion c, Point new_coord) {
        Point coord = new Point(c.coordonnees.getL(),c.coordonnees.getC());
        Pion voisin = getPion(new_coord,c.epoque);
        c.coordonnees = new_coord;
        Point tmp = new Point(new_coord.getL()+(new_coord.getL()-coord.getL()),new_coord.getC()+(new_coord.getC()-coord.getC()));
        if (voisin != null) {
            if (new_coord.getL() >= 4 || new_coord.getC() >= 4 || new_coord.getL()<0 || new_coord.getC()<0) {
                pions.remove(c);
            } else {
                if(voisin.joueur == c.joueur)
                {
                    pions.remove(c);
                    pions.remove(voisin);
                }
                else {
                        move(voisin, tmp);
                }
            }
        }
        else
        {
            if (new_coord.getL() >= 4 || new_coord.getC() >= 4 || new_coord.getL()<0 || new_coord.getC()<0)
            {
                pions.remove(c);
            }
            else
            {
                c.coordonnees = new_coord;
            }
        }
    }



    // toString
    @Override
    public String toString() {
        return "Jeu{" +
                "pions=" + pions +
                ", joueurs=" + Arrays.toString(joueurs) +
                ", joueurActuel=" + joueurActuel +
                '}';
    }


}
