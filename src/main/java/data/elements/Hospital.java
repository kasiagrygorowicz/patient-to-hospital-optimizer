package data.elements;

import data.graph.Point;
import data.graph.Vertex;
import exceptions.WrongNumericArgumentException;

import java.util.zip.DataFormatException;

public class Hospital extends Vertex {
    private final int id;
    private final String name;
    private int beds;
    private int freeBeds;
    private boolean wasThePatientHere = false;
    private Point location;
    private Point appearance;

    public Hospital(int id) throws DataFormatException {
        super(0, 0);
        if (id < 0) throw new WrongNumericArgumentException("Id szpitala nie może byc mniejsze od zera");
        this.id = id;
        this.name = null;
    }

    public int getId() {
        return id;
    }

    public Hospital(int id, String name, double x, double y, int beds, int freeBeds) throws DataFormatException {
        super(x, y);
        if (id < 0) throw new WrongNumericArgumentException("Id szpitala nie może byc mniejsze od zera");
        if (beds < 0) throw new WrongNumericArgumentException("Liczba łóżek w szpitalu nie może być mniejsza od zera");
        if (freeBeds < 0)
            throw new WrongNumericArgumentException("Liczba wolnych łóżek w szpitalu nie może być mniejsza od zera");
        if (beds < freeBeds)
            throw new WrongNumericArgumentException("Liczba wolnych łóżek nie może być większa niż liczba całkowitych łóżek dostepnych w szpitalu");
        this.id = id;
        this.name = name;
        this.beds = beds;
        this.freeBeds = freeBeds;
        this.location = new Point(x, y);
    }

    public String getName() {
        return name;
    }

    public Point getLocation() {
        return location;
    }

    public int getBeds() {
        return beds;
    }

    public int getFreeBeds() {
        return freeBeds;
    }

    public void addPatient() {
        this.freeBeds--;
    }

    public void setWasThePatientHere(boolean flag) {
        if (flag) this.wasThePatientHere = true;
        else this.wasThePatientHere = false;
    }

    public boolean isWasThePatientHere() {
        return this.wasThePatientHere;
    }

    @Override
    public int hashCode() {
        return this.id * 654;
    }

    @Override
    public boolean equals(Object o) {
        if (o == this) return true;
        if (!(o instanceof Hospital)) {
            return false;
        }
        Hospital hospital = (Hospital) o;
        return (hospital.id == this.id || this.location.equals(hospital.location));
    }

    @Override
    public String toString() {
        return id + " " + name + " x: " + super.getLocation().getX() + " y: " + super.getLocation().getY() + " " + beds + " " + freeBeds;
    }

    public Point getAppearance() {
        return appearance;
    }

    public void setAppearance(Point p) {
        this.appearance = p;
    }

}
