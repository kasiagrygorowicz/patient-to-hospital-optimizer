package data.graph;

import java.util.zip.DataFormatException;

public class Vertex {

    private final Point location;

    public Vertex(double x, double y) throws DataFormatException {
        this.location = new Point(x, y);
    }

    public Point getLocation() {
        return location;
    }

    @Override
    public boolean equals(Object o) {

        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Vertex vertex = (Vertex) o;
        return this.location.equals(vertex);
    }

    @Override
    public int hashCode() {
        return this.location.hashCode();
    }
}
