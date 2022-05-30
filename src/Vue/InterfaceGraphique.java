package Vue;

import Modele.DeroulementJeu;
import Modele.EPOQUE;
import Modele.ETAT;
import Modele.Emplacement;
import Patterns.Observateur;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.awt.event.*;
import java.net.URL;
import java.util.Vector;

public class InterfaceGraphique implements Runnable, Observateur, InterfaceUtilisateur {

    private DeroulementJeu jeu;
    private CollecteurEvenements controle;
    private JFrame frame;
    private PlateauSwing plateauPasse, plateauPresent, plateauFutur;
    private int frameWidth = 1600;
    private int frameHeight = 700;
    ImageIcon victoryIcon;
    JLabel victoryImage;
    JLabel victoryLabel;
    IHMState state;
    JLabel suggestionGif;
    ImageIcon suggestionIcon;

    public InterfaceGraphique(DeroulementJeu jeu, CollecteurEvenements c, IHMState state) {
        this.jeu = jeu;
        jeu.ajouteObservateur(this);
        this.state = state;
        state.ajouteObservateur(this);
        controle = c;
        c.fixerInterfaceUtilisateur(this);
        URL in = ClassLoader.getSystemResource("Img/confetti3.gif");
        victoryIcon = new ImageIcon(in);
        in = ClassLoader.getSystemResource("Img/suggestion.gif");
        suggestionIcon = new ImageIcon(in);
        suggestionIcon.setImage(suggestionIcon.getImage().getScaledInstance(100,100,Image.SCALE_DEFAULT));
    }

    @Override
    public void metAJour() {
        plateauPasse.repaint();
        plateauPresent.repaint();
        plateauFutur.repaint();
        if(jeu.getEtape()!= ETAT.END){
            victoryImage.setVisible(false);
            victoryLabel.setVisible(false);
        }else{
            victoryIcon.setImage(victoryIcon.getImage().getScaledInstance(frameWidth,frameHeight,Image.SCALE_DEFAULT));
            victoryImage.setIcon(victoryIcon);
            victoryImage.setVisible(true);
            if(jeu.getState()=="j1gagne"){
                if(state.getIA1()){
                    victoryLabel.setText("L'IA 1 a gagné !");
                }else{
                    victoryLabel.setText("Le joueur 1 a gagné !");
                }
            }else{
                if(state.getIA2()){
                    victoryLabel.setText("l'IA 2 a gagné !");
                }else{
                    victoryLabel.setText("Le joueur 2 a gagné !");
                }
            }
            victoryLabel.setVisible(true);
        }
        if(state.getPauseIA()){
            suggestionGif.setVisible(true);
            suggestionGif.repaint();
        }else{
            suggestionGif.setVisible(false);
            suggestionGif.repaint();
        }
    }

    public static void demarrer(DeroulementJeu jeu, CollecteurEvenements c, IHMState state) {
        SwingUtilities.invokeLater(new InterfaceGraphique(jeu, c, state));
    }

