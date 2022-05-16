package Vue;

import Modele.DeroulementJeu;
import Patterns.Observateur;

import javax.swing.*;
import java.awt.*;

public class LabelEtat implements Observateur {

    DeroulementJeu dj;
    JLabel label;

    LabelEtat(String s, DeroulementJeu djeu){
        label = new JLabel(s, SwingConstants.CENTER);
        label.setPreferredSize(new Dimension(280,20));
        dj = djeu;
        dj.ajouteObservateur(this);
        metAJour();
    }

    @Override
    public void metAJour() {
        String state= "Joueur ";
        state+=dj.getJoueurActuel().getID()==1 ? "Blanc ":"Noir ";
        switch (dj.getState()){
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
                state+="choisi où il jouera son prochain tour";
                break;
            case "j1gagne":
                state="Blanc a gagné";
                break;
            case "j2gagne":
                state="Noir a gagné";
                break;
        }
        label.setText(state);
    }

    public JLabel getLabel() {
        return this.label;
    }
}
