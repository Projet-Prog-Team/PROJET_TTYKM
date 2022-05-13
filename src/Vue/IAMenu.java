package Vue;


import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

public class IAMenu  {

    private JMenu menu;

    IAMenu(int IA, CollecteurEvenements controle){

        menu = new JMenu("Difficulté IA "+IA);

        String difficultees[] = {"  Facile  ","  Moyenne  ","  Difficile  "};
        JList list = new JList<>(difficultees);
        list.addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                Commande c = new Commande("setDifficulty");
                c.setIA(IA);
                c.setDifficulty(list.getSelectedValue().toString().toLowerCase());
                controle.commande(c);
            }
        });

        if(IA==1){
            switch(controle.getDifficultyIA1()){
                case "facile":
                    list.setSelectedIndex(0);
                    break;
                case "moyenne":
                    list.setSelectedIndex(1);
                    break;
                case "difficile":
                    list.setSelectedIndex(2);
                    break;
            }
        }else{
            switch(controle.getDifficultyIA2()){
                case "facile":
                    list.setSelectedIndex(0);
                    break;
                case "moyenne":
                    list.setSelectedIndex(1);
                    break;
                case "difficile":
                    list.setSelectedIndex(2);
                    break;
            }
        }

        menu.add(list);
    }

    public JMenu getMenu() {
        return menu;
    }
}
