package Structures;

import Modele.Pion;
import Modele.PionBasique;
import Modele.Statue;
import Modele.ETAT;

public class Grille {
    private PionBasique [] cases;
    private Statue[] statues;
    public ETAT etat;
    public int PionFocus;
    public int FocusJ1;
    public int FocusJ2;
    public PionBasique [] GetCases()
    {
        return cases;
    }

    public Statue [] GetStatue() {return statues;}
    public Pion [] GetPions()
    {
        return cases;
    }

    public Grille(Pion [] t_cases, ETAT t_etat,int t_pionfocus)
    {
        Grille tmpgrille = new Grille(t_cases);
        cases = tmpgrille.GetCases();
        statues=tmpgrille.GetStatue();
        etat=t_etat;
        PionFocus = t_pionfocus;

    }

    public Grille(Pion [] t_cases, ETAT t_etat,int t_pionfocus,int t_focus, int t_focus2)
    {
        Grille tmpgrille = new Grille(t_cases);
        cases = tmpgrille.GetCases();
        statues=tmpgrille.GetStatue();
        etat=t_etat;
        PionFocus = t_pionfocus;
        FocusJ1 = t_focus;
        FocusJ2 = t_focus2;
    }
    public Grille(Pion [] t_cases)
    {
        PionBasique[] tcases = new PionBasique[t_cases.length];
        Statue[] tstatues = new Statue[t_cases.length];
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

        cases = new PionBasique[i];
        cases = (PionBasique[]) tcases;
        statues = new Statue[i];
        statues = (Statue[]) tstatues;
    }
    public static Pion[] Clone(Pion[] original)
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
    public int Compare(PionBasique[] second)
    {

        if(second == null || cases == null)
            return -1;
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
