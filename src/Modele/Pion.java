package Modele;

import Structures.Point;

import java.util.Objects;

public class Pion {
    Point coordonnees;
    int epoque;
    Joueur joueur;

    public Pion(Point c, int e, Joueur j) {
        coordonnees =  c;
        epoque = e;
        joueur = j;
    }

    public Pion(Pion tmp)
    {
        epoque = tmp.epoque;
        joueur=tmp.joueur;
        coordonnees = new Point(tmp.coordonnees);
    }

    public int getEpoque() {
        return epoque;
    }

    public Point getCoordonnees() { return coordonnees; }

    public Joueur getJoueur() { return joueur; }

    public Pion copy(Joueur joueur) {
        return new Pion(getCoordonnees().copy(), getEpoque(), joueur);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Pion pion = (Pion) o;
        return epoque == pion.epoque && Objects.equals(coordonnees, pion.coordonnees) && Objects.equals(joueur, pion.joueur);
    }

    @Override
    public int hashCode() {
        return Objects.hash(coordonnees, epoque, joueur);
    }

    @Override
    public String toString() {
        return "Pion{" +
                "coordonnees=" + coordonnees +
                ", epoque=" + epoque +
                ", joueur=" + joueur.getID() +
                '}' + "\n";
    }

    public Pion Clone()
    {
        Point tmp_point = new Point(coordonnees.getX(), coordonnees.getY());
        Pion tmp = new Pion(tmp_point,epoque,joueur);
        return tmp;
    }
}

