package Vue;

import Modele.Jeu;
import Patterns.Observateur;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class Inventory implements Observateur {

    private Image pionBasique;
    private Box horizonBox;
    private ArrayList<JLabel> labels;
    private Jeu jeu;
    private int joueur;

    Inventory(int joueur, Jeu jeu){

        this.jeu = jeu;
        jeu.ajouteObservateur(this);

        try {
            if(joueur==1){
                this.joueur = 0;
                pionBasique = ImageIO.read(new File("res/Img/blanc.png")).getScaledInstance(50, 50, Image.SCALE_SMOOTH);
            }else{
                this.joueur = 1;
                pionBasique = ImageIO.read(new File("res/Img/noir.png")).getScaledInstance(50, 50, Image.SCALE_SMOOTH);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        horizonBox = Box.createHorizontalBox();
        labels = new ArrayList<>();

        for(int i=0; i<jeu.getJoueurActuel().getNbPionsRestants(); i++){
            JLabel label = new JLabel(new ImageIcon(pionBasique));
            label.setSize(10,10);
            label.setVisible(true);
            labels.add(label);
            horizonBox.add(label);
        }

    }

    public Box getBox(){
        return this.horizonBox;
    }

    @Override
    public void metAJour() {
        for(int i=jeu.getJoueurs()[joueur].getNbPionsRestants(); i<4; i++){
            labels.get(i).setVisible(false);
        }
    }
}
