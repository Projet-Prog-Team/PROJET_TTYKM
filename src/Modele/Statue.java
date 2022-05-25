package Modele;

import java.util.Objects;

public class Statue extends Pion {
    int id;

    public Statue(Emplacement emplacement, int id) {
        super(emplacement);
        this.id = id;
    }

    public Statue(Emplacement emplacement, int t_ID, boolean t_focused, int id) {
        super(emplacement, t_ID, t_focused);
        this.id = id;
    }

    public int getID() {
        return id;
    }

    public void setID(int id) {
        this.id = id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        Statue statue = (Statue) o;
        return id == statue.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
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
        return new Statue(getEmplacement().copy(), ID, focused, id);
    }

    @Override
    public String toString() {
        return super.toString() +
                "id=" + getID() +
                " TYPE = STATUE" +
                '}';
    }
}
