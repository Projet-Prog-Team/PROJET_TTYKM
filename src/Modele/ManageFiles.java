package Modele;
import Controleur.ControleurMediateur;
import Structures.Grille;
import Structures.Point;

import java.io.*;
import java.util.Scanner;
import java.util.Vector;

import java.util.ArrayList;

public class ManageFiles   {
    
    private ArrayList<Grille> temp;
    private DeroulementJeu DJgame;
    private Jeu game;
    private String path;
    private Pion[] pions;
    public boolean move=true;
    private int addetat=1;
    public boolean init = false;
    public int Actual_pos=0;
    public int Max_pos=1;

    private ControleurMediateur controleur;

    private final int SEPSECT = 10000;
    private final int NBPIONS;
    private final int NBSTATUES;

    public ManageFiles(ControleurMediateur t_controleur, String absolutepath) {
        controleur = t_controleur;
        DJgame = controleur.dj;
        game = DJgame.getJeu();
        NBPIONS = game.NBPIONS;
        NBSTATUES = game.NBSTATUES;
        temp = new ArrayList<Grille>();
        pions = new Pion[NBPIONS + NBSTATUES];
        Pion[] tmp = game.getPions().toArray(new Pion[0]);
        for (int i = 0; i < tmp.length; i++) {
            if(tmp[i] instanceof PionBasique)
            {
                pions[tmp[i].ID] = tmp[i].copy(tmp[i].getJoueur());
            }
            else
            {
                pions[tmp[i].ID] = tmp[i].copy();
            }
        }
        temp.add(new Grille(Grille.Clone(pions), ETAT.IDLE, 0, game.joueurs[0].getFocus(), game.joueurs[1].getFocus()));
        try
        {

            if(System.getProperty("os.name").toLowerCase().contains("win"))
            {
                path = new File(absolutepath).getCanonicalPath() + "\\";
            }
            else
            {
                path = new File(absolutepath).getCanonicalPath() + "/";
            }

        }
        catch(IOException e)
        {
            System.out.println(e);
        }

    }

    public String GetPath()
    {
        return path;
    }

