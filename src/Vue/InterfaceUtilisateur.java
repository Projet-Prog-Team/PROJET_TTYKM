package Vue;

import Modele.Emplacement;

public interface InterfaceUtilisateur {
    void decale(double dL, double dC, int l, int c, int epoque);
    void tp(Emplacement depart, Emplacement arrive, double alphaDep, double alphaArr);
    void reset();
}
