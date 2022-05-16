package Controleur;

import Modele.Jeu;
import Modele.Pion;
import Structures.Point;
import Vue.*;

import javax.swing.*;
import java.util.ArrayList;

public class ControleurMediateur implements CollecteurEvenements {

    Jeu jeu;
    IA joueur1, joueur2, suggestion;
    int [] joueurs;
    String difficulty1 = "facile", difficulty2 = "facile";
    Timer t;
    IHMState state;
    int speed;

    public ControleurMediateur (Jeu j, int temps, IHMState state) {
        jeu = j;
        this.state = state;
        suggestion = IA.nouvelle(j, "difficile", "Heuristique3");
        speed = temps;
        init();
    }

    public void init() {
        t = new Timer(speed, new AdaptateurTemps(this));
        t.start();
        joueurs = new int[2];
        joueurs[0] = 0;
        joueurs[1] = 0;
        jeu.init();
        state.initPreview();
        state.setIA1(joueurs[0]==1);
        state.setIA2(joueurs[1]==1);
        state.setDifficultyIA1(difficulty1);
        state.setDifficultyIA2(difficulty2);
        state.setPauseIA(false);
        activerIA(1, difficulty2, "Heuristique3");
    }

    // Clique sur une case
    @Override
    public void clicSouris(int l, int c, int epoque) {
        int id = jeu.getJoueurActuel().getID()-1;
        if (joueurs[id] == 0) {
            switch (jeu.getEtape()) {
                case 1:
                    jeu.selectPion(l, c, epoque);
                    break;
                case 2:
                    jeu.jouerCoup(l, c, epoque);
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
        int id = jeu.getJoueurActuel().getID()-1;
        if (joueurs[id] == 1) {
            IA j;
            if (id == 0) {
               j = joueur1;
            } else {
                j = joueur2;
            }
            switch(jeu.getEtape()) {
                case 1:
                    Pion p = j.selectPion();
                    Point coord = p.getCoordonnees();
                    jeu.selectPion(coord.getL(), coord.getC(), p.getEpoque());
                    break;
                case 2:
                    p = j.jouerCoup();
                    state.initPreview();
                    coord = p.getCoordonnees();
                    jeu.jouerCoup(coord.getL(), coord.getC(), p.getEpoque());
                    break;
                case 3:
                    int focus = j.choixFocus();
                    jeu.getJoueurActuel().setFocus(focus);
                    jeu.switchPlayer();
                    break;
                case 4:
                    break;
            }
        }
    }

    public void suggestion () {
        ArrayList<Pion> l = new ArrayList<>();
        switch(jeu.getEtape()) {
            case 1:
                jeu.setSuggestionPions(suggestion.selectPion(), suggestion.jouerCoup());
                break;
            case 2:
                jeu.setSuggestionPions(jeu.getPionActuel(), suggestion.jouerCoup());
                break;
            case 3:
                jeu.setSuggestionFocus(suggestion.choixFocus());
                break;
            case 4:
                break;
        }
    }

    // Clique sur un bouton
    @Override
    public boolean commande(Commande c) {
        System.out.println(c.getCommande());
        switch(c.getCommande()){
            case "clicFocus":
                if(jeu.getEtape()==3){
                    int id = jeu.getJoueurActuel().getID()-1;
                    if (joueurs[id] == 0) {
                        //choix focus
                        if (jeu.peutSelectionnerFocus(c.getEpoque(), c.getJoueur())) {
                            jeu.getJoueurActuel().setFocus(c.getEpoque());
                            jeu.switchPlayer();
                        } else {
                            System.out.println("Modification du focus adverse impossible");
                        }
                    }
                } else if (jeu.getEtape() == 4){
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
                activerIA(0, difficulty1, "Heuristique3");
                break;
            case "toggleIA2":
                activerIA(1, difficulty2, "Heuristique3");
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
            joueur1 = IA.nouvelle(jeu, type, heuristique);
            state.setIA1(joueurs[j]==1);
        } else {
            joueur2 = IA.nouvelle(jeu, type, heuristique);
            state.setIA2(joueurs[j]==1);
        }
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
                activerIA(0, difficulty1, "Heuristique3");
                state.setDifficultyIA1(difficulty);
            }
        } else {
            difficulty2 = difficulty;
            if (joueurs[1] == 1) {
                desactiverIA(1); //désactiver
                activerIA(1, difficulty2, "Heuristique3");
                state.setDifficultyIA2(difficulty);
            }
        }
    }

    public void enablePreview(int l, int c, int epoque){
        state.setPreview(jeu.getPreview(l,c,epoque));
    }
}
