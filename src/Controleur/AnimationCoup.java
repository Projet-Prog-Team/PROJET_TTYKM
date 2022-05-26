package Controleur;

import Modele.Coup;
import Modele.Deplacement;
import Vue.InterfaceUtilisateur;

public class AnimationCoup extends Animation {

    Coup cp;
    double progres, vitesse;
    InterfaceUtilisateur vue;

    AnimationCoup(Coup c, InterfaceUtilisateur i){
        super(1);
        cp = c;
        progres = 0;
        vitesse = 0.1;
        vue = i;
        miseAjour();
    }

    @Override
    void miseAjour() {
        progres+=vitesse;
        if (progres > 1){
            progres = 1;
        }
        for(Deplacement deplacement : cp.deplacements()){
            // Si déplacement dans la même époque
            if(deplacement.dep!=null && deplacement.dep.getEpoque()==deplacement.vers.getEpoque()){
                double dC = (deplacement.dep.getCoordonnees().getC() - deplacement.vers.getCoordonnees().getC())*(1 - progres);
                double dL = (deplacement.dep.getCoordonnees().getL() - deplacement.vers.getCoordonnees().getL())*(1 - progres);
                vue.decale(dL, dC, deplacement.vers.getCoordonnees().getL(), deplacement.vers.getCoordonnees().getC(), deplacement.dep.getEpoque());
            }else{
                double alphaDep = 1-progres;
                double alphaArr = progres;
                vue.tp(deplacement.dep, deplacement.vers, alphaDep, alphaArr);
            }
        }
    }

    @Override
    boolean estTermine() {
        return progres >= 1;
    }
}
