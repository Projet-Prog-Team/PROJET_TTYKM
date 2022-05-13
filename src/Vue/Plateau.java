package Vue;

public interface Plateau {
    void tracerPion(int l,int c, double alpha, int joueur);
    void tracerBrillancePion(int l, int c);
    void tracerBrillanceCase(int l, int c);
    void tracerBrillanceFocusBlanc();
    void tracerBrillanceFocusNoir();
    void tracerFocusBlanc(double alpha);
    void tracerFocusNoir(double alpha);
    int getEpoque();
}
