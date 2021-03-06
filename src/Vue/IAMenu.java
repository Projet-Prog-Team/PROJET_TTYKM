package Vue;


import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

public class IAMenu  {

    private JMenu menu;
    IHMState state;

    IAMenu(int IA, CollecteurEvenements controle, IHMState state){

        this.state = state;

        menu = new JMenu("DifficultÃ© IA "+IA);

        String[] difficultees = {"  Facile  ","  Moyenne  ","  Difficile  "};
        JList list = new JList<>(difficultees);
        if(IA==1){
            switch(state.getDifficultyIA1()){
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
            switch(state.getDifficultyIA2()){
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
        list.addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                Commande c = new Commande("setDifficulty");
                c.setIA(IA);
                c.setDifficulty(list.getSelectedValue().toString().toLowerCase().trim());
                controle.commande(c);
            }
        });

        menu.add(list);
    }

    public JMenu getMenu() {
        return menu;
    }
}
