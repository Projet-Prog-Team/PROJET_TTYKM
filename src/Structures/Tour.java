package Structures;

import Modele.Pion;

public class Tour {
    Pion pionSelectionne;
    Pion coup1;
    Pion coup2;
    Integer focus;

    public Tour(){
    }

    public Pion getPionSelectionne() {
        return pionSelectionne;
    }

    public void setPionSelectionne(Pion pionSelectionne) {
        this.pionSelectionne = pionSelectionne;
    }

    public Pion getCoup1() {
        return coup1;
    }

    public void setCoup1(Pion coup1) {
        this.coup1 = coup1;
    }

    public Pion getCoup2() {
        return coup2;
    }

    public void setCoup2(Pion coup2) {
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