    public void Save(String filepath)
    {
        try
        {

            File file = new File(path+filepath);
            System.out.println(path+filepath);
            if(!file.exists())
                file.createNewFile();
            FileWriter Writer = new FileWriter(file);
            //ia? si oui difficulté

            Writer.write(controleur.joueurs[0]);
            Writer.write(controleur.joueurs[1]);
            switch(controleur.state.getDifficultyIA1())
            {
                case "facile":
                    Writer.write(1);
                    break;

                case "moyenne":
                    Writer.write(2);
                    break;

                case "difficile":
                    Writer.write(3);
                    break;
            }
            switch(controleur.state.getDifficultyIA2())
            {
                case "facile":
                    Writer.write(1);
                    break;

                case "moyenne":
                    Writer.write(2);
                    break;

                case "difficile":
                    Writer.write(3);
                    break;
            }
            Writer.write(Actual_pos);
            Writer.write(Max_pos);
            Writer.write('\n');
            Grille tmpgrille=new Grille(new Pion[game.NBPIONS+NBSTATUES]);
            for(Grille t_grille : temp)
            {
                Writer.write(t_grille.FocusJ1);
                Writer.write(t_grille.FocusJ2);

                Writer.write(t_grille.PionFocus);
                Writer.write(ETAT.Convert(t_grille.etat));
                do
                {
                    int i =tmpgrille.Compare(t_grille.GetCases());
                    if(i==(-1))
                        i=0;
                    Writer.write("\n");
                    Writer.write(Integer.toString(i));
                    Writer.write("\n");
                    System.out.println(i);
                    if(t_grille.GetCases()[i] != null) {

                        Writer.write(t_grille.GetCases()[i].getCoordonnees().getC());
                        Writer.write(t_grille.GetCases()[i].getCoordonnees().getL());
                        Writer.write(t_grille.GetCases()[i].getEpoque());
                        Writer.write(t_grille.GetCases()[i].focused ? 1 : 0);
                        if(i>=NBPIONS)
                        {
                            tmpgrille.GetCases()[i] = t_grille.GetCases()[i].copy();
                        }
                        else
                        {
                            tmpgrille.GetCases()[i] = t_grille.GetCases()[i].copy(t_grille.GetCases()[i].getJoueur());
                        }
                    }
                    else
                    {
                        tmpgrille.GetCases()[i] = null;
                        Writer.write('\t');
                    }
                }while(tmpgrille.Compare(t_grille.GetCases())!=-1);
                Writer.write(' ');
                Writer.write('\n');

            }

            Writer.close();
        } catch (IOException e) {
            System.out.println("cannot find file or cannot write file");
            e.printStackTrace();
        }
    }
    public void BoardLoad(int index)
    {
        game.SetPions(filter(Grille.Clone(temp.get(index).GetCases())));
        game.getJoueurs()[0].setFocus(temp.get(index).FocusJ1);
        game.getJoueurs()[1].setFocus(temp.get(index).FocusJ2);
        ETAT t_etat;
        int pos =temp.get(index).PionFocus;
        int j1=0;
        int j2=0;
        int i=0;
        for(Pion t_pion : temp.get(index).GetCases())
        {
            if(t_pion!=null && t_pion instanceof PionBasique)
                if(i<NBPIONS/2)
                {
                    j1++;
                }
                else
                {
                    j2++;
                }
            i++;
        }
        game.getJoueur(0).SetNbPionsRestants( NBPIONS/2-(j1)-(NBPIONS/2-(j1)-game.getJoueur(0).getNbPionsRestants()));
        game.getJoueur(0).SetNbPionsRestants( NBPIONS/2-(j2)-(NBPIONS/2-(j2)-game.getJoueur(0).getNbPionsRestants()));
        if(pos>=NBPIONS/2)
        {
            pos = (pos-(NBPIONS/2-(NBPIONS/2-j1)))-1-(NBPIONS/2-(j2+game.getJoueur(1).getNbPionsRestants()));
        }
        DJgame.setPionActuel((PionBasique) game.getPions().get(pos));
        if(temp.get(index).PionFocus>=NBPIONS/2)
        {
            DJgame.joueurActuel=game.joueurs[1];
        }
        else
        {
            DJgame.joueurActuel=game.joueurs[0];
        }
        t_etat=temp.get(index).etat;
        if(index-1>=0 && temp.get(index-1).etat == ETAT.MOVE2 && temp.get(index).etat==ETAT.MOVE2)
            t_etat = ETAT.SELECT;
        switch(t_etat)
        {
            case MOVE2:
                DJgame.getJoueurActuel().nbActionsRestantes=0;
                break;
            case MOVE1:
                DJgame.getJoueurActuel().nbActionsRestantes=1;
                break;

            case SELECT:
                DJgame.getJoueurActuel().nbActionsRestantes=2;
                DJgame.setPionActuel(null);
                break;

            case IDLE:
                DJgame.getJoueurActuel().nbActionsRestantes=2;
                DJgame.setPionActuel(game.pionsFocusJoueur((index>=NBPIONS/2?temp.get(index).FocusJ2:temp.get(index).FocusJ1),DJgame.getJoueurActuel()).get(0));
                break;

            case UNDEFINED:

                break;
        }
        System.out.println("Lock and Load");
    }
    public void Load(String filepath)
    {
        try
        {
            FileInputStream fis=new FileInputStream(path+filepath);
            Scanner scan = new Scanner(fis);
            byte info[] = scan.nextLine().getBytes();
            controleur.joueurs[0]=info[0];
            controleur.joueurs[1]=info[1];
            controleur.state.setIA1(info[0]==1);
            controleur.state.setIA2(info[1]==1);
            String diff="";
            switch(info[2])
            {
                case 1:
                    diff="facile";
                    break;

                case 2:
                    diff="moyenne";
                    break;

                case 3:
                    diff="difficile";
                    break;
            }
            controleur.state.setDifficultyIA1(diff);
            switch(info[3])
            {
                case 1:
                    diff="facile";
                    break;

                case 2:
                    diff="moyenne";
                    break;

                case 3:
                    diff="difficile";
                    break;
            }
            controleur.state.setDifficultyIA2(diff);
            Actual_pos= info[4];
            Max_pos= info[5];
            Grille tmpgrille = null;
            temp= new ArrayList<>();
            int tour=-1;

            for(int i=0; i<Max_pos;i++)
            {
                info = scan.nextLine().getBytes();
                System.out.println("i: "+i);
                if(tmpgrille != null)
                {
                    tmpgrille=new Grille(Grille.Clone(tmpgrille.GetCases()),tmpgrille.etat, tmpgrille.PionFocus,tmpgrille.FocusJ1, tmpgrille.FocusJ2);
                }
                else
                {
                    tmpgrille= new Grille(new Pion[game.NBPIONS+ game.NBSTATUES]);
                }

                tmpgrille.FocusJ1=info[0];
                tmpgrille.FocusJ2=info[1];
                tmpgrille.PionFocus= info[2];
                tmpgrille.etat=ETAT.Convert(info[3]);
                if(tmpgrille.etat == ETAT.SELECT || tmpgrille.etat == ETAT.IDLE)
                    tour++;
                do {
                    String idtmp = scan.nextLine();
                    int id = Integer.parseInt(idtmp);
                    info = scan.nextLine().getBytes();
                    if (info[0] != '\t') {
                        int c = info[0];
                        int l = info[1];
                        int e = info[2];
                        if(id>=NBPIONS)
                        {
                            int color=0;
                            if(id>=NBPIONS+3)
                            {
                                color = id>=NBPIONS+6 ? 2 : 1;
                            }
                            else
                            {
                                color=3;
                            }
                            tmpgrille.GetCases()[id] = new Statue(new Emplacement(new Point(l, c), e), id, false,color);
                        }
                        else
                        {
                            tmpgrille.GetCases()[id] = new PionBasique(new Emplacement(new Point(l, c), e), game.getJoueur(id >= NBPIONS / 2 ? 1 : 0), id, info[3] == 1);
                        }

                    } else {
                        tmpgrille.GetCases()[id] = null;
                    }
                }while(scan.hasNextInt());
                temp.add(tmpgrille);

            }
            BoardLoad(Actual_pos);
            DJgame.miseAJour();
            pions = new Pion[NBPIONS];
            pions = Grille.Clone(temp.get(Actual_pos).GetCases());
        } catch (IOException e) {
            System.out.println("cannot write  in memory or cannot read file");
            e.printStackTrace();
        }

    }
    public void CTRLZ()
    {
        if(Actual_pos>0)
        {
            Actual_pos--;
            ETAT t_etat = temp.get(Actual_pos).etat;
            switch(temp.get(Actual_pos+1).etat)
            {
                /*case FOCUS:
                    t_etat=ETAT.FOCUS;
                    System.out.println("hey");
                    break;*/
                case MOVE1:
                    if(game.pionsFocusJoueur(DJgame.getPionActuel().getEpoque(),DJgame.getJoueurActuel()).size()!=1)
                    {
                        t_etat=ETAT.SELECT;
                    }break;
            }
            int pred_focus=-1;
            int pos=temp.get(Actual_pos).PionFocus;
            Pion[] temp0=temp.get(Actual_pos).GetCases();
            int j1=0;
            int j2=0;
            int i=0;
            boolean S_j1 =false;
            boolean S_j2 =false;
            for(Pion t_pion : temp0)
            {
                if(t_pion!=null)
                    if(t_pion instanceof PionBasique) {
                        if (i < NBPIONS / 2) {
                            j1++;
                        } else {
                            j2++;
                        }
                    }
                    else
                    {
                        if(t_pion.ID>NBPIONS+2)
                        if(t_pion.ID>NBPIONS+5)
                        {
                            S_j2=true;
                            System.out.println("findj2");
                        }
                        else
                        {
                            System.out.println("findj1");
                            S_j1=true;
                        }
                    }
                i++;
            }
            game.getJoueur(0).SetNbPionsRestants( NBPIONS/2-(j1)-(NBPIONS/2-(j1)-game.getJoueur(0).getNbPionsRestants()));
            game.getJoueur(1).SetNbPionsRestants( NBPIONS/2-(j2)-(NBPIONS/2-(j2)-game.getJoueur(1).getNbPionsRestants()));
            game.getJoueur(0).SetStatuePlaced(S_j1);
            game.getJoueur(1).SetStatuePlaced(S_j2);
            int oldpos = pos;

            if(pos>=NBPIONS/2)
            {
                pos = (pos-(NBPIONS/2-(NBPIONS/2-j1)))-1-(NBPIONS/2-(j2+game.getJoueur(1).getNbPionsRestants()));
            }
            System.out.println(pos);
            int pred_focusJ1;
            int pred_focusJ2;
            if(Actual_pos-1>0 && t_etat!=ETAT.IDLE)
            {
                pred_focusJ1 = temp.get(Actual_pos-1).FocusJ1;
                pred_focusJ2=temp.get(Actual_pos-1).FocusJ2;
                pred_focus=(oldpos>=NBPIONS/2?temp.get(Actual_pos-1).FocusJ2:temp.get(Actual_pos-1).FocusJ1);
            }
            else
            {

                if(t_etat==ETAT.IDLE)
                    game.SetPions(filter(Grille.Clone(temp.get(Actual_pos).GetCases())));
                //pred_focus=game.getPions().get(pos).getEpoque();
                pred_focusJ1 = temp.get(Actual_pos).FocusJ1;
                pred_focusJ2=temp.get(Actual_pos).FocusJ2;
                pred_focus=(oldpos>=NBPIONS/2?pred_focusJ2:pred_focusJ1);
            }


            switch(t_etat)
            {
                case IDLE:
                    System.out.println("IDLE");
                    game.SetPions(filter(Grille.Clone(temp.get(Actual_pos).GetCases())));
                    DJgame.setPionActuel(game.pionsFocusJoueur(pred_focus,DJgame.getJoueurActuel()).get(0));
                    DJgame.joueurActuel.nbActionsRestantes=2;
                    break;
                case SELECT:
                    System.out.println("SELECT");
                    game.SetPions(filter(Grille.Clone(temp.get(Actual_pos).GetCases())));
                    DJgame.joueurActuel.nbActionsRestantes=2;
                    DJgame.setPionActuel(null);
                    break;

                case MOVE1:
                    System.out.println("MOVE1");
                    game.SetPions(filter(Grille.Clone(temp.get(Actual_pos).GetCases())));
                    DJgame.setPionActuel((PionBasique) game.getPions().get(pos));
                    DJgame.joueurActuel.nbActionsRestantes=1;
                    break;

                case MOVE2:
                    System.out.println("FOCUS");
                    // TODO : vérifier que ça ne cause pas de problèmes
                    if (game.getPions().get(pos) instanceof PionBasique) {
                        DJgame.setPionActuel((PionBasique) game.getPions().get(0));
                    }
                    DJgame.joueurActuel.nbActionsRestantes=0;
                    if( DJgame.joueurActuel== game.joueurs[0])
                    {
                        DJgame.joueurActuel=game.joueurs[1];
                    }
                    else
                    {
                        DJgame.joueurActuel=game.joueurs[0];
                    }

                    game.getJoueurs()[0].setFocus(pred_focusJ1);
                    game.getJoueurs()[1].setFocus(pred_focusJ2);
                    break;
            }
                move=true;
                pions=new Pion[NBPIONS+NBSTATUES];
                for(Pion t_pion : game.getPions())
                {
                    pions[t_pion.ID]=t_pion;
                }

            DJgame.miseAJour();

        }
    }

