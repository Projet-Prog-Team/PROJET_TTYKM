package Modele;
import Patterns.Grille;
import java.io.File; 
import java.io.FileWriter;
import java.io.FileReader;
import java.util.Vector;

import javax.lang.model.util.SimpleElementVisitor6;

import java.util.ArrayList;
import java.util.Arrays;
import java.io.IOException;

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

    private final int SEPSECT = 10000;
    private final int NBPIONS;

    public ManageFiles(DeroulementJeu t_game, String absolutepath)
    {
        DJgame = t_game;
        game=DJgame.getJeu();
        NBPIONS=game.NBPIONS;
        temp =  new ArrayList<Grille>();
        pions=new Pion[NBPIONS];
        Pion [] tmp =game.getPions().toArray(new Pion[0]);
        for(int i =0 ;i < tmp.length;i++)
        {
            pions[tmp[i].ID]=tmp[i].copy(tmp[i].getJoueur());
        }
        temp.add(new Grille(Grille.Clone(pions),ETAT.IDLE,0));
        path=absolutepath;

    }

    public String GetPath()
    {
        return path;
    }
/*
    public void Save(String filepath)
    {
        try
        {
            File file = new File(path+filepath);
            if(!file.exists())
                file.createNewFile();
            FileWriter Writer = new FileWriter(file);
            int k=0;
            //player, nblignes nbcolonnes, pos,maxpos
            Writer.write(game.getPlayer() ? 1 : 0);
            
            Writer.write(" ");
            Writer.write(game.lignes);
            
            Writer.write(" ");
            Writer.write(game.colonnes);
           
            Writer.write(" ");
            Writer.write(Actual_pos);
           
            Writer.write(" ");
            Writer.write(Max_pos);
            
            //content of logs
            while(k!=Max_pos)
            {
                Etat [][] tmp=temp.get(k).GetCases();
                for(int i=0;i<game.lignes;i++)
                {
                    for(int j=0;j<game.colonnes;j++)
                    {
                        int p=0;
                        switch(tmp[i][j])
                        {
                            case EMPTY:
                                p=1;
                                break;

                            case NORMAL:
                                p=2;
                                break;

                            case POISON:
                                p=3;
                                break;
                        }
                        Writer.write(p);
                    }
                    Writer.write("|");
                }
                k++;
                Writer.write("\n");

            }
            Writer.close();
        } catch (IOException e) {
            System.out.println("Gaufre tempfile cannot read or Gauffre cannot write file");
            e.printStackTrace();
        }
    }

    public void Load(String filepath)
    {
        try
        {
            FileReader Reader = new FileReader(path+filepath);

            int player=Reader.read();
            if(player == 0)
            {
                game.setPlayer(false);
            }
            else
            {
                game.setPlayer(true);
            }
            //read colonnes and lignes
            Reader.read();
            int lignes=Reader.read();
            Reader.read();
            int colonnes=Reader.read();
            game.cases= new Etat[lignes][colonnes];
            game.lignes = lignes;
            game.colonnes=colonnes;
            int k=0;
            //read pos and maxpos
            Reader.read();
            Actual_pos=Reader.read();
            Reader.read();
            Max_pos=Reader.read();
            //read content
            temp =  new ArrayList<Grille>();
            while(k!=Max_pos)
            {
                Etat [][] tmp=new Etat[lignes][colonnes];
                for(int i=0;i<game.lignes;i++)
                {
                    for(int j=0;j<game.colonnes;j++)
                    {
                        int p=Reader.read();
                        switch(p)
                        {
                            case 1:
                                tmp[i][j] = Etat.EMPTY;
                                break;

                            case 2:
                                tmp[i][j] = Etat.NORMAL;
                                break;

                            case 3:
                                tmp[i][j] = Etat.POISON;
                                break;
                        }
                
                    }
                    Reader.read();
                }
                Reader.read();
                
                temp.add(new Grille(tmp));
                k++;

            }
            init=true;
            game.cases=temp.get(Actual_pos).GetCases();
            game.miseAJour();
            Reader.close();
        } catch (IOException e) {
            System.out.println("Gaufre tempfile cannot write or Gauffre cannot read file");
            e.printStackTrace();
        }

    }*/

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
            int pred = -2;
            if(Actual_pos-1>=0)
                pred = temp.get(Actual_pos).Compare(temp.get(Actual_pos-1).GetCases()); // si pred et actual reçoivent -1 alors on est sur la selection d'un focus sinon sur un pions
 
            int actual = temp.get(Actual_pos).Compare(temp.get(Actual_pos+1).GetCases());
            int pred_focus=-1;
            int red=(NBPIONS/2-game.joueurs[0].getNbPionsRestants())+1;
            int pos=temp.get(Actual_pos).PionFocus;
            System.out.println("New ETAT: "+temp.get(Actual_pos).etat);
            System.out.println("Next ETAT: "+temp.get(Actual_pos+1).etat);
            Pion[] temp0=temp.get(Actual_pos).GetCases();
            /*for(pos=0;pos<temp0.length;pos++)
            {
                if(temp0[pos]!=null && temp0[pos].focused)
                    break;
            }*/
            int j1=0;
            int i=0;
            for(Pion t_pion : temp0)
            {
                if(t_pion != null && i<=NBPIONS/2)
                {
                    j1++;
                }
                i++;
            }
            int oldpos = pos;
            if(pos>=NBPIONS/2)
            {
                pos = pos-(NBPIONS/2-(NBPIONS/2-j1));
            }

            if(Actual_pos-1>0 && t_etat!=ETAT.IDLE)
            {
                pred_focus=temp.get(Actual_pos-1).GetCases()[oldpos].getEpoque();
            }
            else
            {

                if(t_etat==ETAT.IDLE)
                    game.SetPions(filter(Grille.Clone(temp.get(Actual_pos).GetCases())));
                pred_focus=game.getPions().get(pos).getEpoque();
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
                    DJgame.setPionActuel(game.getPions().get(pos));
                    System.out.println(DJgame.getJoueurActuel().getID()-1+" "+(DJgame.getPionActuel().ID/2)%2);
                    /*if(game.getJoueurActuel().getID()-1 != (game.getPionActuel().ID>NBPIONS/2 ? 1 :0))
                        game.joueurActuel=game.joueurs[(game.getPionActuel().ID>NBPIONS/2 ? 1 :0)];*/
                    DJgame.joueurActuel.nbActionsRestantes=1;
                    break;

                /*case MOVE2:
                    System.out.println("MOVE2");
                    game.SetPions(filter(Grille.Clone(temp.get(Actual_pos).GetCases())));
                    game.pionActuel=game.getPions().get(pos);
                    game.joueurActuel.nbActionsRestantes=0;
                    break;*/

                case MOVE2:
                    System.out.println("FOCUS");
                    DJgame.setPionActuel(game.getPions().get(0));
                    DJgame.joueurActuel.nbActionsRestantes=0;
                    if( DJgame.joueurActuel== game.joueurs[0])
                    {
                        DJgame.joueurActuel=game.joueurs[1];
                    }
                    else
                    {
                        DJgame.joueurActuel=game.joueurs[0];
                    }
                    DJgame.getJoueurActuel().setFocus(pred_focus);
                    break;
            }
            for(Grille t_grille : temp)
            {
                System.out.println(t_grille.etat);
            }
           /*
                float nbtour = (float)(Actual_pos+1)/4;
                //int etat = (int)((Actual_pos/4-(float)(Actual_pos/4))*4); 
                int etat;
                if((int)((nbtour-(float)(Max_pos/4))*4)>-2)
                {
                    etat=(int)((nbtour-(float)(Max_pos/4))*4);
                } 
                else
                {
                    etat=(int)(((float)(Max_pos/4)-nbtour)*4);
                }
                
                float tmp =((float)(addetat)/4-(int)((float)(addetat)/4))*4;
                float etat2=(float)etat;
                etat = (int)(((etat2+tmp)/4-(int)((etat2+tmp)/4))*4);
                System.out.println("n: "+etat);
                System.out.println("nbt: "+nbtour);
                System.out.println("maxpos: "+(float)(Max_pos/4));
                System.out.println("new addetat: "+tmp);
                if(etat == 1)
                {//au cas où il y a un seul pion sur le plateau
                    int nbpions = game.pionsFocusJoueur(game.getPionActuel().getEpoque(),game.getJoueurActuel()).size();
                    if(nbpions == 1)
                    {
                        addetat--;
                        etat--;
                        System.out.println("etat1!!!");
                    }
                    
                }
                System.out.println("CTRLZ-etat:"+etat);
                switch(etat)
                {
                    case 1:
                        game.SetPions(filter(Grille.Clone(temp.get(Actual_pos).GetCases())));
                        game.joueurActuel.nbActionsRestantes=2;
                        game.pionActuel=null;
                        break;

                    case 2:
                        game.SetPions(filter(Grille.Clone(temp.get(Actual_pos).GetCases())));
                        game.pionActuel=game.getPions().get(actual-red);
                        game.joueurActuel.nbActionsRestantes=2;
                        break;

                    case 3:
                        game.SetPions(filter(Grille.Clone(temp.get(Actual_pos).GetCases())));
                        game.pionActuel=game.getPions().get(pred-red);
                        game.joueurActuel.nbActionsRestantes=1;
                        break;

                    case 0:
                        
                        game.pionActuel=game.getPions().get(0);
                        game.joueurActuel.nbActionsRestantes=0;
                        if( game.joueurActuel== game.joueurs[0])
                        {
                            game.joueurActuel=game.joueurs[1];
                        }
                        else
                        {
                            game.joueurActuel=game.joueurs[0];
                        }
                        game.getJoueurActuel().setFocus(pred_focus);
                        //game.switchPlayer();
                        break;
                }*/
                
                move=true;
                pions=new Pion[NBPIONS];
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
                System.out.println(etat);
            }while(etat != ETAT.MOVE1 && DJgame.getEtape() != ETAT.SELECT);
           /* if(etat == ETAT.IDLE) {
                DJgame.setPionActuel(game.pionsFocusJoueur(temp.get(Actual_pos).Focus, DJgame.getJoueurActuel()).get(0));
                System.out.println("hey1");
            }
            else
            {
                DJgame.setPionActuel(null);
                System.out.println("hey2");
            }
            game.SetPions(filter(temp.get(Actual_pos).GetCases()));
            DJgame.joueurActuel.nbActionsRestantes=2;*/

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
            /*switch(temp.get(Actual_pos-1).etat)
            {
            /*case FOCUS:
                t_etat=ETAT.FOCUS;
                System.out.println("hey");
                break;
                case MOVE1:
                    if(game.pionsFocusJoueur(game.getPionActuel().getEpoque(),game.getJoueurActuel()).size()!=1)
                    {
                        t_etat=ETAT.SELECT;
                    }break;
            }*/
            int pred = -2;
            if(Actual_pos-2>=0)
                pred = temp.get(Actual_pos).Compare(temp.get(Actual_pos-2).GetCases()); // si pred et actual reçoivent -1 alors on est sur la selection d'un focus sinon sur un pions

            int actual = temp.get(Actual_pos).Compare(temp.get(Actual_pos-1).GetCases());
            int pred_focus=-1;
            int red=(NBPIONS/2-game.joueurs[0].getNbPionsRestants())+1;
            int pos=temp.get(Actual_pos).PionFocus;
            System.out.println("ETAT: "+temp.get(Actual_pos-1).etat);
            System.out.println("New ETAT: "+temp.get(Actual_pos).etat);
            Pion[] temp0=temp.get(Actual_pos).GetCases();
        /*for(pos=0;pos<temp0.length;pos++)
        {
            if(temp0[pos]!=null && temp0[pos].focused)
                break;
        }*/
            int j1=0;
            int i=0;
            for(Pion t_pion : temp0)
            {
                if(t_pion != null && i<=NBPIONS/2)
                {
                    j1++;
                }
                i++;
            }
            int oldpos = pos;
            if(pos>=NBPIONS/2)
            {
                pos = pos-(NBPIONS/2-(NBPIONS/2-j1));
            }

            if(Actual_pos-2>0 && t_etat!=ETAT.IDLE)
            {
                pred_focus=temp.get(Actual_pos-2).GetCases()[oldpos].getEpoque();
            }
            else
            {

                if(t_etat==ETAT.IDLE)
                    game.SetPions(filter(Grille.Clone(temp.get(Actual_pos).GetCases())));
                pred_focus=game.getPions().get(pos).getEpoque();
            }

            switch(t_etat)
            {
                case IDLE:
                    System.out.println("IDLE");
                    Actual_pos--;
                    DJgame.getJoueurActuel().setFocus(temp.get(Actual_pos).Focus);
                    DJgame.switchPlayer();
                    game.SetPions(filter(Grille.Clone(temp.get(Actual_pos).GetCases())));
                    DJgame.setPionActuel(game.pionsFocusJoueur(pred_focus,DJgame.getJoueurActuel()).get(0));
                    DJgame.joueurActuel.nbActionsRestantes=2;
                    break;
                case SELECT:
                    System.out.println("SELECT");
                    Actual_pos--;
                    DJgame.getJoueurActuel().setFocus(temp.get(Actual_pos).Focus);
                    DJgame.switchPlayer();
                    game.SetPions(filter(Grille.Clone(temp.get(Actual_pos).GetCases())));
                    DJgame.joueurActuel.nbActionsRestantes=2;

                    DJgame.setPionActuel(null);
                    break;

                case MOVE1:
                    System.out.println("MOVE1");
                    game.SetPions(filter(Grille.Clone(temp.get(Actual_pos).GetCases())));
                    DJgame.setPionActuel(game.getPions().get(pos));
                    DJgame.joueurActuel.nbActionsRestantes=1;
                    break;

                case MOVE2:
                    System.out.println("FOCUS");
                    game.SetPions(filter(Grille.Clone(temp.get(Actual_pos).GetCases())));
                    DJgame.setPionActuel(game.getPions().get(0));
                    DJgame.joueurActuel.nbActionsRestantes=0;
                    break;
            }
            move=true;
            pions=new Pion[NBPIONS];
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
                temp.add(new Grille(Grille.Clone(pions),t_etat,DJgame.getPionActuel().ID,DJgame.joueurActuel.focus));
            }
            else
            {
                temp.add(new Grille(Grille.Clone(pions),t_etat,0,DJgame.joueurActuel.focus));
            }

        }
        else
        {
            temp.add(new Grille(Grille.Clone(pions),DJgame.getEtape(),DJgame.getPionActuel().ID));
        }

        Max_pos=temp.size();
        Actual_pos++;
        move=false;
       /* for (Grille t_pion : temp)
        {
                /*Pion[] t_pions=t_pion.GetCases();
                for(int i=0;i<NBPIONS;i++)
                {
                    if(t_pions[i]!=null)
                    {
                        System.out.println("i :"+i+" "+t_pions[i].getCoordonnees());
                    }
                    else
                    {
                        System.out.println("i :"+i+" null");
                    }
                }
                System.out.println("__________");
            System.out.println("al: "+t_pion.etat);
        }
        System.out.println("________________________");*/
    }

    public void UpdateLog(Pion be, Pion af)
    {
        
        if(Actual_pos<(Max_pos-1) && !move && !init )
        {
            System.out.println("changement");
            ArrayList<Grille> tmp = new ArrayList<>();
            for(int i=0;i<=Actual_pos;i++)
            {
                tmp.add(new Grille(Grille.Clone(temp.get(i).GetCases()),temp.get(i).etat,temp.get(i).PionFocus));
            }
            temp = tmp;
            Max_pos=temp.size();
            Actual_pos=Max_pos-1;
        }

        if(!move && !init )
        {

            int nbpions = game.pionsFocusJoueur(DJgame.getPionActuel().getEpoque(),DJgame.getJoueurActuel()).size();
            float nbtour = (float)(Actual_pos+1)/4;
            int etat = (int)((nbtour-(float)(Max_pos/4))*4); 
            if(nbpions == 1 && etat == 1)
                addetat++;
            if(af != null)
            {
                temp.get(Actual_pos).GetCases()[af.ID]=af.copy(af.getJoueur());
                pions[af.ID]=af.copy(af.getJoueur());
            }
            else
            {
                if(be!=null)
                {
                    temp.get(Actual_pos).GetCases()[be.ID]=null;
                    pions[be.ID]=null;
                }
                temp.get(Actual_pos).Focus=DJgame.getJoueurActuel().getFocus();

            }      
            Max_pos=temp.size();
        
        //----------------
            /*for (Grille t_pion : temp)
            {
                /*Pion[] t_pions=t_pion.GetCases();
                for(int i=0;i<NBPIONS;i++)
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
                System.out.println("__________");
                System.out.println(t_pion.Focus);
            }
            System.out.println("________________________");*/
            System.out.println(DJgame.getJoueurActuel().nbActionsRestantes);

        }
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
}
