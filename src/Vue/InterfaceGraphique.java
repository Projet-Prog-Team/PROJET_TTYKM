package Vue;

import Modele.Jeu;
import Patterns.Observateur;

import javax.swing.*;

public class InterfaceGraphique implements Runnable, Observateur {
    Jeu jeu;
    CollecteurEvenements controle;
    JFrame frame;


    public InterfaceGraphique(Jeu j, CollecteurEvenements c) {
        jeu = j;
        jeu.ajouteObservateur(this);
        controle = c;
    }
    @Override
    public void metAJour() {

    }

    public static void demarrer(Jeu j, CollecteurEvenements c) {
        SwingUtilities.invokeLater(new InterfaceGraphique(j, c));
    }

    @Override
    public void run() {
        frame = new JFrame("That time you killed me");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(600, 400);
        frame.setVisible(true);
    }
}
