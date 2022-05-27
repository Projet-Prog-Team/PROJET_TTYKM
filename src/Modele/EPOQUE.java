package Modele;

public enum EPOQUE {
    PASSE,
    PRESENT,
    FUTUR;


    public static EPOQUE Convert(int i)
    {
        switch(i)
        {
            default:
                return PASSE;
            case 1 :
                return PRESENT;
            case 2 :
                return FUTUR;
        }
    }

    public static int Convert(EPOQUE i)
    {
        switch(i)
        {
            default:
                return 0;
            case PRESENT:
                return 1;
            case FUTUR :
                return 2;
        }
    }
}
