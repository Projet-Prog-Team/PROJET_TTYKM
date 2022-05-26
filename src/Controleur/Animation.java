package Controleur;

public abstract class Animation {

    private int lenteur;
    private int decompte;

    Animation(int l){
        lenteur = l;
        decompte = 0;
    }

    void tic(){
        decompte++;
        if(decompte >= lenteur){
            miseAjour();
            decompte = 0;
        }
    }

    boolean estTermine(){
        return false;
    }

    abstract void miseAjour();
}
