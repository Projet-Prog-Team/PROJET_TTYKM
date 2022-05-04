package Vue;

public class Commande {
    private String commande;

    Commande(String commande){
        this.commande = commande;
    }

    public String getCommande() {
        return commande;
    }

    public void setCommand(String commande) {
        this.commande = commande;
    }
}
