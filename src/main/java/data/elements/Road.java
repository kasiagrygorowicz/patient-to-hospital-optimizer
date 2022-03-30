package data.elements;

import exceptions.WrongNumericArgumentException;

public class Road {
    private final int id;
    private final Hospital hospital1;
    private final Hospital hospital2;
    private final double distance;

    public Road(int id, Hospital hospital1, Hospital hospital2, double distance) {
        if (id < 0) throw new WrongNumericArgumentException("Id drogi nie może byc mniejsze od zera");
        if (distance < 0) throw new WrongNumericArgumentException("Dystans miedzy szpitalami musi być większy od zera");
        this.id = id;
        this.hospital1 = hospital1;
        this.hospital2 = hospital2;
        this.distance = distance;
    }

    public double getDistance() {
        return distance;
    }

    public Hospital getHospital1() {
        return hospital1;
    }

    public Hospital getHospital2() {
        return hospital2;
    }

    public int getId() {
        return id;
    }

    @Override
    public int hashCode() {
        return this.id * 6221 + 98;
    }

    @Override
    public boolean equals(Object o) {
        if (o == this) return true;
        if (!(o instanceof Road)) {
            return false;
        }
        Road road = (Road) o;
        return road.id == this.id || this.hospital1.equals(road.hospital1) && this.hospital2.equals(road.hospital2) || this.hospital1.equals(road.hospital2) && this.hospital2.equals(road.hospital1);
    }

    @Override
    public String toString() {
        return "Id drogi: " + this.id + " szpital1: " + this.hospital1.getName() + " szpital2: " + this.hospital2.getName();
    }
}
