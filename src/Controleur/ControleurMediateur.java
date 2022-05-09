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
    public void clicSouris(int l, int c, int epoque) {
        System.out.println(epoque+" x : "+c+" y : "+l);
        switch(jeu.getEtape()) {
            case 1:
                jeu.selectPion(l, c, epoque);
                break;
            case 2:
                jeu.jouerCoup(l,c,epoque);
                break;
            case 3:
                break;
            case 4:
                System.out.println("La partie est terminée");
                break;
        }
        jeu.miseAJour();
    }

    @Override
    public boolean commande(Commande c) {
        System.out.println(c.getCommande());
        switch(c.getCommande()){
            case "clicFocus":
                if(jeu.getEtape()==3){
                    //choix focus
                    if (jeu.peutSelectionnerFocus(c.getEpoque(), c.getJoueur())) {
                        jeu.getJoueurActuel().setFocus(c.getEpoque());
                        jeu.switchPlayer();
                        jeu.miseAJour();
                    } else {
                        System.out.println("Modification du focus adverse impossible");
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
                break;
            case "toggleIA2":
                break;
            case "setDifficulty":
                System.out.println(c.getIA());
                System.out.println(c.getDifficulty());
                break;
            default:
                return false;
        }
        return true;
    }
}
