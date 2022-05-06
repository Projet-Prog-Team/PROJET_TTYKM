package Vue;

public interface Plateau {
    void tracerPion(int l,int c, int joueur);
    void tracerBrillance(int l, int c);
    void tracerBrillanceFocus1();
    void tracerBrillanceFocus2();
    void tracerFocus1();
    void tracerFocus2();
    int getEpoque();
}
