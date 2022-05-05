import Controleur.ControleurMediateur;
import Modele.Jeu;
import Structures.Point;
import Vue.InterfaceGraphique;

public class Main {
    public static void main(String[] args) {
        Jeu j = new Jeu();
        ControleurMediateur c = new ControleurMediateur(j);
        System.out.println(j);
        j.joueurs[0].setPionActuel(j.getPion(new Point(0, 0), 1));
        j.joueurs[0].setFocus(1);
        j.joueurActuel = j.joueurs[1];

        j.move(j.joueurActuel.getPionActuel(), new Point(0, 1), true);
        j.move(j.joueurActuel.getPionActuel(), new Point(0, 2), true);
        j.changerEpoque(0);
        j.joueurActuel = j.joueurs[0];
        j.joueurs[1].setPionActuel(j.getPion(new Point(3,3 ), 1));
        j.move(j.joueurActuel.getPionActuel(), new Point(2, 3), true);
        j.move(j.joueurActuel.getPionActuel(), new Point(2, 2), true);
        j.move(j.joueurActuel.getPionActuel(), new Point(1, 2), true);
        j.move(j.joueurActuel.getPionActuel(), new Point(0, 2), true);
        j.move(j.joueurActuel.getPionActuel(), new Point(0, 1), true);
        j.changerEpoque(0);
        j.joueurActuel = j.joueurs[1];
        j.joueurs[1].setPionActuel(j.getPion(new Point(0,0 ), 0));
        j.move(j.joueurActuel.getPionActuel(), new Point(0, 1), true);
        System.out.println(j.joueurActuel.getPionActuel());

        //j.changerEpoque(0);
        System.out.println(j);
        InterfaceGraphique.demarrer(j, c);
    }
}
