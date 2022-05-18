package Controleur;

import Modele.CalculJeu;
import Modele.DeroulementJeu;
import Modele.Pion;
import Structures.Point;
import Vue.AdaptateurTemps;
import Vue.CollecteurEvenements;
import Vue.Commande;
import Vue.IHMState;

import javax.swing.*;

public class ControleurMediateur implements CollecteurEvenements {

    DeroulementJeu dj;
    IA joueur1, joueur2, suggestion;
    int [] joueurs;
    String difficulty1 = "facile", difficulty2 = "facile";
    IHMState state;
    Timer t;
    int speed;

    String heuristique = "Heuristique4";

    public ControleurMediateur (DeroulementJeu djeu, int temps, IHMState state) {
        dj = djeu;
        this.state = state;
        suggestion = IA.nouvelle(new CalculJeu(dj), "difficile", heuristique);
        speed = temps;
        init();
    }

    public void init() {
        t = new Timer(speed, new AdaptateurTemps(this));
        t.start();
        joueurs = new int[2];
        joueurs[0] = 0;
        joueurs[1] = 0;
        dj.init();
        state.initPreview();
        state.setIA1(joueurs[0]==1);
        state.setIA2(joueurs[1]==1);
        state.setDifficultyIA1(difficulty1);
        state.setDifficultyIA2(difficulty2);
        state.setPauseIA(false);
        activerIA(1, difficulty2, heuristique);
    }

    // Clique sur une case
    @Override
    public void clicSouris(int l, int c, int epoque) {
        int id = dj.getJoueurActuel().getID()-1;
        if (joueurs[id] == 0) {
            switch (dj.getEtape()) {
                case 1:
                    dj.selectPion(l, c, epoque);
                    break;
                case 2:
                    dj.jouerCoup(l, c, epoque);
                    state.initPreview();
                    break;
                case 3:
                    break;
                case 4:
                    break;
            }
        }
    }

    // Tic du timer (toutes les 1 sec)
    @Override
    public void tic() {
        int id = dj.getJoueurActuel().getID()-1;
        if (joueurs[id] == 1) {
            IA j;
            if (id == 0) {
               j = joueur1;
            } else {
                j = joueur2;
            }
            switch(dj.getEtape()) {
                case 1:
                    Pion p = j.selectPion();
                    Point coord = p.getCoordonnees();
                    dj.selectPion(coord.getL(), coord.getC(), p.getEpoque());
                    break;
                case 2:
                    p = j.jouerCoup();
                    coord = p.getCoordonnees();
                    dj.jouerCoup(coord.getL(), coord.getC(), p.getEpoque());
                    state.initPreview();
                    break;
                case 3:
                    int focus = j.choixFocus();
                    dj.getJoueurActuel().setFocus(focus);
                    dj.switchPlayer();
                    state.initPreview();
                    break;
                case 4:
                    break;
            }
        }
    }

    public void suggestion () {
        suggestion.calculCoup(dj, 10, true);
        switch(dj.getEtape()) {
            case 1:
                state.setSuggestionSource(suggestion.selectPion());
                state.setSuggestionDestination(suggestion.jouerCoup());
                break;
            case 2:
                state.setSuggestionSource(dj.getPionActuel());
                state.setSuggestionDestination(suggestion.jouerCoup());
                break;
            case 3:
                state.setSuggestionFocus(suggestion.choixFocus());
                break;
        }
    }

    // Clique sur un bouton
    @Override
    public boolean commande(Commande c) {
        System.out.println(c.getCommande());
        switch(c.getCommande()){
            case "clicFocus":
                if(dj.getEtape()==3){
                    int id = dj.getJoueurActuel().getID()-1;
                    if (joueurs[id] == 0) {
                        //choix focus
                        if (dj.peutSelectionnerFocus(c.getEpoque(), c.getJoueur())) {
                            dj.getJoueurActuel().setFocus(c.getEpoque());
                            dj.switchPlayer();
                            state.initPreview();
                        } else {
                            System.out.println("Modification du focus adverse impossible");
                        }
                    }
                } else if (dj.getEtape() == 4){
                }
                break;
            case "save":
                System.out.println(c.getSaveName());
                break;
            case "load":
                System.out.println(c.getSaveName());
                break;
            case "annuler":
                state.setPauseIA(true);
                break;
            case "annulerTour":
                state.setPauseIA(true);
                break;
            case "refaire":
                state.setPauseIA(true);
                break;
            case "suggestion":
                suggestion();
                break;
            case "reprendre":
                state.setPauseIA(false);
                break;
            case "IASpeed":
                t.stop();
                speed = c.getIA();
                t = new Timer(speed, new AdaptateurTemps(this));
                t.start();
                break;
            case "newGame":
                t.stop();
                init();
                break;
            case "toggleIA1":
                activerIA(0, difficulty1, heuristique);
                break;
            case "toggleIA2":
                activerIA(1, difficulty2, heuristique);
                break;
            case "setDifficulty":
                System.out.println(c.getDifficulty());
                setDifficultyIA(c.getIA(), c.getDifficulty());
                break;
            default:
                return false;
        }
        return true;
    }

    // Fonctions appelés lors d'un clique sur un bouton
    public void activerIA(int j, String type, String heuristique) {
        joueurs[j] = (joueurs[j] + 1) % 2;
        if (j == 0) {
            joueur1 = IA.nouvelle(new CalculJeu(dj), type, heuristique);
            state.setIA1(joueurs[j]==1);
        } else {
            joueur2 = IA.nouvelle(new CalculJeu(dj), type, heuristique);
            state.setIA2(joueurs[j]==1);
        }
        dj.miseAJour();
    }

    public void desactiverIA(int j) {
        if (joueurs[j] == 1) {
            joueurs[j] = 0;
        }
        if(j==0){
            state.setIA1(false);
        }else if (j==1){
            state.setIA2(false);
        }
    }

    public void setDifficultyIA(int ia, String difficulty) {
        if (ia == 1) {
            difficulty1 = difficulty;
            if (joueurs[0] == 1) {
                desactiverIA(0); //désactiver
                activerIA(0, difficulty1, heuristique);
            }
            state.setDifficultyIA1(difficulty);
        } else {
            difficulty2 = difficulty;
            if (joueurs[1] == 1) {
                desactiverIA(1); //désactiver
                activerIA(1, difficulty2, heuristique);
            }
            state.setDifficultyIA2(difficulty);
        }
    }

    public void enablePreview(int l, int c, int epoque){
        if(dj.getEtape()==2){
            state.setPreview(dj.getPreview(l,c,epoque));
        }
    }
}
