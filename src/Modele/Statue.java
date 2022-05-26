package Modele;

import java.util.Objects;

public class Statue extends Pion {
    int color;

    public Statue(Emplacement emplacement, int t_color) {
        super(emplacement);
        this.color = t_color;
    }

    public Statue(Emplacement emplacement, int t_ID, boolean t_focused, int t_color) {
        super(emplacement, t_ID, t_focused);
        this.color = t_color;
    }

    public int getColor() {
        return color;
    }

    public void setColor(int color) {
        this.color = color;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        Statue statue = (Statue) o;
        return color == statue.color;
    }

    @Override
    public int hashCode() {
        return Objects.hash(color);
    }

    @Override
    public Joueur getJoueur() {
        return null;
    }

    @Override
    public Pion copy(Joueur joueur) {
        return null;
    }

    @Override
    public Pion copy() {
        return new Statue(getEmplacement().copy(), ID, focused, color);
    }

    @Override
    public String toString() {
        return super.toString() +
                "id=" + getColor() +
                " TYPE = STATUE" +
                '}';
    }
}
