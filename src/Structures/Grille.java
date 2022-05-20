package Structures;

import Modele.Pion;
import Modele.ETAT;
import java.util.Arrays;

public class Grille {
    private Pion [] cases;
    public ETAT etat;
    public int PionFocus;
    public int Focus;
    public Pion [] GetCases()
    {
        return cases;
    }

    public Grille(Pion [] t_cases, ETAT t_etat,int t_pionfocus)
    {
        cases = t_cases;
        etat=t_etat;
        PionFocus = t_pionfocus;

    }

    public Grille(Pion [] t_cases, ETAT t_etat,int t_pionfocus,int t_focus)
    {
        cases = t_cases;
        etat=t_etat;
        PionFocus = t_pionfocus;
        Focus = t_focus;
    }

    public static Pion[] Clone(Pion[]original)
    {
        Pion[] t_pions = new Pion[original.length];
        int i=0;
        for(Pion t_pion : original)
        {
            if(t_pion!=null)
                //TODO : Verifier quel copy est appelé
                t_pions[i]=t_pion.copy(t_pion.getJoueur());
            i++;
        }
        return t_pions;
        //return Arrays.copyOf(original, original.length);
        
    }
    public int Compare(Pion[] second)
    {
        int ln= cases.length;
        for (int i=0 ; i<ln;i++) {
            if(cases[i]!=null && second[i]==null || cases[i] ==null && second[i]!=null)
                return i;
            if(cases[i]!=null && second[i]!=null)
            {
                if(cases[i].getCoordonnees().getC()!= second[i].getCoordonnees().getC() || cases[i].getCoordonnees().getL()!= second[i].getCoordonnees().getL())
                    return i;
                if(cases[i].getEpoque()!= second[i].getEpoque())
                    return i;
            }

        }
        return -1;
    }
}
