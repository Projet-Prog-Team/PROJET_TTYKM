package Modele;

import Controleur.ControleurMediateur;
import Patterns.Observable;
import Structures.Point;
import Vue.AdaptateurTemps;

import javax.swing.*;
import java.util.ArrayList;

public class DeroulementJeu extends Observable implements Comparable  {
    Joueur joueurActuel;
    PionBasique pionActuel;
    private boolean real;
    int aGagne;
    public ManageFiles MemoryManager;
    private boolean constructionStatue;
    Jeu j;

    public DeroulementJeu(Jeu jeu,boolean t_real,ControleurMediateur controleur) {
        j = jeu;
        init(controleur);
        real=t_real;
    }

    public void init(ControleurMediateur controleur) {
        setJoueurActuel(j.getJoueur(0)); // joueur 1 commence
        ArrayList<PionBasique> pionInFocus = j.pionsFocusJoueur(joueurActuel.getFocus(), joueurActuel);
        if (pionInFocus.size() == 1) {
            setPionActuel(pionInFocus.get(0));
        }
        aGagne = 0;
        if(controleur == null)
        {
            real=false;
        }
        else
        {

            MemoryManager= new ManageFiles(controleur,"./saves/",real);
        }

        constructionStatue = false;
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
    public Boolean getConstructionStatue(){return constructionStatue; }
    public void setStatue(boolean b){constructionStatue = b; }

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
                return "action1";
            case MOVE2 :
                return "action2";
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
    public void switchStatue(){
        this.constructionStatue = !this.constructionStatue;
        miseAJour();
    }

    // ------------------Actions sur le jeu------------------
    public void selectPion(Emplacement e) {
        Pion selected = j.getPion(e);
        if (selected != null && selected instanceof PionBasique && e.getEpoque() == joueurActuel.getFocus() && selected.getJoueur() == joueurActuel) {
            setPionActuel((PionBasique) selected);
        }
        miseAJour();
    }
    public void changerEpoque(int epoque, Coup coup) { // Si possible, va d??placer le pion selectionn?? dans epoque et faire les copies ??ventuelles
        int PionEpoque = getPionActuel().getEpoque();
        if(Math.abs(PionEpoque-epoque)==1) { // Si l'??poque vis??e est accessible +- 1
            PionBasique pionActuel = getPionActuel();
            Emplacement e = new Emplacement(pionActuel.getCoordonnees(), epoque);
            if (j.getPion(e) == null) { // Si l'emplacement est une case libre
                PionBasique tmp0 = pionActuel.copy(pionActuel.getJoueur());
                coup.deplace(pionActuel.getEmplacement().copy(), e.copy());
                pionActuel.setEpoque(epoque);
                if(real)
                    MemoryManager.UpdateLog(tmp0, pionActuel.copy(pionActuel.getJoueur()));
                if (epoque < PionEpoque) { // Si on va dans le pass??, il faut cr??er un clone sur l'emplacement du pion avant d??placement
                    PionBasique tmp;
                    Emplacement emplacement = new Emplacement(pionActuel.getCoordonnees(), PionEpoque);
                    if(joueurActuel.getID()==1) {
                        tmp=new PionBasique(emplacement, joueurActuel, getJeu().NBPIONS/2-joueurActuel.getNbPionsRestants(),false);
                    }
                    else {
                        tmp=new PionBasique(emplacement, joueurActuel,getJeu().NBPIONS-joueurActuel.getNbPionsRestants(),false);
                    }
                    j.getPions().add(tmp);
                    coup.deplace(null, tmp.getEmplacement().copy());
                    if(real) {
                        MemoryManager.UpdateLog(null, tmp);
                    }
                    //ne supprime pas un pion du plateau mais du nombre total encore disponible ?? placer
                    joueurActuel.supprimerPion();
                }
                else {
                    if(real)
                        MemoryManager.UpdateLog(tmp0,pionActuel.copy(pionActuel.getJoueur()));
                }
            }
        }
    }

    public Coup creerStatue(Emplacement e) {
        Coup coup = new Coup();
        boolean casParticulier = false;
        ArrayList<Emplacement> cases = getJeu().casesDispoStatue(pionActuel);
        Statue tmp_statue;
        if (cases.contains(e) && !joueurActuel.isStatuePlaced()) {
            // On cr??e le pion statue sur la case
            int x=joueurActuel.getID()*3;
            tmp_statue=new Statue(e,getJeu().NBPIONS+x+ e.getEpoque(),false, joueurActuel.getID());
            getJeu().getPions().add(tmp_statue);
            if(real) {
                MemoryManager.UpdateLog(null, tmp_statue);
            }
            coup.deplace(null, e.copy());
            for (int i = pionActuel.getEpoque(); i < 2; i++) {
                Emplacement eSuivant = new Emplacement(e.getCoordonnees(), i+1);
                Pion p = getJeu().getPion(eSuivant);
                if (p == null) { // Si case vide dans l'??poque suivante
                    tmp_statue=new Statue(eSuivant,getJeu().NBPIONS+x+ eSuivant.getEpoque(),false, joueurActuel.getID());
                    getJeu().getPions().add(tmp_statue);
                    if(real)
                        MemoryManager.UpdateLog(null,tmp_statue);
                    coup.deplace(null, eSuivant.copy());
                } else {    // Si il y a un pion p
                    Point cActuel = pionActuel.getEmplacement().getCoordonnees();
                    int dL = eSuivant.getCoordonnees().getL() - cActuel.getL(); // Distance entre la case cliqu??e et le pion actuel
                    int dC = eSuivant.getCoordonnees().getC() - cActuel.getC();
                    Point dest;
                    if (p instanceof Statue && p.distancePionBord() == 0) { // Il faut inverser la direction
                        // Bouger les pions dans direction inverse
                        eSuivant = new Emplacement(pionActuel.getCoordonnees(), i+1); // Emplacement devant la statue
                        if ((i+1) == 1) {
                            casParticulier = true;
                        }
                        dest = new Point(eSuivant.getCoordonnees().getL() - dL, eSuivant.getCoordonnees().getC() - dC);
                        p = getJeu().getPion(eSuivant);
                    } else {                  // On bouge tout puis on place
                        dest = new Point(eSuivant.getCoordonnees().getL() + dL, eSuivant.getCoordonnees().getC() + dC);
                    }
                    if (p != null) {
                        move(p, dest, coup);
                    }
                    tmp_statue=new Statue(eSuivant, getJeu().NBPIONS+x+ eSuivant.getEpoque(),false, joueurActuel.getID());
                    getJeu().getPions().add(new Statue(eSuivant, getJeu().NBPIONS+x+ eSuivant.getEpoque(),false, joueurActuel.getID()));
                    if(real)
                        MemoryManager.UpdateLog(null,tmp_statue);
                    coup.deplace(null, eSuivant.copy());
                }
            }
            if (casParticulier) {   // Si le pion du pr??sent a ??t?? pouss?? dans le sens inverse de la cr??ation
                Pion statueFutur = null, statuePresent = null;
                for (Pion pion : getJeu().getPions()) {
                    if (pion instanceof Statue && ((Statue) pion).getColor() == joueurActuel.getID()) {
                        if (pion.getEpoque() == 2) {
                            statueFutur = pion;
                        } else if (pion.getEpoque() == 1) {
                            statuePresent = pion;
                        }
                    }
                }
                if (!statueFutur.getCoordonnees().equals(statuePresent.getCoordonnees())) {
                    move(statueFutur, statuePresent.getCoordonnees(), coup);
                }
            }
            constructionStatue = false;
            joueurActuel.nbActionsRestantes--;
            joueurActuel.setStatuePlaced(true);

            if (getJeu().joueurAGagne(joueurActuel)) {
                aGagne = joueurActuel.getID();
            } else if (getJeu().joueurAGagne(getJeu().joueurs[(joueurActuel.getID()) % 2]) && joueurActuel.nbActionsRestantes == 0) {
                aGagne = j.joueurs[(joueurActuel.getID()) % 2].getID();
            }
            miseAJour();
        }
        return coup;
    }

    public Coup jouerCoup(Emplacement e, boolean real) {
        //System.out.println(getPionActuel() + " etape + " + getEtape() + "joueur act" + joueurActuel);
        Coup coup = new Coup();
        ArrayList<Emplacement> cases = getJeu().casesDispo(joueurActuel,pionActuel);
        if(real && MemoryManager.getControlleur().state.getPauseIA() && MemoryManager.getControlleur().joueurs[joueurActuel.getID()-1]==0)
            MemoryManager.getControlleur().SetPauseIA(false);

        if (cases.contains(e)) {    // Si l'emplacement en param??tre est un emplacement sur lequel on peut jouer un coup

            if(real) {
                MemoryManager.AddLog(null);
            }
            if (e.getEpoque() != getPionActuel().getEpoque()) {
                joueurActuel.nbActionsRestantes--;
                changerEpoque(e.getEpoque(),coup);
            } else {
                move(getPionActuel(), e.getCoordonnees(), coup);
                joueurActuel.nbActionsRestantes--;
                if (getJeu().getPion(getPionActuel().getEmplacement()) == null || getJeu().getPion(getPionActuel().getEmplacement()) instanceof Statue) {   // Si le joueur a tu?? son propre pion
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
        return coup;
    }

    public boolean move (Pion pion, Point dest, Coup coup)  {
        Pion pionDestination = j.getPion(new Emplacement(dest.copy(), pion.getEpoque()));
        boolean pousse = false;
        int dL = dest.getL() - pion.getCoordonnees().getL();
        int dC = dest.getC() - pion.getCoordonnees().getC();
        Point pointSD = new Point(dest.getL() + dL, dest.getC() + dC); // Derri??re le pionDestination"
        //System.out.println("pionActuel :" + pion + "\n" + "dest : " + dest + "\n" + "pionDestination : " + pionDestination +  "\npointSD : " + pointSD + "\n-------------------------\n");
        if (pion instanceof PionBasique) {
            // Pull

            Point casPull = null;
            Pion pionDerrierePion = j.getPion(new Emplacement(new Point(pion.getCoordonnees().getL() - dL, pion.getCoordonnees().getC() - dC), pion.getEpoque()));
            if (pionDerrierePion instanceof Statue && !constructionStatue && pion.equals(getPionActuel())) {
                coup.deplace(pionDerrierePion.getEmplacement().copy(), pion.getEmplacement().copy());
                casPull = pion.getCoordonnees().copy();
            }

            if (pionDestination == null) {
                // C'est vide, on peut d??placer le pion dans cette case
                // juste set les coordonn??es de pion & return true
                if (dest.getL() >= 4 || dest.getC() >= 4 || dest.getL()<0 || dest.getC()<0) {
                    j.getPions().remove(pion);
                    if(real)
                        MemoryManager.UpdateLog(pion,null);
                    pousse = false;
                } else {
                    coup.deplace(pion.getEmplacement().copy(), new Emplacement(dest, pion.getEpoque()));
                    pion.setCoordonnees(dest);
                    if(real)
                         MemoryManager.UpdateLog(null,pion);
                    pousse = true;
                }
            } else if (pionDestination instanceof PionBasique) {
                if (pion.getJoueur() == pionDestination.getJoueur()) {
                    // Remove les deux & return true
                    coup.deplace(pion.getEmplacement().copy(), new Emplacement(dest, pion.getEpoque()));
                    j.getPions().remove(pion);
                    j.getPions().remove(pionDestination);
                    if(real) {
                        MemoryManager.UpdateLog(pion, null);
                        MemoryManager.UpdateLog(pionDestination, null);
                    }
                    pousse = true;
                } else {
                    if (pointSD.getL() >= 4 || pointSD.getC() >= 4 || pointSD.getL()<0 || pointSD.getC()<0) {
                        // Si la case derri??re la destination est en dehors du plateau, ??a veut dire qu'on pousse le pion dans le vide
                        // Supprimer pionDestination
                        // Puis d??placer pion & return true
                        coup.deplace(pion.getEmplacement().copy(), new Emplacement(dest, pion.getEpoque()));
                        j.getPions().remove(pionDestination);
                        pion.setCoordonnees(dest);
                        if(real)
                        {
                            MemoryManager.UpdateLog(pionDestination,null);
                            MemoryManager.UpdateLog(null,pion);
                        }

                        pousse = true;
                    } else {
                        // return appel r??cursif sur pionDestination avec pointSD
                        coup.deplace(pion.getEmplacement().copy(), new Emplacement(dest, pion.getEpoque()));
                        pion.setCoordonnees(dest);
                        if(real)
                            MemoryManager.UpdateLog(null,pion);
                        pousse = move(pionDestination, pointSD, coup);
                    }
                }
            } else if (pionDestination instanceof Statue) {
                if (pointSD.getL() >= 4 || pointSD.getC() >= 4 || pointSD.getL()<0 || pointSD.getC()<0) {
                    j.getPions().remove(pion);
                    if(real)
                        MemoryManager.UpdateLog(pion,null);
                    pousse =  true;
                } else {
                    if (move(pionDestination, pointSD, coup) == true) {
                        // d??placer pion & return true
                        coup.deplace(pion.getEmplacement().copy(), new Emplacement(dest, pion.getEpoque()));
                        pion.setCoordonnees(dest);
                        if(real)
                            MemoryManager.UpdateLog(null,pion);
                        pousse = true;
                    } else {
                        j.getPions().remove(pion);
                        if(real)
                            MemoryManager.UpdateLog(pion,null);
                        pousse = true;
                    }
                }
            }
            if (casPull != null) { // Si pull de statue
                deplacerStatue(pionDerrierePion, casPull, coup);
            }
        } else { // Si pion ?? d??placer sur dest est une statue
            if (pionDestination == null) {
                // On peut d??placer la statue dans cette case : set les coo & return true
                if (dest.getL() >= 4 || dest.getC() >= 4 || dest.getL()<0 || dest.getC()<0) {
                    pousse = false;
                } else {
                    coup.deplace(pion.getEmplacement().copy(), new Emplacement(dest, pion.getEpoque()));
                    deplacerStatue(pion, dest, coup);
                    pousse = true;
                }
            } else if (pionDestination instanceof PionBasique) {
                if (pointSD.getL() >= 4 || pointSD.getC() >= 4 || pointSD.getL()<0 || pointSD.getC()<0) {
                    // Si la case derri??re la destination est en dehors du plateau, ??a veut dire qu'on pousse le pion dans le vide
                    // Supprimer pionDestination
                    // Puis d??placer statue & return true
                    coup.deplace(pion.getEmplacement().copy(), new Emplacement(dest, pion.getEpoque()));
                    j.getPions().remove(pionDestination);
                    deplacerStatue(pion, dest, coup);
                    if(real) {
                        MemoryManager.UpdateLog(pionDestination, null);
                    }
                    pousse = true;
                } else {
                    // D??placer statue et return appel r??cursif sur pionDestination avec pointSD
                    coup.deplace(pion.getEmplacement().copy(), new Emplacement(dest, pion.getEpoque()));
                    deplacerStatue(pion, dest, coup);
                    pousse = move(pionDestination, pointSD, coup);
                }
            } else if (pionDestination instanceof Statue) {
                if (pointSD.getL() >= 4 || pointSD.getC() >= 4 || pointSD.getL()<0 || pointSD.getC()<0) {
                    pousse = false;
                } else {
                    // bool = appel recursif sur pionDestination et pointSD
                    // si bool = true : D??placer statue & return true
                    // sinon : return false
                    boolean poussable = move(pionDestination, pointSD, coup);
                    if (poussable) {
                        coup.deplace(pion.getEmplacement().copy(), new Emplacement(dest, pion.getEpoque()));
                        deplacerStatue(pion, dest, coup);
                        pousse = true;
                    } else {
                        pousse = false;
                    }
                }
            }
        }
        return pousse;
    }

    private void deplacerStatue(Pion pion, Point dest, Coup coup) {
        int dL = dest.getL() - pion.getCoordonnees().getL();
        int dC = dest.getC() - pion.getCoordonnees().getC();
        pion.setCoordonnees(dest);
        if(real)
            MemoryManager.UpdateLog(null,pion);
        ArrayList<Pion> pions = (ArrayList<Pion>) getJeu().getPions().clone();
        for (Pion pion1 : pions) {
            if (pion1 instanceof Statue && ((Statue) pion1).getColor() == ((Statue) pion).getColor() && pion1.getEpoque()==(pion.getEpoque()+1)) {
                int L = pion1.getCoordonnees().getL() + dL;
                int C = pion1.getCoordonnees().getC() + dC;
                Point newDest = new Point(L, C);
                move(pion1, newDest, coup);
            }
        }
    }

    public void switchPlayer() {
        constructionStatue = false;
        // Inversion du joueurActuel
        if(joueurActuel == getJeu().joueurs[0]) {
            joueurActuel=getJeu().joueurs[1];
        }
        else {
            joueurActuel=getJeu().joueurs[0];
        }

        // On initialise ses param??tres
        ArrayList<PionBasique> pionInFocus = getJeu().pionsFocusJoueur(joueurActuel.getFocus(), joueurActuel);
        joueurActuel.nbActionsRestantes=2;
        if (pionInFocus.size() == 1) {
            // forcer la s??lection
            setPionActuel(pionInFocus.get(0));
            if(real)
                MemoryManager.AddLog(ETAT.IDLE);
        } else if (pionInFocus.size() == 0){
            setPionActuel(new PionBasique(new Emplacement(new Point(-1, -1), joueurActuel.getFocus()), joueurActuel));
            joueurActuel.nbActionsRestantes=0;
            if(real)
                MemoryManager.AddLog(ETAT.MOVE2);
        } else {
            setPionActuel(null);
            if(real)
                MemoryManager.AddLog(ETAT.SELECT);
        }
        miseAJour();
    }

    //------------------------------------
    public Preview getPreview(Emplacement e) {
        DeroulementJeu djeu = copy();
        Coup cp;
        if(constructionStatue){
            cp = djeu.creerStatue(e);
        }else{
            cp = djeu.jouerCoup(e, false);
        }
        return new Preview(cp, djeu.getJeu().getPions());
    }

    //------------------------------------

    public DeroulementJeu copy() {
        Jeu jCopy = getJeu().copy();
        DeroulementJeu djeu = new DeroulementJeu(jCopy,false,MemoryManager.getControlleur());
        djeu.joueurActuel = jCopy.getJoueur(getJoueurActuel().getID() - 1);
        djeu.aGagne = aGagne;
        djeu.setStatue(constructionStatue);
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
                "joueurActuel=" + joueurActuel + "\n" +
                ", pionActuel=" + pionActuel + "\n" +
                ", aGagne=" + aGagne + "\n" +
                ", j=" + j + "\n" +
                '}';
    }

    @Override
    public int compareTo(Object o) {
        return 0;
    }


}
