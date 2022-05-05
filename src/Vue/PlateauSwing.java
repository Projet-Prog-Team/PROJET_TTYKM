package Vue;

import Modele.EPOQUE;
import Modele.Jeu;

import javax.swing.*;
import java.awt.*;
import java.util.Random;

public class PlateauSwing extends JComponent implements Plateau {

    Image bg;
    Image focus1;
    Image focus2;
    int epoque;
    VuePlateau vue;
    Graphics2D drawable;
    private final double pionSize = 0.6;
    private int xOffset = 0;
    private int yOffset = 50;
    private int focusRadius = 20;

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
    }

    @Override
    public void paintComponent(Graphics g) {
        drawable = (Graphics2D) g;
        drawable.drawImage(bg,xOffset,yOffset,getWidth()-2*xOffset,getHeight()-2*yOffset,this);
        vue.dessinerPlateau();
        //tracerFocus1();
        //tracerFocus2();
        tracerBrillance(2,2);
        tracerBrillance(0,2);
        tracerBrillance(1,3);
        tracerBrillance(1,1);
        tracerBrillanceFocus1();
        tracerBrillanceFocus2();
    }

    @Override
    public EPOQUE getEpoque() {
        return this.epoque;
    }

    @Override
    public void tracerPion(int l, int c) {
        Random rand = new Random();
        float r = rand.nextFloat();
        float g = rand.nextFloat();
        float b = rand.nextFloat();
        Color randomColor = new Color(r, g, b);
        drawable.setColor(randomColor);
        drawable.fillOval(getXoffset()+c*getLargeurCase()+(getLargeurCase()-getPionlargeur())/2, getYoffset()+l* getHauteurCase()+(getHauteurCase()-getPionHauteur())/2, getPionlargeur(), getPionHauteur());
    }

    public void tracerFocus1(){
        int width = getWidth()/5;
        int height = getHeight()/5;
        drawable.drawImage(focus1, (getWidth()-width)/2,getHeight()-yOffset-height/2,width,height, this);
    }

    public void tracerFocus2(){
        int width = getWidth()/5;
        int height = getHeight()/5;
        drawable.drawImage(focus2, (getWidth()-width)/2,yOffset-height/2,width,height, this);
    }

    public void tracerBrillance(int l, int c){
        int x = getXoffset()+c*getLargeurCase();
        int y = getYoffset()+l* getHauteurCase();
        Color myColour = new Color(95, 255, 163,125 );
        drawable.setColor(myColour);
        drawable.fillRect(x,y,getLargeurCase(),getHauteurCase());
    }

    public void tracerBrillanceFocus1(){
        int xFocus = getWidth()/2;
        int yFocus = yOffset;
        Color myColour = new Color(231, 245, 124,125 );
        drawable.setColor(myColour);
        drawable.fillOval(xFocus-focusRadius, yFocus-focusRadius, focusRadius*2, focusRadius*2);
    }

    public void tracerBrillanceFocus2(){
        int xFocus = getWidth()/2;
        int yFocus = getHeight()-yOffset;
        Color myColour = new Color(231, 245, 124,125 );
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
        return (x-getXoffset())/getLargeurCase();
    }

    public int getLig(int y){
        return (y-getYoffset())/getHauteurCase();
    }

    public int getFocusRadius() {
        return focusRadius;
    }
}
