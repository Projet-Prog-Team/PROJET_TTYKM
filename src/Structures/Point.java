package Structures;

import java.util.Objects;

public class Point {
    int l;
    int c;
    public Point(int x, int y) {
        this.l = x;
        this.c = y;
    }

    @Override
    public String toString() {
        return "Point{" +
                "l=" + l +
                ", c=" + c +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Point point = (Point) o;
        return l == point.l && c == point.c;
    }

    @Override
    public int hashCode() {
        return Objects.hash(l, c);
    }

    public int getL() {
        return l;
    }

    public int getC() {
        return c;
    }
}
