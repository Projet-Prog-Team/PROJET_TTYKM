package Controleur;

import Modele.Jeu;
import Modele.Pion;
import Structures.Point;
import Vue.CollecteurEvenements;
import Vue.Commande;

import javax.swing.*;

public class ControleurMediateur implements CollecteurEvenements {

    Jeu jeu;
    IA joueur1, joueur2;
    int [] joueurs;
    String difficulty1, difficulty2;

    public ControleurMediateur (Jeu j) {
        jeu = j;
        joueurs = new int[2];
        difficulty1 = "facile";
        difficulty2 = "facile";
    }

    // Clique sur une case
    @Override
    public void clicSouris(int l, int c, int epoque) {
        int id = jeu.getJoueurActuel().getID()-1;
        if (joueurs[id] == 0) {
            System.out.println(epoque + " x : " + c + " y : " + l);
            switch (jeu.getEtape()) {
                case 1:
                    jeu.selectPion(l, c, epoque);
                    break;
                case 2:
                    jeu.jouerCoup(l, c, epoque);
                    break;
                case 3:
                    break;
                case 4:
                    System.out.println("La partie est terminée");
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
                    coord = p.getCoordonnees();
                    jeu.jouerCoup(coord.getL(), coord.getC(), p.getEpoque());
                    break;
                case 3:
                    int focus = j.choixFocus();
                    jeu.getJoueurActuel().setFocus(focus);
                    jeu.switchPlayer();
                    break;
                case 4:
                    System.out.println("La partie est terminée");
                    break;
            }
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
                    System.out.println("La partie est terminée");
                }
                break;
            case "save":
                System.out.println(c.getSaveName());
                break;
            case "load":
                System.out.println(c.getSaveName());
                break;
            case "annuler":
                break;
            case "refaire":
                break;
            case "newGame":
                break;
            case "toggleIA1":
                activerIA(0, difficulty1);
                break;
            case "toggleIA2":
                activerIA(1, difficulty2);
                break;
            case "setDifficulty":
                setDifficultyIA(c.getIA(), c.getDifficulty());
                break;
            default:
                return false;
        }
        return true;
    }

    // Fonctions appelés lors d'un clique sur un bouton
    public void activerIA(int j, String type) {
        joueurs[j] = (joueurs[j] + 1) % 2;
        if (j == 0) {
            joueur1 = IA.nouvelle(jeu, type);
        } else {
            joueur2 = IA.nouvelle(jeu, type);
        }
    }

    public void setDifficultyIA(int ia, String difficulty) {
        if (ia == 1) {
            difficulty1 = difficulty;
        } else {
            difficulty2 = difficulty;
        }
    }
}
