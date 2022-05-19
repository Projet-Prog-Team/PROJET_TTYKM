package Modele;

import Patterns.Observable;
import Structures.Point;

import java.util.ArrayList;

public class DeroulementJeu extends Observable implements Comparable  {
    Joueur joueurActuel;
    PionBasique pionActuel;
    private boolean real;
    int aGagne;
    public ManageFiles MemoryManager;
    Jeu j;

    public DeroulementJeu(Jeu jeu,boolean t_real) {
        j = jeu;
        init();
        real=t_real;
    }

    public void init() {
        setJoueurActuel(j.getJoueur(0)); // joueur 1 commence
        ArrayList<PionBasique> pionInFocus = j.pionsFocusJoueur(joueurActuel.getFocus(), joueurActuel);
        if (pionInFocus.size() == 1) {
            setPionActuel(pionInFocus.get(0));
        }
        aGagne = 0;
        MemoryManager= new ManageFiles(this,"/Saves/");
        miseAJour();

    }

    // ------------------Getters & setters------------------
    public Joueur getJoueurActuel() {
        return joueurActuel;
    }
    public void setJoueurActuel(Joueur j) {
        joueurActuel = j;
    }
    public PionBasique getPionActuel() {
        return pionActuel;
    }
    public void setPionActuel(PionBasique pionActuel) {
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
    public void selectPion(Emplacement e) {
        Pion selected = j.getPion(e);
        if (selected != null && selected instanceof PionBasique && e.getEpoque() == joueurActuel.getFocus() && selected.getJoueur() == joueurActuel) {
            setPionActuel((PionBasique) selected);
        }
        miseAJour();
    }
    public void changerEpoque(int epoque) { // Si possible, va déplacer le pion selectionné dans epoque et faire les copies éventuelles
        int PionEpoque = getPionActuel().getEpoque();
        if(Math.abs(PionEpoque-epoque)==1) { // Si l'époque visée est accessible +- 1
            PionBasique pionActuel = getPionActuel();
            Emplacement e = new Emplacement(pionActuel.getCoordonnees(), epoque);
            if (j.getPion(e) == null) { // Si l'emplacement est une case libre
                PionBasique tmp0 = pionActuel.copy(pionActuel.getJoueur());
                pionActuel.setEpoque(epoque);
                if(real)
                    MemoryManager.UpdateLog(tmp0, pionActuel.copy(pionActuel.getJoueur()));
                if (epoque < PionEpoque) { // Si on va dans le passé, il faut créer un clone sur l'emplacement du pion avant déplacement
                    PionBasique tmp;
                    Emplacement emplacement = new Emplacement(pionActuel.getCoordonnees(), PionEpoque);
                    if(joueurActuel.getID()==1) {
                        tmp=new PionBasique(emplacement, joueurActuel, getJeu().NBPIONS/2-joueurActuel.getNbPionsRestants(),false);
                    }
                    else {
                        tmp=new PionBasique(emplacement, joueurActuel,getJeu().NBPIONS-joueurActuel.getNbPionsRestants(),false);
                    }
                    j.getPions().add(tmp);
                    if(real) {
                        MemoryManager.move=false;
                        MemoryManager.UpdateLog(null, tmp);
                    }
                    //ne supprime pas un pion du plateau mais du nombre total encore disponible à placer
                    joueurActuel.supprimerPion();
                }
                else {
                    if(real)
                        MemoryManager.UpdateLog(tmp0,pionActuel.copy(pionActuel.getJoueur()));
                }
            }
        }
    }

    public void jouerCoup(Emplacement e, boolean real) {
        PionBasique clic;
        if(joueurActuel.getID()==1) {
            clic = new PionBasique(e, joueurActuel,getJeu().NBPIONS/2-joueurActuel.getNbActionsRestantes(),false);
        }
        else {
            clic = new PionBasique(e, joueurActuel,getJeu().NBPIONS-joueurActuel.getNbActionsRestantes(),false);
        }
        ArrayList<Emplacement> cases = getJeu().casesDispo(joueurActuel,pionActuel);
        if (cases.contains(clic.getEmplacement())) {    // Si l'emplacement en paramètre est un emplacement sur lequel on peut jouer un coup
            if(real) {
                MemoryManager.AddLog(null);
            }
            if (e.getEpoque() != getPionActuel().getEpoque()) {
                joueurActuel.nbActionsRestantes--;
                changerEpoque(e.getEpoque());
            } else {
                move(getPionActuel(), e.getCoordonnees());
                joueurActuel.nbActionsRestantes--;
                if (getJeu().getPion(getPionActuel().getEmplacement()) == null) {   // Si le joueur a tué son propre pion
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

    public void move(Pion pion, Point dest) {
        Point source = new Point(pion.getCoordonnees().getL(),pion.getCoordonnees().getC());
        Pion voisin = j.getPion(new Emplacement(dest,pion.getEpoque()));
        pion.setCoordonnees(dest);
        Point tmp = new Point(dest.getL()+(dest.getL()-source.getL()),dest.getC()+(dest.getC()-source.getC()));
        if (voisin != null) {
            if (dest.getL() >= 4 || dest.getC() >= 4 || dest.getL()<0 || dest.getC()<0) {
                if(real)
                    MemoryManager.UpdateLog(pion,null);
                j.getPions().remove(pion);
            } else {
                if(voisin.getJoueur() == pion.getJoueur()) {
                    if(real)
                    {
                        MemoryManager.UpdateLog(pion, null);
                        MemoryManager.UpdateLog(voisin, null);
                    }
                    j.getPions().remove(pion);
                    j.getPions().remove(voisin);
                }
                else {
                    move(voisin, tmp);
                }
            }
        }
        else {
            if (dest.getL() >= 4 || dest.getC() >= 4 || dest.getL()<0 || dest.getC()<0) {
                if(real)
                    MemoryManager.UpdateLog(pion,null);
                j.getPions().remove(pion);
            }
            else {
                Pion tmp2 = pion.copy(pion.getJoueur());
                pion.setCoordonnees(dest);
                if(real)
                    MemoryManager.UpdateLog(tmp2,pion);
            }
        }
    }

    public void switchPlayer() {
        // Inversion du joueurActuel
        if(joueurActuel == getJeu().joueurs[0]) {
            joueurActuel=getJeu().joueurs[1];
        }
        else {
            joueurActuel=getJeu().joueurs[0];
        }

        // On initialise ses paramètres
        ArrayList<PionBasique> pionInFocus = getJeu().pionsFocusJoueur(joueurActuel.getFocus(), joueurActuel);
        joueurActuel.nbActionsRestantes=2;
        if (pionInFocus.size() == 1) {
            // forcer la sélection
            setPionActuel(pionInFocus.get(0));
            MemoryManager.AddLog(ETAT.IDLE);
        } else if (pionInFocus.size() == 0){
            setPionActuel(new PionBasique(new Emplacement(new Point(-1, -1), joueurActuel.getFocus()), joueurActuel));
            joueurActuel.nbActionsRestantes=0;
        } else {
            setPionActuel(null);
            MemoryManager.AddLog(ETAT.SELECT);
        }
        miseAJour();
    }

    //------------------------------------
    public ArrayList<Pion> getPreview(Emplacement e) {
        DeroulementJeu djeu = copy();
        djeu.jouerCoup(e, real);
        ArrayList<Pion> preview = djeu.getJeu().getPions();
        return preview;
    }

    //------------------------------------

    public DeroulementJeu copy() {
        Jeu jCopy = getJeu().copy();
        DeroulementJeu djeu = new DeroulementJeu(jCopy,false);
        djeu.joueurActuel = jCopy.getJoueur(getJoueurActuel().getID() - 1);
        djeu.aGagne = aGagne;
        for (Pion pion : jCopy.getPions()) {
            if (pion instanceof PionBasique) {
                PionBasique pb = (PionBasique) pion;
                if (pb.equals(pionActuel)) {
                    djeu.pionActuel = pb;
                }
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
