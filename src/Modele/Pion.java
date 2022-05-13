package Modele;

import Structures.Point;

import java.util.Objects;

public class Pion {
    private Point coordonnees;
    int epoque;
    public int ID;
    public boolean focused = false;
    private Joueur joueur;

    public Pion(Point c, int e, Joueur j) {
        coordonnees =  c;
        epoque = e;
        joueur = j;
    }

    public Pion(Point c, int e, Joueur j,int t_ID) {
        coordonnees =  c;
        epoque = e;
        joueur = j;
        ID=t_ID;
    }


    public int getEpoque() {
        return epoque;
    }

    public Point getCoordonnees() { return coordonnees; }
    public void SetCoordonnees(Point t_coordonnees)
    {
        coordonnees = t_coordonnees;
    }

    public Joueur getJoueur() { return joueur; }

    public Pion copy(Joueur joueur) {
        return new Pion(getCoordonnees().copy(), getEpoque(), joueur,ID);
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

