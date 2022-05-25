import Controleur.ControleurMediateur;
import Modele.CalculJeu;
import Modele.DeroulementJeu;
import Modele.Jeu;
import Structures.Couple;
import Structures.Tour;
import Vue.IHMState;
import Vue.InterfaceGraphique;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.InterfaceAddress;

public class Main {

    public static void test(int nbParties, String dif1, String dif2, String heuristique1, String heuristique2) {
        int cpt0 = 0;
        int cpt1 = 0;
        for(int i = 0; i < nbParties; i++) {
            Jeu j = new Jeu();
            DeroulementJeu dj = new DeroulementJeu(j,false,null);
            IHMState state = new IHMState();
            ControleurMediateur c = new ControleurMediateur(dj,1, state);
            dj.init(c);
            c.desactiverIA(1);
            c.activerIA(0, dif1, heuristique1);
            c.activerIA(1, dif2, heuristique2);
            System.out.println(i);
//            int finalI = i;
            Timer time = new javax.swing.Timer(10000, new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    System.out.println("---------------------------");
                    InterfaceGraphique.demarrer(dj, c, state);
                    System.out.println("---------------------------");
                }
            });
            time.start();
            while(!dj.estTermine()) {
            }
            time.stop();
            if (j.joueurAGagne(j.joueurs[0])) {
                cpt0++;
            } else {
                cpt1++;
            }
        }
        System.out.println("Sur " + nbParties + " parties : ");
        System.out.println("Le joueur 1 (IA " + dif1 + ") à gagné " + cpt0 + " fois.");
        System.out.println("Le joueur 2 (IA " + dif2 + ") à gagné " + cpt1 + " fois.");
    }

    public static void classique() {
        Jeu j = new Jeu();
        DeroulementJeu dj = new DeroulementJeu(j,true,null);
        IHMState state = new IHMState();
        ControleurMediateur c = new ControleurMediateur(dj,1000, state);
        /*ArrayList<Couple<DeroulementJeu, Tour>> liste = gros.Branchements();
        for (Couple<DeroulementJeu, Tour> lalala : liste) {
            InterfaceGraphique.demarrer(lalala.getFirst(), c, state);
            try {
                Thread.sleep(5000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }*/
        InterfaceGraphique.demarrer(dj, c, state);
    }

    public static void main(String[] args) {
        if (args.length == 0) {
            classique();
        } else {
            switch (args[0]) {
                case "classique":
                    classique();
                    break;
                case "test":
                    test(Integer.parseInt(args[1]), args[2], args[3], args[4], args[5]);
                    break;
            }
        }

        //InterfaceGraphique.demarrer(j, c);
    }
}
