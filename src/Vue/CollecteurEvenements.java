package Vue;

import Modele.EPOQUE;

public interface CollecteurEvenements {
    void clicSouris(int l, int c, int epoque);
    boolean commande(Commande c);
    void tic();
    void enablePreview(int l, int c, int epoque);
    void setPreviewFocus1(int epoque);
    void setPreviewFocus2(int epoque);
    boolean isEnabledIA1();
    boolean isEnabledIA2();
    String getDifficultyIA1();
    String getDifficultyIA2();
}
