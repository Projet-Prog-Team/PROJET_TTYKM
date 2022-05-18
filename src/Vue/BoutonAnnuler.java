package Vue;

import Modele.DeroulementJeu;
import Modele.Jeu;
import Patterns.Observateur;

import javax.swing.*;
import java.awt.*;

public class BoutonAnnuler implements Observateur {

    private DeroulementJeu dj;
    private JButton button;

    BoutonAnnuler(String s, DeroulementJeu t_dj) {
        button = new JButton(s);
        dj = t_dj;
        button.setAlignmentX(Component.CENTER_ALIGNMENT);
        button.setFocusable(false);
        button.setEnabled(false);
        dj.ajouteObservateur(this);
    }

    @Override
    public void metAJour() {
        // TODO: Fonction du jeu permettant de savoir si un coup peut être refait
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