    public boolean CanCTRLZ()
    {
        if(Actual_pos==0)
        {
            return false;
        }
        else
        {
            return true;
        }
    }

    public void CTRLTZ()
    {
        if(Actual_pos>0)
        {
            ETAT etat;
            do {
                CTRLZ();
                etat = DJgame.getEtape();
            }while(etat != ETAT.MOVE1 && DJgame.getEtape() != ETAT.SELECT);

        }
    }

    public boolean CanCTRLTZ()
    {
        if(Actual_pos==0 || DJgame.getEtape() == ETAT.MOVE1 ||DJgame.getEtape() == ETAT.SELECT )
        {
            return false;
        }
        else
        {
            return true;
        }
    }

    public void CTRLY()
    {
        if(Actual_pos<Max_pos)
        {
            Actual_pos++;
            ETAT t_etat = temp.get(Actual_pos).etat;
            int pred_focus=-1;
            int pos=temp.get(Actual_pos).PionFocus;
            Pion[] temp0=temp.get(Actual_pos).GetCases();
            int j1=0;
            int j2=0;
            int i=0;
            boolean S_j1 = false;
            boolean S_j2 = false;
            for(Pion t_pion : temp0)
            {
                if(t_pion!=null)
                if(t_pion instanceof PionBasique) {
                    if (i < NBPIONS / 2) {
                        j1++;
                    } else {
                        j2++;
                    }
                }
                else
                {
                    if(t_pion.ID>NBPIONS+2)
                    if(t_pion.ID>NBPIONS+5)
                    {
                        S_j2=true;
                        System.out.println("findj2");
                    }
                    else
                    {
                        S_j1=true;
                        System.out.println("findj1");
                    }
                }
                i++;
            }
            game.getJoueur(0).SetNbPionsRestants( NBPIONS/2-(j1)-(NBPIONS/2-(j1)-game.getJoueur(0).getNbPionsRestants()));
            game.getJoueur(1).SetNbPionsRestants( NBPIONS/2-(j2)-(NBPIONS/2-(j2)-game.getJoueur(1).getNbPionsRestants()));
            game.getJoueur(0).SetStatuePlaced(S_j1);
            game.getJoueur(1).SetStatuePlaced(S_j2);
            int oldpos = pos;
            if(pos>=NBPIONS/2)
            {
                pos = (pos-(NBPIONS/2-(NBPIONS/2-j1)))-1-(NBPIONS/2-(j2+game.getJoueur(1).getNbPionsRestants()));
            }
            int pred_focusJ1;
            int pred_focusJ2;
            if(Actual_pos-1>0)
            {
                //pred_focus=temp.get(Actual_pos-2).GetCases()[oldpos].getEpoque();
                pred_focusJ1 = temp.get(Actual_pos-1).FocusJ1;
                pred_focusJ2=temp.get(Actual_pos-1).FocusJ2;
                pred_focus=(oldpos>=NBPIONS/2?temp.get(Actual_pos-1).FocusJ2:temp.get(Actual_pos-1).FocusJ1);
            }
            else
            {

                if(t_etat==ETAT.IDLE)
                    game.SetPions(filter(Grille.Clone(temp.get(Actual_pos).GetCases())));
                //pred_focus=game.getPions().get(pos).getEpoque();
                pred_focusJ1 = temp.get(Actual_pos).FocusJ1;
                pred_focusJ2=temp.get(Actual_pos).FocusJ2;
                pred_focus=(oldpos>=NBPIONS/2?pred_focusJ2:pred_focusJ1);

            }
            System.out.println("etat: "+t_etat);
            switch(t_etat)
            {
                case IDLE:
                    System.out.println("IDLE");
                    game.getJoueurs()[0].setFocus(pred_focusJ1);
                    game.getJoueurs()[1].setFocus(pred_focusJ2);
                    Actual_pos--;
                    DJgame.switchPlayer();
                    Max_pos--;
                    temp.remove(temp.size()-1);
                    game.SetPions(filter(Grille.Clone(temp.get(Actual_pos).GetCases())));
                    DJgame.setPionActuel(game.pionsFocusJoueur(pred_focus,DJgame.getJoueurActuel()).get(0));
                    DJgame.joueurActuel.nbActionsRestantes=2;
                    break;
                case SELECT:
                    System.out.println("SELECT");
                    game.getJoueurs()[0].setFocus(pred_focusJ1);
                    game.getJoueurs()[1].setFocus(pred_focusJ2);
                    Actual_pos--;
                    DJgame.switchPlayer();
                    Max_pos--;
                    temp.remove(temp.size()-1);
                    game.SetPions(filter(Grille.Clone(temp.get(Actual_pos).GetCases())));
                    DJgame.joueurActuel.nbActionsRestantes=2;

                    DJgame.setPionActuel(null);
                    break;

                case MOVE1:
                    System.out.println("MOVE1");
                    game.SetPions(filter(Grille.Clone(temp.get(Actual_pos).GetCases())));
                    // TODO : vérifier que ça ne cause pas de problèmes
                    if (game.getPions().get(pos) instanceof PionBasique) {
                        DJgame.setPionActuel((PionBasique) game.getPions().get(pos));
                    }
                    DJgame.joueurActuel.nbActionsRestantes=1;
                    break;

                case MOVE2:
                    System.out.println("FOCUS");
                    game.SetPions(filter(Grille.Clone(temp.get(Actual_pos).GetCases())));
                    // TODO : vérifier que ça ne cause pas de problèmes
                    if (game.getPions().get(pos) instanceof PionBasique) {
                        DJgame.setPionActuel((PionBasique) game.getPions().get(0));
                    }
                    DJgame.joueurActuel.nbActionsRestantes=0;
                    if(temp.get(Actual_pos-1).etat==ETAT.MOVE2)
                    {
                        if( DJgame.joueurActuel== game.joueurs[0])
                        {
                            DJgame.joueurActuel=game.joueurs[1];
                        }
                        else
                        {
                            DJgame.joueurActuel=game.joueurs[0];
                        }
                        System.out.println("hey222");
                    }

                    break;
            }
            /*for (Grille t_pion : temp)
            {
                Pion[] t_pions=t_pion.GetCases();
                System.out.println(t_pion.etat);
                for( i=0;i<NBPIONS+NBSTATUES;i++)
                {
                    if(t_pions[i]!=null)
                    {
                        System.out.println("i :"+i+" "+t_pions[i].getCoordonnees()+" "+t_pions[i].getEpoque());
                    }
                    else
                    {
                        System.out.println("i :"+i+" null");
                    }
                }
                System.out.println("__________");}
            System.out.println("________________________");*/
            move=true;
            pions=new Pion[NBPIONS+NBSTATUES];
            for(Pion t_pion : game.getPions())
            {
                pions[t_pion.ID]=t_pion;
            }
            DJgame.miseAJour();

        }

    }

