package Patterns;

import Modele.Pion;
import java.util.Arrays;

public class Grille {
    private Pion [] cases;

    public Pion [] GetCases()
    {
        return cases;
    }

    public Grille(Pion [] t_cases)
    {
        cases = t_cases;
    }

    public static Pion[] Clone(Pion[]original)
    {
        return Arrays.copyOf(original, original.length);
        
    }
    public int Compare(Pion[] second)
    {
        int ln= cases.length;
        for (int i=0 ; i<ln;i++) {
            if(cases[i]!=null && second[i]==null || cases[i] ==null && second[i]!=null)
                return i;
            if(cases[i]!=null && second[i]!=null)
            {
                if(cases[i].getCoordonnees()!= second[i].getCoordonnees())
                    return i;
                if(cases[i].getEpoque()!= second[i].getEpoque())
                    return i;
            }

        }
        return -1;
    }
}
