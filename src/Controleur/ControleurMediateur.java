package Controleur;

import Modele.CalculJeu;
import Modele.DeroulementJeu;
import Modele.Emplacement;
import Modele.Pion;
import Modele.ETAT;
import Modele.Coup;
import Modele.Preview;
import Structures.Point;
import Structures.Couple;
import Vue.*;

import javax.swing.*;
import java.util.ArrayList;

public class ControleurMediateur implements CollecteurEvenements {

    public DeroulementJeu dj;
    IA joueur1, joueur2, suggestion;
    public int [] joueurs;
    String difficulty1 = "facile", difficulty2 = "facile";
    public IHMState state;
    Timer t;
    int speed;
    ArrayList<Animation> animations;
    InterfaceUtilisateur inter;
    Animation mouvement;
    Animation previewAnim;
    Emplacement previewEmp;
    Boolean animationsActives=true;

    String heuristique = "Heuristique";

    public ControleurMediateur (DeroulementJeu djeu, int temps, IHMState state) {
        dj = djeu;
        this.state = state;
        suggestion = IA.nouvelle(new CalculJeu(dj), "difficile", heuristique);
        speed = temps;
        init();
    }

    public void init() {
        t = new Timer(speed, new AdaptateurTemps(this));
        t.start();
        animations = new ArrayList<>();
        mouvement = null;
        previewAnim = null;
        previewEmp = null;
        joueurs = new int[2];
        joueurs[0] = 0;
        joueurs[1] = 0;
        dj.init(this);

        state.initPreview();
        state.setIA1(joueurs[0]==1);
        state.setIA2(joueurs[1]==1);
        state.setDifficultyIA1(difficulty1);
        state.setDifficultyIA2(difficulty2);
        state.setPauseIA(false);
        activerIA(1, difficulty2, heuristique);
    }

    @Override
    public void fixerInterfaceUtilisateur(InterfaceUtilisateur i) {
        inter = i;
    }

    // Clique sur une case
    @Override
    public void clicSouris(int l, int c, int epoque) {
        Emplacement e = new Emplacement(new Point(l, c), epoque);
        int id = dj.getJoueurActuel().getID()-1;
        if (joueurs[id] == 0) {
            switch (dj.getEtape()) {
                case SELECT:
                    dj.selectPion(e);
                    break;
                case MOVE2:
                case MOVE1:
                    if (dj.getConstructionStatue()) {
                        dj.MemoryManager.AddLog(ETAT.MOVE1);
                        creerStatue(e);

                        //dj.creerStatue(e);
                    } else {
                        deplace(e, true);
                        //dj.jouerCoup(e, true);
                    }
                    state.initPreview();
                    break;
                case FOCUS:
                    break;
                case END:
                    break;
            }
        }
    }

    // Tic du timer (toutes les 1 sec)
    @Override
    public void tic() {
        int id = dj.getJoueurActuel().getID()-1;
        if (joueurs[id] == 1) {
            IA j;
            if (id == 0) {
               j = joueur1;
            } else {
                j = joueur2;
            }
            switch(dj.getEtape()) {
                case SELECT:
                    dj.selectPion(j.selectPion().getEmplacement());
                    break;
                case MOVE1:
                    Couple<Integer, Emplacement> c = j.getCoup1();
                    if (c.getFirst() == 1) {
                        deplace(c.getSecond(),true);
                    } else if (c.getFirst() == 2){
                        creerStatue(c.getSecond());
                    }
                    state.initPreview();
                    break;
                case MOVE2:
                    c = j.getCoup2();
                    if (c.getFirst() == 1) {
                        deplace(c.getSecond(),true);
                    } else if (c.getFirst() == 2){
                        dj.MemoryManager.AddLog(ETAT.MOVE1);
                        creerStatue(c.getSecond());


                    }
                    state.initPreview();
                    break;
                case FOCUS:
                    dj.getPionActuel().focused=false;
                    int focus = j.choixFocus();
                    dj.getJoueurActuel().setFocus(focus);
                    dj.switchPlayer();
                    state.initPreview();
                    break;
                case END:
                    break;
            }
        }
    }

    @Override
    public void ticAnim() {
        if(animationsActives || mouvement != null){
            ArrayList<Animation> remove = new ArrayList<>();
            for (Animation animation : animations) {
                animation.tic();
                if(animation.estTermine()){
                    remove.add(animation);
                    if(animation == mouvement){
                        mouvement = null;
                    }
                }
            }
            animations.removeAll(remove);
        }
    }

    void deplace(Emplacement e, boolean real){
        if(mouvement == null){
            Coup cp = dj.jouerCoup(e, real);
            state.initPreview();
            animations.remove(previewAnim);
            if (cp!=null && animationsActives){
                mouvement = new AnimationCoup(cp, inter);
                animations.add(mouvement);
            }
        }
    }

    void creerStatue(Emplacement e){
        if(mouvement == null){
            Coup cp = dj.creerStatue(e);
            state.initPreview();
            animations.remove(previewAnim);
            if (cp!=null && animationsActives){
                mouvement = new AnimationCoup(cp, inter);
                animations.add(mouvement);
            }
        }
    }

