package Modele;

import Controleur.ControleurMediateur;
import Patterns.Observable;
import Structures.Point;

import java.util.ArrayList;

public class DeroulementJeu extends Observable implements Comparable  {
    Joueur joueurActuel;
    Pion pionActuel;
    private boolean real;
    int aGagne;
    public ManageFiles MemoryManager;
    Jeu j;

    public DeroulementJeu(Jeu jeu,boolean t_real,ControleurMediateur controleur) {
        j = jeu;
        init(controleur);
        real=t_real;
    }

    public void init(ControleurMediateur controleur) {
        setJoueurActuel(j.getJoueur(0)); // joueur 1 commence
        ArrayList<Pion> pionInFocus = j.pionsFocusJoueur(joueurActuel.getFocus(), joueurActuel);
        if (pionInFocus.size() == 1) {
            setPionActuel(pionInFocus.get(0));
        }
        aGagne = 0;
        //C:/Users/Theo/Desktop/Code/PROJET_TTYKM/res/Saves
        if(controleur!=null)
            MemoryManager= new ManageFiles(controleur,"./saves/");
        miseAJour();

    }

    // ------------------Getters & setters------------------
    public Joueur getJoueurActuel() {
        return joueurActuel;
    }
    public void setJoueurActuel(Joueur j) {
        joueurActuel = j;
    }
    public Pion getPionActuel() {
        return pionActuel;
    }
    public void setPionActuel(Pion pionActuel) {
        this.pionActuel = pionActuel;
    }
    public Jeu getJeu(){ return j; }

    // ------------------Etat du jeu------------------
    public boolean estTermine() {
        return (aGagne != 0);
    }
    public boolean peutSelectionnerFocus(int epoque, int id) {
        return id == joueurActuel.getID()
                && epoque != joueurActuel.getFocus()
                && j.pionsFocusJoueur(epoque, joueurActuel).size() != 0;
    }
    public ETAT getEtape() {
        if (aGagne != 0) {
            return ETAT.END;
        } else {
            if (getPionActuel() == null) {
                return ETAT.SELECT;
            } else {
                switch(joueurActuel.getNbActionsRestantes())
                {
                    case 0 :
                        return ETAT.FOCUS;
                    case 1 :
                        return ETAT.MOVE2;
                    case 2:
                        return ETAT.MOVE1;
                }
            }
        }
        return ETAT.UNDEFINED;
    }
    public String getState(){
        switch(getEtape())
        {
            case SELECT:
                return "selection";
            case MOVE1 :
                return "action2";
            case MOVE2 :
                return "action1";
            case FOCUS:
                return "focus";
            case END:
                if(aGagne==1){
                    return "j1gagne";
                }else{
                    return "j2gagne";
                }
            default :
                return "";
        }
    }

    // ------------------Actions sur le jeu------------------
    public void selectPion(int l, int c, int epoque) {
        Pion selected = j.getPion(new Point(l, c), epoque);
        if (selected != null && epoque == joueurActuel.getFocus() && selected.getJoueur() == joueurActuel) {
            setPionActuel(selected);
        }
        miseAJour();
    }
    public void changerEpoque(int epoque) { // Si possible, va déplacer le pion selectionné dans epoque et faire les copies éventuelles
        int PionEpoque = getPionActuel().getEpoque();
        if(Math.abs(PionEpoque-epoque)==1) { // Si l'époque visée est accessible +- 1
            Pion pionActuel = getPionActuel();
            if (j.getPion(pionActuel.getCoordonnees(), epoque) == null) {
                Pion tmp0 = pionActuel.copy(pionActuel.getJoueur());
                pionActuel.SetEpoque(epoque);
                if(real)
                    MemoryManager.UpdateLog(tmp0, pionActuel.copy(pionActuel.getJoueur()));
                if (epoque < PionEpoque) { // Si l'époque visée est plus loin (dans le futur) que l'époque du pion.
                    Pion tmp;
                    if(joueurActuel.getID()==1)
                    {
                        tmp=new Pion(pionActuel.getCoordonnees(), PionEpoque, joueurActuel, getJeu().NBPIONS/2-joueurActuel.getNbPionsRestants(),false);
                    }
                    else
                    {
                        tmp=new Pion(pionActuel.getCoordonnees(), PionEpoque, joueurActuel,getJeu().NBPIONS-joueurActuel.getNbPionsRestants(),false);
                    }
                    j.getPions().add(tmp);
                    if(real) {
                        MemoryManager.move=false;
                        MemoryManager.UpdateLog(null, tmp);

                    }
                    //ne supprime pas un pion du plateau mais du nombre total encore disponible à placer
                    joueurActuel.supprimerPion();
                }
                else
                {
                    if(real)
                        MemoryManager.UpdateLog(tmp0,pionActuel.copy(pionActuel.getJoueur()));
                }

            }
        }
    }

