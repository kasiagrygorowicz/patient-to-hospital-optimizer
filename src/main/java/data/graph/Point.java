package data.graph;

import java.util.zip.DataFormatException;

public class Point {
    public double x;
    public double y;
    public static final double max = 1000;


    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public Point(double x, double y) throws DataFormatException {
        if (x < -max || x > max || y < -max || y > max)
            throw new DataFormatException("Wartość jednej ze współrzędnych wykracza poza zakres ustalony przez naczelnika!");
        this.x = x;
        this.y = y;
    }

    @Override
    public int hashCode() {
        return (int) (this.x * 1238 + this.y * 3219);
    }

    @Override
    public boolean equals(Object o) {

        if (o == this) return true;
        if (!(o instanceof Point)) {
            return false;
        }
        Point point = (Point) o;
        return point.x == this.x && point.y == this.y;

    }

    @Override
    public String toString() {
        return "x " + x + " y: " + y;
    }

}
