package Controleur;

import Modele.Jeu;

import java.awt.*;

public abstract class IA {
    Jeu jeu;
    public static IA nouvelle(Jeu j, String type) {
        return null;
    }

    public abstract Point joue();
}