    public void suggestion () {
        suggestion.calculCoup(dj, 3, true, null);
        switch(dj.getEtape()) {
            case SELECT:
                state.setSuggestionSource(suggestion.selectPion().getEmplacement());

                Couple<Integer, Emplacement> c = suggestion.getCoup1();
                if (c.getFirst() == 1) {
                    state.setSuggestionDestination(c.getSecond());
                }
                break;
            case MOVE1:
                c = suggestion.getCoup1();
                if (c.getFirst() == 1) {
                    state.setSuggestionSource(dj.getPionActuel().getEmplacement());
                    state.setSuggestionDestination(c.getSecond());
                } else if (c.getFirst() == 2){
                    state.setSuggestionSource(null);
                    state.setSuggestionDestination(c.getSecond());
                }
                break;
            case MOVE2:
                c = suggestion.getCoup2();
                if (c.getFirst() == 1) {
                    state.setSuggestionSource(dj.getPionActuel().getEmplacement());
                    state.setSuggestionDestination(c.getSecond());
                } else if (c.getFirst() == 2){
                    state.setSuggestionSource(null);
                    state.setSuggestionDestination(c.getSecond());
                }
                break;
            case FOCUS:
                state.setSuggestionFocus(suggestion.choixFocus());
                break;
            case END:
                break;
        }
    }

    // Clique sur un bouton
    @Override
    public boolean commande(Commande c) {
        System.out.println(c.getCommande());
        switch(c.getCommande()){
            case "clicFocus":
                if(dj.getEtape()==ETAT.FOCUS){
                    int id = dj.getJoueurActuel().getID()-1;
                    if (joueurs[id] == 0) {
                        //choix focus
                        if (dj.peutSelectionnerFocus(c.getEpoque(), c.getJoueur())) {
                            dj.getJoueurActuel().setFocus(c.getEpoque());
                            dj.MemoryManager.UpdateLog(null,null);
                            dj.switchPlayer();
                            state.initPreview();
                        } else {
                            System.out.println("Modification du focus adverse impossible");
                        }
                    }
                } else if (dj.getEtape() == ETAT.END){
                }
                break;
            case "clicStatue":
                System.out.println(c.getJoueur());
                if(c.getJoueur()==dj.getJoueurActuel().getID()){
                    dj.switchStatue();
                }
                break;
            case "save":
                System.out.println(c.getSaveName());
                dj.MemoryManager.Save(c.getSaveName());
                break;
            case "load":
                System.out.println(c.getSaveName());
                dj.MemoryManager.Load(c.getSaveName());
                break;
            case "annuler":
                dj.MemoryManager.CTRLZ();
                state.initPreview();
                state.setPauseIA(true);
                t.stop();
                break;
            case "refaire":
                dj.MemoryManager.CTRLY();
                state.initPreview();
                state.setPauseIA(true);
                t.stop();
                break;
            case "annulerTour":
                dj.MemoryManager.CTRLTZ();
                state.initPreview();
                state.setPauseIA(true);
                t.stop();
                break;
            case "suggestion":
                suggestion();
                break;
            case "reprendre":
                t = new Timer(speed, new AdaptateurTemps(this));
                t.start();
                state.setPauseIA(false);
                break;
            case "IASpeed":
                t.stop();
                speed = c.getIA();
                t = new Timer(speed, new AdaptateurTemps(this));
                t.start();
                break;
            case "newGame":
                t.stop();
                init();
                inter.reset();
                break;
            case "anim":
                animationsActives = !animationsActives;
                break;
            case "toggleIA1":
                activerIA(0, difficulty1, heuristique);
                break;
            case "toggleIA2":
                activerIA(1, difficulty2, heuristique);
                break;
            case "setDifficulty":
                System.out.println(c.getDifficulty());
                setDifficultyIA(c.getIA(), c.getDifficulty());
                break;
            default:
                return false;
        }
        return true;
    }

    // Fonctions appelés lors d'un clique sur un bouton
    public void activerIA(int j, String type, String heuristique) {
        joueurs[j] = (joueurs[j] + 1) % 2;
        if (j == 0) {
            joueur1 = IA.nouvelle(new CalculJeu(dj), type, heuristique);
            state.setIA1(joueurs[j]==1);
        } else {
            joueur2 = IA.nouvelle(new CalculJeu(dj), type, heuristique);
            state.setIA2(joueurs[j]==1);
        }
        dj.miseAJour();
    }

    public void desactiverIA(int j) {
        if (joueurs[j] == 1) {
            joueurs[j] = 0;
        }
        if(j==0){
            state.setIA1(false);
        }else if (j==1){
            state.setIA2(false);
        }
    }

    public void setDifficultyIA(int ia, String difficulty) {
        if (ia == 1) {
            difficulty1 = difficulty;
            if (joueurs[0] == 1) {
                desactiverIA(0); //désactiver
                activerIA(0, difficulty1, heuristique);
            }
            state.setDifficultyIA1(difficulty);
        } else {
            difficulty2 = difficulty;
            if (joueurs[1] == 1) {
                desactiverIA(1); //désactiver
                activerIA(1, difficulty2, heuristique);
            }
            state.setDifficultyIA2(difficulty);
        }
    }

    public void enablePreview(int l, int c, int epoque){
        Emplacement e = new Emplacement(new Point(l, c), epoque);
        // Si on est dans le bon etat
        if((dj.getEtape()==ETAT.MOVE1 || dj.getEtape()==ETAT.MOVE2) && !joueurActuelEstIA()){
            // Si la preview est une nouvelle preview
            if(!e.equals(previewEmp)){
                animations.remove(previewAnim);
                // Si le coup est possible
                Preview preview = dj.getPreview(e);
                state.setPreview(preview.getPlateau());
                if (!preview.getCoup().deplacements().isEmpty() && animationsActives) {
                    previewEmp = e;
                    previewAnim = new AnimationPreview(preview.getCoup(), inter);
                    animations.add(previewAnim);
                    //state.setPreview(preview.getPlateau());
                }else {
                    previewEmp = null;
                    previewAnim = null;
                    //state.setPreview(null);
                    inter.reset();
                }
            }
        }
    }

    public boolean joueurActuelEstIA(){
        int id = dj.getJoueurActuel().getID()-1;
        return joueurs[id] == 1;
    }
}
