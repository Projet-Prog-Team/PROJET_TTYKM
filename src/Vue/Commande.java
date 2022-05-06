package Vue;

public class Commande {

    private String commande;
    private int epoque;
    private int joueur;

    Commande(String commande){
        this.commande = commande;
    }

    public String getCommande() {
        return commande;
    }

    public int getEpoque(){ return epoque; }

    public void setEpoque(int epoque){ this.epoque = epoque; }

    public int getJoueur(){ return joueur; }

    public void setJoueur(int joueur) { this.joueur = joueur; }
}
