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

        ArrayList<Pion> pions;

        // Dessine les cases disponibles pour le déplacement
        if (jeu.getEtape() == 2) {
            pions = jeu.casesDispo();
            for (int i = 0; i < pions.size(); i++) {
                Pion pion = pions.get(i);
                if (pion.getEpoque() == plateau.getEpoque()) {
                    plateau.tracerBrillancev2(pion.getCoordonnees().getL(), pion.getCoordonnees().getC());
                }
            }
        }

        // Desine les pions selectionnables
        if (jeu.getEtape() == 1 && plateau.getEpoque()==jeu.getJoueurActuel().getFocus()) {
            pions = jeu.pionsFocusJoueur(jeu.getJoueurActuel().getFocus(), jeu.getJoueurActuel());
            for (int i = 0; i < pions.size(); i++) {
                plateau.tracerBrillance(pions.get(i).getCoordonnees().getL(), pions.get(i).getCoordonnees().getC());
            }
        }

        // Dessine les pions en mode preview ou non
        if(jeu.getPreview()!=null && jeu.getEtape()==2){
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

        // Dessine les brillances de focus
        if (jeu.getEtape() == 3) {
            Joueur joueur = jeu.getJoueurActuel();
            if (joueur.getID() == 1 && jeu.peutSelectionnerFocus(plateau.getEpoque(), 1)) {
                plateau.tracerBrillanceFocus1();
            } else if (joueur.getID() == 2 && jeu.peutSelectionnerFocus(plateau.getEpoque(), 2)) {
                plateau.tracerBrillanceFocus2();
            }

        }

        // Dessine les focus
        Joueur[] joueurs = jeu.getJoueurs();

        if(jeu.getJoueurActuel().getID()==1){
            if (joueurs[1].getFocus() == plateau.getEpoque()) {
                plateau.tracerFocus2(1);
            }
            if(jeu.getPreviewFocus1()==3 || jeu.getEtape()!=3){
                if (joueurs[0].getFocus() == plateau.getEpoque()) {
                    plateau.tracerFocus1(1);
                }
            }else{
                if (jeu.getPreviewFocus1() == plateau.getEpoque()) {
                    plateau.tracerFocus1(1);
                }
                if (joueurs[0].getFocus() == plateau.getEpoque()) {
                    plateau.tracerFocus1(0.5);
                }
            }
        }else{
            if (joueurs[0].getFocus() == plateau.getEpoque()) {
                plateau.tracerFocus1(1);
            }
            if(jeu.getPreviewFocus2()==3 || jeu.getEtape()!=3){
                if (joueurs[1].getFocus() == plateau.getEpoque()) {
                    plateau.tracerFocus2(1);
                }
            }else{
                if (jeu.getPreviewFocus2() == plateau.getEpoque()) {
                    plateau.tracerFocus2(1);
                }
                if (joueurs[1].getFocus() == plateau.getEpoque()) {
                    plateau.tracerFocus2(0.5);
                }
            }
        }
    }
}
