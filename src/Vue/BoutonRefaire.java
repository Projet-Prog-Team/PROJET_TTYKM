package Vue;

import Modele.Jeu;
import Patterns.Observateur;

import javax.swing.*;
import java.awt.*;

public class BoutonRefaire implements Observateur {

    private Jeu jeu;
    private JButton button;

    BoutonRefaire(String s) {
        button = new JButton(s);
        button.setAlignmentX(Component.CENTER_ALIGNMENT);
        button.setFocusable(false);

//        jeu = j;
//        jeu.ajouteObservateur(this);
    }

    @Override
    public void metAJour() {
        // TODO: Fonction du jeu permettant de savoir si un coup peut être refait
        button.setEnabled(true);
    }

    public JButton getButton(){
        return this.button;
    }

}
