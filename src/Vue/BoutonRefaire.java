package Vue;

import Modele.Jeu;
import Patterns.Observateur;

import javax.swing.*;
import java.awt.*;

public class BoutonRefaire implements Observateur {

    private Jeu jeu;
    private JButton button;

    BoutonRefaire(String s, Jeu j) {
        button = new JButton(s);
        jeu = j;
        button.setAlignmentX(Component.CENTER_ALIGNMENT);
        button.setFocusable(false);
        button.setEnabled(false);
        jeu.ajouteObservateur(this);
    }

    @Override
    public void metAJour() {
        // TODO: Fonction du jeu permettant de savoir si un coup peut être refait
        if(jeu.MemoryManager.CanCTRLY())
        {
            button.setEnabled(true);
        }
        else
        {
            button.setEnabled(false);
        }
    }

    public JButton getButton(){
        return this.button;
    }

}
