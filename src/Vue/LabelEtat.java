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
        //TODO: fonction du jeu permettant de connaitre l'etat du jeu
    }

    public JLabel getLabel() {
        return this.label;
    }
}
