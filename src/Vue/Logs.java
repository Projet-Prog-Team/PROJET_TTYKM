package Vue;

import Modele.DeroulementJeu;
import Patterns.Observateur;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.*;

public class Logs implements Observateur {

    private DeroulementJeu dj;
    private JList list;
    public Logs(JList t_list, DeroulementJeu t_dj,CollecteurEvenements t_control)
    {
        dj=t_dj;
        list=t_list;
        t_dj.ajouteObservateur(this);
        t_list.addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                if(!e.getValueIsAdjusting()) {
                    Commande tmpCom = new Commande("historique");
                    JList tmplist = (JList) e.getSource();
                    tmpCom.setSaveName(Integer.toString(tmplist.getSelectedIndex()));
                    if(tmplist.getSelectedIndex() != -1)
                    t_control.commande(tmpCom);
                }
            }
        });
    }

    @Override
    public void metAJour()
    {
        NewList();
    }

    public void NewList()
    {
        list.removeAll();
        String [] actionlist = dj.MemoryManager.ListAction();
        DefaultListModel listModel= new DefaultListModel();
        for(String t_action : actionlist)
            listModel.addElement(t_action);
        list.setModel(listModel);
    }
}
