package Modele;
import Patterns.Observateur;
import Patterns.Grille;
import java.io.File; 
import java.io.FileWriter;
import java.io.FileReader;
import java.util.Vector;
import java.util.ArrayList;
import java.util.Arrays;
import java.io.IOException;

public class ManageFiles implements Observateur {
    
    private ArrayList<Grille> temp;
    private Jeu game;
    private String path;
    private Pion[] pions; 
    private boolean move=false;
    public boolean init = true;
    public int Actual_pos=0;
    public int Max_pos=1;
    private int nbjpions[];

    private final int SEPSECT = 10000;
    private final int NBPIONS;

    public ManageFiles(Jeu t_game, String absolutepath)
    {
        game = t_game;
        NBPIONS=game.NBPIONS;
        temp =  new ArrayList<Grille>();
        pions=new Pion[NBPIONS];
        Pion[] tmp0 = new Pion[NBPIONS];
        System.arraycopy(tmp0,0,game.getPions().toArray(),0,game.getPions().size());
        temp.add(new Grille(tmp0));
        nbjpions=new int[2];
        nbjpions[0]=0;
        nbjpions[1]=NBPIONS/2;
        path=absolutepath;
        for (Pion pion : game.getPions()) 
        {
            if(pion.getJoueur()==game.getJoueurs()[0])
            {
                pions[nbjpions[0]]=pion;
                nbjpions[0]++;
            }
            else
            {
                pions[nbjpions[1]]=pion;
                nbjpions[1]++;
            }

        }
        //Actual_pos++;

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
            int pred_focus;
            System.out.println("pred:"+pred);
            System.out.println("actual:"+actual);
            if(Actual_pos-1>=0)
            {
                System.out.println("pred");
                pred_focus=game.getPions().get(pred).getEpoque();
            }
            else
            { 
                System.out.println("actual");
                pred_focus=game.getPions().get(actual).getEpoque();
            }

            if(actual!=(-1) && pred == (-1))
            {
                if(game.joueurActuel == game.joueurs[0]) {
                    game.joueurActuel=game.joueurs[1];
                }
                else {
                    game.joueurActuel=game.joueurs[0];
                }
                game.joueurActuel.nbActionsRestantes=0;
                game.joueurActuel.setFocus(pred_focus);
            }
            else
            {   
                game.SetPions(Grille.Clone(temp.get(Actual_pos).GetCases()));
                int nbite = (int)(Math.floor(temp.size()-Actual_pos)/3);
                int player = ((int)nbite/2)&1;
                int nbAction = ((int)(Math.floor(temp.size()-(nbite+Actual_pos)))/4)&1; //paire pour la 2 ème action et impaire pour la première action 
                if(nbAction==0)
                {
                    if(game.joueurActuel == game.joueurs[0]) {
                        game.joueurActuel=game.joueurs[1];
                        System.out.println("joueur1");
                    }
                    else {
                        game.joueurActuel=game.joueurs[0];
                        System.out.println("joueur2");
                    }

                   // game.joueurActuel.setFocus();
                    game.switchPlayer();
                }
                else
                {
                    if(actual==-1)
                    {
                        
                        game.pionActuel=game.getPions().get(pred);
                    }
                    else
                    {
                        game.pionActuel=game.getPions().get(actual);
                    }
                    
                    game.joueurActuel.nbActionsRestantes=1;
                    game.joueurActuel.setFocus(pred_focus);
                }
                move=true;
                game.miseAJour();
            }
            
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
            game.SetPions(Grille.Clone(temp.get(Actual_pos).GetCases()));
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
        init = true;
    }

    @Override
    public void metAJour()
    {
        if(Actual_pos!=(Max_pos-1) && !move && !init)
        {
            ArrayList<Grille> tmp = new ArrayList<>();
            for(int i=0;i<=Actual_pos;i++)
            {
                tmp.add(temp.get(i));
            }
            temp = tmp;
            Max_pos=temp.size();
            Actual_pos=Max_pos-1;
        }
        if(!move && !init)
        {
            Joueur j = game.getJoueurActuel();
            int player = j == game.getJoueurs()[0]? 0 : 1;
            Grille tmpgrille = new Grille(pions);

            int rs;
            Pion[] tmp_pions =game.getPions().toArray(new Pion[0]);
            tmp_pions = Arrays.copyOf(tmp_pions, tmp_pions.length);
            if(tmp_pions != null)
            {
                rs = tmpgrille.Compare(tmp_pions);
                if(pions[rs]==null)
                {
                    pions[nbjpions[player]]=new Pion(tmp_pions[rs]);
                    nbjpions[player]++;
                }
                else
                {
                    pions[rs]=new Pion(tmp_pions[rs].Clone());
                }
            }
            
            temp.add(new Grille(Grille.Clone(pions)));
            Max_pos=temp.size();
            Actual_pos=Max_pos-1;
        }
        
        move=false;
        init=false;
        //----------------
        for (Grille t_pion : temp) 
        {
            Pion[] t_pions=t_pion.GetCases();
            for(int i=0;i<NBPIONS;i++)
            {
                if(t_pions[i]!=null)
                {
                    System.out.println("i :"+i+" "+t_pions[i].coordonnees);
                }
                else
                {
                    System.out.println("i :"+i+" null");
                }
            }
            System.out.println("__________");  
        }    
        System.out.println("________________________");   
        
    }
    
    private int find(Pion t_pion)
    {
        for(int i=0 ;i<NBPIONS;i++)
        {
            if(pions[i]==t_pion && pions[i]!= null)
                return i;
        }
        return -1;
    }
}
