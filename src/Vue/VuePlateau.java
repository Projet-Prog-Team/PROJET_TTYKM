package Vue;

import Modele.ETAT;
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

        ArrayList<Pion> pions;

        // Affiche les cases disponibles pour le déplacement
        if (jeu.getEtape() == ETAT.MOVE1 || jeu.getEtape()== ETAT.MOVE2) {
            pions = jeu.casesDispo();
            for (int i = 0; i < pions.size(); i++) {
                Pion pion = pions.get(i);
                if (pion.getEpoque() == plateau.getEpoque()) {
                    plateau.tracerBrillanceCase(pion.getCoordonnees().getL(), pion.getCoordonnees().getC());
                }
            }
        }

        // Affiche les pions selectionnables
        if (jeu.getEtape() == ETAT.SELECT && plateau.getEpoque()==jeu.getJoueurActuel().getFocus()) {
            pions = jeu.pionsFocusJoueur(jeu.getJoueurActuel().getFocus(), jeu.getJoueurActuel());
            for (int i = 0; i < pions.size(); i++) {
                plateau.tracerBrillancePion(pions.get(i).getCoordonnees().getL(), pions.get(i).getCoordonnees().getC());
            }
        }

        // Affiche les pions en mode preview ou non
        if(jeu.getPreview()!=null && (jeu.getEtape() == ETAT.MOVE1 || jeu.getEtape()== ETAT.MOVE2)){
            pions = jeu.getPreview();
            for (int i = 0; i < pions.size(); i++) {
                Pion pion = pions.get(i);
                if (pion.getEpoque() == plateau.getEpoque()) {
                        plateau.tracerPion(pion.getCoordonnees().getL(), pion.getCoordonnees().getC(),1, pion.getJoueur().getID());
                }
            }
            Pion pionActuel =  jeu.getPionActuel();
            if(pionActuel.getEpoque()== plateau.getEpoque()){
                plateau.tracerPion(pionActuel.getCoordonnees().getL(), pionActuel.getCoordonnees().getC(),0.3, pionActuel.getJoueur().getID());
            }
        }else{
            pions = jeu.getPions();
            for (int i = 0; i < pions.size(); i++) {
                Pion pion = pions.get(i);
                if (pion.getEpoque() == plateau.getEpoque()) {
                    plateau.tracerPion(pion.getCoordonnees().getL(), pion.getCoordonnees().getC(), 1, pion.getJoueur().getID());
                }
            }
        }

        // Affiche les brillances de focus
        if (jeu.getEtape() == ETAT.FOCUS) {
            Joueur joueur = jeu.getJoueurActuel();
            if (joueur.getID() == 1 && jeu.peutSelectionnerFocus(plateau.getEpoque(), 1)) {
                plateau.tracerBrillanceFocusBlanc();
            } else if (joueur.getID() == 2 && jeu.peutSelectionnerFocus(plateau.getEpoque(), 2)) {
                plateau.tracerBrillanceFocusNoir();
            }

        }

        // Affiche les focus en mode preview ou non
        Joueur[] joueurs = jeu.getJoueurs();

        if(jeu.getJoueurActuel().getID()==1){
            if (joueurs[1].getFocus() == plateau.getEpoque()) {
                plateau.tracerFocusNoir(1);
            }
            if(jeu.getPreviewFocus1()==3 || jeu.getEtape()!=ETAT.FOCUS){
                if (joueurs[0].getFocus() == plateau.getEpoque()) {
                    plateau.tracerFocusBlanc(1);
                }
            }else{
                if (jeu.getPreviewFocus1() == plateau.getEpoque()) {
                    plateau.tracerFocusBlanc(1);
                }
                if (joueurs[0].getFocus() == plateau.getEpoque()) {
                    plateau.tracerFocusBlanc(0.5);
                }
            }
        }else{
            if (joueurs[0].getFocus() == plateau.getEpoque()) {
                plateau.tracerFocusBlanc(1);
            }
            if(jeu.getPreviewFocus2()==3 || jeu.getEtape()!=ETAT.FOCUS){
                if (joueurs[1].getFocus() == plateau.getEpoque()) {
                    plateau.tracerFocusNoir(1);
                }
            }else{
                if (jeu.getPreviewFocus2() == plateau.getEpoque()) {
                    plateau.tracerFocusNoir(1);
                }
                if (joueurs[1].getFocus() == plateau.getEpoque()) {
                    plateau.tracerFocusNoir(0.5);
                }
            }
        }
    }
}