    public boolean CanCTRLY()
    {
        if(Actual_pos==(Max_pos-1))
        {
            return false;
        }
        else
        {
            return true;
        }
    }

    public Vector<String> GetFiles()
    {
        Vector<String> savesNames = new Vector<>();
        File dir = new File(path);
        File[] directoryListing = dir.listFiles();
        if (directoryListing != null) {
            for (File child : directoryListing) {
                String name = child.getName();
                savesNames.add(name.contains(".") ? name.substring(0, name.lastIndexOf('.')) : name);
            }
        } else {
            System.out.println("Erreur : dossier de sauvegardes non trouvé");
        }
        return savesNames;
    }

    public void reset()
    {
        Actual_pos=0;
        Max_pos=1;
        temp= new ArrayList<Grille>();
        move=false;
    }

    public void AddLog(ETAT t_etat)
    {
        if(t_etat != null)
        {
            if(DJgame.getPionActuel() != null)
            {
                temp.add(new Grille(Grille.Clone(pions),t_etat,DJgame.getPionActuel().ID,game.joueurs[0].getFocus(),game.joueurs[1].getFocus()));
            }
            else
            {
                temp.add(new Grille(Grille.Clone(pions),t_etat,0,game.joueurs[0].getFocus(),game.joueurs[1].getFocus()));
            }

        }
        else
        {
            if(DJgame.getPionActuel() != null)
            {
                temp.add(new Grille(Grille.Clone(pions),DJgame.getEtape(),DJgame.getPionActuel().ID,game.joueurs[0].getFocus(),game.joueurs[1].getFocus()));
            }
            else
            {
                temp.add(new Grille(Grille.Clone(pions),DJgame.getEtape(),0,game.joueurs[0].getFocus(),game.joueurs[1].getFocus()));
            }
        }

        Max_pos=temp.size();
        Actual_pos++;
        move=false;
    }

