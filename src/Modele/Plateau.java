package Modele;

import java.awt.*;
import java.util.Arrays;

public class Plateau {
    Cases[][] plateau = new Cases[4][4];

    public Plateau() {
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                plateau[i][j] = new Cases(TYPE.VIDE);
            }
        }
    }

    public void set(Cases c, int x, int y) {
        plateau[x][y] = c;
    }

    public Cases get(int i, int j) {
        return plateau[i][j];
    }

    public void bouger() {

    }
    public Point[] deplacementPossible() {
        return null;
    }

    @Override
    public String toString() {
        String chaine = "";
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                chaine += " " + plateau[i][j].toString() + " |";
            }
            chaine += "\n";
        }
        return chaine;
    }
}
