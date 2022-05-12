package Modele;

import Patterns.Observable;
import Modele.ManageFiles;
import Structures.Point;
import java.util.ArrayList;
import java.util.Arrays;


public class Jeu extends Observable {
    private final ArrayList<Pion> pions = new ArrayList<>();
    public Joueur[] joueurs = new Joueur[2];
    Joueur joueurActuel;
    public Pion pionActuel;
    public ManageFiles MemoryManager;
    int aGagne;
    public final int NBPIONS = 12;

    public Jeu() {
        init();
       
    }

    public void init() {
        joueurs[0] = new Joueur(1,NBPIONS/2);
        joueurs[1] = new Joueur(2,NBPIONS/2);
        for(int i = 0; i < 3; i++) {
            Pion p1 = new Pion(new Point(0, 0), i, joueurs[0]);
            pions.add(p1);
            Pion p2 = new Pion(new Point(3, 3), i, joueurs[1]);
            pions.add(p2);
            joueurs[0].nbPionsRestants--;
            joueurs[1].nbPionsRestants--;
        }
        joueurs[0].setFocus(0);
        joueurs[1].setFocus(2);
        pionActuel= getPion(new Point(0, 0), 0);
        pionActuel=getPion(new Point(3, 3), 2);
        aGagne = 0;
        MemoryManager=new ManageFiles(this, "C:/Users/theo/Desktop/Code/PROJET_TTYKM/save/");
        ajouteObservateur(MemoryManager);

    }

    public void jouerCoup(int l, int c, int epoque) {


        Pion clic=new Pion(new Point(l, c), epoque, joueurActuel);

        ArrayList<Pion> cases = casesDispo();

        if (cases.contains(clic)) {    // Si coup jouable
            joueurActuel.nbActionsRestantes--;
            if (epoque != pionActuel.getEpoque()) {
                changerEpoque(epoque);
            } else {
                move(pionActuel, new Point(l, c));
                if (getPion(pionActuel.getCoordonnees(), joueurActuel.getFocus()) == null) {   // Si le joueur a tué son propre pion
                    joueurActuel.nbActionsRestantes = 0;
                }
            }
            if (joueurAGagne(joueurActuel)) {
                aGagne = joueurActuel.getID();
            } else if (joueurAGagne(joueurs[(joueurActuel.getID()) % 2]) && joueurActuel.nbActionsRestantes == 0) {
                aGagne = joueurs[(joueurActuel.getID()) % 2].getID();
            }
        }
        System.out.println("nbActionsRestantes  : " + joueurActuel.getNbActionsRestantes());
        System.out.println("nbPionsRestants  : " + joueurActuel.nbPionsRestants);
    }

    public Pion getPionActuel() { return pionActuel; }
    public void setPionActuel(Pion p) {this.pionActuel = p;}

    boolean joueurAGagne(Joueur joueur) {
        Joueur adversaire = joueurs[(joueur.getID()) % 2];
        int nbPlateauxVides = 0;
        for (int i = 0; i < 3; i++) {
            if (pionsFocusJoueur(i, adversaire).size() == 0) {
                nbPlateauxVides++;
            }
        }
        return nbPlateauxVides >= 2;
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
        joueurActuel = j;
    }

    public void changerEpoque(int epoque) { // Si possible, va déplacer le pion selectionné dans epoque et faire les copies éventuelles
        int PionEpoque = pionActuel.getEpoque();
        if(Math.abs(PionEpoque-epoque)==1) { // Si l'époque visée est accessible +- 1
            Pion t_pionActuel = pionActuel;
            if (getPion(t_pionActuel.coordonnees, epoque) == null) {
                t_pionActuel.epoque = epoque;
                if (epoque < PionEpoque) { // Si l'époque visée est plus loin (dans le futur) que l'époque du pion.
                    if (joueurActuel.peutSupprimerPion()) {
                            pions.add(new Pion(t_pionActuel.getCoordonnees(), PionEpoque, joueurActuel));

                        //ne supprime pas un pion du plateau mais du nombre total encore disponible à placer
                        joueurActuel.supprimerPion();
                    } else {
                        t_pionActuel.epoque = PionEpoque;
                    }
                }
            }
        }
    }

    public ArrayList<Pion> casesDispo() {   // Retourne une liste des pions jouables pour le joueur cou
        Pion t_pionActuel = pionActuel;
        ArrayList<Pion> casesDisponibles = new ArrayList<>();
        Point coo = t_pionActuel.getCoordonnees();
        int epoque = t_pionActuel.getEpoque();
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

    public void SetPions(Pion[] t_pions)
    {
        pions.clear();
        for(Pion t_pion : t_pions)
        {
            if(t_pion!=null)
                pions.add(t_pion);
        }
    }

    public void switchPlayer() {
        if(joueurActuel.getID() == 1) {
            joueurActuel=joueurs[1];
        }
        else {
            joueurActuel=joueurs[0];
        }
        ArrayList<Pion> pionInFocus = pionsFocusJoueur(joueurActuel.getFocus(), joueurActuel);
        if (pionInFocus.size() == 1) {
            // forcer la sélection
            pionActuel=pionInFocus.get(0);
        } else {
            pionActuel=null;
        }
        joueurActuel.nbActionsRestantes=2;
    }

    public void selectPion(int l, int c, int epoque) {
        Pion selected = getPion(new Point(l, c), epoque);
        if (selected != null && epoque == joueurActuel.getFocus() && selected.getJoueur() == joueurActuel) {
            pionActuel=selected;
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
        if (aGagne != 0) {
            System.out.println("Joueur qui a gagné : " + aGagne);
            return 4;
        } else {
            if (pionActuel == null) {
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
