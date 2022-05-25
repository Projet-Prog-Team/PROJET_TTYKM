package Structures;

import Modele.Emplacement;
import Modele.Pion;
import Modele.PionBasique;

public class Tour {
    PionBasique pionSelectionne;
    Emplacement coup1;
    int typeCoup1; // 1 : deplacement et 2 : creer tour
    Emplacement coup2;
    int typeCoup2;

    Integer focus;

    public Tour(){
    }


    public int getTypeCoup1() {
        return typeCoup1;
    }

    public void setTypeCoup1(int typeCoup1) {
        this.typeCoup1 = typeCoup1;
    }

    public int getTypeCoup2() {
        return typeCoup2;
    }

    public void setTypeCoup2(int typeCoup2) {
        this.typeCoup2 = typeCoup2;
    }
    public PionBasique getPionSelectionne() {
        return pionSelectionne;
    }

    public void setPionSelectionne(PionBasique pionSelectionne) {
        this.pionSelectionne = pionSelectionne;
    }

    public Emplacement getCoup1() {
        return coup1;
    }

    public void setCoup1(Emplacement coup1) {
        this.coup1 = coup1;
    }

    public Emplacement getCoup2() {
        return coup2;
    }

    public void setCoup2(Emplacement coup2) {
        this.coup2 = coup2;
    }

    public Integer getFocus() {
        return focus;
    }

    public void setFocus(Integer focus) {
        this.focus = focus;
    }

    @Override
    public String toString() {
        return "Tour{" +
                "pionSelectionne=" + pionSelectionne +
                ", coup1=" + coup1 +
                ", coup2=" + coup2 +
                ", focus=" + focus +
                '}';
    }
}
