package Modele;

import Structures.Couple;
import Structures.Point;
import Structures.Tour;

import java.util.ArrayList;

public class CalculJeu {
    DeroulementJeu dj;

    public CalculJeu(DeroulementJeu djeu) {
        dj = djeu;
    }

    // -------GETTER & SETTERS-------
    public DeroulementJeu getDj() {
        return dj;
    }

    // -------HEURISTIQUES-------
    public int Heuristique() {
        if (dj.getJeu().joueurAGagne(dj.joueurActuel)) { // Si on gagne
            return 10000;
        } else if (!dj.getJeu().joueurAGagne(dj.getJeu().getJoueur(dj.joueurActuel.getID() % 2))) { // Si on ne perd pas
            //int pionsJoueur1 = dj.getJeu().getJoueur(0).nbPionsRestants;
            //int pionsJoueur2 = dj.getJeu().getJoueur(1).nbPionsRestants;
            int pionsJoueur1 = 0;
            int pionsJoueur2 = 0;

            for (int i = 0; i < 3; i++) {
                pionsJoueur1 += dj.getJeu().pionsFocusJoueur(i, dj.getJeu().getJoueur(0)).size();
            }
            for (int i = 0; i < 3; i++) {
                pionsJoueur2 += dj.getJeu().pionsFocusJoueur(i, dj.getJeu().getJoueur(1)).size();
            }

            if (dj.joueurActuel.getID() == 1) {
                return (pionsJoueur1 - pionsJoueur2) * 100;
            } else {
                return (pionsJoueur2 - pionsJoueur1) * 100;
            }
        } else { // Si on perd
            return -10000;
        }
        /* Objectif : permettre à l'IA de tuer des pions
            sinon se démultiplier
         * */
    }
    public int Heuristique2() {
        int heuristique = Heuristique();
        for (Pion pion : dj.getJeu().getPions()) {
            if (pion.getJoueur() == dj.getJoueurActuel()) {
                heuristique += 30*distancePionBord(pion);
            }
        }
        return heuristique;
        /*
         * Objectif : favoriser les pions au milieu (4 cases au milieu)
         * distancePionBord = soit 0 soit 1
         */
    }
    public int Heuristique3() {
        int heuristique = Heuristique2();
        for (int i = 0; i < 3; i++) {
            ArrayList<Pion> pions = getDj().getJeu().pionsFocusJoueur(i, getDj().getJoueurActuel());
            ArrayList<Pion> pionsVisites = new ArrayList<>();
            if (pions.size() >= 2) {
                for (Pion pion1 : pions) {
                    pionsVisites.add(pion1);
                    for (Pion pion2 : pions) {
                        if (!pionsVisites.contains(pion2)) {
                            if (pion1.colle(pion2)) {
                                heuristique -= 30;
                            }
                        }
                    }
                }
            }
        }
        return heuristique;
        /*
         * Objectif : éviter les pions collés
         */
    }
    public int Heuristique4() {
        int heuristique = Heuristique3();
        int pionsJoueur1 = dj.getJeu().getJoueur(0).getNbPionsRestants();
        int pionsJoueur2 = dj.getJeu().getJoueur(1).getNbPionsRestants();
        for (int i = 0; i < 3; i++) {
            pionsJoueur1 += dj.getJeu().pionsFocusJoueur(i, dj.getJeu().getJoueur(0)).size();
        }
        for (int i = 0; i < 3; i++) {
            pionsJoueur2 += dj.getJeu().pionsFocusJoueur(i, dj.getJeu().getJoueur(1)).size();
        }

        if (dj.joueurActuel.getID() == 1) {
            return heuristique + (pionsJoueur1 - pionsJoueur2) * 61;
        } else {
            return heuristique + (pionsJoueur2 - pionsJoueur1) * 61;
        }
        // Privilégie de tuer un pion plutôt que de se dédoubler si c'est possible
    }
    /*
    J1 : Heuristique3 vs J2 : Heuristique3  -> 30% victoire pour J2
    J1 : Heuristique4 vs J2 : Heuristique4  -> 58% victoire pour J2


    J1 : Heuristique3 vs J2 : Heuristique4  -> 61% victoire pour J2
    J1 : Heuristique4 vs J2 : Heuristique3  -> 22% victoire pour J2


    Heuristique 4 semble être meilleur que 3 !
     */

