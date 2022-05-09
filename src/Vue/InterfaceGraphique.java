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

        Box mainBox = Box.createVerticalBox();

        // Menu bar
        JMenuBar menuBar = new JMenuBar();

        // File Menu
        JMenu fileMenu = new JMenu("File");

        JMenuItem newGameMenu = new JMenuItem("Nouvelle partie");
        newGameMenu.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                controle.commande(new Commande("newGame"));
            }
        });
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

        JMenu IAMenu = new JMenu("IA");

        IAMenu IA1Menu = new IAMenu(1, controle);
        IAMenu.add(IA1Menu.getMenu());

        IAMenu IA2Menu = new IAMenu(2, controle);
        IAMenu.add(IA2Menu.getMenu());

        menuBar.add(IAMenu);

        // Help
        JMenu HelpMenu = new JMenu("Help");
        menuBar.add(HelpMenu);

        frame.setJMenuBar(menuBar);

        // Top bar
        Box topBar = Box.createHorizontalBox();

        JToggleButton toggleIA1 = new JToggleButton("Activer IA 1");
        toggleIA1.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                controle.commande(new Commande("toggleIA1"));
            }
        });
        topBar.add(toggleIA1);

        JToggleButton toggleIA2 = new JToggleButton("Activer IA 2");
        toggleIA2.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                controle.commande(new Commande("toggleIA2"));
            }
        });
        topBar.add(toggleIA2);

        LabelEtat labelEtat = new LabelEtat("Joueur 1 effectue son premier mouvement", jeu);
        topBar.add(labelEtat.getLabel());

        BoutonAnnuler boutonAnnuler = new BoutonAnnuler("Annuler", jeu);
        boutonAnnuler.getButton().addActionListener(new AdaptateurCommande(controle, new Commande("annuler")));
        topBar.add(boutonAnnuler.getButton());

        BoutonRefaire boutonRefaire = new BoutonRefaire("Refaire", jeu);
        boutonRefaire.getButton().addActionListener(new AdaptateurCommande(controle, new Commande("refaire")));
        topBar.add(boutonRefaire.getButton());

        mainBox.add(topBar);

        // Pions joueur 1
        Inventory inv1 = new Inventory(1, jeu);
        mainBox.add(inv1.getBox());

        // Plateaux
        Box plateauBox = Box.createHorizontalBox();

        passe = new PlateauSwing(EPOQUE.PASSE, jeu);
        passe.addMouseListener(new AdaptateurSouris(passe, controle));
        plateauBox.add(passe);

        present = new PlateauSwing(EPOQUE.PRESENT, jeu);
        present.addMouseListener(new AdaptateurSouris(present, controle));
        plateauBox.add(present);

        futur = new PlateauSwing(EPOQUE.FUTUR, jeu);
        futur.addMouseListener(new AdaptateurSouris(futur, controle));
        plateauBox.add(futur);

        mainBox.add(plateauBox);

        // Pions joueur 2
        Inventory inv2 = new Inventory(2, jeu);
        mainBox.add(inv2.getBox());


        Timer t = new Timer(1500, new AdaptateurTemps(controle));
        t.start();
        frame.add(mainBox);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(1500, 600);
        frame.setVisible(true);
    }

}
