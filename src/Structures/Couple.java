package Structures;

public class Couple<T1, T2> {
    T1 first;
    T2 second;

    public Couple(T1 f, T2 s) {
        first = f;
        second = s;
    }

    public T1 getFirst() {
        return first;
    }

    public void setFirst(T1 first) {
        this.first = first;
    }

    public T2 getSecond() {
        return second;
    }

    public void setSecond(T2 second) {
        this.second = second;
    }
}
