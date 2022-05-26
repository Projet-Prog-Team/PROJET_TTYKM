package Structures;

import java.util.Objects;

public class Couple<T1 extends Comparable, T2> implements Comparable<Couple<T1, T2>> {
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

    @Override
    public int compareTo(Couple<T1, T2> c) {
        return getFirst().compareTo(c.getFirst());
    }

    @Override
    public boolean equals(Object o) {
        Couple <T1, T2> c = (Couple <T1, T2>) o;
        return getFirst().equals(c.getFirst());
    }

    @Override
    public String toString() {
        return "Couple{" +
                "first=" + first +
                ", second=" + second +
                '}';
    }
}
