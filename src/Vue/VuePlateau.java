package Vue;

import Modele.Jeu;
import Modele.Joueur;
import Modele.Pion;

import java.util.ArrayList;


public class VuePlateau {

    Jeu jeu;
    Plateau plateau;

    public VuePlateau(Jeu jeu, Plateau p) {
        this.jeu = jeu;
        this.plateau = p;
    }

    public void dessinerPlateau() {

        // Dessine les pions
        ArrayList<Pion> p = jeu.getPions();
        for (int i = 0; i < p.size(); i++) {
            Pion pion = p.get(i);
            if (pion.getEpoque() == plateau.getEpoque()) {
                plateau.tracerPion(pion.getCoordonnees().getL(), pion.getCoordonnees().getC(), pion.getJoueur().getID());
            }
        }

        // Dessine les focus
        Joueur[] joueurs = jeu.getJoueurs();
        if (joueurs[0].getFocus() == plateau.getEpoque()) {
            plateau.tracerFocus1();
        }
        if (joueurs[1].getFocus() == plateau.getEpoque()) {
            plateau.tracerFocus2();
        }

        // Desine les pions selectionnables
        if (jeu.getEtape() == 1 && plateau.getEpoque()==jeu.getJoueurActuel().getFocus()) {
            ArrayList<Pion> pions = jeu.pionsFocusJoueur(jeu.getJoueurActuel().getFocus(), jeu.getJoueurActuel());
            for (int i = 0; i < pions.size(); i++) {
                plateau.tracerBrillance(pions.get(i).getCoordonnees().getL(), pions.get(i).getCoordonnees().getC());
            }
        }

        // Dessine les cases disponibles pour le déplacement
        if (jeu.getEtape() == 2) {
            p = jeu.casesDispo();
            for (int i = 0; i < p.size(); i++) {
                Pion pion = p.get(i);
                if (pion.getEpoque() == plateau.getEpoque()) {
                    plateau.tracerBrillance(pion.getCoordonnees().getL(), pion.getCoordonnees().getC());
                }
            }
        }

        // Dessine les brillances de focus
        if (jeu.getEtape() == 3) {
            Joueur joueur = jeu.getJoueurActuel();
            if (joueur.getID() == 1 && jeu.peutSelectionnerFocus(plateau.getEpoque(), 1)) {
                plateau.tracerBrillanceFocus1();
            } else if (joueur.getID() == 2 && jeu.peutSelectionnerFocus(plateau.getEpoque(), 2)) {
                plateau.tracerBrillanceFocus2();
            }

        }
    }
}
