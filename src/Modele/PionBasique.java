package Modele;

import java.util.Objects;

public class PionBasique extends Pion{
    private Joueur joueur;

    public PionBasique(Emplacement emplacement, Joueur j) {
        super(emplacement);
        joueur = j;
    }

    public PionBasique(Emplacement emplacement, Joueur j, int t_ID, boolean t_focused) {
        super(emplacement, t_ID, t_focused);
        joueur = j;
    }

    public Joueur getJoueur() {
        return joueur;
    }

    public void setJoueur(Joueur joueur) {
        this.joueur = joueur;
    }

    public PionBasique copy(Joueur joueur) {
        return new PionBasique(getEmplacement().copy(), joueur,ID,focused);
    }

    @Override
    public Pion copy() {
        System.out.println("hey");
        return null;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        PionBasique that = (PionBasique) o;
        return Objects.equals(joueur, that.joueur);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), joueur);
    }

    @Override
    public String toString() {
        return super.toString() +
                "joueur=" + joueur +
                " TYPE = PION BASIQUE" +
                '}';
    }
}
