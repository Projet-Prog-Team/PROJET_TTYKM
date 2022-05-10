package Vue;

import Modele.Jeu;
import Patterns.Observateur;

import javax.swing.*;

public class LabelEtat implements Observateur {

    Jeu jeu;
    JLabel label;

    LabelEtat(String s, Jeu j){
        label = new JLabel(s);
        jeu = j;
        jeu.ajouteObservateur(this);
    }

    @Override
    public void metAJour() {
        String state= "Joueur ";
        state+=jeu.getJoueurActuel().getID()+" ";
        switch (jeu.getState()){
            case "selection":
                state+="sélectionne un pion";
                break;
            case "action1":
                state+="effectue son premier mouvement";
                break;
            case "action2":
                state+="effectue son second mouvement";
                break;
            case "focus":
                state+="choisit ou il jouera son prochain tour";
                break;
            case "j1gagne":
                state="Joueur 1 à gagné";
            case "j2gagne":
                state="Joueur 2 à gagné";
                break;
        }
        label.setText(state);
    }

    public JLabel getLabel() {
        return this.label;
    }
}
