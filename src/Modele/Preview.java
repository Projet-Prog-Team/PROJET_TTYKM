package Modele;

import java.util.ArrayList;

public class Preview {

    private ArrayList<Pion> plateau;
    private Coup coup;

    Preview(Coup c, ArrayList<Pion> p){
        plateau = p;
        coup= c;
    }

    public ArrayList<Pion> getPlateau() {
        return plateau;
    }

    public void setPlateau(ArrayList<Pion> plateau) {
        this.plateau = plateau;
    }

    public Coup getCoup() {
        return coup;
    }

    public void setCoup(Coup coup) {
        this.coup = coup;
    }
}
