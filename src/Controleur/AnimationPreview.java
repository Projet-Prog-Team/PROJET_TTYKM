package Controleur;

import Modele.Coup;
import Modele.Deplacement;
import Vue.InterfaceUtilisateur;

public class AnimationPreview extends Animation {

    Coup cp;
    double progres, vitesse;
    InterfaceUtilisateur vue;

    AnimationPreview(Coup c, InterfaceUtilisateur i){
        super(1);
        cp = c;
        progres = 0;
        vitesse = 0.02;
        vue = i;
        miseAjour();
    }

    @Override
    void miseAjour() {
        progres+=vitesse;
        if (progres > 1){
            progres = 0;
        }
        for(Deplacement deplacement : cp.deplacements()){
            double dC = (deplacement.dep.getCoordonnees().getC() - deplacement.vers.getCoordonnees().getC())*(1 - progres);
            double dL = (deplacement.dep.getCoordonnees().getL() - deplacement.vers.getCoordonnees().getL())*(1 - progres);
            vue.decale(dL, dC, deplacement.vers.getCoordonnees().getL(), deplacement.vers.getCoordonnees().getC(), deplacement.dep.getEpoque());
        }
    }

    @Override
    boolean estTermine() {
        return false;
    }
}
