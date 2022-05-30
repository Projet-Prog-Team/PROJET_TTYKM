package Vue;

import Modele.DeroulementJeu;
import Patterns.Observateur;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class Historique implements Observateur {

    JScrollPane pane;
    private JList list;
    DeroulementJeu dj;

    Historique(DeroulementJeu dj, CollecteurEvenements controle, IHMState state){
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
        list.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                state.setPasseNum(true);
            }

            @Override
            public void mouseExited(MouseEvent e) {
                state.setPasseNum(false);
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
        list.ensureIndexIsVisible(list.getModel().getSize() -1);
    }
}
