package Modele;

import Structures.Point;

import java.util.Objects;

public class Emplacement {
    private Point coordonnees;
    private int epoque;

    public Emplacement(Point coordonnees, int epoque) {
        this.coordonnees = coordonnees;
        this.epoque = epoque;
    }

    public int getEpoque() {
        return epoque;
    }

    public void setEpoque(int epoque) {
        this.epoque = epoque;
    }

    public Point getCoordonnees() {
        return coordonnees;
    }

    public void setCoordonnees(Point coordonnees) {
        this.coordonnees = coordonnees;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Emplacement that = (Emplacement) o;
        return epoque == that.epoque && Objects.equals(coordonnees, that.coordonnees);
    }

    public Emplacement copy() {
        return new Emplacement(coordonnees.copy(), getEpoque());
    }

    @Override
    public int hashCode() {
        return Objects.hash(coordonnees, epoque);
    }

    @Override
    public String toString() {
        return "Emplacement{" +
                "coordonnees=" + coordonnees +
                ", epoque=" + epoque +
                '}';
    }
}
