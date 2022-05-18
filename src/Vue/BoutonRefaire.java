package Vue;

import Modele.DeroulementJeu;
import Patterns.Observateur;

import javax.swing.*;
import java.awt.*;

public class BoutonRefaire implements Observateur {

    private JButton button;
    private DeroulementJeu dj;

    BoutonRefaire(String s, DeroulementJeu t_dj) {
        button = new JButton(s);
        dj=t_dj;
        button.setAlignmentX(Component.CENTER_ALIGNMENT);
		button.setEnabled(false);
        button.setFocusable(false);
        dj.ajouteObservateur(this);
    }

    @Override
    public void metAJour() {
        // TODO: Fonction du jeu permettant de savoir si un coup peut être refait
        if(dj.MemoryManager.CanCTRLY())
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
