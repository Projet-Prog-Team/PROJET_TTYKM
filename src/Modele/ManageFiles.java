package Modele;
import Controleur.ControleurMediateur;
import Patterns.Grille;
import Structures.Point;

import java.io.File;
import java.io.FileWriter;
import java.io.FileReader;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.Path;
import java.nio.file.Paths;
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

    private ControleurMediateur controleur;

    private final int SEPSECT = 10000;
    private final int NBPIONS;

    public ManageFiles(ControleurMediateur t_controleur, String absolutepath)
    {
        controleur=t_controleur;
        DJgame = controleur.dj;
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
        Path res =null;
       /* try
        {
            res=Paths.get(this.getClass().getClassLoader().getResource("/").toURI());
        }catch(URISyntaxException e)
        {
            System.out.println(e);
        }
        //System.out.println(this.getClass().getClassLoader().getResource("/").getPath());
        path=res.toAbsolutePath().toString();*/
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
            Grille tmpgrille=null;
            for(Grille t_grille : temp)
            {
                if(tmpgrille == null)
                    tmpgrille=t_grille;
                Writer.write(t_grille.Focus);
                Writer.write(t_grille.PionFocus);
                while(tmpgrille.Compare(t_grille.GetCases())!=-1)
                {
                    int i =tmpgrille.Compare(t_grille.GetCases());
                    Writer.write(i);
                    if(t_grille.GetCases()[i] != null) {
                        Writer.write(t_grille.GetCases()[i].getCoordonnees().getC());
                        Writer.write(t_grille.GetCases()[i].getCoordonnees().getL());
                        Writer.write(t_grille.GetCases()[i].getEpoque());
                        Writer.write(t_grille.GetCases()[i].focused ? 1 : 0);
                        tmpgrille.GetCases()[i] = t_grille.GetCases()[i].copy(t_grille.GetCases()[i].getJoueur());
                    }
                    else
                    {
                        tmpgrille.GetCases()[i] = null;
                        Writer.write('\t');
                    }
                }
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

    }
    public void Load(String filepath)
    {
        try
        {
            FileReader Reader = new FileReader(path+filepath);
            controleur.joueurs[0]=Reader.read();
            controleur.joueurs[1]=Reader.read();
            String diff="";
            switch(Reader.read())
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
            switch(Reader.read())
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
            Actual_pos= Reader.read();
            Max_pos= Reader.read();
            Reader.read();
            Grille tmpgrille=null;
            temp= new ArrayList<>();
            for(int i=0; i<Max_pos;i++)
            {
                if(tmpgrille != null)
                    tmpgrille=new Grille(Grille.Clone(tmpgrille.GetCases()),tmpgrille.etat, tmpgrille.PionFocus,tmpgrille.Focus);
                tmpgrille.Focus=Reader.read();
                tmpgrille.PionFocus= Reader.read();
                int rd=Reader.read();
                while(rd!='\n')
                {
                    int id =rd;
                    rd = Reader.read();
                    if(rd != '\t') {

                        int c = Reader.read();
                        int l = Reader.read();
                        tmpgrille.GetCases()[i].SetCoordonnees(new Point(l,c));
                        tmpgrille.GetCases()[i].SetEpoque(Reader.read());
                        tmpgrille.GetCases()[i].focused= Reader.read()==1;
                    }
                    else
                    {
                        tmpgrille.GetCases()[i] = null;
                    }
                    rd= Reader.read();
                }
                temp.add(tmpgrille);
            }
            Reader.close();
            BoardLoad(Actual_pos);
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
            int pred = -2;
            if(Actual_pos-1>=0)
                pred = temp.get(Actual_pos).Compare(temp.get(Actual_pos-1).GetCases()); // si pred et actual reçoivent -1 alors on est sur la selection d'un focus sinon sur un pions
 
            int actual = temp.get(Actual_pos).Compare(temp.get(Actual_pos+1).GetCases());
            int pred_focus=-1;
            int red=(NBPIONS/2-game.joueurs[0].getNbPionsRestants())+1;
            int pos=temp.get(Actual_pos).PionFocus;
            Pion[] temp0=temp.get(Actual_pos).GetCases();
            int j1=0;
            int j2=0;
            int i=0;
            for(Pion t_pion : temp0)
            {
                if(t_pion!=null)
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
            int oldpos = pos;

            if(pos>=NBPIONS/2)
            {
                pos = (pos-(NBPIONS/2-(NBPIONS/2-j1)))-1;
            }
            if(Actual_pos-1>0 && t_etat!=ETAT.IDLE)
            {
                //pred_focus=temp.get(Actual_pos-1).GetCases()[oldpos].getEpoque();
                pred_focus=temp.get(Actual_pos-1).Focus;
            }
            else
            {

                if(t_etat==ETAT.IDLE)
                    game.SetPions(filter(Grille.Clone(temp.get(Actual_pos).GetCases())));
                //pred_focus=game.getPions().get(pos).getEpoque();
                pred_focus=temp.get(Actual_pos).Focus;
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
                    DJgame.joueurActuel.nbActionsRestantes=1;
                    break;

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
            int pred = -2;
            if(Actual_pos-2>=0)
                pred = temp.get(Actual_pos).Compare(temp.get(Actual_pos-2).GetCases()); // si pred et actual reçoivent -1 alors on est sur la selection d'un focus sinon sur un pions

            int actual = temp.get(Actual_pos).Compare(temp.get(Actual_pos-1).GetCases());
            int pred_focus=-1;
            int red=(NBPIONS/2-game.joueurs[0].getNbPionsRestants())+1;
            int pos=temp.get(Actual_pos).PionFocus;
            Pion[] temp0=temp.get(Actual_pos).GetCases();
        /*for(pos=0;pos<temp0.length;pos++)
        {
            if(temp0[pos]!=null && temp0[pos].focused)
                break;
        }*/
            int j1=0;
            int j2=0;
            int i=0;
            for(Pion t_pion : temp0)
            {
                if(t_pion!=null)
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
            //game.getJoueur(1).SetNbPionsRestants(NBPIONS/2-(j2));
            int oldpos = pos;
            if(pos>=NBPIONS/2)
            {
                pos = (pos-(NBPIONS/2-(NBPIONS/2-j1)))-1;
            }
            if(Actual_pos-1>0)
            {
                //pred_focus=temp.get(Actual_pos-2).GetCases()[oldpos].getEpoque();
                pred_focus=temp.get(Actual_pos-1).Focus;
            }
            else
            {

                if(t_etat==ETAT.IDLE)
                    game.SetPions(filter(Grille.Clone(temp.get(Actual_pos).GetCases())));
                //pred_focus=game.getPions().get(pos).getEpoque();
                pred_focus=temp.get(Actual_pos).Focus;

            }
            switch(t_etat)
            {
                case IDLE:
                    System.out.println("IDLE");
                    DJgame.getJoueurActuel().setFocus(pred_focus);
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
                    DJgame.getJoueurActuel().setFocus(pred_focus);
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
                    if(be.ID == temp.get(Actual_pos).PionFocus)
                        temp.get(Actual_pos).etat=ETAT.MOVE2;
                    temp.get(Actual_pos).GetCases()[be.ID]=null;
                    pions[be.ID]=null;
                }


            }
            if(DJgame.getEtape() != ETAT.SELECT && DJgame.getEtape() != ETAT.IDLE)
            {
                temp.get(Actual_pos).Focus=DJgame.getJoueurActuel().getFocus();
            }
            else
            {
                temp.get(Actual_pos-1).Focus=DJgame.getJoueurActuel().getFocus();
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

    public ControleurMediateur getControlleur()
    {
        return controleur;
    }
}
