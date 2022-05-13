package Vue;

import Modele.Jeu;
import Patterns.Observateur;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ActiverIA implements Observateur {

    private Jeu jeu;
    private CollecteurEvenements controleur;
    private JMenuItem menuItem;
    private int ia;

    ActiverIA(Jeu j, CollecteurEvenements c, int ia, String commande){
        jeu = j;
        jeu.ajouteObservateur(this);
        this.ia = ia;
        controleur = c;
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
            if (controleur.isEnabledIA1()){
                menuItem.setText("Desactiver IA 1");
            }else{
                menuItem.setText("Activer IA 1");
            }
        }else{
            if (controleur.isEnabledIA2()){
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
