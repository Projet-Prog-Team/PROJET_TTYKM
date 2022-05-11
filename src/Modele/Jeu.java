package Modele;

import Patterns.Observable;
import Structures.Couple;
import Structures.Point;
import Structures.Tour;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;


public class Jeu extends Observable {
    ArrayList<Pion> pions;
    public Joueur[] joueurs = new Joueur[2];
    Joueur joueurActuel;
    Pion pionActuel;
    private ArrayList<Pion> preview;
    int aGagne;

    public Jeu() {
        init();
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
        setJoueurActuel(joueurs[0]); // joueur 1 commence
        ArrayList<Pion> pionInFocus = pionsFocusJoueur(joueurActuel.getFocus(), joueurActuel);
        setPionActuel(pionInFocus.get(0));
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
    public Pion getPionActuel() {
        return pionActuel;
    }
    public void setPionActuel(Pion pionActuel) {
        this.pionActuel = pionActuel;
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

    public ArrayList<Pion> getPreview() {
        return preview;
    }

    // ---------------Infos sur le jeu---------------
    public boolean estTermine() {
        return (aGagne != 0);
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
    public ArrayList<Pion> casesDispo() {   // Retourne une liste des pions jouables pour le joueur cou
        Pion pionActuel = getPionActuel();
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
            if (pion.getJoueur().equals(j) && pion.getEpoque() == focus) {
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
    public void enablePreview(int l, int c, int epoque) {
        Jeu j = copy();
        j.jouerCoup(l, c, epoque);
        preview = j.getPions();
        miseAJour();
    }
    public int Heuristique() {
        int pionsJoueur1 = joueurs[0].nbPionsRestants;
        int pionsJoueur2 = joueurs[1].nbPionsRestants;

        for (int i = 0; i < 3; i++) {
            pionsJoueur1 += pionsFocusJoueur(i, joueurs[0]).size();
        }
        for (int i = 0; i < 3; i++) {
            pionsJoueur2 += pionsFocusJoueur(i, joueurs[1]).size();
        }

        if (joueurActuel.getID() == 1) {
            return (pionsJoueur1 - pionsJoueur2);
        } else {
            return (pionsJoueur2 - pionsJoueur1);
        }
    }
    public ArrayList<Couple<Jeu, Tour>> branchementsSelect(Jeu j) {   // On considere que le jeu j est dans l'étape selection
        ArrayList<Couple<Jeu, Tour>> jeux = new ArrayList<>();
        ArrayList<Pion> pionsToBeSelected = pionsFocusJoueur(getJoueurActuel().getFocus(), getJoueurActuel());

        for (Pion pionTBS : pionsToBeSelected) {
            Jeu jeuSelect = j.copy();
            Point cooPionSelect = pionTBS.getCoordonnees();
            jeuSelect.selectPion(cooPionSelect.getL(), cooPionSelect.getC(), pionTBS.getEpoque());
            Tour t = new Tour();
            t.setPionSelectionne(pionTBS);
            jeux.add(new Couple(jeuSelect, t));
        }
        return jeux;
    }
    public ArrayList<Couple<Jeu, Tour>> branchementsCoup(Jeu j) {     // j est dans l'étape jouer un coup
        ArrayList<Couple<Jeu, Tour>> jeux = new ArrayList<>();
        ArrayList<Pion> casesDispo = j.casesDispo();

        for (Pion caseDispo : casesDispo) {
            Jeu jeuCoup = j.copy();
            Point cooCase = caseDispo.getCoordonnees();
            jeuCoup.jouerCoup(cooCase.getL(), cooCase.getC(), caseDispo.getEpoque());
            Tour t = new Tour();
            if (j.getJoueurActuel().getNbActionsRestantes() == 2) {
                t.setCoup1(caseDispo);
            } else {
                t.setCoup2(caseDispo);
            }
            jeux.add(new Couple(jeuCoup, t));
        }
        return jeux;
    }
    public ArrayList<Couple<Jeu, Tour>> branchementsFocus(Jeu j) {    // j est dans l'étape choisir un focus
        ArrayList<Couple<Jeu, Tour>> jeux = new ArrayList<>();

        for (int i = 0; i < 3; i++) {
            if (j.peutSelectionnerFocus(i, j.getJoueurActuel().getID())) {
                Jeu jeuFocus = j.copy();
                jeuFocus.getJoueurActuel().setFocus(i);
                Tour t = new Tour();
                t.setFocus(i);
                jeux.add(new Couple(jeuFocus, t));
            }
        }
        return jeux;
    }
    public ArrayList<Couple<Jeu, Tour>> Branchements() {

        ArrayList<Couple<Jeu, Tour>> jeuxSelect = new ArrayList<>();
        ArrayList<Couple<Jeu, Tour>> jeuxCoup1 = new ArrayList<>();
        ArrayList<Couple<Jeu, Tour>> jeuxCoup2 = new ArrayList<>();
        ArrayList<Couple<Jeu, Tour>> jeuxFocus = new ArrayList<>();

        switch(getEtape()) {
            case 1:
                jeuxSelect = branchementsSelect(this);
            case 2:
                if (jeuxSelect.size() == 0) {                           // Si pion deja selectionné
                    jeuxSelect.add(new Couple(this, new Tour()));
                }

                if (getJoueurActuel().getNbActionsRestantes() == 2) {   // Si etape du jeu : coup 1
                    for (Couple<Jeu, Tour> j : jeuxSelect) {
                        ArrayList<Couple<Jeu, Tour>> coups = branchementsCoup(j.getFirst());
                        for (Couple<Jeu, Tour> c : coups) {             // Ajout de la selection aux tours de jeuxCoup1
                            c.getSecond().setPionSelectionne(j.getSecond().getPionSelectionne());
                        }
                        jeuxCoup1.addAll(coups);

                    }
                } else {                                                // Si etape du jeu : coup 2
                    jeuxCoup1.add(new Couple(this, new Tour()));
                }

                for (Couple<Jeu, Tour> j : jeuxCoup1) {
                    ArrayList<Couple<Jeu, Tour>> coups = branchementsCoup(j.getFirst());
                    for (Couple<Jeu, Tour> c : coups) {
                        c.getSecond().setPionSelectionne(j.getSecond().getPionSelectionne());
                        c.getSecond().setCoup1(j.getSecond().getCoup1());
                    }
                    jeuxCoup2.addAll(coups);
                }
            case 3:
                if (jeuxCoup2.size() == 0) {                            // Si etape du jeu : choix focus
                    jeuxCoup2.add(new Couple(this, new Tour()));
                }
                for (Couple<Jeu, Tour> j : jeuxCoup2) {
                    ArrayList<Couple<Jeu, Tour>> focus = branchementsFocus(j.getFirst());
                    for (Couple<Jeu, Tour> f : focus) {
                        f.getSecond().setPionSelectionne(j.getSecond().getPionSelectionne());
                        f.getSecond().setCoup1(j.getSecond().getCoup1());
                        f.getSecond().setCoup2(j.getSecond().getCoup2());
                    }
                    jeuxFocus.addAll(focus);
                }
                break;
        }

        return jeuxFocus;
    }

    // ---------------Actions modifiants le jeu---------------
    public void selectPion(int l, int c, int epoque) {
        Pion selected = getPion(new Point(l, c), epoque);
        if (selected != null && epoque == joueurActuel.getFocus() && selected.getJoueur() == joueurActuel) {
            setPionActuel(selected);
        }
        miseAJour();
    }
    public void jouerCoup(int l, int c, int epoque) {
        Pion clic = new Pion(new Point(l, c), epoque, joueurActuel);
        ArrayList<Pion> cases = casesDispo();

        if (cases.contains(clic)) {    // Si coup jouable
            if (epoque != getPionActuel().getEpoque()) {
                joueurActuel.nbActionsRestantes--;
                changerEpoque(epoque);
            } else {
                move(getPionActuel(), new Point(l, c));
                joueurActuel.nbActionsRestantes--;
                if (getPion(getPionActuel().getCoordonnees(), joueurActuel.getFocus()) == null) {   // Si le joueur a tué son propre pion
                    joueurActuel.nbActionsRestantes = 0;
                }
            }
            if (joueurAGagne(joueurActuel)) {
                aGagne = joueurActuel.getID();
            } else if (joueurAGagne(joueurs[(joueurActuel.getID()) % 2]) && joueurActuel.nbActionsRestantes == 0) {
                aGagne = joueurs[(joueurActuel.getID()) % 2].getID();
            }
            preview = null;
        }
        miseAJour();
    }
    public void changerEpoque(int epoque) { // Si possible, va déplacer le pion selectionné dans epoque et faire les copies éventuelles
        int PionEpoque = getPionActuel().getEpoque();
        if(Math.abs(PionEpoque-epoque)==1) { // Si l'époque visée est accessible +- 1
            Pion pionActuel = getPionActuel();
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
            setPionActuel(pionInFocus.get(0));
        } else if (pionInFocus.size() == 0){
            setPionActuel(new Pion(new Point(-1, -1), joueurActuel.getFocus(), joueurActuel));
            joueurActuel.nbActionsRestantes=0;
        } else {
            setPionActuel(null);
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
                if(voisin.joueur == c.joueur) {
                    pions.remove(c);
                    pions.remove(voisin);
                }
                else {
                    move(voisin, tmp);
                }
            }
        }
        else {
            if (new_coord.getL() >= 4 || new_coord.getC() >= 4 || new_coord.getL()<0 || new_coord.getC()<0) {
                pions.remove(c);
            }
            else {
                c.coordonnees = new_coord;
            }
        }
    }



    // Autres
    public Jeu copy() {
        Jeu j = new Jeu();
        j.joueurs[0] = joueurs[0].copy();
        j.joueurs[1] = joueurs[1].copy();
        j.setJoueurActuel(j.joueurs[getJoueurActuel().getID() - 1]);
        j.pions.clear();
        for (Pion pion : pions) {
            Pion p = pion.copy(j.joueurs[pion.getJoueur().getID()-1]);
            j.pions.add(p);
            if (pion == pionActuel) {
                j.pionActuel = p;
            }
        }
        j.aGagne = aGagne;
        return j;
    }
    @Override
    public String toString() {
        return "Jeu{" +
                "pions=" + pions +
                ", joueurs=" + Arrays.toString(joueurs) +
                ", joueurActuel=" + joueurActuel +
                '}';
    }


}
