package Vue;

import Modele.DeroulementJeu;
import Modele.Jeu;
import Patterns.Observateur;

import javax.swing.*;
import java.awt.*;

public class BoutonAnnuler implements Observateur {

    private DeroulementJeu dj;
    private JButton button;

    BoutonAnnuler(String s, DeroulementJeu dj) {
        button = new JButton(s);
        this.dj = dj;
        dj.ajouteObservateur(this);
        button.setAlignmentX(Component.CENTER_ALIGNMENT);
        button.setFocusable(false);
        metAJour();
    }

    @Override
    public void metAJour() {
        if(dj.MemoryManager.CanCTRLZ())
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
