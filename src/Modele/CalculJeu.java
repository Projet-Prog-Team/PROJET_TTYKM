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
            return 1000;
        } else if (!dj.getJeu().joueurAGagne(dj.getJeu().getJoueur(dj.joueurActuel.getID() % 2))) { // Si on ne perd pas
            int pionsJoueur1 = dj.getJeu().getJoueur(0).nbPionsRestants;
            int pionsJoueur2 = dj.getJeu().getJoueur(1).nbPionsRestants;

            for (int i = 0; i < 3; i++) {
                pionsJoueur1 += dj.getJeu().pionsFocusJoueur(i, dj.getJeu().getJoueur(0)).size();
            }
            for (int i = 0; i < 3; i++) {
                pionsJoueur2 += dj.getJeu().pionsFocusJoueur(i, dj.getJeu().getJoueur(1)).size();
            }

            if (dj.joueurActuel.getID() == 1) {
                if (pionsJoueur1 - pionsJoueur2 < 0) {
                    return (pionsJoueur1 - pionsJoueur2) * 100;
                } else {
                    return (pionsJoueur1 - pionsJoueur2) * 50;
                }
            } else {
                if (pionsJoueur2 - pionsJoueur1 < 0) {
                    return (pionsJoueur2 - pionsJoueur1) * 100;
                } else {
                    return (pionsJoueur2 - pionsJoueur1) * 50;
                }
            }
        } else { // Si on perd
            return -1000;
        }
        /* Objectif : permettre à l'IA de tuer des pions
         * Renvoie
         * 500 si on gagne
         * -500 si on perd
         * sinon en général 0..200
         * */
    }
    public int Heuristique2() {
        int heuristique = Heuristique();
        int moyennePions = 0;
        int maxPionsPlateau = 0;
        int pionsPlateau;
        for (int i = 0; i < 3; i++) {
            pionsPlateau = dj.getJeu().pionsFocusJoueur(i, dj.getJoueurActuel()).size();
            moyennePions += pionsPlateau;
            if (pionsPlateau > maxPionsPlateau) {
                maxPionsPlateau = pionsPlateau;
            }
        }
        moyennePions = (moyennePions*10)/3;
        return heuristique + (((maxPionsPlateau*10) - moyennePions));
        /* Objectif : favoriser un équilibre sur le plateau (idéalement ~2 - 2 - 2)
         * Moyenne des pions 10..30
         * MaxPions 10..60
         * MaxPions - Moyenne des pions ~= 20..30
         * Heuristique total : 100..200 - 20..30 ~= 70..170
         */
    }
    public int Heuristique3() {
        int heuristique = Heuristique();
        for (Pion pion : dj.getJeu().getPions()) {
            if (pion.getJoueur() == dj.getJoueurActuel()) {
                heuristique += 10*distancePionBord(pion);
            }
        }
        return heuristique;
        /*
         * Objectif : favoriser les pions au milieu (4 cases au milieu)
         * distancePionBord = soit 0 soit 1
         * 10..30 en général
         * Heuristique total 40..140
         */
    }
    // Idées pour heuristique 4 > faire en sorte de limiter les pions collés

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
            jeuCoup.jouerCoup(cooCase.getL(), cooCase.getC(), caseDispo.getEpoque());
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
            case 1:
                jeuxSelect = branchementsSelect(dj);
            case 2:
                if (jeuxSelect.size() == 0) {                           // Si pion deja selectionné
                    jeuxSelect.add(new Couple(dj, new Tour()));
                }

                if (dj.getJoueurActuel().getNbActionsRestantes() == 2) {   // Si etape du jeu : coup 1
                    for (Couple<DeroulementJeu, Tour> j : jeuxSelect) {
                        ArrayList<Couple<DeroulementJeu, Tour>> coups = branchementsCoup(j.getFirst());
                        for (Couple<DeroulementJeu, Tour> c : coups) {             // Ajout de la selection aux tours de jeuxCoup1
                            c.getSecond().setPionSelectionne(j.getSecond().getPionSelectionne());
                        }
                        jeuxCoup1.addAll(coups);

                    }
                } else {                                                // Si etape du jeu : coup 2
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
            case 3:
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
            case 4:
                break;
        }

        return jeuxFocus;
    }
}
