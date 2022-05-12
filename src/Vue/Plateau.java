package Vue;

public interface Plateau {
    void tracerPion(int l,int c, double alpha, int joueur);
    void tracerBrillance(int l, int c);
    void tracerBrillancev2(int l, int c);
    void tracerBrillanceFocus1();
    void tracerBrillanceFocus2();
    void tracerFocus1(double alpha);
    void tracerFocus2(double alpha);
    int getEpoque();
}
