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

    public Pion(Point c, int e, Joueur j,int t_ID,boolean t_focused) {
        coordonnees =  c;
        epoque = e;
        joueur = j;
        ID=t_ID;
        focused=t_focused;
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
        return new Pion(getCoordonnees().copy(), getEpoque(), joueur,ID,focused);
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
        return epoque == pion.epoque && coordonnees.equals(pion.coordonnees) && joueur.equals(pion.joueur);
    }

    @Override
    public int hashCode() {
        return Objects.hash(coordonnees, epoque, ID, joueur);
    }

    @Override
    public String toString() {
        return "Pion{" +
                "coordonnees=" + coordonnees +
                ", epoque=" + epoque +
                ", joueur=" + joueur.getID() +
                '}' + "\n";
    }

    public boolean colle(Pion pion2) {
        int dColonne = Math.abs(pion2.getCoordonnees().getC()-getCoordonnees().getC());
        int dLigne = Math.abs(pion2.getCoordonnees().getL()-getCoordonnees().getL());
        return (dColonne == 0 && dLigne == 1) || (dColonne == 1 && dLigne == 0);
    }
}

