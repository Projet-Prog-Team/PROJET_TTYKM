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
    private Jeu game;
    private String path;
    private Pion[] pions; 
    private boolean move=true;
    private int addetat=1;
    public boolean init = false;
    public int Actual_pos=0;
    public int Max_pos=1;

    private final int SEPSECT = 10000;
    private final int NBPIONS;

    public ManageFiles(Jeu t_game, String absolutepath)
    {
        game = t_game;
        NBPIONS=game.NBPIONS;
        temp =  new ArrayList<Grille>();
        pions=new Pion[NBPIONS];
        Pion [] tmp =game.getPions().toArray(new Pion[0]);
        for(int i =0 ;i < 6;i++)
        {
            pions[tmp[i].ID]=tmp[i].copy(tmp[i].getJoueur());
        }
        temp.add(new Grille(Grille.Clone(pions)));
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
            int pred = -2;
            if(Actual_pos-1>=0)
                pred = temp.get(Actual_pos).Compare(temp.get(Actual_pos-1).GetCases()); // si pred et actual reçoivent -1 alors on est sur la selection d'un focus sinon sur un pions
 
            int actual = temp.get(Actual_pos).Compare(temp.get(Actual_pos+1).GetCases());
            int pred_focus=-1;
            int red=(NBPIONS/2-game.joueurs[0].getNbPionsRestants())+1;
            if(pred != -1 && pred != -2)
            {
                if(pred<NBPIONS/2 || red==1)
                    red=0;
                pred_focus=game.getPions().get(pred-red).getEpoque();
                
            }
            else
            { 
                if(actual != -1)
                {
                    if(actual<NBPIONS/2 || red==1)
                    red=0;
                    pred_focus=game.getPions().get(actual-red).getEpoque();
                }
            }
           
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
                }
                
                move=true;
                pions=new Pion[NBPIONS];
                for(Pion t_pion : game.getPions())
                {
                    pions[t_pion.ID]=t_pion;
                }
                game.miseAJour();
            
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

    public void CTRLY()
    {
        if(Actual_pos<Max_pos)
        {
            Actual_pos++;
            //game.SetPions(Grille.Clone(temp.get(Actual_pos).GetCases()));
            move=true;
            game.switchPlayer();
            game.miseAJour();
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

    public void AddLog()
    {
        temp.add(new Grille(Grille.Clone(pions)));
        Max_pos=temp.size();
        Actual_pos++;
        move=false;
    }

    public void UpdateLog(Pion be, Pion af)
    {
        
        if(Actual_pos!=(Max_pos-1) && !move && !init )
        {
            System.out.println("changement");
            ArrayList<Grille> tmp = new ArrayList<>();
            for(int i=0;i<=Actual_pos;i++)
            {
                tmp.add(new Grille(Grille.Clone(temp.get(i).GetCases())));
            }
            temp = tmp;
            Max_pos=temp.size();
            Actual_pos=Max_pos-1;
        }

        if(!move && !init )
        {
            int nbpions = game.pionsFocusJoueur(game.getPionActuel().getEpoque(),game.getJoueurActuel()).size();
            float nbtour = (float)(Actual_pos+1)/4;
            int etat = (int)((nbtour-(float)(Max_pos/4))*4); 
            if(nbpions == 1 && etat == 1)
                addetat++;
            System.out.println("etat:"+etat);
            System.out.println("addetat:"+addetat);
            if(af != null)
            {
                temp.get(Actual_pos).GetCases()[af.ID]=af.copy(af.getJoueur());
                pions[af.ID]=af.copy(af.getJoueur());
            }
            else
            {
                temp.get(Actual_pos).GetCases()[be.ID]=null;
                pions[be.ID]=null;
            }      
            Max_pos=temp.size();
        
        //----------------
            /*for (Grille t_pion : temp) 
            {
                Pion[] t_pions=t_pion.GetCases();
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
            }    
            System.out.println("________________________"); */
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
