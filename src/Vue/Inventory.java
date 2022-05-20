package Vue;

import Modele.DeroulementJeu;
import Patterns.Observateur;
import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

public class Inventory implements Observateur {

    private Image pionBasique;
    private ArrayList<JLabel> pionBasiques;
    private Image pionStatue;
    private JLabel labelStatue;
    private DeroulementJeu jeu;
    private int joueur;
    private JPanel panel;
    JLabel labelPlayer;
    IHMState state;
    CollecteurEvenements controleur;

    Inventory(int joueur, DeroulementJeu jeu, IHMState state, CollecteurEvenements controleur){

        this.jeu = jeu;
        jeu.ajouteObservateur(this);

        this.state = state;
        state.ajouteObservateur(this);

        this.controleur = controleur;

        this.joueur = joueur;

        try {
            if(joueur==1){
                InputStream in = ClassLoader.getSystemClassLoader().getResourceAsStream("Img/pionBlanc2.png");
                pionBasique = ImageIO.read(in).getScaledInstance(50, 50, Image.SCALE_SMOOTH);
                in = ClassLoader.getSystemClassLoader().getResourceAsStream("Img/statueBlanche.png");
                pionStatue = ImageIO.read(in).getScaledInstance(50, 50, Image.SCALE_SMOOTH);
            }else{
                InputStream in = ClassLoader.getSystemClassLoader().getResourceAsStream("Img/pionNoir2.png");
                pionBasique = ImageIO.read(in).getScaledInstance(50, 50, Image.SCALE_SMOOTH);
                in = ClassLoader.getSystemClassLoader().getResourceAsStream("Img/statueNoire.png");
                pionStatue = ImageIO.read(in).getScaledInstance(50, 50, Image.SCALE_SMOOTH);
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

        pionBasiques = new ArrayList<>();
        for(int i=0; i<jeu.getJoueurActuel().getNbPionsRestants(); i++){
            JLabel label = new JLabel(new ImageIcon(pionBasique));
            pionBasiques.add(label);
            panel.add(label);
        }

        labelStatue = new JLabel(new ImageIcon(pionStatue));
        labelStatue.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                Commande c = new Commande("clicStatue");
                c.setJoueur(joueur);
                controleur.commande(c);
            }
        });
        panel.add(labelStatue);

        metAJour();
    }

    public JPanel getPanel() {
        return panel;
    }

    @Override
    public void metAJour() {
        //Affiche les pions du joueur
        for(int i=jeu.getJeu().getJoueurs()[joueur-1].getNbPionsRestants(); i<4; i++){
            pionBasiques.get(i).setVisible(false);
        }
        for(int i=0; i<jeu.getJeu().getJoueurs()[joueur-1].getNbPionsRestants(); i++){
            pionBasiques.get(i).setVisible(true);
        }

        // TODO: voir si le joueur a encore son pion statue
        if(jeu.getJeu().getJoueur(joueur-1).isStatuePlaced()){
            labelStatue.setVisible(false);
        }else{
            labelStatue.setVisible(true);
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