    public JMenuItem createMenuItem(String s, String c){
        JMenuItem menuItem = new JMenuItem(s);
        menuItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                controle.commande(new Commande(c));
            }
        });
        return menuItem;
    }

    @Override
    public void run() {
        frame = new JFrame("That time you killed me");

        JLayeredPane layeredPane = new JLayeredPane();

        JPanel mainPanel = new JPanel(new BorderLayout());

        victoryImage = new JLabel(victoryIcon);
        victoryImage.setVisible(false);
        layeredPane.add(victoryImage, Integer.valueOf(1));

        victoryLabel = new JLabel("Joueur 1 gagne !");
        victoryLabel.setHorizontalAlignment(SwingConstants.CENTER);
        victoryLabel.setForeground(Color.white);
        Font font = victoryLabel.getFont();
        victoryLabel.setFont(new Font(font.getFontName(),Font.BOLD,80));
        victoryLabel.setVisible(false);
        layeredPane.add(victoryLabel, Integer.valueOf(2));

        // Menu bar
        JMenuBar menuBar = new JMenuBar();

        // File Menu
        JMenu fileMenu = new JMenu("File");

        // New game
        JMenuItem newGameMenu = createMenuItem("Nouvelle partie","newGame");
        fileMenu.add(newGameMenu);

        JMenuItem saveMenu = new JMenuItem("Sauvegarder partie");
        saveMenu.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                SaveDialog saveDialog = new SaveDialog(controle);
            }
        });
        fileMenu.add(saveMenu);

        // Load save
        JMenuItem loadMenu = new JMenuItem("Charger partie");
        loadMenu.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Vector<String> saveNames = new Vector<>();
                saveNames=jeu.MemoryManager.GetFiles();
                LoadDialog loadDialog = new LoadDialog(controle, saveNames);
            }
        });
        fileMenu.add(loadMenu);

        menuBar.add(fileMenu);

        // Config Menu
        JMenu ConfigMenu = new JMenu("Configuration");

        IAMenu IA1DifficultyMenu = new IAMenu(1, controle, state);
        ConfigMenu.add(IA1DifficultyMenu.getMenu());

        IAMenu IA2DifficultyMenu = new IAMenu(2, controle, state);
        ConfigMenu.add(IA2DifficultyMenu.getMenu());

        ActiverIA toggleIA1 = new ActiverIA(controle,1,"toggleIA1", state);
        ConfigMenu.add(toggleIA1.getMenuItem());

        ActiverIA toggleIA2 = new ActiverIA(controle,2,"toggleIA2", state);
        ConfigMenu.add(toggleIA2.getMenuItem());

        JLabel IASpeedLabel = new JLabel("Vitesse IA : 1000ms");
        IASpeedLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        ConfigMenu.add(IASpeedLabel);

        JSlider IASpeedSlider = new JSlider(1,2000);
        IASpeedSlider.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                IASpeedLabel.setText("Vitesse IA : "+IASpeedSlider.getValue()+" ms");
                Commande c = new Commande("IASpeed");
                c.setIA(IASpeedSlider.getValue());
                controle.commande(c);
            }
        });
        ConfigMenu.add(IASpeedSlider);

        menuBar.add(ConfigMenu);

        frame.setJMenuBar(menuBar);

        // Lateral Bar
        JPanel lateralPane = new JPanel();
        lateralPane.setLayout(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();

        // Historique
        Historique historique = new Historique(jeu,controle,state);
        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridx = 0;
        c.gridy = 0;
        c.ipady = 0;
        c.insets = new Insets(0,20,0,20);
        lateralPane.add(historique.getPane(), c);

        // Boutons annuler/refaire
        JPanel buttonPanel1 = new JPanel(new FlowLayout(FlowLayout.CENTER));

        BoutonAnnulerTour boutonAnnulerTour = new BoutonAnnulerTour("Annuler le tour",jeu);
        boutonAnnulerTour.getButton().addActionListener(new AdaptateurCommande(controle, new Commande("annulerTour")));
        buttonPanel1.add(boutonAnnulerTour.getButton());

        BoutonAnnuler boutonAnnuler = new BoutonAnnuler("Annuler",jeu);
        boutonAnnuler.getButton().addActionListener(new AdaptateurCommande(controle, new Commande("annuler")));
        buttonPanel1.add(boutonAnnuler.getButton());

        BoutonRefaire boutonRefaire = new BoutonRefaire("Refaire",jeu);
        boutonRefaire.getButton().addActionListener(new AdaptateurCommande(controle, new Commande("refaire")));
        buttonPanel1.add(boutonRefaire.getButton());

        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridx = 0;
        c.gridy = 1;
        lateralPane.add(buttonPanel1, c);

        // Bouton suggestion/reprendre
        JPanel buttonPanel2 = new JPanel(new FlowLayout(FlowLayout.CENTER));

        BoutonReprendre boutonReprendre = new BoutonReprendre("Relancer IA", state);
        boutonReprendre.getButton().addActionListener(new AdaptateurCommande(controle, new Commande("reprendre")));
        buttonPanel2.add(boutonReprendre.getButton());

        JButton boutonSuggestion = new JButton("Suggestion");
        boutonSuggestion.addActionListener(new AdaptateurCommande(controle, new Commande("suggestion")));
        boutonSuggestion.setFocusable(false);
        buttonPanel2.add(boutonSuggestion);

        c.fill = GridBagConstraints.NONE;
        c.gridx = 0;
        c.gridy = 2;
        lateralPane.add(buttonPanel2, c);

        // Gif pour le bouton relancer IA
        c.fill = GridBagConstraints.NONE;
        c.gridx = 0;
        c.gridy = 3;
        suggestionGif = new JLabel(suggestionIcon);
        lateralPane.add(suggestionGif, c);

        // Label etat du jeu
        LabelEtat labelEtat = new LabelEtat("Joueur 1 effectue son premier mouvement", jeu);
        c.fill = GridBagConstraints.NONE ;
        c.gridx = 0;
        c.gridy = 4;
        lateralPane.add(labelEtat.getLabel(), c);

        mainPanel.add(lateralPane, BorderLayout.EAST);

        // Inventaire joueur 1
        Inventory inv1 = new Inventory(1, jeu, state, controle);
        mainPanel.add(inv1.getPanel(), BorderLayout.NORTH);

        // Plateaux
        Box plateauBox = Box.createHorizontalBox();

        plateauPasse = new PlateauSwing(EPOQUE.Convert(EPOQUE.PASSE), jeu, state);
        AdaptateurSouris a1 = new AdaptateurSouris(plateauPasse, controle);
        plateauPasse.addMouseMotionListener(a1);
        plateauPasse.addMouseListener(a1);
        plateauBox.add(plateauPasse);

        plateauPresent = new PlateauSwing(EPOQUE.Convert(EPOQUE.PRESENT), jeu, state);
        AdaptateurSouris a2 = new AdaptateurSouris(plateauPresent, controle);
        plateauPresent.addMouseMotionListener(a2);
        plateauPresent.addMouseListener(a2);
        plateauBox.add(plateauPresent);

        plateauFutur = new PlateauSwing(EPOQUE.Convert(EPOQUE.FUTUR), jeu, state);
        AdaptateurSouris a3 = new AdaptateurSouris(plateauFutur, controle);
        plateauFutur.addMouseMotionListener(a3);
        plateauFutur.addMouseListener(a3);
        plateauBox.add(plateauFutur);

        mainPanel.add(plateauBox, BorderLayout.CENTER);

        // Inventaire joueur 2
        Inventory inv2 = new Inventory(2, jeu, state, controle);
        mainPanel.add(inv2.getPanel(), BorderLayout.SOUTH);

        frame.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                super.componentResized(e);
                frameWidth = frame.getWidth();
                frameHeight = frame.getHeight();
                mainPanel.setBounds(0,0,frameWidth, frameHeight-inv2.getPanel().getHeight());
                victoryImage.setBounds(0,0,frameWidth,frameHeight);
                victoryLabel.setBounds(0,0,frameWidth, frameHeight);
            }
        });

        frame.addWindowStateListener(new WindowAdapter() {
            @Override
            public void windowStateChanged(WindowEvent e) {
                super.windowStateChanged(e);
                frameWidth = frame.getWidth();
                frameHeight = frame.getHeight();
                mainPanel.setBounds(0,0,frameWidth, frameHeight-inv2.getPanel().getHeight());
                victoryImage.setBounds(0,0,frameWidth,frameHeight);
                victoryLabel.setBounds(0,0,frameWidth, frameHeight);
            }
        });

        layeredPane.add(mainPanel,Integer.valueOf(0));

        Timer time = new Timer(16, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                controle.ticAnim();
            }
        });
        time.start();

        frame.setFocusable(true);
        frame.addKeyListener(new AdaptateurClavier(controle));
        frame.add(layeredPane);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(frameWidth, frameHeight);
        frame.setVisible(true);
    }

    @Override
    public void decale(double dL, double dC, int l, int c, int epoque) {
        switch (epoque){
            case 0:
                plateauPasse.decale(dL,dC,l,c);
                break;
            case 1:
                plateauPresent.decale(dL,dC,l,c);
                break;
            case 2:
                plateauFutur.decale(dL,dC,l,c);
                break;
        }
    }

    @Override
    public void tp(Emplacement depart, Emplacement arrive, double alphaDep, double alphaArr) {
        plateauPasse.tp(depart, arrive, alphaDep, alphaArr);
        plateauPresent.tp(depart, arrive, alphaDep, alphaArr);
        plateauFutur.tp(depart, arrive, alphaDep, alphaArr);
    }

    @Override
    public void reset() {
        plateauFutur.reset();
        plateauPasse.reset();
        plateauPresent.reset();
    }
}
