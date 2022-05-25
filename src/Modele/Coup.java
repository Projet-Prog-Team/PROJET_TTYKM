package Modele;

import java.util.ArrayList;

public class Coup {

    ArrayList<Deplacement> mouvements;

    public Coup(){
        mouvements = new ArrayList<>();
    }

    void deplace(Emplacement d,Emplacement v){
        mouvements.add(new Deplacement(d,v));
    }

    public ArrayList<Deplacement> deplacements(){
        return mouvements;
    }
}
