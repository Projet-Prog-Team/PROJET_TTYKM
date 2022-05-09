package Vue;

public class Commande {

    private final String commande;
    private int epoque;
    private int joueur;
    private int IA;
    private String difficulty;
    private String saveName;

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

    public int getIA() {
        return IA;
    }

    public void setIA(int IA) {
        this.IA = IA;
    }

    public String getDifficulty() {
        return difficulty;
    }

    public void setDifficulty(String difficulty) {
        this.difficulty = difficulty;
    }

    public String getSaveName() {
        return saveName;
    }

    public void setSaveName(String saveName) {
        this.saveName = saveName;
    }
}
