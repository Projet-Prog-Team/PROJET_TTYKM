package Modele;

import Patterns.Observable;
import Structures.Point;
import java.util.ArrayList;
import java.util.Arrays;


public class Jeu extends Observable {
    ArrayList<Pion> pions = new ArrayList<>();
    Joueur[] joueurs = new Joueur[2];
    Joueur joueurActuel;

    public Jeu() {
        init();
    }

    public void init() {
        joueurs[0] = new Joueur(1);
        joueurs[1] = new Joueur(2);
        for(int i = 0; i < 3; i++) {
            Pion p1 = new Pion(new Point(0, 0), i, joueurs[0]);
            pions.add(p1);
            Pion p2 = new Pion(new Point(3, 3), i, joueurs[1]);
            pions.add(p2);
        }

    }

    public Pion getPion(Point p, int e) {
        if ((p.getX() <= 3 && p.getX() >= 0) && (p.getY() <= 3 && p.getY() >= 0)) {
            for (Pion pion : pions) {
                if (p.equals(pion.getCoordonnees()) && e == pion.getEpoque()) {
                    return pion;
                }
            }
        }
        return null;
    }

    public void changerEpoque(int epoque) { // Si possible, va déplacer le pion selectionné dans epoque et faire les copies éventuelles
        int PionEpoque = joueurActuel.getPionActuel().getEpoque();
        if(Math.abs(PionEpoque-epoque)==1) { // Si l'époque visée est accessible +- 1
            Pion pionActuel = joueurActuel.getPionActuel();
            if (getPion(pionActuel.coordonnees, pionActuel.getEpoque()) == null) {
                pionActuel.epoque = epoque;
                if (epoque < PionEpoque) { // Si l'époque visée est plus loin (dans le futur) que l'époque du pion.
                    if (joueurActuel.peutSupprimerPion()) {
                        pions.add(new Pion(pionActuel.getCoordonnees(), PionEpoque, joueurActuel));
                        //ne supprime pas un piont du plateau mais du nombre total encore disponible à placer
                        joueurActuel.supprimerPion();
                    }
                }
            }
        }
    }

    public ArrayList<Pion> casesDispo() {   // Retourne une liste des pions jouables pour le joueur cou
        Pion pionActuel = joueurActuel.getPionActuel();
        ArrayList<Pion> casesDisponibles = new ArrayList<>();
        Point coo = pionActuel.getCoordonnees();
        int epoque = pionActuel.getEpoque();
        if (epoque - 1 >= EPOQUE.PASSE) {   // Si on peut aller vers le passé
            if (getPion(coo, epoque-1) == null) {
                casesDisponibles.add(new Pion(coo, epoque-1, joueurActuel));
            }
        }
        if (epoque + 1 <= EPOQUE.FUTUR) {   // Si on peut aller vers le futur
            if (getPion(coo, epoque+1) == null) {
                casesDisponibles.add(new Pion(coo, epoque+1, joueurActuel));
            }
        }

        int [] dX = {-1, 0, 0, 1};
        int [] dY = {0, -1, 1, 0};
        Point p;
        for (int i = 0; i < 4; i++) {   // Vérification des cases autour du pion selectionné
            p = new Point(coo.getX() + dX[i], coo.getY() + dY[i]);
            if (getPion(p, epoque) == null) {
                casesDisponibles.add(new Pion(p, epoque, joueurActuel));
            }
        }

        return casesDisponibles;
    }

    public void move(Pion c, Point new_coord) {
        Point coord = new Point(c.coordonnees.getX(),c.coordonnees.getY());
        c.coordonnees = new_coord;
        Pion voisin = getPion(new_coord,c.epoque);
        Point tmp = new Point(new_coord.getX()+(new_coord.getX()-coord.getX())*-1,new_coord.getY()+(new_coord.getY()-coord.getY())*-1);
        if (voisin != null) {
            if (coord.getX() >= 4 || coord.getY() >= 4) {
                pions.remove(c);
            } else {
                if(voisin.joueur == c.joueur)
                {
                    pions.remove(c);
                    pions.remove(voisin);
                }
                else {
                    move(voisin, tmp);
                }
            }
        }
        else
        {
            c.coordonnees = coord;
        }
    }

    @Override
    public String toString() {
        return "Jeu{" +
                "pions=" + pions + "\n" +
                ", joueurs=" + Arrays.toString(joueurs) + "\n" +
                ", joueurActuel=" + joueurActuel + "\n" +
                '}';
    }
}
