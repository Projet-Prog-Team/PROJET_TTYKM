package Modele;

import Patterns.Observable;

public class Jeu extends Observable {
    Plateau[] plateaux = new Plateau[3];
    Joueur[] joueurs = new Joueur[2];
    int jouerActuel;

    public Jeu() {

    }

    public void nouvellePartie() {

    }

    public void changerEpoque(Pion pion, EPOQUE epoque) {

    }
}
