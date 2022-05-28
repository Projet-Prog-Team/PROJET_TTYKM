package Modele;

public enum ETAT {
    SELECT,
    MOVE1,
    MOVE2,
    FOCUS,
    IDLE,
    END,
    UNDEFINED;

    public static int Convert(ETAT t_etat)
    {
        switch(t_etat)
        {
            case SELECT :
                return 0;
            case MOVE1:
                return 1;
            case MOVE2:
                return 2;
            case FOCUS :
                return 3;
            case END:
                return 4;
            case UNDEFINED:
                return 5;
            case IDLE:
                return 6;
        }
        return -1;
    }

    public static ETAT Convert(int t_etat)
    {
        switch(t_etat)
        {
            case 0 :
                return SELECT;
            case 1:
                return MOVE1;
            case 2:
                return MOVE2;
            case 3 :
                return FOCUS;
            case 4:
                return END;
            case 5:
                return UNDEFINED;
            case 6:
                return IDLE;
        }
        return UNDEFINED;
    }
}
