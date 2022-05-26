package Vue;

import Modele.EPOQUE;

public interface CollecteurEvenements {
    void clicSouris(int l, int c, int epoque);
    boolean commande(Commande c);
    void tic();
    void ticAnim();
    void fixerInterfaceUtilisateur(InterfaceUtilisateur i);
    void enablePreview(int l, int c, int epoque);
}
