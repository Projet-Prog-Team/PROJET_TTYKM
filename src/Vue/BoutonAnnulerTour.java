package Vue;

import Patterns.Observateur;

import javax.swing.*;
import java.awt.*;

public class BoutonAnnulerTour implements Observateur {

    private JButton button;

    BoutonAnnulerTour(String s) {
        button = new JButton(s);
        button.setAlignmentX(Component.CENTER_ALIGNMENT);
        button.setFocusable(false);
    }

    @Override
    public void metAJour() {
        // TODO: Fonction permettant de savoir si un coup peut être annulé
        button.setEnabled(true);
    }

    public JButton getButton(){
        return this.button;
    }
}
