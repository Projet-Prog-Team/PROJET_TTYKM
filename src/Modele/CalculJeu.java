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
        if (dj.getJeu().joueurAGagne(dj.joueurActuel)) { // Si le joueur adverse perd
            return -10000;
        } else if (!dj.getJeu().joueurAGagne(dj.getJeu().getJoueur(dj.joueurActuel.getID() % 2))) { // Si on ne perd pas
            int diffMains;
            int diffPlateaux;
            int pionsJoueur1 = 0;
            int pionsJoueur2 = 0;

            for (int i = 0; i < 3; i++) {
                pionsJoueur1 += dj.getJeu().pionsFocusJoueur(i, dj.getJeu().getJoueur(0)).size();
            }
            for (int i = 0; i < 3; i++) {
                pionsJoueur2 += dj.getJeu().pionsFocusJoueur(i, dj.getJeu().getJoueur(1)).size();
            }
            if (dj.joueurActuel.getID() == 1) {
                diffMains = dj.getJeu().getJoueur(1).getNbPionsRestants() - dj.getJeu().getJoueur(0).getNbPionsRestants();
                diffPlateaux = pionsJoueur2 - pionsJoueur1;
            } else {
                diffMains = dj.getJeu().getJoueur(0).getNbPionsRestants() - dj.getJeu().getJoueur(1).getNbPionsRestants();
                diffPlateaux = pionsJoueur1 - pionsJoueur2;
            }
            return diffMains + diffPlateaux;
        } else { // Si on gagne
            return 10000;
        }
    }

    // -------CALCUL DES BRANCHEMENTS-------
    public ArrayList<Couple<DeroulementJeu, Tour>> branchementsSelect(DeroulementJeu djeu) {   // On considere que le jeu j est dans l'étape selection
        ArrayList<Couple<DeroulementJeu, Tour>> jeux = new ArrayList<>();
        ArrayList<PionBasique> pionsToBeSelected = dj.getJeu().pionsFocusJoueur(dj.getJoueurActuel().getFocus(), dj.getJoueurActuel());

        for (PionBasique pionTBS : pionsToBeSelected) {
            DeroulementJeu jeuSelect = djeu.copy();
            jeuSelect.selectPion(pionTBS.getEmplacement());
            Tour t = new Tour();
            t.setPionSelectionne(pionTBS);
            jeux.add(new Couple(jeuSelect, t));
        }
        return jeux;
    }
    public ArrayList<Couple<DeroulementJeu, Tour>> branchementsCoup(DeroulementJeu djeu) {     // j est dans l'étape jouer un coup
        ArrayList<Couple<DeroulementJeu, Tour>> jeux = new ArrayList<>();
        ArrayList<Emplacement> casesDispo = djeu.getJeu().casesDispo(djeu.getJoueurActuel(), djeu.getPionActuel());
        ArrayList<Emplacement> casesDispoStatue = djeu.getJeu().casesDispoStatue(djeu.getPionActuel());
        for (Emplacement caseDispo : casesDispo) {
            DeroulementJeu jeuCoup = djeu.copy();   // Déplacement
            jeuCoup.jouerCoup(caseDispo,false);
            Tour t = new Tour();
            if (djeu.getJoueurActuel().getNbActionsRestantes() == 2) {
                t.setTypeCoup1(1);
                t.setCoup1(caseDispo);
            } else {
                t.setTypeCoup2(1);
                t.setCoup2(caseDispo);
            }
            jeux.add(new Couple(jeuCoup, t));
        }
        for (Emplacement caseDispoS : casesDispoStatue) {
            if (djeu.getJeu().getPion(caseDispoS) == null && !djeu.getJoueurActuel().isStatuePlaced()) { // Si on peut y créer une statue
                DeroulementJeu jeuStatue = djeu.copy();
                jeuStatue.creerStatue(caseDispoS);
                Tour tourStatue = new Tour();
                if (djeu.getJoueurActuel().getNbActionsRestantes() == 2) {
                    tourStatue.setTypeCoup1(2);
                    tourStatue.setCoup1(caseDispoS);
                } else {
                    tourStatue.setTypeCoup2(2);
                    tourStatue.setCoup2(caseDispoS);
                }
                jeux.add(new Couple(jeuStatue, tourStatue));
            }
        }
        return jeux;
    }
    public ArrayList<Couple<DeroulementJeu, Tour>> branchementsFocus(DeroulementJeu djeu) {    // j est dans l'étape choisir un focus
        ArrayList<Couple<DeroulementJeu, Tour>> jeux = new ArrayList<>();

        for (int i = 0; i < 3; i++) {
            if (djeu.peutSelectionnerFocus(i, djeu.getJoueurActuel().getID())) {
                DeroulementJeu jeuFocus = djeu.copy();
                jeuFocus.getJoueurActuel().setFocus(i);
                jeuFocus.switchPlayer();
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
            case MOVE2:
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
                            c.getSecond().setTypeCoup1(j.getSecond().getTypeCoup1());
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
                        f.getSecond().setTypeCoup1(j.getSecond().getTypeCoup1());
                        f.getSecond().setCoup1(j.getSecond().getCoup1());
                        f.getSecond().setTypeCoup2(j.getSecond().getTypeCoup2());
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
}
