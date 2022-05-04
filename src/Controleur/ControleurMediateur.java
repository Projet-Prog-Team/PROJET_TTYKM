package Controleur;

import Modele.Jeu;
import Vue.CollecteurEvenements;
import Vue.Commande;

public class ControleurMediateur implements CollecteurEvenements {
    Jeu jeu;
    public ControleurMediateur (Jeu j) {
        jeu = j;
    }

    @Override
    public void clicSouris(int l, int c) {

    }

    @Override
    public boolean commande(Commande c) {
        switch(c.getCommande()){
            case "":
                break;
            default:
                return false;
        }
        return true;
    }
}
