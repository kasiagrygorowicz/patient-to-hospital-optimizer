package algorithm;

import data.graph.Point;
import org.junit.Test;

import java.util.ArrayList;
import java.util.zip.DataFormatException;

import static org.junit.Assert.*;

public class GraphUtilTest {

    @Test
    public void is_orientation_correct_counterclockwise() throws DataFormatException {
//        given
        Point p1 =new Point (0,0);
        Point p2 =new Point (4,4);
        Point p3 =new Point (1,2);
//        when
        Orientation o =GraphUtil.orientation(p1,p2,p3);
//        then
        assertEquals(Orientation.COUNTERCLOCKWISE, o);
    }

    @Test
    public void is_orientation_correct_collinear() throws DataFormatException {
//        given
        Point p1 =new Point (3,3);
        Point p2 =new Point (10,10);
        Point p3 =new Point (-20,-20);
//        when
        Orientation o =GraphUtil.orientation(p1,p2,p3);
//        then
        assertEquals(Orientation.COLLINEAR, o);
    }

    @Test
    public void is_orientation_correct_clockwise() throws DataFormatException {
//        given
        Point p1 =new Point (-200,-10);
        Point p2 =new Point (10,84);
        Point p3 =new Point (15,2);
//        when
        Orientation o =GraphUtil.orientation(p1,p2,p3);
//        then
        assertEquals(Orientation.CLOCKWISE, o);
    }

    @Test
    public void id_min_Y_point_correct() throws DataFormatException {
        //        given
        ArrayList<Point> points =new ArrayList<>();
        points.add(new Point (-200,10));
        points.add(new Point (745,2.735));
        points.add(new Point (15,-2.356));
        points.add(new Point (-624,0));
        points.add(new Point (378,84));
        points.add(new Point (25,-2.356));
        Point correctResult =new Point (15,-2.356);
//        when
        Point result =GraphUtil.getPointWithMinY(points);
//        then
        assertEquals(correctResult,result);
    }
}