package Vue;

public interface Plateau {
    void tracerBrillancePion(int l, int c);
    void tracerBrillanceCase(int l, int c);
    void tracerBrillanceFocus(int focus);
    void tracerPion(double l, double c, double alpha, int joueur);
    void tracerStatue(double l, double c, double alpha, int joueur);
    void tracerFocusBlanc(double alpha);
    void tracerFocusNoir(double alpha);
    void tracerSuggestionCase(int l, int c);
    void tracerSuggestionFocus(int focus);
    int getEpoque();
    IHMState getState();
    void decale(double dL, double dC, int l, int c);
}
