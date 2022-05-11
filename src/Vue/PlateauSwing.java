package Vue;

import Modele.EPOQUE;
import Modele.Jeu;

import javax.swing.*;
import java.awt.*;

public class PlateauSwing extends JComponent implements Plateau {

    Image bg;
    Image focus1;
    Image focus2;
    Image pionBlanc;
    Image pionNoir;
    VuePlateau vue;
    Graphics2D drawable;
    private final double pionSize = 0.6;
    private int xOffset = 0;
    private int yOffset = 50;
    private int focusRadius = 40;
    private int epoque;

    PlateauSwing(int epoque, Jeu j){
        this.epoque = epoque;
        vue = new VuePlateau(j,this);

        ImageIcon img = null;
        switch(epoque){
            case EPOQUE.PASSE:
                img = new ImageIcon("res/Img/passe.png");
                break;
            case EPOQUE.PRESENT:
                img = new ImageIcon("res/Img/present.png");
                break;
            case EPOQUE.FUTUR:
                img = new ImageIcon("res/Img/futur.png");
                break;
        }
        bg = img.getImage();

        img = new ImageIcon("res/Img/focus1.png");
        focus1 = img.getImage();

        img = new ImageIcon("res/Img/focus2.png");
        focus2 = img.getImage();

        img = new ImageIcon("res/Img/blanc.png");
        pionBlanc = img.getImage();

        img = new ImageIcon("res/Img/noir.png");
        pionNoir = img.getImage();
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        drawable = (Graphics2D) g;
        drawable.drawImage(bg,xOffset,yOffset,getWidth()-2*xOffset,getHeight()-2*yOffset,this);
        vue.dessinerPlateau();
    }

    @Override
    public int getEpoque() {
        return this.epoque;
    }

    @Override
    public void tracerPion(int l, int c, double alpha, int joueur) {
        Composite compo = drawable.getComposite();
        if(joueur==1){
            drawable.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, (float) alpha));
            drawable.drawImage(pionBlanc,getXoffset()+c*getLargeurCase()+(getLargeurCase()-getPionlargeur())/2, getYoffset()+l* getHauteurCase()+(getHauteurCase()-getPionHauteur())/2, getPionlargeur(), getPionHauteur(), this);
        }else{
            drawable.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, (float) alpha));
            drawable.drawImage(pionNoir,getXoffset()+c*getLargeurCase()+(getLargeurCase()-getPionlargeur())/2, getYoffset()+l* getHauteurCase()+(getHauteurCase()-getPionHauteur())/2, getPionlargeur(), getPionHauteur(), this);
        }
        drawable.setComposite(compo);
    }

    public void tracerFocus1(double alpha){
        int width = focusRadius*2;
        int height = focusRadius*2;
        Composite compo = drawable.getComposite();
        drawable.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, (float) alpha));
        drawable.drawImage(focus1, (getWidth()-width)/2,yOffset-height/2,width,height, this);
        drawable.setComposite(compo);
    }

    public void tracerFocus2(double alpha){
        int width = focusRadius*2;
        int height = focusRadius*2;
        Composite compo = drawable.getComposite();
        drawable.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, (float) alpha));
        drawable.drawImage(focus2, (getWidth()-width)/2,getHeight()-yOffset-height/2,width,height, this);
        drawable.setComposite(compo);
    }

    public void tracerBrillance(int l, int c){
        int x = getXoffset()+c*getLargeurCase();
        int y = getYoffset()+l* getHauteurCase();
        Color myColour = new Color(95, 255, 163,125 );
        drawable.setColor(myColour);
        drawable.fillRect(x,y,getLargeurCase(),getHauteurCase());
    }

    public void tracerBrillancev2(int l, int c){
        int x = getXoffset()+c*getLargeurCase();
        int y = getYoffset()+l*getHauteurCase();
        Color myColour;
        if((l+c) % 2==0){
            myColour = new Color(158,158,158 );
        }else{
            myColour = new Color(127,127,127 );
        }
        drawable.setColor(myColour);

        int width = getLargeurCase()/3;
        int height = getHauteurCase()/3;

        drawable.fillOval(x+getLargeurCase()/3,y+getHauteurCase()/3,width,height);
    }

    public void tracerBrillanceFocus1(){
        int xFocus = getWidth()/2;
        int yFocus = yOffset;
        Color myColour = new Color(255, 204, 0,125 );
        drawable.setColor(myColour);
        drawable.fillOval(xFocus-focusRadius, yFocus-focusRadius, focusRadius*2, focusRadius*2);
    }

    public void tracerBrillanceFocus2(){
        int xFocus = getWidth()/2;
        int yFocus = getHeight()-yOffset;
        Color myColour = new Color(255, 204, 0,125 );
        drawable.setColor(myColour);
        drawable.fillOval(xFocus-focusRadius, yFocus-focusRadius, focusRadius*2, focusRadius*2);
    }


    public boolean isInFocus1(int clic_x, int clic_y){
        int xFocus = getWidth()/2;
        int yFocus = yOffset;
        double distance = Math.sqrt(Math.pow((clic_x-xFocus),2)+Math.pow((clic_y-yFocus),2));
        return distance<focusRadius;
    }

    public boolean isInFocus2(int clic_x, int clic_y){
        int xFocus = getWidth()/2;
        int yFocus = getHeight()-yOffset;
        double distance = Math.sqrt(Math.pow((clic_x-xFocus),2)+Math.pow((clic_y-yFocus),2));
        return distance<focusRadius;
    }

    public int getXoffset(){
        return (int) ((getWidth()-xOffset*2)/9.6)+xOffset;
    }

    public int getYoffset(){
        return (int) ((getHeight()-yOffset*2)/9.6)+yOffset;
    }

    public int getLargeurCase(){
        return (getWidth()-2*getXoffset())/4;
    }

    public int getHauteurCase(){
        return (getHeight()-2*getYoffset())/4;
    }

    public int getPionlargeur(){
        return (int) (getLargeurCase()*pionSize);
    }

    public int getPionHauteur(){
        return (int) (getHauteurCase()*pionSize);
    }

    public int getCol(int x){
        if(x>getXoffset()){
            return (x-getXoffset())/getLargeurCase();
        }else{
            return -1;
        }
    }

    public int getLig(int y){
        if(y>getYoffset()){
            return (y-getYoffset())/getHauteurCase();
        }else{
            return -1;
        }
    }

    public int getFocusRadius() {
        return focusRadius;
    }
}
