package Vue;

import Modele.Jeu;
import Patterns.Observateur;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ActiverIA implements Observateur {

    private CollecteurEvenements controleur;
    private JMenuItem menuItem;
    private int ia;
    IHMState state;

    ActiverIA(CollecteurEvenements c, int ia, String commande, IHMState state){
        this.controleur = c;
        this.ia = ia;
        this.state = state;
        state.ajouteObservateur(this);
        menuItem = new JMenuItem("Activer IA "+ia);
        menuItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                controleur.commande(new Commande(commande));
            }
        });
        metAJour();
    }

    @Override
    public void metAJour() {
        if (ia==1){
            if (state.getIA1()){
                menuItem.setText("Desactiver IA 1");
            }else{
                menuItem.setText("Activer IA 1");
            }
        }else{
            if (state.getIA2()){
                menuItem.setText("Desactiver IA 2");
            }else{
                menuItem.setText("Activer IA 2");
            }
        }
    }

    public JMenuItem getMenuItem() {
        return menuItem;
    }
}
