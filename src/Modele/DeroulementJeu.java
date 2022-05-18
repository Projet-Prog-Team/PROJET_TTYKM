package Modele;

import Patterns.Observable;
import Structures.Point;

import java.util.ArrayList;

public class DeroulementJeu extends Observable implements Comparable  {
    Joueur joueurActuel;
    Pion pionActuel;
    int aGagne;
    Jeu j;

    public DeroulementJeu(Jeu jeu) {
        j = jeu;
        init();
    }

    public void init() {
        setJoueurActuel(j.getJoueur(0)); // joueur 1 commence
        ArrayList<Pion> pionInFocus = j.pionsFocusJoueur(joueurActuel.getFocus(), joueurActuel);
        if (pionInFocus.size() == 1) {
            setPionActuel(pionInFocus.get(0));
        }
        aGagne = 0;
        miseAJour();
    }

    // ------------------Getters & setters------------------
    public Joueur getJoueurActuel() {
        return joueurActuel;
    }
    public void setJoueurActuel(Joueur j) {
        joueurActuel = j;
    }
    public Pion getPionActuel() {
        return pionActuel;
    }
    public void setPionActuel(Pion pionActuel) {
        this.pionActuel = pionActuel;
    }
    public Jeu getJeu(){ return j; }

    // ------------------Etat du jeu------------------
    public boolean estTermine() {
        return (aGagne != 0);
    }
    public boolean peutSelectionnerFocus(int epoque, int id) {
        return id == joueurActuel.getID()
                && epoque != joueurActuel.getFocus()
                && j.pionsFocusJoueur(epoque, joueurActuel).size() != 0;
    }
    public int getEtape() {
        if (aGagne != 0) {
            return 4;
        } else {
            if (getPionActuel() == null) {
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

    // ------------------Actions sur le jeu------------------
    public void selectPion(int l, int c, int epoque) {
        Pion selected = j.getPion(new Point(l, c), epoque);
        if (selected != null && epoque == joueurActuel.getFocus() && selected.getJoueur() == joueurActuel) {
            setPionActuel(selected);
        }
        miseAJour();
    }
    public void changerEpoque(int epoque) { // Si possible, va déplacer le pion selectionné dans epoque et faire les copies éventuelles
        int PionEpoque = getPionActuel().getEpoque();
        if(Math.abs(PionEpoque-epoque)==1) { // Si l'époque visée est accessible +- 1
            Pion pionActuel = getPionActuel();
            if (j.getPion(pionActuel.coordonnees, epoque) == null) {
                pionActuel.epoque = epoque;
                if (epoque < PionEpoque) { // Si l'époque visée est plus loin (dans le futur) que l'époque du pion.
                    j.getPions().add(new Pion(pionActuel.getCoordonnees(), PionEpoque, joueurActuel));
                    //ne supprime pas un pion du plateau mais du nombre total encore disponible à placer
                    joueurActuel.supprimerPion();
                }
            }
        }
    }
    public void jouerCoup(int l, int c, int epoque) {
        Pion clic = new Pion(new Point(l, c), epoque, joueurActuel);
        ArrayList<Pion> cases = j.casesDispo(joueurActuel, pionActuel);

        if (cases.contains(clic)) {    // Si coup jouable
            if (epoque != getPionActuel().getEpoque()) {
                joueurActuel.nbActionsRestantes--;
                changerEpoque(epoque);
            } else {
                move(getPionActuel(), new Point(l, c));
                joueurActuel.nbActionsRestantes--;
                if (j.getPion(getPionActuel().getCoordonnees(), joueurActuel.getFocus()) == null) {   // Si le joueur a tué son propre pion
                    joueurActuel.nbActionsRestantes = 0;
                }
            }
            if (j.joueurAGagne(joueurActuel)) {
                aGagne = joueurActuel.getID();
            } else if (j.joueurAGagne(j.getJoueur((joueurActuel.getID()) % 2)) && joueurActuel.nbActionsRestantes == 0) {
                aGagne = j.getJoueur((joueurActuel.getID()) % 2).getID();
            }
        }
        miseAJour();
    }
    public void move(Pion c, Point new_coord) {
        Point coord = new Point(c.coordonnees.getL(),c.coordonnees.getC());
        Pion voisin = j.getPion(new_coord,c.epoque);
        c.coordonnees = new_coord;
        Point tmp = new Point(new_coord.getL()+(new_coord.getL()-coord.getL()),new_coord.getC()+(new_coord.getC()-coord.getC()));
        if (voisin != null) {
            if (new_coord.getL() >= 4 || new_coord.getC() >= 4 || new_coord.getL()<0 || new_coord.getC()<0) {
                j.getPions().remove(c);
            } else {
                if(voisin.joueur == c.joueur) {
                    j.getPions().remove(c);
                    j.getPions().remove(voisin);
                }
                else {
                    move(voisin, tmp);
                }
            }
        }
        else {
            if (new_coord.getL() >= 4 || new_coord.getC() >= 4 || new_coord.getL()<0 || new_coord.getC()<0) {
                j.getPions().remove(c);
            }
            else {
                c.coordonnees = new_coord;
            }
        }
    }
    public void switchPlayer() {
        if(joueurActuel == j.getJoueur(0)) {
            joueurActuel = j.getJoueur(1);
        }
        else {
            joueurActuel = j.getJoueur(0);
        }
        ArrayList<Pion> pionInFocus = j.pionsFocusJoueur(joueurActuel.getFocus(), joueurActuel);
        joueurActuel.nbActionsRestantes=2;
        if (pionInFocus.size() == 1) {
            // forcer la sélection
            setPionActuel(pionInFocus.get(0));
        } else if (pionInFocus.size() == 0){
            setPionActuel(new Pion(new Point(-1, -1), joueurActuel.getFocus(), joueurActuel));
            joueurActuel.nbActionsRestantes=0;
        } else {
            setPionActuel(null);
        }
        miseAJour();
    }

    //------------------------------------
    public ArrayList<Pion> getPreview(int l, int c, int epoque) {
        DeroulementJeu djeu = copy();
        djeu.jouerCoup(l, c, epoque);
        ArrayList<Pion> preview = djeu.getJeu().getPions();
        return preview;
    }

    //------------------------------------

    public DeroulementJeu copy() {
        Jeu jCopy = getJeu().copy();
        DeroulementJeu djeu = new DeroulementJeu(jCopy);
        djeu.joueurActuel = jCopy.getJoueur(getJoueurActuel().getID() - 1);
        djeu.aGagne = aGagne;
        for (Pion pion : jCopy.getPions()) {
            if (pion.equals(pionActuel)) {
                djeu.pionActuel = pion;
            }
        }
        return djeu;
    }

    @Override
    public String toString() {
        return "DeroulementJeu{" +
                "joueurActuel=" + joueurActuel +
                ", pionActuel=" + pionActuel +
                ", aGagne=" + aGagne +
                ", j=" + j +
                '}';
    }

    @Override
    public int compareTo(Object o) {
        return 0;
    }


}
