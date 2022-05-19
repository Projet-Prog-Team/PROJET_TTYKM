package Vue;

import Modele.Emplacement;
import Modele.Pion;
import Modele.PionBasique;
import Patterns.Observable;

import java.util.ArrayList;


public class IHMState extends Observable {

    private ArrayList<Pion> preview;
    private int previewFocus1;
    private int previewFocus2;
    private boolean IA1;
    private boolean IA2;
    private String difficultyIA1;
    private String difficultyIA2;
    private boolean pauseIA;
    private Emplacement suggestionSource;
    private Emplacement suggestionDestination;
    private int suggestionFocus;

    public IHMState(){
    }

    public void initPreview(){
        this.preview = null;
        this.previewFocus1 = 3;
        this.previewFocus2 = 3;
        this.suggestionSource = null;
        this.suggestionDestination = null;
        this.suggestionFocus = 3;
        miseAJour();
    }

    public ArrayList<Pion> getPreview() {
        return preview;
    }

    public void setPreview(ArrayList<Pion> preview) {
        if(!preview.equals(this.preview)){
            this.preview = preview;
            miseAJour();
        }
    }

    public int getPreviewFocus1() {
        return previewFocus1;
    }

    public void setPreviewFocus1(int previewFocus1) {
        if(previewFocus1!=this.previewFocus1){
            this.previewFocus1 = previewFocus1;
            miseAJour();
        }
    }

    public int getPreviewFocus2() {
        return previewFocus2;
    }

    public void setPreviewFocus2(int previewFocus2) {
        if(previewFocus2!=this.previewFocus2){
            this.previewFocus2 = previewFocus2;
            miseAJour();
        }
    }

    public boolean getIA1() {
        return IA1;
    }

    public void setIA1(boolean IA1) {
        this.IA1 = IA1;
        miseAJour();
    }

    public boolean getIA2() {
        return IA2;
    }

    public void setIA2(boolean IA2) {
        this.IA2 = IA2;
        miseAJour();
    }

    public String getDifficultyIA1() {
        return difficultyIA1;
    }

    public void setDifficultyIA1(String difficultyIA1) {
        this.difficultyIA1 = difficultyIA1;
        miseAJour();
    }

    public String getDifficultyIA2() {
        return difficultyIA2;
    }

    public void setDifficultyIA2(String difficultyIA2) {
        this.difficultyIA2 = difficultyIA2;
        miseAJour();
    }

    public boolean getPauseIA() {
        return pauseIA;
    }

    public void setPauseIA(boolean pauseIA) {
        this.pauseIA = pauseIA;
        miseAJour();
    }

    public Emplacement getSuggestionSource() {
        return suggestionSource;
    }

    public void setSuggestionSource(Emplacement suggestionSource) {
        this.suggestionSource = suggestionSource;
        miseAJour();
    }

    public Emplacement getSuggestionDestination() {
        return suggestionDestination;
    }

    public void setSuggestionDestination(Emplacement suggestionDestination) {
        this.suggestionDestination = suggestionDestination;
        miseAJour();
    }

    public int getSuggestionFocus() {
        return suggestionFocus;
    }

    public void setSuggestionFocus(int suggestionFocus) {
        this.suggestionFocus = suggestionFocus;
        miseAJour();
    }
}
