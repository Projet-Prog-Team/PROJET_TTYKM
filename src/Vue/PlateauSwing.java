package Vue;

import Modele.DeroulementJeu;
import Modele.EPOQUE;
import Modele.Emplacement;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.io.InputStream;

public class PlateauSwing extends JComponent implements Plateau {

    Image bg;
    Image bgNum;
    Image focusBlanc;
    Image focusNoir;
    Image pionBlanc;
    Image pionNoir;
    Image statueBlanche;
    Image statueNoire;
    Image statueViolette;
    Image statueVerte;
    VuePlateau vue;
    Graphics2D drawable;
    private final double pionSize = 0.8;
    private int xOffset = 0;
    private int yOffset = 50;
    private int focusRadius = 40;
    private int epoque;
    private IHMState state;

    PlateauSwing(int epoque, DeroulementJeu j, IHMState state){
        this.epoque = epoque;
        this.state = state;

        vue = new VuePlateau(j,this);

        try {
        InputStream in = null;
        switch(EPOQUE.Convert(epoque)){
            case PASSE:
                in = ClassLoader.getSystemClassLoader().getResourceAsStream("Img/passe.png");
                bg = ImageIO.read(in);
                in = ClassLoader.getSystemClassLoader().getResourceAsStream("Img/passeNum.png");
                bgNum = ImageIO.read(in);
                break;
            case PRESENT:
                in = ClassLoader.getSystemClassLoader().getResourceAsStream("Img/present.png");
                bg = ImageIO.read(in);
                in = ClassLoader.getSystemClassLoader().getResourceAsStream("Img/presentNum.png");
                bgNum = ImageIO.read(in);
                break;
            case FUTUR:
                in = ClassLoader.getSystemClassLoader().getResourceAsStream("Img/futur.png");
                bg = ImageIO.read(in);
                in = ClassLoader.getSystemClassLoader().getResourceAsStream("Img/futurNum.png");
                bgNum = ImageIO.read(in);
                break;
        }

            in = ClassLoader.getSystemClassLoader().getResourceAsStream("Img/focusBlanc.png");
            focusBlanc = ImageIO.read(in);

            in = ClassLoader.getSystemClassLoader().getResourceAsStream("Img/focusNoir.png");
            focusNoir = ImageIO.read(in);

            in = ClassLoader.getSystemClassLoader().getResourceAsStream("Img/pionBlanc2.png");
            pionBlanc = ImageIO.read(in);

            in = ClassLoader.getSystemClassLoader().getResourceAsStream("Img/pionNoir2.png");
            pionNoir = ImageIO.read(in);

            in = ClassLoader.getSystemClassLoader().getResourceAsStream("Img/statueBlanche.png");
            statueBlanche = ImageIO.read(in);

            in = ClassLoader.getSystemClassLoader().getResourceAsStream("Img/statueNoire.png");
            statueNoire = ImageIO.read(in);

            in = ClassLoader.getSystemClassLoader().getResourceAsStream("Img/statueViolette.png");
            statueViolette = ImageIO.read(in);

            in = ClassLoader.getSystemClassLoader().getResourceAsStream("Img/statueVerte.png");
            statueVerte = ImageIO.read(in);

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        drawable = (Graphics2D) g;
        if(state.getPasseNum()){
            drawable.drawImage(bgNum,xOffset,yOffset,getWidth()-2*xOffset,getHeight()-2*yOffset,this);
        }else{
            drawable.drawImage(bg,xOffset,yOffset,getWidth()-2*xOffset,getHeight()-2*yOffset,this);
        }

        vue.dessinerPlateau();
    }

    @Override
    public int getEpoque() {
        return this.epoque;
    }

    @Override
    public void tracerPion(double l, double c, double alpha, int joueur) {
        Composite compo = drawable.getComposite();
        if(joueur==1){
            drawable.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, (float) alpha));
            drawable.drawImage(pionBlanc, (int) (getXoffset()+c*getLargeurCase()+(getLargeurCase()-getPionlargeur())/2), (int) (getYoffset()+l* getHauteurCase()+(getHauteurCase()-getPionHauteur())/2), getPionlargeur(), getPionHauteur(), this);
        }else{
            drawable.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, (float) alpha));
            drawable.drawImage(pionNoir, (int) (getXoffset()+c*getLargeurCase()+(getLargeurCase()-getPionlargeur())/2), (int) (getYoffset()+l* getHauteurCase()+(getHauteurCase()-getPionHauteur())/2), getPionlargeur(), getPionHauteur(), this);
        }
        drawable.setComposite(compo);
    }

    @Override
    public void tracerStatue(double l, double c, double alpha, int joueur) {
        Composite compo = drawable.getComposite();
        if(joueur==1){
            drawable.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, (float) alpha));
            drawable.drawImage(statueBlanche, (int) (getXoffset()+c*getLargeurCase()+(getLargeurCase()-getPionlargeur())/2), (int) (getYoffset()+l* getHauteurCase()+(getHauteurCase()-getPionHauteur())/2), getPionlargeur(), getPionHauteur(), this);
        }else if(joueur==2){
            drawable.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, (float) alpha));
            drawable.drawImage(statueNoire, (int) (getXoffset()+c*getLargeurCase()+(getLargeurCase()-getPionlargeur())/2), (int) (getYoffset()+l* getHauteurCase()+(getHauteurCase()-getPionHauteur())/2), getPionlargeur(), getPionHauteur(), this);
        }else{
            drawable.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, (float) alpha));
            drawable.drawImage(statueViolette, (int) (getXoffset()+c*getLargeurCase()+(getLargeurCase()-getPionlargeur())/2), (int) (getYoffset()+l* getHauteurCase()+(getHauteurCase()-getPionHauteur())/2), getPionlargeur(), getPionHauteur(), this);
        }
        drawable.setComposite(compo);
    }

    public void tracerFocusBlanc(double alpha){
        int width = focusRadius*2;
        int height = focusRadius*2;
        Composite compo = drawable.getComposite();
        drawable.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, (float) alpha));
        drawable.drawImage(focusBlanc, (getWidth()-width)/2,yOffset-height/2,width,height, this);
        drawable.setComposite(compo);
    }

    public void tracerFocusNoir(double alpha){
        int width = focusRadius*2;
        int height = focusRadius*2;
        Composite compo = drawable.getComposite();
        drawable.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, (float) alpha));
        drawable.drawImage(focusNoir, (getWidth()-width)/2,getHeight()-yOffset-height/2,width,height, this);
        drawable.setComposite(compo);
    }

    public void tracerBrillancePion(int l, int c){
        int x = getXoffset()+c*getLargeurCase();
        int y = getYoffset()+l* getHauteurCase();
        Color myColour = new Color(255, 200,0,125 );
        drawable.setColor(myColour);
        drawable.fillRect(x,y,getLargeurCase()+3,getHauteurCase());
    }

    public void tracerBrillanceCase(int l, int c){
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

    public void tracerSuggestionCase(int l, int c){
        int x = getXoffset()+c*getLargeurCase();
        int y = getYoffset()+l* getHauteurCase();
        Color myColour = new Color(18, 255, 0,125 );
        drawable.setColor(myColour);
        drawable.fillRect(x,y,getLargeurCase()+3,getHauteurCase());
    }

    public void tracerSuggestionFocus(int focus){
        int xFocus = getWidth()/2;
        int yFocus = focus==1 ? yOffset:getHeight()-yOffset;
        Color myColour = new Color(18, 255, 0,175 );
        drawable.setColor(myColour);
        drawable.fillOval(xFocus-focusRadius, yFocus-focusRadius, focusRadius*2, focusRadius*2);
    }

    public void tracerSuggestionStatue(double l, double c) {
        drawable.drawImage(statueVerte, (int) (getXoffset()+c*getLargeurCase()+(getLargeurCase()-getPionlargeur())/2), (int) (getYoffset()+l* getHauteurCase()+(getHauteurCase()-getPionHauteur())/2), getPionlargeur(), getPionHauteur(), this);
    }

    public void tracerBrillanceFocus(int focus){
        int xFocus = getWidth()/2;
        int yFocus = focus==1 ? yOffset:getHeight()-yOffset;
        Color myColour = new Color(255, 200,0, 175);
        drawable.setColor(myColour);
        drawable.fillOval(xFocus-focusRadius, yFocus-focusRadius, focusRadius*2, focusRadius*2);
    }

    public boolean isInFocusBlanc(int clic_x, int clic_y){
        int xFocus = getWidth()/2;
        int yFocus = yOffset;
        double distance = Math.sqrt(Math.pow((clic_x-xFocus),2)+Math.pow((clic_y-yFocus),2));
        return distance<focusRadius;
    }

    public boolean isInFocusNoir(int clic_x, int clic_y){
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

    public IHMState getState() {
        return state;
    }

    @Override
    public void decale(double dL, double dC, int l, int c) {
        vue.fixerDecalage(dL,dC,l,c);
        repaint();
    }

    public void tp(Emplacement depart, Emplacement arrive, double alphaDep, double alphaArr){
        if(depart!=null && depart.getEpoque()==epoque){
            vue.tp(depart, alphaDep);
        }else if (arrive.getEpoque()==epoque){
            vue.tp(arrive, alphaArr);
        }
        repaint();
    }

    public void reset(){
        vue.resetDecalage();
        repaint();
    }

}
