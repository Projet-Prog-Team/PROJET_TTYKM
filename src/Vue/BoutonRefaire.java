package Vue;

import Modele.DeroulementJeu;
import Patterns.Observateur;

import javax.swing.*;
import java.awt.*;

public class BoutonRefaire implements Observateur {

    private JButton button;
    private DeroulementJeu dj;

    BoutonRefaire(String s, DeroulementJeu dj) {
        button = new JButton(s);
        this.dj=dj;
        dj.ajouteObservateur(this);
        button.setAlignmentX(Component.CENTER_ALIGNMENT);
		button.setEnabled(false);
        button.setFocusable(false);
        metAJour();
    }

    @Override
    public void metAJour() {
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
