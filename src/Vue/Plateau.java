package Vue;

public interface Plateau {
    void tracerPion(int l,int c, double alpha, int joueur);
    void tracerBrillancePion(int l, int c);
    void tracerBrillanceCase(int l, int c);
    void tracerBrillanceFocus(int focus);
    void tracerFocusBlanc(double alpha);
    void tracerFocusNoir(double alpha);
    void tracerSuggestionCase(int l, int c);
    void tracerSuggestionFocus(int focus);
    int getEpoque();
    IHMState getState();
}
