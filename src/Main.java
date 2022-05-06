import Controleur.ControleurMediateur;
import Modele.Jeu;
import Structures.Point;
import Vue.InterfaceGraphique;

public class Main {
    public static void main(String[] args) {
        Jeu j = new Jeu();
        ControleurMediateur c = new ControleurMediateur(j);
        j.setJoueurActuel(j.joueurs[0]); // joueur 1 commence
        InterfaceGraphique.demarrer(j, c);
    }
}
