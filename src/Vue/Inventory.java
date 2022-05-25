package Vue;

import Modele.DeroulementJeu;
import Patterns.Observateur;
import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
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
    ImageIcon loadingIcon;
    JLabel loadingLabel;
    Image arrowIcon;
    JLabel arrowLabel;
    JLabel infoLabel;

    Inventory(int joueur, DeroulementJeu jeu, IHMState state, CollecteurEvenements controleur){

        this.jeu = jeu;
        jeu.ajouteObservateur(this);

        this.state = state;
        state.ajouteObservateur(this);

        this.controleur = controleur;

        this.joueur = joueur;

        try {
            InputStream in;
            if(joueur==1){
                in = ClassLoader.getSystemClassLoader().getResourceAsStream("Img/pionBlanc2.png");
                pionBasique = ImageIO.read(in).getScaledInstance(50, 50, Image.SCALE_SMOOTH);
                in = ClassLoader.getSystemClassLoader().getResourceAsStream("Img/statueBlanche.png");
                pionStatue = ImageIO.read(in).getScaledInstance(50, 50, Image.SCALE_SMOOTH);
            }else{
                in = ClassLoader.getSystemClassLoader().getResourceAsStream("Img/pionNoir2.png");
                pionBasique = ImageIO.read(in).getScaledInstance(50, 50, Image.SCALE_SMOOTH);
                in = ClassLoader.getSystemClassLoader().getResourceAsStream("Img/statueNoire.png");
                pionStatue = ImageIO.read(in).getScaledInstance(50, 50, Image.SCALE_SMOOTH);
            }

            URL url = ClassLoader.getSystemResource("Img/arrow.gif");
            loadingIcon = new ImageIcon(url);

            in = ClassLoader.getSystemClassLoader().getResourceAsStream("Img/whiteArrow.png");
            arrowIcon = ImageIO.read(in).getScaledInstance(50, 50, Image.SCALE_SMOOTH);

        } catch (IOException e) {
            e.printStackTrace();
        }

        panel = new JPanel();
        panel.setLayout(new FlowLayout(FlowLayout.CENTER));
        panel.setMaximumSize(new Dimension((int) panel.getMaximumSize().getWidth(), 60));
        panel.setPreferredSize(new Dimension((int) panel.getMaximumSize().getWidth(), 60));

        // Gif du joueur courant
        loadingLabel = new JLabel();
        loadingIcon.setImage(loadingIcon.getImage().getScaledInstance(50,50,Image.SCALE_DEFAULT));
        loadingLabel.setIcon(loadingIcon);
        loadingLabel.setVisible(false);
        Border border = loadingLabel.getBorder();
        Border margin = new EmptyBorder(0,0,0,20);
        loadingLabel.setBorder(new CompoundBorder(border, margin));
        panel.add(loadingLabel);

        // Label du joueur
        labelPlayer = new JLabel("Joueur "+joueur+" : ");
        labelPlayer.setFont(new Font(Font.MONOSPACED, Font.BOLD, 30));
        panel.add(labelPlayer);

        // Liste des pions du joueur
        pionBasiques = new ArrayList<>();
        for(int i=0; i<jeu.getJoueurActuel().getNbPionsRestants(); i++){
            JLabel label = new JLabel(new ImageIcon(pionBasique));
            pionBasiques.add(label);
            panel.add(label);
        }

        MouseAdapter statueAdapter = new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                Commande c = new Commande("clicStatue");
                c.setJoueur(joueur);
                controleur.commande(c);
            }
        };

        // Statue du joueur
        labelStatue = new JLabel(new ImageIcon(pionStatue));
        labelStatue.addMouseListener(statueAdapter);
        panel.add(labelStatue);

        // Flèche
        arrowLabel = new JLabel(new ImageIcon(arrowIcon));
        arrowLabel.addMouseListener(statueAdapter);
        panel.add(arrowLabel);

        infoLabel = new JLabel("Cliquer pour créer une statue");
        infoLabel.addMouseListener(statueAdapter);
        panel.add(infoLabel);

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

        // Affiche le joueur courant
        if(jeu.getJoueurActuel().getID()==joueur){
            panel.setBackground(Color.ORANGE);
            labelPlayer.setForeground(Color.BLACK);
            loadingLabel.setVisible(true);
            if(!jeu.getJeu().getJoueur(joueur-1).isStatuePlaced()){
                infoLabel.setVisible(true);
                arrowLabel.setVisible(true);
            }else{
                infoLabel.setVisible(false);
                arrowLabel.setVisible(false);
            }
        }else{
            panel.setBackground(Color.DARK_GRAY);
            labelPlayer.setForeground(Color.WHITE);
            loadingLabel.setVisible(false);
            infoLabel.setVisible(false);
            arrowLabel.setVisible(false);
        }

        if(!jeu.getJeu().getJoueur(joueur-1).isStatuePlaced()){
            labelStatue.setVisible(true);
        }else{
            labelStatue.setVisible(false);
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
