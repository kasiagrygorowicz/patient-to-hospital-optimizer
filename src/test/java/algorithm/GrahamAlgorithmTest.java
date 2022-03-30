package algorithm;

import data.graph.Point;
import org.junit.Test;

import java.util.ArrayList;
import java.util.zip.DataFormatException;

import static org.junit.Assert.*;

public class GrahamAlgorithmTest {

    @Test
    public void test_graham_algorithm() throws DataFormatException {
//        given
        ArrayList<Point> points = new ArrayList<>();
        points.add(new Point(2,-2));
        points.add(new Point(-4,-3));
        points.add(new Point(3,3));
        points.add(new Point(2,-5));
        points.add(new Point(2,1));
        points.add(new Point(-4,9));

        ArrayList<Point> correctResult = new ArrayList<>();
        correctResult.add(new Point(2,-5));
        correctResult.add(new Point(3,3));
        correctResult.add(new Point(-4,9));
        correctResult.add(new Point(-4,-3));
//        when
        ArrayList<Point> result = GrahamAlgorithm.grahamScan(points);
//        then
        assertEquals(correctResult,result);
    }

    @Test
    public void test_graham_algorithm2_already_sorted_by_angle() throws DataFormatException {
//        given
        ArrayList<Point> points = new ArrayList<>();
        points.add(new Point(0,-3));
        points.add(new Point(2,-1));
        points.add(new Point(4,0));
        points.add(new Point(2,1));
        points.add(new Point(0,3));
        points.add(new Point(-2,1));
        points.add(new Point(-4,0));
        points.add(new Point(-2,-1));

        ArrayList<Point> correctResult = new ArrayList<>();
        correctResult.add(new Point(0,-3));
        correctResult.add(new Point(4,0));
        correctResult.add(new Point(0,3));
        correctResult.add(new Point(-4,0));
//        when
        ArrayList<Point> result = GrahamAlgorithm.grahamScan(points);
//        then
        assertEquals(correctResult,result);
    }

    @Test(expected = RuntimeException.class)
    public void not_enough_elements() throws DataFormatException {
        //        given
        ArrayList<Point> points = new ArrayList<>();
        points.add(new Point(2,-2));
        points.add(new Point(-4,-3));
        //        when
        ArrayList<Point> result = GrahamAlgorithm.grahamScan(points);
    }

}