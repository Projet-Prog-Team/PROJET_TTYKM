import Controleur.ControleurMediateur;
import Modele.Jeu;
import Vue.InterfaceGraphique;

public class Main {

    public static void test(int nbParties, String dif1, String dif2, String heuristique1, String heuristique2) {
        int cpt0 = 0;
        int cpt1 = 0;
        for(int i = 0; i < nbParties; i++) {
            Jeu j = new Jeu();
            ControleurMediateur c = new ControleurMediateur(j,1);
            c.activerIA(0, dif1, heuristique1);
            c.activerIA(1, dif2, heuristique2);
            while(!j.estTermine()) {

            }
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
        ControleurMediateur c = new ControleurMediateur(j,1000);
        InterfaceGraphique.demarrer(j, c);
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
