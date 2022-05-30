package Vue;

import Modele.DeroulementJeu;
import Patterns.Observateur;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.event.AdjustmentEvent;
import java.awt.event.AdjustmentListener;

public class Historique implements Observateur {

    JScrollPane pane;
    private JList list;
    DeroulementJeu dj;

    Historique(DeroulementJeu dj, CollecteurEvenements controle){
        dj.ajouteObservateur(this);
        list = new JList();
        this.dj=dj;
        list.addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                if(!e.getValueIsAdjusting()) {
                    Commande tmpCom = new Commande("historique");
                    tmpCom.setSaveName(Integer.toString(list.getSelectedIndex()));
                    if(list.getSelectedIndex() != -1)
                        controle.commande(tmpCom);
                }
            }
        });
        pane = new JScrollPane(list);
        pane.getVerticalScrollBar().addAdjustmentListener(new AdjustmentListener() {
            public void adjustmentValueChanged(AdjustmentEvent e) {
                e.getAdjustable().setValue(e.getAdjustable().getMaximum());
            }
        });
    }

    public JScrollPane getPane() {
        return pane;
    }

    @Override
    public void metAJour() {
        list.removeAll();
        String [] actionlist = dj.MemoryManager.ListAction();
        DefaultListModel listModel= new DefaultListModel();
        for(String t_action : actionlist)
            listModel.addElement(t_action);
        list.setModel(listModel);
    }
}
