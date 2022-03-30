package algorithm;

import data.graph.Point;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.zip.DataFormatException;

import static org.junit.Assert.*;

public class BoarderTest {

    private ArrayList<Point> polygon;
    private Point point;

    @Before
    public void setUp() throws DataFormatException {
        polygon =new ArrayList<>();
        polygon.add(new Point(0,5));
        polygon.add(new Point(3,3));
        polygon.add(new Point(2.5,-2));
        polygon.add(new Point(0,-3));
        polygon.add(new Point(-1.5,0));
    }

    @Test
    public void point_p2_is_on_segment() throws DataFormatException {
//        given
        Point p1 =new Point (0,0);
        Point p2 =new Point (2,2);
        Point p3 =new Point (4,4);
//        when
        boolean answer = Border.onSegment(p1,p2,p3);
//        then
        assertTrue(answer);

    }

    @Test
    public void point_p2_is_not_on_segment() throws DataFormatException {
//        given
        Point p1 =new Point (0,0);
        Point p2 =new Point (2.5,2);
        Point p3 =new Point (4,4);
//        when
        boolean answer = Border.onSegment(p1,p2,p3);
//        then
        assertFalse(answer);

    }

    @Test
    public void points_intersect() throws DataFormatException {
//        given
        Point p1 =new Point (0,0);
        Point q1 =new Point (4,4);
        Point p2 =new Point (2.5,-3);
        Point q2 =new Point (0,6);
//        when
        boolean answer = Border.doIntersect(p1,q1,p2,q2);
//        then
        assertTrue(answer);

    }

    @Test
    public void points_dont_intersect() throws DataFormatException {
//        given
        Point p1 =new Point (0,0);
        Point q1 =new Point (4,4);
        Point p2 =new Point (-20,-3);
        Point q2 =new Point (0,6);
//        when
        boolean answer = Border.doIntersect(p1,q1,p2,q2);
//        then
        assertFalse(answer);

    }

    @Test
    public void points_inside_polygon() throws DataFormatException {
//        given
        Point p1 =new Point (0,0);
        Point p2 =new Point (0,5);
        Point p3 =new Point (-1,0);
        Point p4 =new Point (2,2);
//        when
//        then
        assertTrue(Border.isInsidePolygon(polygon,p1));
        assertTrue(Border.isInsidePolygon(polygon,p2));
        assertTrue(Border.isInsidePolygon(polygon,p3));
        assertTrue(Border.isInsidePolygon(polygon,p3));

    }

    @Test
    public void points_outside_polygon() throws DataFormatException {
//        given
        Point p1 =new Point (90,1.0);
        Point p2 =new Point (10,51);
        Point p3 =new Point (-10,0);
        Point p4 =new Point (2,28);
//        when
//        then
        assertFalse(Border.isInsidePolygon(polygon,p1));
        assertFalse(Border.isInsidePolygon(polygon,p2));
        assertFalse(Border.isInsidePolygon(polygon,p3));
        assertFalse(Border.isInsidePolygon(polygon,p3));

    }

    @Test(expected=RuntimeException.class)
    public void not_enough_points() throws DataFormatException {
//        given
        ArrayList<Point> p = new ArrayList<>();
        p.add(new Point (90,1.0));
        p.add(new Point (10,51));
        Point p1 =new Point (2,28);
//        when
        Border.isInsidePolygon(p,p1);


    }
}