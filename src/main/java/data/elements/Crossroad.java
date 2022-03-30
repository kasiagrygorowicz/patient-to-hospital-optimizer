package data.elements;

import data.graph.Vertex;

import java.util.zip.DataFormatException;

public class Crossroad extends Vertex {

    private final int id;
    private double distanceToVertex;

    public Crossroad(int id, double x, double y) throws DataFormatException {
        super(x, y);
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public double getDistanceToVertex() {
        return distanceToVertex;
    }

    public void setDistanceToVertex(double distanceToHospital) {
        this.distanceToVertex = distanceToHospital;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Crossroad)) return false;
        Crossroad crossroad = (Crossroad) o;
        return super.getLocation().equals(crossroad.getLocation());
    }

    @Override
    public int hashCode() {
        return super.getLocation().hashCode() + 123;
    }

    @Override
    public String toString() {
        return "Id szkrzyżowania: " + this.id + " współrzędne x: " + super.getLocation().getX()
                + " y: " + super.getLocation().getY();
    }
}
