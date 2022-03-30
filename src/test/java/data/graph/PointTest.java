package data.graph;

import org.junit.Test;

import java.util.zip.DataFormatException;

import static org.junit.Assert.*;

public class PointTest {


    @Test(expected=DataFormatException.class)
    public void pass_x_out_of_range_negative() throws DataFormatException {
        Point p =new Point (-100000,89);
    }

    @Test(expected=DataFormatException.class)
    public void pass_y_out_of_range_negative() throws DataFormatException {
        Point p =new Point (-100,-100000);
    }

    @Test(expected=DataFormatException.class)
    public void pass_x_out_of_range_positive() throws DataFormatException {
        Point p =new Point (100000,89);
    }

    @Test(expected=DataFormatException.class)
    public void pass_y_out_of_range_positive() throws DataFormatException {
        Point p =new Point (100,100000);
    }

    @Test
    public void pass_normal_values() throws DataFormatException {
        Point p =new Point (100,888);
        assertEquals(100,p.getX(),0);
        assertEquals(888, p.getY(),0);
    }

}