    public void UpdateLog(Pion be, Pion af)
    {
        
        if(Actual_pos<(Max_pos-1) && !move && !init )
        {
            System.out.println("changement");
            ArrayList<Grille> tmp = new ArrayList<>();
            for(int i=0;i<=Actual_pos;i++)
            {
                tmp.add(new Grille(Grille.Clone(temp.get(i).GetCases()),temp.get(i).etat,temp.get(i).PionFocus, temp.get(i).FocusJ1, temp.get(i).FocusJ2));
            }
            temp = tmp;
            Max_pos=temp.size();
            Actual_pos=Max_pos-1;
        }

            int nbpions = game.pionsFocusJoueur(DJgame.getPionActuel().getEpoque(),DJgame.getJoueurActuel()).size();
            float nbtour = (float)(Actual_pos+1)/4;
            int etat = (int)((nbtour-(float)(Max_pos/4))*4); 
            if(nbpions == 1 && etat == 1)
                addetat++;
            if(af != null)
            {
                if(af instanceof PionBasique)
                {

                    pions[af.ID]=af.copy(af.getJoueur());
                    temp.get(Actual_pos).GetCases()[af.ID]=(PionBasique) pions[af.ID];

                }
                else
                {
                    pions[af.ID]=af.copy();
                    temp.get(Actual_pos).GetCases()[af.ID]=(Statue) pions[af.ID];
                    System.out.println(temp.get(Actual_pos).GetStatue()[af.ID]);
                }


            }
            else
            {
                if(be!=null)
                {
                    if(be.ID == temp.get(Actual_pos).PionFocus)
                        temp.get(Actual_pos).etat=ETAT.MOVE2;
                    temp.get(Actual_pos).GetCases()[be.ID]=null;
                    pions[be.ID]=null;
                }

            }
            temp.get(Actual_pos).FocusJ1=game.joueurs[0].getFocus();
            temp.get(Actual_pos).FocusJ2=game.joueurs[1].getFocus();

            Max_pos=temp.size();

        //----------------
            for (Grille t_pion : temp)
            {
                Pion[] t_pions=t_pion.GetCases();
                for(int i=0;i<NBPIONS+NBSTATUES;i++)
                {
                    if(t_pions[i]!=null)
                    {
                        System.out.println("i :"+i+" "+t_pions[i].getCoordonnees()+" "+t_pions[i].getEpoque());
                    }
                    else
                    {
                        System.out.println("i :"+i+" null");
                    }
                }
                System.out.println("__________");}
            System.out.println("________________________");


        move=true;
        init=false;
    }
    
    private Pion[] filter(Pion[] t_pion)
    {
        ArrayList<Pion> t_pion2= new ArrayList<>();
       int j=0;
        for(int i=0 ;i<t_pion.length;i++)
        {
            if(t_pion[i]!= null)
            {
                t_pion2.add(t_pion[i]);
            }
        }
        return t_pion2.toArray(new Pion[0]);
    }

    public ControleurMediateur getControlleur()
    {
        return controleur;
    }
}
