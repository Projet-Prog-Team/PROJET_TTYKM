package Controleur;

import Modele.Jeu;
import Vue.CollecteurEvenements;

public class ControleurMediateur implements CollecteurEvenements {
    Jeu jeu;
    public ControleurMediateur (Jeu j) {
        jeu = j;
    }
}
