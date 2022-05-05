package Vue;

import Modele.Cases;
import Modele.Jeu;
import Modele.Pion;
import Modele.TYPE;


public class VuePlateau {

    Jeu jeu;
    Plateau plateau;

    public VuePlateau(Jeu jeu, Plateau p){
        this.jeu = jeu;
        this.plateau = p;
    }

    public void dessinerPlateau() {
        Cases[][] cases = new Cases[4][4];
        switch (plateau.getEpoque()){
            case PASSE:
                cases = jeu.getPlateaux()[0].getPlateau();
                break;
            case PRESENT:
                cases = jeu.getPlateaux()[1].getPlateau();
                break;
            case FUTUR:
                cases = jeu.getPlateaux()[2].getPlateau();
                break;
        }

        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                //if(cases[i][j].getType() == TYPE.PION) {
                    plateau.tracerPion(i, j);
                //}
            }
        }

    }

}
