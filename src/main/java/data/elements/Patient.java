package data.elements;

import data.graph.Point;
import exceptions.WrongNumericArgumentException;

public class Patient {
    private int id;
    private Point location;
    private Point appearance;

    public Patient(int id, Point location) {
        if (id < 0) throw new WrongNumericArgumentException("Id pacjenta nie może byc mniejsze od zera");
        this.id = id;
        this.location = location;
    }

    public int getId() {
        return id;
    }

    public Point getLocation() {
        return location;
    }

    @Override
    public int hashCode() {
        return this.id * 999;
    }

    @Override
    public boolean equals(Object o) {
        if (o == this) return true;
        if (!(o instanceof Patient)) {
            return false;
        }
        Patient patient = (Patient) o;
        return patient.id == this.id;
    }

    @Override
    public String toString() {
        return "Id pacjenta: " + this.id + " x: " + this.location.getX() + " y: " + this.location.getY();
    }

    public Point getAppearance() {
        return appearance;
    }

    public void setAppearance(Point p) {
        this.appearance = p;
    }
}

