package Vue;

import Modele.Jeu;
import Patterns.Observateur;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class Inventory implements Observateur {

    private Image pionBasique;
    private ArrayList<JLabel> labels;
    private Jeu jeu;
    private int joueur;
    private JPanel panel;

    Inventory(int joueur, Jeu jeu){

        this.jeu = jeu;
        jeu.ajouteObservateur(this);

        this.joueur = joueur;

        try {
            if(joueur==1){
                pionBasique = ImageIO.read(new File("res/Img/pionBlanc.png")).getScaledInstance(50, 50, Image.SCALE_SMOOTH);
            }else{
                pionBasique = ImageIO.read(new File("res/Img/pionNoir.png")).getScaledInstance(50, 50, Image.SCALE_SMOOTH);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        panel = new JPanel();
        panel.setLayout(new FlowLayout(FlowLayout.CENTER));
        panel.setMaximumSize(new Dimension((int) panel.getMaximumSize().getWidth(), 60));
        panel.setPreferredSize(new Dimension((int) panel.getMaximumSize().getWidth(), 60));

        labels = new ArrayList<>();
        for(int i=0; i<jeu.getJoueurActuel().getNbPionsRestants(); i++){
            JLabel label = new JLabel(new ImageIcon(pionBasique));
            labels.add(label);
            panel.add(label);
        }

        metAJour();
    }

    public JPanel getPanel() {
        return panel;
    }

    @Override
    public void metAJour() {
        for(int i=jeu.getJoueurs()[joueur-1].getNbPionsRestants(); i<4; i++){
            labels.get(i).setVisible(false);
        }
        if(jeu.getJoueurActuel().getID()==joueur){
            panel.setBackground(Color.ORANGE);
        }else{
            panel.setBackground(Color.DARK_GRAY);

        }
    }
}