    public void jouerCoup(int l, int c, int epoque,boolean real) {
        Pion clic;
        if(joueurActuel.getID()==1)
        {
            clic = new Pion(new Point(l, c), epoque, joueurActuel,getJeu().NBPIONS/2-joueurActuel.getNbActionsRestantes(),false);
        }
        else
        {
            clic = new Pion(new Point(l, c), epoque, joueurActuel,getJeu().NBPIONS-joueurActuel.getNbActionsRestantes(),false);
        }


        ArrayList<Pion> cases = getJeu().casesDispo(joueurActuel,pionActuel);

        if (cases.contains(clic)) {    // Si coup jouable

            if(real)
            {
                MemoryManager.AddLog(null);
            }

            if (epoque != getPionActuel().getEpoque()) {
                joueurActuel.nbActionsRestantes--;
                changerEpoque(epoque);
            } else {
                move(getPionActuel(), new Point(l, c));
                joueurActuel.nbActionsRestantes--;
                if (getJeu().getPion(getPionActuel().getCoordonnees(), joueurActuel.getFocus()) == null) {   // Si le joueur a tué son propre pion
                    joueurActuel.nbActionsRestantes = 0;
                }
            }
            if (getJeu().joueurAGagne(joueurActuel)) {
                aGagne = joueurActuel.getID();
            } else if (getJeu().joueurAGagne(getJeu().joueurs[(joueurActuel.getID()) % 2]) && joueurActuel.nbActionsRestantes == 0) {
                aGagne = j.joueurs[(joueurActuel.getID()) % 2].getID();
            }
        }
        miseAJour();
    }

    public void move(Pion c, Point new_coord) {
        Point coord = new Point(c.getCoordonnees().getL(),c.getCoordonnees().getC());
        Pion voisin = j.getPion(new_coord,c.getEpoque());
        c.SetCoordonnees(new_coord);
        Point tmp = new Point(new_coord.getL()+(new_coord.getL()-coord.getL()),new_coord.getC()+(new_coord.getC()-coord.getC()));
        if (voisin != null) {
            if (new_coord.getL() >= 4 || new_coord.getC() >= 4 || new_coord.getL()<0 || new_coord.getC()<0) {
                if(real)
                {
                    MemoryManager.move=false;
                    MemoryManager.UpdateLog(c,null);
                }
                j.getPions().remove(c);
            } else {
                if(voisin.getJoueur() == c.getJoueur()) {
                    if(real)
                    {
                        MemoryManager.move=false;
                        MemoryManager.UpdateLog(c, null);
                        MemoryManager.move=false;
                        MemoryManager.UpdateLog(voisin, null);
                    }
                    j.getPions().remove(c);
                    j.getPions().remove(voisin);
                }
                else {
                    MemoryManager.move=false;
                    MemoryManager.UpdateLog(null,c.copy(c.getJoueur()));
                    move(voisin, tmp);
                }
            }
        }
        else {
            if (new_coord.getL() >= 4 || new_coord.getC() >= 4 || new_coord.getL()<0 || new_coord.getC()<0) {
                if(real)
                {
                    MemoryManager.move=false;
                    MemoryManager.UpdateLog(c,null);
                }

                j.getPions().remove(c);
            }
            else {
                Pion tmp2 = c.copy(c.getJoueur());
                c.SetCoordonnees(new_coord);
                if(real) {
                    MemoryManager.move=false;
                    MemoryManager.UpdateLog(tmp2, c);
                }
            }
        }
    }

    public void switchPlayer() {
        if(joueurActuel == getJeu().joueurs[0]) {
            joueurActuel=getJeu().joueurs[1];
        }
        else {
            joueurActuel=getJeu().joueurs[0];
        }
        ArrayList<Pion> pionInFocus = getJeu().pionsFocusJoueur(joueurActuel.getFocus(), joueurActuel);
        joueurActuel.nbActionsRestantes=2;
        if (pionInFocus.size() == 1) {
            // forcer la sélection
            setPionActuel(pionInFocus.get(0));
            MemoryManager.AddLog(ETAT.IDLE);
        } else if (pionInFocus.size() == 0){
            setPionActuel(new Pion(new Point(-1, -1), joueurActuel.getFocus(), joueurActuel));
            joueurActuel.nbActionsRestantes=0;
            MemoryManager.AddLog(ETAT.MOVE2);
        } else {
            setPionActuel(null);
            MemoryManager.AddLog(ETAT.SELECT);
        }
        miseAJour();
    }

    //------------------------------------
    public ArrayList<Pion> getPreview(int l, int c, int epoque) {
        DeroulementJeu djeu = copy();
        djeu.jouerCoup(l, c, epoque,real);
        ArrayList<Pion> preview = djeu.getJeu().getPions();
        return preview;
    }

    //------------------------------------

    public DeroulementJeu copy() {
        Jeu jCopy = getJeu().copy();
        DeroulementJeu djeu = new DeroulementJeu(jCopy,false,MemoryManager.getControlleur());
        djeu.joueurActuel = jCopy.getJoueur(getJoueurActuel().getID() - 1);
        djeu.aGagne = aGagne;
        for (Pion pion : jCopy.getPions()) {
            if (pion.equals(pionActuel)) {
                djeu.pionActuel = pion;
            }
        }
        return djeu;
    }

    @Override
    public String toString() {
        return "DeroulementJeu{" +
                "joueurActuel=" + joueurActuel +
                ", pionActuel=" + pionActuel +
                ", aGagne=" + aGagne +
                ", j=" + j +
                '}';
    }

    @Override
    public int compareTo(Object o) {
        return 0;
    }


}
