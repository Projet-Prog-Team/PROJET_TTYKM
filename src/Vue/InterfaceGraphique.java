package Vue;

import Modele.EPOQUE;
import Modele.Jeu;
import Patterns.Observateur;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Vector;

public class InterfaceGraphique implements Runnable, Observateur {

    private Jeu jeu;
    private CollecteurEvenements controle;
    private JFrame frame;
    private PlateauSwing passe,present,futur;
    private int width = 1500;


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
        frame.setLayout(new BorderLayout());

        Box mainBox = Box.createVerticalBox();

        // Menu bar
        JMenuBar menuBar = new JMenuBar();

        // File Menu
        JMenu fileMenu = new JMenu("File");

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

        JMenuItem loadMenu = new JMenuItem("Charger partie");
        loadMenu.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // TODO: Fonction permettant de récupérer les noms des fichiers de save disponibles
                Vector<String> saveNames = new Vector<>();
                saveNames.add("Test1");
                saveNames.add("Test2");
                saveNames.add("Test3");
                LoadDialog loadDialog = new LoadDialog(controle, saveNames);
            }
        });
        fileMenu.add(loadMenu);

        menuBar.add(fileMenu);

        // IA Menu

        JMenu ConfigMenu = new JMenu("Configuration");

        IAMenu IA1Menu = new IAMenu(1, controle);
        ConfigMenu.add(IA1Menu.getMenu());

        IAMenu IA2Menu = new IAMenu(2, controle);
        ConfigMenu.add(IA2Menu.getMenu());

        ActiverIA ia1 = new ActiverIA(jeu, controle,1,"toggleIA1");
        ConfigMenu.add(ia1.getMenuItem());

        ActiverIA ia2 = new ActiverIA(jeu, controle,2,"toggleIA2");
        ConfigMenu.add(ia2.getMenuItem());

        menuBar.add(ConfigMenu);

        // Help
        JMenu HelpMenu = new JMenu("Help");
        menuBar.add(HelpMenu);

        frame.setJMenuBar(menuBar);

        // Lateral Bar
        Box topBar = Box.createHorizontalBox();

        topBar.add(Box.createGlue());

        LabelEtat labelEtat = new LabelEtat("Joueur 1 effectue son premier mouvement", jeu);
        topBar.add(labelEtat.getLabel());

        BoutonAnnuler boutonAnnuler = new BoutonAnnuler("Annuler", jeu);
        boutonAnnuler.getButton().addActionListener(new AdaptateurCommande(controle, new Commande("annuler")));
        topBar.add(boutonAnnuler.getButton());

        BoutonRefaire boutonRefaire = new BoutonRefaire("Refaire", jeu);
        boutonRefaire.getButton().addActionListener(new AdaptateurCommande(controle, new Commande("refaire")));
        topBar.add(boutonRefaire.getButton());

        topBar.add(Box.createGlue());

        frame.add(topBar, BorderLayout.NORTH);

        // Pions joueur 1
        Inventory inv1 = new Inventory(1, jeu);
        mainBox.add(inv1.getPanel());

        // Plateaux
        Box plateauBox = Box.createHorizontalBox();

        passe = new PlateauSwing(EPOQUE.PASSE, jeu);
        AdaptateurSouris a1 = new AdaptateurSouris(passe, controle);
        passe.addMouseMotionListener(a1);
        passe.addMouseListener(a1);
        plateauBox.add(passe);

        present = new PlateauSwing(EPOQUE.PRESENT, jeu);
        AdaptateurSouris a2 = new AdaptateurSouris(present, controle);
        present.addMouseMotionListener(a2);
        present.addMouseListener(a2);
        plateauBox.add(present);

        futur = new PlateauSwing(EPOQUE.FUTUR, jeu);
        AdaptateurSouris a3 = new AdaptateurSouris(futur, controle);
        futur.addMouseMotionListener(a3);
        futur.addMouseListener(a3);
        plateauBox.add(futur);

        mainBox.add(plateauBox);

        // Pions joueur 2
        Inventory inv2 = new Inventory(2, jeu);
        mainBox.add(inv2.getPanel());

        frame.add(mainBox, BorderLayout.CENTER);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(width, 750);
        frame.setVisible(true);
    }

}
