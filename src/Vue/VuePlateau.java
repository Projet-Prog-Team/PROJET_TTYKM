package Vue;

import Modele.DeroulementJeu;
import Modele.Joueur;
import Modele.Pion;

import java.util.ArrayList;


public class VuePlateau {

    DeroulementJeu jeu;
    Plateau plateau;

    public VuePlateau(DeroulementJeu jeu, Plateau p) {
        this.jeu = jeu;
        this.plateau = p;
    }

    public void dessinerPlateau() {

        ArrayList<Pion> pions;
        IHMState state = plateau.getState();

        // Affiche les cases disponibles pour le déplacement
        if (jeu.getEtape() == 2) {
            pions = jeu.getJeu().casesDispo(jeu.getJoueurActuel(), jeu.getPionActuel());
            for (Pion pion : pions) {
                if (pion.getEpoque() == plateau.getEpoque()) {
                    plateau.tracerBrillanceCase(pion.getCoordonnees().getL(), pion.getCoordonnees().getC());
                }
            }
        }

        // Affiche les pions selectionnables
        if (jeu.getEtape() == 1 && plateau.getEpoque()==jeu.getJoueurActuel().getFocus()) {
            pions = jeu.getJeu().pionsFocusJoueur(jeu.getJoueurActuel().getFocus(), jeu.getJoueurActuel());
            for (Pion pion : pions) {
                plateau.tracerBrillancePion(pion.getCoordonnees().getL(), pion.getCoordonnees().getC());
            }
        }

        // Affiche les pions en mode preview ou non
        if(state.getPreview()!=null && jeu.getEtape()==2){
            pions = state.getPreview();
            for (Pion pion : pions) {
                if (pion.getEpoque() == plateau.getEpoque()) {
                    plateau.tracerPion(pion.getCoordonnees().getL(), pion.getCoordonnees().getC(), 1, pion.getJoueur().getID());
                }
            }
            Pion pionActuel =  jeu.getPionActuel();
            if(pionActuel.getEpoque()== plateau.getEpoque()){
                plateau.tracerPion(pionActuel.getCoordonnees().getL(), pionActuel.getCoordonnees().getC(),0.3, pionActuel.getJoueur().getID());
            }
        }else{
            pions = jeu.getJeu().getPions();
            for (Pion pion : pions) {
                if (pion.getEpoque() == plateau.getEpoque()) {
                    plateau.tracerPion(pion.getCoordonnees().getL(), pion.getCoordonnees().getC(), 1, pion.getJoueur().getID());
                }
            }
        }

        // Affiche les brillances de focus
        if (jeu.getEtape() == 3) {
            Joueur joueur = jeu.getJoueurActuel();
            if (joueur.getID() == 1 && jeu.peutSelectionnerFocus(plateau.getEpoque(), 1)) {
                plateau.tracerBrillanceFocusBlanc();
            } else if (joueur.getID() == 2 && jeu.peutSelectionnerFocus(plateau.getEpoque(), 2)) {
                plateau.tracerBrillanceFocusNoir();
            }

        }

        // Affiche les focus en mode preview ou non
        Joueur[] joueurs = jeu.getJeu().getJoueurs();

        if(jeu.getJoueurActuel().getID()==1){
            if (joueurs[1].getFocus() == plateau.getEpoque()) {
                plateau.tracerFocusNoir(1);
            }
            if(state.getPreviewFocus1()==3 || jeu.getEtape()!=3){
                if (joueurs[0].getFocus() == plateau.getEpoque()) {
                    plateau.tracerFocusBlanc(1);
                }
            }else{
                if (state.getPreviewFocus1() == plateau.getEpoque()) {
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
            if(state.getPreviewFocus2()==3 || jeu.getEtape()!=3){
                if (joueurs[1].getFocus() == plateau.getEpoque()) {
                    plateau.tracerFocusNoir(1);
                }
            }else{
                if (state.getPreviewFocus2() == plateau.getEpoque()) {
                    plateau.tracerFocusNoir(1);
                }
                if (joueurs[1].getFocus() == plateau.getEpoque()) {
                    plateau.tracerFocusNoir(0.5);
                }
            }
        }

        // Affiche les suggestions
        Pion source = state.getSuggestionSource();
        Pion dest = state.getSuggestionDestination();
        if(source!=null && dest!=null){
            if(plateau.getEpoque()==source.getEpoque()){
                plateau.tracerBrillancePion(source.getCoordonnees().getL(), source.getCoordonnees().getC());
            }
            if(plateau.getEpoque()==dest.getEpoque()){
                plateau.tracerBrillancePion(dest.getCoordonnees().getL(), dest.getCoordonnees().getC());
            }
        }

    }
}
