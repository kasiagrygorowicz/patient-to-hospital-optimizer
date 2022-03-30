package data.elements;

import data.graph.Point;
import exceptions.WrongNumericArgumentException;
import org.junit.Test;

import java.util.zip.DataFormatException;

import static org.junit.Assert.*;

public class PatientTest {
    private int id = 10;
    private Point location;
    private Point appearance;

    @Test(expected= WrongNumericArgumentException.class)
    public void pass_negative_id() throws DataFormatException {
        try {
            location = new Point(10.5, 78);
            appearance = new Point(2, 3);
        } catch(DataFormatException e) {
            assert false;
        }
        Patient patient = new Patient(-1, location);
        assert false;
    }

}
