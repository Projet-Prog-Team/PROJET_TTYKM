package Vue;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class AdaptateurSouris extends MouseAdapter {

    PlateauSwing plateau;
    CollecteurEvenements controle;

    AdaptateurSouris(PlateauSwing plateau, CollecteurEvenements c) {
        this.plateau = plateau;
        controle = c;
    }

    @Override
    public void mouseMoved(MouseEvent e) {
        int l = plateau.getLig(e.getY());
        int c = plateau.getCol(e.getX());
        controle.enablePreview(l,c,plateau.getEpoque());
    }

    @Override
    public void mousePressed(MouseEvent e) {
        if(e.getX()>=plateau.getXoffset() && e.getX()<=(plateau.getWidth() - plateau.getXoffset()) && e.getY()>=plateau.getYoffset() && e.getY() <= (plateau.getHeight() - plateau.getYoffset())){
            int l = plateau.getLig(e.getY());
            int c = plateau.getCol(e.getX());
            controle.clicSouris(l, c, plateau.getEpoque());
        }else {
            if(plateau.isInFocus1(e.getX(),e.getY())){
                Commande c = (new Commande("clicFocus"));
                c.setJoueur(1);
                c.setEpoque(plateau.getEpoque());
                controle.commande(c);
            }else if(plateau.isInFocus2(e.getX(),e.getY())){
                Commande c = (new Commande("clicFocus"));
                c.setJoueur(2);
                c.setEpoque(plateau.getEpoque());
                controle.commande(c);
            }
        }
    }
}