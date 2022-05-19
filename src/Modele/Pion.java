package Modele;

import Structures.Point;

import java.util.Objects;

public abstract class Pion {
    private Emplacement e;
    public int ID;
    public boolean focused = false;

    public Pion(Emplacement emplacement) {
        e = emplacement;
    }

    public Pion(Emplacement emplacement, int t_ID,boolean t_focused) {
        e = emplacement;
        ID=t_ID;
        focused=t_focused;
    }

    public Emplacement getEmplacement() {
        return e;
    }
    public void setEpoque(int epoque) {
        e.setEpoque(epoque);
    }
    public int getEpoque() {
        return e.getEpoque();
    }
    public Point getCoordonnees() { return e.getCoordonnees(); }
    public void setCoordonnees(Point coord)
    {
        e.setCoordonnees(coord);
    }

    public abstract Joueur getJoueur();

    public boolean colle(Pion pion2) {
        int dColonne = Math.abs(pion2.getCoordonnees().getC()-getCoordonnees().getC());
        int dLigne = Math.abs(pion2.getCoordonnees().getL()-getCoordonnees().getL());
        return (dColonne == 0 && dLigne == 1) || (dColonne == 1 && dLigne == 0);
    }
    public abstract Pion copy(Joueur joueur);

    @Override
    public String toString() {
        return "Pion{" +
                "e=" + e +
                ", ID=" + ID +
                ", focused=" + focused;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Pion pion = (Pion) o;
        return ID == pion.ID && focused == pion.focused && Objects.equals(e, pion.e);
    }
}

