package Modele;

import Patterns.Observable;

import java.util.Arrays;

public class Jeu extends Observable {
    Plateau[] plateaux = new Plateau[3];
    Joueur[] joueurs = new Joueur[2];
    int jouerActuel;

    public Jeu() {
        init();
    }

    public void init() {
        // Je créer les deux joueurs
        joueurs[0] = new Joueur(1);
        joueurs[1] = new Joueur(2);
        // Je remplis les plateaux
        for(int i = 0; i < 3; i++) {
            Plateau plateau = new Plateau();
            Pion p1 = new Pion(joueurs[0]);
            plateau.set(p1, 0, 0);
            Pion p2 = new Pion(joueurs[1]);
            plateau.set(p2, 3, 3);
            plateaux[i] = plateau;
        }
    }

    public void nouvellePartie() {

    }

    public void changerEpoque(Pion pion, EPOQUE epoque) {

    }

    @Override
    public String toString() {
        String chaine = "";
        chaine += joueurs[0].toString() + "\n";
        chaine += joueurs[1].toString() + "\n";
        chaine += "PASSE : \n";
        chaine += plateaux[0].toString() + "\n";
        chaine += "PRESENT : \n";
        chaine += plateaux[1].toString() + "\n";
        chaine += "FUTUR : \n";
        chaine += plateaux[2].toString() + "\n";

        return chaine;
    }

    public Plateau[] getPlateaux() {
        return plateaux;
    }

}
