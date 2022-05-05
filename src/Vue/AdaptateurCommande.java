package Vue;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class AdaptateurCommande implements ActionListener {
    CollecteurEvenements control;
    Commande commande;

    AdaptateurCommande(CollecteurEvenements c, Commande com) {
        control = c;
        commande = com;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        control.commande(commande);
    }
}
