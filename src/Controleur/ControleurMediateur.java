package Controleur;

import Modele.Jeu;
import Modele.ETAT;
import Modele.ManageFiles;
import Modele.Pion;
import Structures.Point;
import Vue.AdaptateurTemps;
import Vue.CollecteurEvenements;
import Vue.Commande;

import javax.swing.*;
import java.util.ArrayList;

public class ControleurMediateur implements CollecteurEvenements {

    Jeu jeu;
    IA joueur1, joueur2, suggestion;
    int [] joueurs;
    String difficulty1 = "facile", difficulty2 = "facile";
    Timer t;

    int speed;

    public ControleurMediateur (Jeu j, int temps) {
        jeu = j;
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

        //jeu.init();

        activerIA(1, difficulty2, "Heuristique3");
    }

    // Clique sur une case
    @Override
    public void clicSouris(int l, int c, int epoque) {
        int id = jeu.getJoueurActuel().getID()-1;
        if (joueurs[id] == 0) {
            switch (jeu.getEtape()) {
                case SELECT:
                    jeu.selectPion(l, c, epoque);
                    break;
                case MOVE2:
                case MOVE1:
                    jeu.jouerCoup(l, c, epoque,true);
                    break;
                case FOCUS:
                    break;
                case END:
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
                case SELECT:
                    Pion p = j.selectPion();
                    Point coord = p.getCoordonnees();
                    jeu.selectPion(coord.getL(), coord.getC(), p.getEpoque());
                    jeu.MemoryManager.AddLog(ETAT.SELECT);
                    break;
                case MOVE1:
                case MOVE2:
                    p = j.jouerCoup();
                    coord = p.getCoordonnees();
                    jeu.jouerCoup(coord.getL(), coord.getC(), p.getEpoque(),false);
                    break;
                case FOCUS:
                    jeu.getPionActuel().focused=false;
                    int focus = j.choixFocus();
                    jeu.getJoueurActuel().setFocus(focus);
                    jeu.switchPlayer();
                    break;
                case END:
                    break;
            }
        }
    }

    public void suggestion () {
        ArrayList<Pion> l = new ArrayList<>();
        switch(jeu.getEtape()) {
            case SELECT:
                jeu.setSuggestionPions(suggestion.selectPion(), suggestion.jouerCoup());
                break;
            case MOVE1:
            case MOVE2:
                jeu.setSuggestionPions(jeu.getPionActuel(), suggestion.jouerCoup());
                break;
            case FOCUS:
                jeu.setSuggestionFocus(suggestion.choixFocus());
                break;
            case END:
                break;
        }
    }

    // Clique sur un bouton
    @Override
    public boolean commande(Commande c) {
        System.out.println(c.getCommande());
        switch(c.getCommande()){
            case "clicFocus":
                if(jeu.getEtape()==ETAT.FOCUS){
                    int id = jeu.getJoueurActuel().getID()-1;
                    if (joueurs[id] == 0) {
                        //choix focus
                        if (jeu.peutSelectionnerFocus(c.getEpoque(), c.getJoueur())) {
                            jeu.getJoueurActuel().setFocus(c.getEpoque());
                            jeu.MemoryManager.move =false;
                            System.out.println("hey");
                            jeu.MemoryManager.UpdateLog(null,null);
                            jeu.switchPlayer();
                        } else {
                            System.out.println("Modification du focus adverse impossible");
                        }
                    }
                } else if (jeu.getEtape() == ETAT.END){
                }
                break;
            case "save":
                System.out.println(c.getSaveName());
                break;
            case "load":
                System.out.println(c.getSaveName());
                break;
            case "annuler":
                jeu.MemoryManager.CTRLZ();
                break;
            case "refaire":
                jeu.MemoryManager.CTRLY();
                break;
            case "suggestion":
                suggestion();
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
        } else {
            joueur2 = IA.nouvelle(jeu, type, heuristique);
        }
        jeu.miseAJour();
    }

    public void desactiverIA(int j) {
        if (joueurs[j] == 1) {
            joueurs[j] = 0;
        }
    }

    public void setDifficultyIA(int ia, String difficulty) {
        if (ia == 1) {
            difficulty1 = difficulty;
            if (joueurs[0] == 1) {
                desactiverIA(0); //désactiver
                activerIA(0, difficulty1, "Heuristique3");
            }
        } else {
            difficulty2 = difficulty;
            if (joueurs[1] == 1) {
                desactiverIA(1); //désactiver
                activerIA(1, difficulty2, "Heuristique3");
            }
        }
    }

    public void enablePreview(int l, int c, int epoque){
        jeu.enablePreview(l,c,epoque);
    }

    public void setPreviewFocus1(int epoque){ jeu.setPreviewFocus1(epoque); }

    public void setPreviewFocus2(int epoque){ jeu.setPreviewFocus2(epoque); }

    public boolean isEnabledIA1(){
        return joueurs[0] == 1;
    }

    public boolean isEnabledIA2(){
        return joueurs[1] == 1;
    }

    public String getDifficultyIA1(){
        return difficulty1;
    }

    public String getDifficultyIA2(){
        return difficulty2;
    }
}
