package data.elements;

import exceptions.WrongNumericArgumentException;
import org.junit.Before;
import org.junit.Test;

import java.util.zip.DataFormatException;

import static org.junit.Assert.*;

public class RoadTest {
    private final int id = 1;
    private Hospital hospital1;
    private Hospital hospital2;
    private final double distance = 17.8;

    @Test(expected= WrongNumericArgumentException.class)
    public void pass_negative_id() throws DataFormatException {
        try {
            hospital1 = new Hospital(1, "szpital_1", 10.5, 15.7, 10, 10);
            hospital2 = new Hospital(2, "szpital_2", 10.7, 16, 100, 17);
        } catch(DataFormatException e) {
            assert false;
        }
        Road road = new Road(-1, hospital1, hospital2, distance);
        assert false;
    }

    @Test(expected= WrongNumericArgumentException.class)
    public void pass_negative_distance() throws DataFormatException {
        try {
            hospital1 = new Hospital(1, "szpital_1", 10.5, 15.7, 10, 10);
            hospital2 = new Hospital(2, "szpital_2", 10.7, 16, 100, 17);
        } catch(DataFormatException e) {
            assert false;
        }
        Road road = new Road(-1, hospital1, hospital2, -100);
        assert false;
    }
}