    public int distancePionBord(Pion p) {
        int d = 1;
        int l = p.getCoordonnees().getL();
        int c = p.getCoordonnees().getC();
        if (c == 0 || l == 0 || c == 3 || l == 3) { // Si pion au bord
            d = 0;
        }
        return d;
    }

    // -------CALCUL DES BRANCHEMENTS-------
    public ArrayList<Couple<DeroulementJeu, Tour>> branchementsSelect(DeroulementJeu djeu) {   // On considere que le jeu j est dans l'étape selection
        ArrayList<Couple<DeroulementJeu, Tour>> jeux = new ArrayList<>();
        ArrayList<Pion> pionsToBeSelected = dj.getJeu().pionsFocusJoueur(dj.getJoueurActuel().getFocus(), dj.getJoueurActuel());

        for (Pion pionTBS : pionsToBeSelected) {
            DeroulementJeu jeuSelect = djeu.copy();
            Point cooPionSelect = pionTBS.getCoordonnees();
            jeuSelect.selectPion(cooPionSelect.getL(), cooPionSelect.getC(), pionTBS.getEpoque());
            Tour t = new Tour();
            t.setPionSelectionne(pionTBS);
            jeux.add(new Couple(jeuSelect, t));
        }
        return jeux;
    }
    public ArrayList<Couple<DeroulementJeu, Tour>> branchementsCoup(DeroulementJeu djeu) {     // j est dans l'étape jouer un coup
        ArrayList<Couple<DeroulementJeu, Tour>> jeux = new ArrayList<>();
        ArrayList<Pion> casesDispo = djeu.getJeu().casesDispo(djeu.getJoueurActuel(), djeu.getPionActuel());
        for (Pion caseDispo : casesDispo) {
            DeroulementJeu jeuCoup = djeu.copy();
            Point cooCase = caseDispo.getCoordonnees();
            jeuCoup.jouerCoup(cooCase.getL(), cooCase.getC(), caseDispo.getEpoque(),false);
            Tour t = new Tour();
            if (djeu.getJoueurActuel().getNbActionsRestantes() == 2) {
                t.setCoup1(caseDispo);
            } else {
                t.setCoup2(caseDispo);
            }
            jeux.add(new Couple(jeuCoup, t));
        }
        return jeux;
    }
    public ArrayList<Couple<DeroulementJeu, Tour>> branchementsFocus(DeroulementJeu djeu) {    // j est dans l'étape choisir un focus
        ArrayList<Couple<DeroulementJeu, Tour>> jeux = new ArrayList<>();

        for (int i = 0; i < 3; i++) {
            if (djeu.peutSelectionnerFocus(i, djeu.getJoueurActuel().getID())) {
                DeroulementJeu jeuFocus = djeu.copy();
                jeuFocus.getJoueurActuel().setFocus(i);
                Tour t = new Tour();
                t.setFocus(i);
                jeux.add(new Couple(jeuFocus, t));
            }
        }
        return jeux;
    }
    public ArrayList<Couple<DeroulementJeu, Tour>> Branchements() {

        ArrayList<Couple<DeroulementJeu, Tour>> jeuxSelect = new ArrayList<>();
        ArrayList<Couple<DeroulementJeu, Tour>> jeuxCoup1 = new ArrayList<>();
        ArrayList<Couple<DeroulementJeu, Tour>> jeuxCoup2 = new ArrayList<>();
        ArrayList<Couple<DeroulementJeu, Tour>> jeuxFocus = new ArrayList<>();

        switch(dj.getEtape()) {
            case SELECT:
                jeuxSelect = branchementsSelect(dj);
            case MOVE1:
                if (jeuxSelect.size() == 0) {                           // Si pion deja selectionné
                    jeuxSelect.add(new Couple(dj, new Tour()));
                }
                for (Couple<DeroulementJeu, Tour> j : jeuxSelect) {
                    ArrayList<Couple<DeroulementJeu, Tour>> coups = branchementsCoup(j.getFirst());
                    for (Couple<DeroulementJeu, Tour> c : coups) {             // Ajout de la selection aux tours de jeuxCoup1
                        c.getSecond().setPionSelectionne(j.getSecond().getPionSelectionne());
                    }
                    jeuxCoup1.addAll(coups);
                }
            case MOVE2:
                // Si etape du jeu : coup 2
                if (jeuxCoup1.size() == 0) {
                    jeuxCoup1.add(new Couple(dj, new Tour()));
                }

                for (Couple<DeroulementJeu, Tour> j : jeuxCoup1) {
                    if (j.getFirst().getJoueurActuel().getNbActionsRestantes() == 1) {
                        ArrayList<Couple<DeroulementJeu, Tour>> coups = branchementsCoup(j.getFirst());
                        for (Couple<DeroulementJeu, Tour> c : coups) {
                            c.getSecond().setPionSelectionne(j.getSecond().getPionSelectionne());
                            c.getSecond().setCoup1(j.getSecond().getCoup1());
                        }
                        jeuxCoup2.addAll(coups);
                    } else {
                        jeuxCoup2.add(j);
                    }
                }
            case FOCUS:
                if (jeuxCoup2.size() == 0) {                            // Si etape du jeu : choix focus
                    jeuxCoup2.add(new Couple(dj, new Tour()));
                }
                for (Couple<DeroulementJeu, Tour> j : jeuxCoup2) {
                    ArrayList<Couple<DeroulementJeu, Tour>> focus = branchementsFocus(j.getFirst());
                    for (Couple<DeroulementJeu, Tour> f : focus) {
                        f.getSecond().setPionSelectionne(j.getSecond().getPionSelectionne());
                        f.getSecond().setCoup1(j.getSecond().getCoup1());
                        f.getSecond().setCoup2(j.getSecond().getCoup2());
                    }
                    jeuxFocus.addAll(focus);
                }
                break;
            case END:
                break;
        }

        return jeuxFocus;
    }
    /*public ArrayList<Couple<DeroulementJeu, Tour>> Branchements() {

        ArrayList<Couple<DeroulementJeu, Tour>> jeuxSelect = new ArrayList<>();
        ArrayList<Couple<DeroulementJeu, Tour>> jeuxCoup1 = new ArrayList<>();
        ArrayList<Couple<DeroulementJeu, Tour>> jeuxCoup2 = new ArrayList<>();
        ArrayList<Couple<DeroulementJeu, Tour>> jeuxFocus = new ArrayList<>();

        switch(dj.getEtape()) {
            case SELECT:
                jeuxSelect = branchementsSelect(dj);
            case MOVE1:
                if (jeuxSelect.size() == 0) {                           // Si pion deja selectionné
                    jeuxSelect.add(new Couple(dj, new Tour()));
                }
                for (Couple<DeroulementJeu, Tour> j : jeuxSelect) {
                    ArrayList<Couple<DeroulementJeu, Tour>> coups = branchementsCoup(j.getFirst());
                    for (Couple<DeroulementJeu, Tour> c : coups) {             // Ajout de la selection aux tours de jeuxCoup1
                        c.getSecond().setPionSelectionne(j.getSecond().getPionSelectionne());
                    }
                    jeuxCoup1.addAll(coups);

                }
                for (Couple<DeroulementJeu, Tour> j : jeuxCoup1) {
                        jeuxCoup2.add(j);

                }
            case MOVE2:
                if (jeuxSelect.size() == 0) {                           // Si pion deja selectionné
                    jeuxSelect.add(new Couple(dj, new Tour()));
                }
                // Si etape du jeu : coup 2
                    jeuxCoup1.add(new Couple(dj, new Tour()));


                for (Couple<DeroulementJeu, Tour> j : jeuxCoup1) {
                        ArrayList<Couple<DeroulementJeu, Tour>> coups = branchementsCoup(j.getFirst());
                        for (Couple<DeroulementJeu, Tour> c : coups) {
                            c.getSecond().setPionSelectionne(j.getSecond().getPionSelectionne());
                            c.getSecond().setCoup1(j.getSecond().getCoup1());
                        }
                        jeuxCoup2.addAll(coups);

                }
            case FOCUS:
                if (jeuxCoup2.size() == 0) {                            // Si etape du jeu : choix focus
                    jeuxCoup2.add(new Couple(dj, new Tour()));
                }
                for (Couple<DeroulementJeu, Tour> j : jeuxCoup2) {
                    ArrayList<Couple<DeroulementJeu, Tour>> focus = branchementsFocus(j.getFirst());
                    for (Couple<DeroulementJeu, Tour> f : focus) {
                        f.getSecond().setPionSelectionne(j.getSecond().getPionSelectionne());
                        f.getSecond().setCoup1(j.getSecond().getCoup1());
                        f.getSecond().setCoup2(j.getSecond().getCoup2());
                    }
                    jeuxFocus.addAll(focus);
                }
                break;
            case END:
                break;
        }

        return jeuxFocus;
    }*/
}
