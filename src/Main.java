import Controleur.ControleurMediateur;
import Modele.Jeu;
import Vue.InterfaceGraphique;

public class Main {
    public static void main(String[] args) {
        Jeu j = new Jeu();
        ControleurMediateur c = new ControleurMediateur(j);
        InterfaceGraphique.demarrer(j, c);
    }
}
