package Vue;

import Modele.*;

import java.util.ArrayList;


public class VuePlateau {

    DeroulementJeu jeu;
    Plateau plateau;

    public VuePlateau(DeroulementJeu jeu, Plateau p) {
        this.jeu = jeu;
        this.plateau = p;
    }

    public void dessinerPlateau() {

        ArrayList<Emplacement> casesDispoDeplacement;
        ArrayList<PionBasique> pionsBasiques;
        ArrayList<Pion> pions;
        IHMState state = plateau.getState();

        // Si c'est au tour de l'IA de jouer on désactive les feedback/feedforward/suggestion
        if((jeu.getJoueurActuel().getID()==1 && !state.getIA1() || jeu.getJoueurActuel().getID()==2 && !state.getIA2())){

            // Affiche les suggestions de pions
            Emplacement source = state.getSuggestionSource();
            Emplacement dest = state.getSuggestionDestination();
            if(source!=null && dest!=null){
                if(plateau.getEpoque()==source.getEpoque()){
                    plateau.tracerSuggestionCase(source.getCoordonnees().getL(), source.getCoordonnees().getC());
                }
                if(plateau.getEpoque()==dest.getEpoque()){
                    plateau.tracerSuggestionCase(dest.getCoordonnees().getL(), dest.getCoordonnees().getC());
                }
            }

            // Affiche les cases disponibles pour le déplacement
            if (jeu.getEtape() == ETAT.MOVE1 || jeu.getEtape()== ETAT.MOVE2 && !jeu.getConstructionStatue()) {
                casesDispoDeplacement = jeu.getJeu().casesDispo(jeu.getJoueurActuel(), jeu.getPionActuel());
                for (Emplacement emplacement : casesDispoDeplacement) {
                    if (emplacement.getEpoque() == plateau.getEpoque()) {
                        plateau.tracerBrillanceCase(emplacement.getCoordonnees().getL(), emplacement.getCoordonnees().getC());
                    }
                }
            }

            // Affiche les cases disponibles pour la construction d'un statue
            if((jeu.getEtape() == ETAT.MOVE1 || jeu.getEtape()== ETAT.MOVE2) && jeu.getConstructionStatue()){
                casesDispoDeplacement = jeu.getJeu().casesDispoStatue(jeu.getPionActuel());
                for (Emplacement emplacement : casesDispoDeplacement) {
                    if (emplacement.getEpoque() == plateau.getEpoque()) {
                        plateau.tracerBrillancePion(emplacement.getCoordonnees().getL(), emplacement.getCoordonnees().getC());
                    }
                }
            }

            // Affiche les pions selectionnables
            if (jeu.getEtape() == ETAT.SELECT && plateau.getEpoque()==jeu.getJoueurActuel().getFocus()) {
                pionsBasiques = jeu.getJeu().pionsFocusJoueur(jeu.getJoueurActuel().getFocus(), jeu.getJoueurActuel());
                for (PionBasique pion : pionsBasiques) {
                    if(!pion.equals(source)){
                        plateau.tracerBrillancePion(pion.getCoordonnees().getL(), pion.getCoordonnees().getC());
                    }
                }
            }

            // Affiche les suggestion de focus
            int suggestionFocus = state.getSuggestionFocus();
            if(suggestionFocus!=3 && plateau.getEpoque()==suggestionFocus){
                plateau.tracerSuggestionFocus(jeu.getJoueurActuel().getID());
            }

            // Affiche les brillances de focus
            if (jeu.getEtape() == ETAT.FOCUS) {
                Joueur joueur = jeu.getJoueurActuel();
                if (joueur.getID() == 1 && jeu.peutSelectionnerFocus(plateau.getEpoque(), 1) && suggestionFocus!= plateau.getEpoque()) {
                    plateau.tracerBrillanceFocus(1);
                } else if (joueur.getID() == 2 && jeu.peutSelectionnerFocus(plateau.getEpoque(), 2) && suggestionFocus!= plateau.getEpoque()) {
                    plateau.tracerBrillanceFocus(0);
                }
            }

        }

        // Affiche les pions en mode preview ou non
        if(state.getPreview()!=null && (jeu.getEtape() == ETAT.MOVE1 || jeu.getEtape()== ETAT.MOVE2)){
            pions = state.getPreview();
            for (Pion pion : pions) {
                if (pion.getEpoque() == plateau.getEpoque()) {
                    if (pion instanceof PionBasique) {
                        plateau.tracerPion(pion.getCoordonnees().getL(), pion.getCoordonnees().getC(), 1, pion.getJoueur().getID());
                    } else {
                        plateau.tracerStatue(pion.getCoordonnees().getL(), pion.getCoordonnees().getC(), 1, ((Statue) pion).getID());
                    }
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
                    if (pion instanceof PionBasique) {
                        plateau.tracerPion(pion.getCoordonnees().getL(), pion.getCoordonnees().getC(), 1, pion.getJoueur().getID());
                    } else {
                        plateau.tracerStatue(pion.getCoordonnees().getL(), pion.getCoordonnees().getC(), 1, ((Statue) pion).getID());
                    }
                }
            }
        }

        //TODO: connaitre l'emplacement des statues
        //plateau.tracerStatue(1,2, 1, 3);

        // Affiche les focus en mode preview ou non
        Joueur[] joueurs = jeu.getJeu().getJoueurs();

        if(jeu.getJoueurActuel().getID()==1){
            if (joueurs[1].getFocus() == plateau.getEpoque()) {
                plateau.tracerFocusNoir(1);
            }
            if(state.getPreviewFocus1()==3 || jeu.getEtape()!=ETAT.FOCUS){
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
            if(state.getPreviewFocus2()==3 || jeu.getEtape()!=ETAT.FOCUS){
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

    }
}
