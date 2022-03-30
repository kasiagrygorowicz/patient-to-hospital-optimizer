package algorithm;

import data.graph.Point;

import java.util.ArrayList;
import java.util.zip.DataFormatException;

import static algorithm.GraphUtil.orientation;

public class Border {

    private static boolean wasEdge = false;
    private static int edgeCounter = 0;


    private static void isOnEdge(Point p1, Point q1, Point p2) {
        if (p1.getY() == p2.getY() || q1.getY() == p1.getY())
            edgeCounter++;

    }

    public static boolean onSegment(Point p1, Point s, Point p2) {
        if (s.getY() <= Math.max(p1.getY(), p2.getY()) && s.getY() >= Math.min(p1.getY(), p2.getY()) && s.getX() <= Math.max(p1.getX(), p2.getX()) && s.getX() >= Math.min(p1.getX(), p2.getX()))
            if (orientation(p1, s, p2) == Orientation.COLLINEAR)
                return true;

        return false;
    }

    public static boolean doIntersect(Point p1, Point q1,
                                      Point p2, Point q2) {
        Orientation orientation1 = orientation(p1, q1, p2);
        Orientation orientation2 = orientation(p1, q1, q2);
        Orientation orientation3 = orientation(p2, q2, p1);
        Orientation orientation4 = orientation(p2, q2, q1);

        if (orientation1 != orientation2 && orientation3 != orientation4)
            return true;
        if (orientation1 == Orientation.COLLINEAR && onSegment(p1, p2, q1))
            return true;
        if (orientation2 == Orientation.COLLINEAR && onSegment(p1, q2, q1))
            return true;
        if (orientation3 == Orientation.COLLINEAR && onSegment(p2, p1, q2))
            return true;
        if (orientation4 == Orientation.COLLINEAR && onSegment(p2, q1, q2))
            return true;
        return false;
    }

    public static boolean isInsidePolygon(ArrayList<Point> polygon, Point point) throws DataFormatException {
        if (polygon.size() < 3)
            throw new RuntimeException("Błąd, granica musi mieć min 3 punkty !!!");

        Point extendedPoint = new Point(Point.max, point.getY());
        int intersectionsCounter = 0;
        int k = 0;


        do {
            int next = (k + 1) % polygon.size();
            if (doIntersect(polygon.get(k), polygon.get(next), point, extendedPoint)) {
                if (orientation(polygon.get(k), polygon.get(next), point) == Orientation.COLLINEAR) {
                    return onSegment(polygon.get(k), point, polygon.get(next));
                }

                isOnEdge(point, polygon.get(k), polygon.get(next));
                intersectionsCounter++;
            }

            k = next;
        } while (k != 0);
        boolean isInside = intersectionsCounter - edgeCounter % 2 == 1;
        edgeCounter = 0;
        return isInside;
    }
}
