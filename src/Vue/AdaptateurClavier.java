package Vue;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class AdaptateurClavier extends KeyAdapter {
    CollecteurEvenements controle;

    AdaptateurClavier(CollecteurEvenements c) {
        controle = c;
    }

    @Override
    public void keyPressed(KeyEvent e) {
        switch (e.getKeyCode()) {
            case KeyEvent.VK_A:
                controle.commande(new Commande("anim"));
                break;
        }
    }
}

