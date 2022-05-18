package Vue;

import Modele.DeroulementJeu;
import Patterns.Observateur;

import javax.swing.*;
import java.awt.*;

public class BoutonAnnulerTour implements Observateur {

    private JButton button;
    private DeroulementJeu dj;

    BoutonAnnulerTour(String s, DeroulementJeu t_dj) {
        button = new JButton(s);
        dj=t_dj;
        button.setAlignmentX(Component.CENTER_ALIGNMENT);
        button.setFocusable(false);
        button.setEnabled(false);
        dj.ajouteObservateur(this);
    }

    @Override
    public void metAJour() {
        // TODO: Fonction permettant de savoir si un coup peut être annulé
        if(dj.MemoryManager.CanCTRLTZ())
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
