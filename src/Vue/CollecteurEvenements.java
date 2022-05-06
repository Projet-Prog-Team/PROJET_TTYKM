package Vue;

import Modele.EPOQUE;

public interface CollecteurEvenements {
    void clicSouris(int l, int c, int epoque);
    boolean commande(Commande c);
}
