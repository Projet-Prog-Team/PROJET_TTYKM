package Structures;

import Modele.Pion;
import Modele.PionBasique;
import Modele.Statue;
import Modele.ETAT;

import java.util.Arrays;

public class Grille {
    private PionBasique [] cases;
    private Statue[] statues;
    private Pion[] result;
    public ETAT etat;

    public int nbrestantJ1=0;
    public int nbrestantJ2=0;
    public int PionFocus;
    public int FocusJ1;
    public int FocusJ2;
    public Pion [] GetCases()
    {
        /*int caseslength = cases.length;

        System.arraycopy(cases, 0, result, 0, caseslength);
        for(Statue t_pion : statues)
        {
            if(t_pion != null)
                result[t_pion.ID]=t_pion;
        }*/
        return result;
    }

    public Statue [] GetStatue() {return statues;}
    public PionBasique [] GetPions()
    {

        return cases;
    }

    public Grille(Pion [] t_cases, ETAT t_etat,int t_pionfocus)
    {
        Grille tmpgrille = new Grille(t_cases);
        result=tmpgrille.GetCases();
        cases = tmpgrille.GetPions();
        statues=tmpgrille.GetStatue();
        etat=t_etat;
        PionFocus = t_pionfocus;

    }

    public Grille(Pion [] t_cases, ETAT t_etat,int t_pionfocus,int t_focus, int t_focus2,int tnbpion1, int tnbpion2)
    {
        Grille tmpgrille = new Grille(t_cases);
        cases = tmpgrille.GetPions();
        statues=tmpgrille.GetStatue();
        result=tmpgrille.GetCases();
        etat=t_etat;
        PionFocus = t_pionfocus;
        FocusJ1 = t_focus;
        FocusJ2 = t_focus2;
        nbrestantJ1=tnbpion1;
        nbrestantJ2=tnbpion2;
    }
    public Grille(Pion [] t_cases)
    {
        PionBasique[] tcases = new PionBasique[t_cases.length];
        Statue[] tstatues = new Statue[t_cases.length];
        result = t_cases;
        int i=0;
        int j=0;
        for(Pion tmp : t_cases)
        {
            if(tmp instanceof PionBasique)
            {
                tcases[tmp.ID]=(PionBasique) tmp;
                i++;
            }
            else
            {
                if(tmp != null)
                {
                    tstatues[tmp.ID]=(Statue)tmp;
                    j++;
                }

            }
        }

        cases = (PionBasique[]) tcases;
        statues = (Statue[]) tstatues;

    }
    public static Pion[] Clone(Pion[] original)
    {
        Pion[] t_pions = new Pion[original.length];
        int i=0;
        for(Pion t_pion : original)
        {
            if(t_pion!=null)
            {
                if(t_pion instanceof PionBasique)
                {
                    t_pions[i]=t_pion.copy(t_pion.getJoueur());
                }
                else
                {
                    t_pions[i]=t_pion.copy();
                }

            }

            i++;
        }
        return t_pions;
        //return Arrays.copyOf(original, original.length);
        
    }
    public int Compare(Pion[] second)
    {
        Pion [] result = GetCases();
        if(second == null || result == null)
            return -1;
        int ln= result.length;
        for (int i=0 ; i<ln;i++) {
            if(result[i]!=null && second[i]==null || result[i] ==null && second[i]!=null)
                return i;
            if(result[i]!=null && second[i]!=null)
            {
                if(result[i].getCoordonnees().getC()!= second[i].getCoordonnees().getC() || result[i].getCoordonnees().getL()!= second[i].getCoordonnees().getL())
                    return i;
                if(result[i].getEpoque()!= second[i].getEpoque())
                    return i;
            }

        }
        return -1;
    }
}
