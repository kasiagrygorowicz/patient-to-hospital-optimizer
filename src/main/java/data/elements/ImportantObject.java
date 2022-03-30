package data.elements;

import data.graph.Point;
import exceptions.WrongNumericArgumentException;

import java.util.zip.DataFormatException;

public class ImportantObject {
    private final int id;
    private final String name;
    private Point location;
    private Point appearance;

    public ImportantObject(int id, String name, double x, double y) throws DataFormatException {
        if (id < 0) throw new WrongNumericArgumentException("Id obiektu nie może byc mniejsze od zera");
        this.id = id;
        this.name = name;
        location = new Point(x, y);
    }

    @Override
    public int hashCode() {
        return this.id * 656;
    }

    @Override
    public boolean equals(Object o) {
        if (o == this) return true;
        if (!(o instanceof ImportantObject)) {
            return false;
        }
        ImportantObject io = (ImportantObject) o;
        return io.id == this.id || this.location.equals(io.location);
    }

    @Override
    public String toString() {
        return id + " " + name + " x: " + location.getX() + " y: " + location.getY();
    }

    public Point getLocation() {
        return location;
    }

    public String getName() {
        return name;
    }

    public Point getAppearance() {
        return appearance;
    }

    public void setAppearance(Point p) {
        this.appearance = p;
    }
}
