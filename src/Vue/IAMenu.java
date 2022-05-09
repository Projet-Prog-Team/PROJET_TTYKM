package Vue;

import Controleur.IA;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class IAMenu {

    private JMenu menu;

    IAMenu(int IA, CollecteurEvenements controle){

        menu = new JMenu("Difficulté IA "+IA);

        JMenuItem easyIA1 = new JMenuItem("Facile");
        easyIA1.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Commande c = new Commande("setDifficulty");
                c.setIA(IA);
                c.setDifficulty("facile");
                controle.commande(c);
            }
        });
        menu.add(easyIA1);

        JMenuItem mediumIA1 = new JMenuItem("Moyenne");
        mediumIA1.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Commande c = new Commande("setDifficulty");
                c.setIA(IA);
                c.setDifficulty("moyen");
                controle.commande(c);
            }
        });
        menu.add(mediumIA1);

        JMenuItem hardIA1 = new JMenuItem("Difficile");
        hardIA1.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Commande c = new Commande("setDifficulty");
                c.setIA(IA);
                c.setDifficulty("difficile");
                controle.commande(c);
            }
        });
        menu.add(hardIA1);
    }

    public JMenu getMenu() {
        return menu;
    }
}
