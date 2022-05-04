package Modele;

public class Cases {
    TYPE type;

    public Cases(TYPE t) {
        type = t;
    }

    public TYPE getType() {
        return type;
    }

    public void setType(TYPE type) {
        this.type = type;
    }

    @Override
    public String toString() {
        if (type == TYPE.VIDE) {
            return "0";
        }
        return "";
    }
}
