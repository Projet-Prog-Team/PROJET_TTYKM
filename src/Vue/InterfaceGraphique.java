package Vue;

import Modele.EPOQUE;
import Modele.Jeu;
import Patterns.Observateur;

import javax.swing.*;
import java.awt.*;

public class InterfaceGraphique implements Runnable, Observateur {

    Jeu jeu;
    CollecteurEvenements controle;
    JFrame frame;
    PlateauSwing passe,present,futur;


    public InterfaceGraphique(Jeu j, CollecteurEvenements c) {
        jeu = j;
        jeu.ajouteObservateur(this);
        controle = c;
    }
    @Override
    public void metAJour() {
        passe.repaint();
        present.repaint();
        futur.repaint();
    }

    public static void demarrer(Jeu j, CollecteurEvenements c) {
        SwingUtilities.invokeLater(new InterfaceGraphique(j, c));
    }

    @Override
    public void run() {
        frame = new JFrame("That time you killed me");

        Box horizonBox = Box.createHorizontalBox();

        passe = new PlateauSwing(EPOQUE.PASSE, jeu);
        passe.addMouseListener(new AdaptateurSouris(passe, controle));
        horizonBox.add(passe);

        present = new PlateauSwing(EPOQUE.PRESENT, jeu);
        present.addMouseListener(new AdaptateurSouris(present, controle));
        horizonBox.add(present);

        futur = new PlateauSwing(EPOQUE.FUTUR, jeu);
        futur.addMouseListener(new AdaptateurSouris(futur, controle));
        horizonBox.add(futur);

        frame.add(horizonBox);

        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(1500, 600);
        frame.setVisible(true);
    }

    private JButton createButton(String s, String c) {
        JButton but = new JButton(s);
        but.addActionListener(new AdaptateurCommande(controle, new Commande(c)));
        but.setAlignmentX(Component.CENTER_ALIGNMENT);
        but.setFocusable(false);
        return but;
    }
}
