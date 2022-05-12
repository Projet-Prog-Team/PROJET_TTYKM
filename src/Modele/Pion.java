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

    public int getEpoque() {
        return epoque;
    }

    public Point getCoordonnees() { return coordonnees; }

    public Joueur getJoueur() { return joueur; }

    public Pion copy(Joueur joueur) {
        return new Pion(getCoordonnees().copy(), getEpoque(), joueur);
    }

    public int distance(Pion p) {
        Point pCoords = p.getCoordonnees();
        int c1 = coordonnees.getC();
        int l1 = coordonnees.getL();
        int c2 = pCoords.getC();
        int l2 = pCoords.getL();
        return (int) Math.abs(Math.sqrt(Math.pow(c1 - c2, 2) + Math.pow(l1 - l2, 2)));
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
}

