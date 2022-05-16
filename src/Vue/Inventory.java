package Vue;

import Modele.DeroulementJeu;
import Patterns.Observateur;
import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

public class Inventory implements Observateur {

    private Image pionBasique;
    private ArrayList<JLabel> labels;
    private DeroulementJeu jeu;
    private int joueur;
    private JPanel panel;
    JLabel labelPlayer;
    IHMState state;

    Inventory(int joueur, DeroulementJeu jeu, IHMState state){

        this.jeu = jeu;
        jeu.ajouteObservateur(this);

        this.state = state;
        state.ajouteObservateur(this);

        this.joueur = joueur;

        try {
            if(joueur==1){
                InputStream in = ClassLoader.getSystemClassLoader().getResourceAsStream("Img/pionBlanc.png");
                pionBasique = ImageIO.read(in).getScaledInstance(50, 50, Image.SCALE_SMOOTH);
            }else{
                InputStream in = ClassLoader.getSystemClassLoader().getResourceAsStream("Img/pionNoir.png");
                pionBasique = ImageIO.read(in).getScaledInstance(50, 50, Image.SCALE_SMOOTH);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        panel = new JPanel();
        panel.setLayout(new FlowLayout(FlowLayout.CENTER));
        panel.setMaximumSize(new Dimension((int) panel.getMaximumSize().getWidth(), 60));
        panel.setPreferredSize(new Dimension((int) panel.getMaximumSize().getWidth(), 60));

        labelPlayer = new JLabel("Joueur "+joueur+" : ");
        labelPlayer.setFont(new Font(Font.MONOSPACED, Font.BOLD, 30));
        panel.add(labelPlayer);

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
        // Affiche les pions
        for(int i=jeu.getJeu().getJoueurs()[joueur-1].getNbPionsRestants(); i<4; i++){
            labels.get(i).setVisible(false);
        }
        for(int i=0; i<jeu.getJeu().getJoueurs()[joueur-1].getNbPionsRestants(); i++){
            labels.get(i).setVisible(true);
        }
        // Affiche le joueur courant
        if(jeu.getJoueurActuel().getID()==joueur){
            panel.setBackground(Color.ORANGE);
            labelPlayer.setForeground(Color.BLACK);
        }else{
            panel.setBackground(Color.DARK_GRAY);
            labelPlayer.setForeground(Color.WHITE);
        }
        // Affiche si joueur ou IA
        if(joueur==1){
            if(state.getIA1()){
                labelPlayer.setText("IA "+joueur+" : ");
            }else{
                labelPlayer.setText("Joueur "+joueur+" : ");
            }
        }else{
            if(state.getIA2()){
                labelPlayer.setText("IA "+joueur+" : ");
            }else{
                labelPlayer.setText("Joueur "+joueur+" : ");
            }
        }
    }
}
