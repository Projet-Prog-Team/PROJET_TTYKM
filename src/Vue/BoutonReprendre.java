package Vue;

import Patterns.Observateur;

import javax.swing.*;
import java.awt.*;

public class BoutonReprendre implements Observateur {

    private JButton button;
    IHMState state;

    BoutonReprendre(String s, IHMState state) {
        button = new JButton(s);
        button.setAlignmentX(Component.CENTER_ALIGNMENT);
        button.setFocusable(false);

        this.state = state;
        state.ajouteObservateur(this);

        metAJour();
    }

    @Override
    public void metAJour() {
        if(state.getPauseIA()){
            button.setVisible(true);
        }else{
            button.setVisible(false);
        }
    }

    public JButton getButton(){
        return this.button;
    }
}