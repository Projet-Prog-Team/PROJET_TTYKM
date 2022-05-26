package Vue;

import Modele.DeroulementJeu;
import Modele.EPOQUE;
import Modele.ETAT;
import Patterns.Observateur;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.net.URL;
import java.util.Vector;

public class InterfaceGraphique implements Runnable, Observateur {

    private DeroulementJeu jeu;
    private CollecteurEvenements controle;
    private JFrame frame;
    private PlateauSwing plateauPasse, plateauPresent, plateauFuture;
    private int frameWidth = 1600;
    private int frameHeight = 700;
    ImageIcon victory;
    JLabel victoryLabel;
    IHMState state;

    public InterfaceGraphique(DeroulementJeu jeu, CollecteurEvenements c, IHMState state) {
        this.jeu = jeu;
        jeu.ajouteObservateur(this);
        this.state = state;
        state.ajouteObservateur(this);
        controle = c;
        URL in = ClassLoader.getSystemResource("Img/victory.gif");
        victory = new ImageIcon(in);
    }

    @Override
    public void metAJour() {
        plateauPasse.repaint();
        plateauPresent.repaint();
        plateauFuture.repaint();
        if(jeu.getEtape()!= ETAT.END){
            victoryLabel.setVisible(false);
        }else{
            victoryLabel.setVisible(true);
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

        victoryLabel = new JLabel(victory);
        victoryLabel.setVisible(false);
        layeredPane.add(victoryLabel, Integer.valueOf(1));

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
                // TODO: Fonction permettant de récupérer les noms des fichiers de save disponibles
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
        //TODO: avoir le vrai historique
        String[] categories = { "Noir Présent 5 -> Passé 5", "Blanc Présent 5 -> Passé 5",
                "Noir Présent 5 -> Passé 5","Blanc Présent 4 -> Présent 5",
                "Noir Présent 5 -> Passé 5","Blanc Présent 5 -> Passé 5",
                "Noir Présent 5 -> Passé 5","Blanc Présent 4 -> Présent 5",
                "Noir Présent 5 -> Passé 5","Blanc Présent 5 -> Passé 5",
                "Noir Présent 5 -> Passé 5","Blanc Présent 4 -> Présent 5",
                "Noir Présent 5 -> Passé 5","Blanc Présent 5 -> Passé 5",
                "Noir Présent 5 -> Passé 5","Blanc Présent 4 -> Présent 5",
                "Noir Présent 5 -> Passé 5","Blanc Présent 5 -> Passé 5",
                "Noir Présent 5 -> Passé 5","Blanc Présent 5 -> Passé 5"};
        JList historyList = new JList<>(categories);
        historyList.addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                controle.commande(new Commande("historique"));
            }
        });
        JScrollPane historyPane = new JScrollPane(historyList);
        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridx = 0;
        c.gridy = 0;
        c.ipady = 150;
        c.insets = new Insets(0,20,0,20);
        lateralPane.add(historyPane, c);
        c.ipady = 0;

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

        // Label etat du jeu
        LabelEtat labelEtat = new LabelEtat("Joueur 1 effectue son premier mouvement", jeu);
        c.fill = GridBagConstraints.NONE ;
        c.gridx = 0;
        c.gridy = 3;
        lateralPane.add(labelEtat.getLabel(), c);

        mainPanel.add(lateralPane, BorderLayout.EAST);

        // Inventaire joueur 1
        Inventory inv1 = new Inventory(1, jeu, state);
        mainPanel.add(inv1.getPanel(), BorderLayout.NORTH);

        // Plateaux
        Box plateauBox = Box.createHorizontalBox();

        plateauPasse = new PlateauSwing(EPOQUE.PASSE, jeu, state);
        AdaptateurSouris a1 = new AdaptateurSouris(plateauPasse, controle);
        plateauPasse.addMouseMotionListener(a1);
        plateauPasse.addMouseListener(a1);
        plateauBox.add(plateauPasse);

        plateauPresent = new PlateauSwing(EPOQUE.PRESENT, jeu, state);
        AdaptateurSouris a2 = new AdaptateurSouris(plateauPresent, controle);
        plateauPresent.addMouseMotionListener(a2);
        plateauPresent.addMouseListener(a2);
        plateauBox.add(plateauPresent);

        plateauFuture = new PlateauSwing(EPOQUE.FUTUR, jeu, state);
        AdaptateurSouris a3 = new AdaptateurSouris(plateauFuture, controle);
        plateauFuture.addMouseMotionListener(a3);
        plateauFuture.addMouseListener(a3);
        plateauBox.add(plateauFuture);

        mainPanel.add(plateauBox, BorderLayout.CENTER);

        // Inventaire joueur 2
        Inventory inv2 = new Inventory(2, jeu, state);
        mainPanel.add(inv2.getPanel(), BorderLayout.SOUTH);

        frame.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                super.componentResized(e);
                frameWidth = frame.getWidth();
                frameHeight = frame.getHeight();
                mainPanel.setBounds(0,0,frameWidth, frameHeight-inv2.getPanel().getHeight());
                victoryLabel.setBounds((frameWidth-800)/2,(frameHeight-300)/2,800,300);
            }
        });

        layeredPane.add(mainPanel,Integer.valueOf(0));
        frame.add(layeredPane);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(frameWidth, frameHeight);
        frame.setVisible(true);
    }

}
