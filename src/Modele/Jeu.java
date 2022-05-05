package Modele;

import Patterns.Observable;
import Structures.Point;
import java.util.ArrayList;
import java.util.Arrays;


public class Jeu extends Observable {
    ArrayList<Pion> pions = new ArrayList<>();
    public Joueur[] joueurs = new Joueur[2];
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
        joueurs[0].setFocus(0);
        joueurs[1].setFocus(2);
        joueurs[0].setPionActuel(getPion(new Point(0, 0), 0));
        joueurs[1].setPionActuel(getPion(new Point(3, 3), 2));

    }

    public void jouerCoup(int l, int c, int epoque) {
        Pion clic = new Pion(new Point(l, c), epoque, joueurActuel);
        ArrayList<Pion> cases = casesDispo();

        if (cases.indexOf(clic) != -1) {    // Si coup jouable
            joueurActuel.nbActionsRestantes--;
            if (epoque != joueurActuel.getPionActuel().getEpoque()) {
                changerEpoque(epoque);
            }
            else {
                move(joueurActuel.getPionActuel(), new Point(l, c));
                if (getPion(joueurActuel.pionActuel.getCoordonnees(), joueurActuel.getFocus()) == null) {   // Si le joueur a tué son propre pion
                    joueurActuel.nbActionsRestantes = 0;
                }
            }
        }
        System.out.println("nbActionsRestantes  : " + joueurActuel.getNbActionsRestantes());
        System.out.println("nbPionsRestants  : " + joueurActuel.nbPionsRestants);
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

    public Joueur[] getJoueurs() {
        return joueurs;
    }

    public Joueur getJoueurActuel() {
        return joueurActuel;
    }

    public void setJoueurActuel(Joueur j) {
        joueurActuel= j;
    }

    public void changerEpoque(int epoque) { // Si possible, va déplacer le pion selectionné dans epoque et faire les copies éventuelles
        int PionEpoque = joueurActuel.getPionActuel().getEpoque();
        if(Math.abs(PionEpoque-epoque)==1) { // Si l'époque visée est accessible +- 1
            Pion pionActuel = joueurActuel.getPionActuel();
            if (getPion(pionActuel.coordonnees, epoque) == null) {
                pionActuel.epoque = epoque;
                if (epoque < PionEpoque) { // Si l'époque visée est plus loin (dans le futur) que l'époque du pion.
                    if (joueurActuel.peutSupprimerPion()) {
                        pions.add(new Pion(pionActuel.getCoordonnees(), PionEpoque, joueurActuel));
                        //ne supprime pas un pion du plateau mais du nombre total encore disponible à placer
                        joueurActuel.supprimerPion();
                    } else {
                        pionActuel.epoque = PionEpoque;
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
            if (p.getX() >=0 && p.getX() <= 3 && p.getY() >= 0  && p.getY() <= 3) {
                casesDisponibles.add(new Pion(p, epoque, joueurActuel));
            }
        }

        return casesDisponibles;
    }

    public void move(Pion c, Point new_coord) {
        Point coord = new Point(c.coordonnees.getX(),c.coordonnees.getY());
        Pion voisin = getPion(new_coord,c.epoque);
        c.coordonnees = new_coord;
        Point tmp = new Point(new_coord.getX()+(new_coord.getX()-coord.getX()),new_coord.getY()+(new_coord.getY()-coord.getY()));
        if (voisin != null) {
            if (new_coord.getX() >= 4 || new_coord.getY() >= 4 || new_coord.getX()<0 || new_coord.getY()<0) {
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
            if (new_coord.getX() >= 4 || new_coord.getY() >= 4 || new_coord.getX()<0 || new_coord.getY()<0)
            {
                pions.remove(c);
            }
            else
            {
                c.coordonnees = new_coord;
            }
        }
    }

    public ArrayList<Pion> getPions() {
        return pions;
    }

    public void switchPlayer() {
        if(joueurActuel == joueurs[0]) {
            joueurActuel=joueurs[1];
        }
        else {
            joueurActuel=joueurs[0];
        }
        ArrayList<Pion> pionInFocus = pionsFocusJoueur(joueurActuel.getFocus(), joueurActuel);
        if (pionInFocus.size() == 1) {
            // forcer la sélection
            joueurActuel.setPionActuel(pionInFocus.get(0));
        } else {
            joueurActuel.setPionActuel(null);
        }
        joueurActuel.nbActionsRestantes=2;
    }

    public int isWin()
    {
        //-1 si pas de gagnant 1 pour joueur1 et 2 joueur2
        int nb1[] = new int[3];
        int nb2[] = new int[3];
        int last [] = new int[3];
        int result=-2;
        for(int i=0;i<3;i++)
        {
            nb1[i]= pionsFocusJoueur(i,joueurs[0]).size();
            nb2[i]= pionsFocusJoueur(i,joueurs[1]).size();
        }

        for(int i=0;i<3;i++)
        {
            if(nb1[i] != 0 && nb2[i] == 0)
            {
                last[i]=1;
            }
            else
            {
                if(nb1[i] == 0 && nb2[i] != 0)
                {
                    last[i]=2;
                }
                else {
                    last[i]=-1;
                }
            }

            if(result == last[i])
                break;
            result=last[i];
        }
        return result;

    }

    public void selectPion(int l, int c, int epoque) {
        Pion selected = getPion(new Point(l, c), epoque);
        if (selected != null && epoque == joueurActuel.getFocus() && selected.getJoueur() == joueurActuel) {
            joueurActuel.setPionActuel(selected);
        }
    }

    // retourne le nombre de pions présents dans le focus pour le joueur
    public ArrayList<Pion> pionsFocusJoueur(int focus, Joueur j) {
        ArrayList<Pion> liste = new ArrayList<>();
        for (Pion pion : pions) {
            if (pion.getJoueur() == j && pion.getEpoque() == focus) {
                liste.add(pion);
            }
        }
        return liste;
    }

    public boolean peutSelectionnerFocus(int epoque, int j) {
        return j == joueurActuel.getID()
                && epoque != joueurActuel.getFocus()
                && pionsFocusJoueur(epoque, joueurActuel).size() != 0;
    }

    public int getEtape() {
        if (isWin() != -1) {
            System.out.println("Joueur qui a gagné : " + isWin());
            return 4;
        } else {
            if (joueurActuel.getPionActuel() == null) {
                return 1;
            } else {
                if (joueurActuel.getNbActionsRestantes() == 0) {
                    return 3;
                } else {
                    return 2;
                }
            }
        }
    }
    @Override
    public String toString() {
        return "Jeu{" +
                "pions=" + pions +
                ", joueurs=" + Arrays.toString(joueurs) +
                ", joueurActuel=" + joueurActuel +
                '}';
    }


}
