package algorithm;

import data.graph.Point;

import java.util.Collections;
import java.util.List;

public class GraphUtil {

    public static Orientation orientation(Point a, Point b, Point c) {
        double result;
        result = (b.getY() - a.getY()) * (c.getX() - b.getX()) - (c.getY() - b.getY()) * (b.getX() - a.getX());
        if (result == 0)
            return Orientation.COLLINEAR;
        if (result > 0)
            return Orientation.CLOCKWISE;
        return Orientation.COUNTERCLOCKWISE;
    }

    public static double distance(Point a, Point b) {
        return Math.sqrt(((a.getX() - b.getX()) * (a.getX() - b.getX())) + ((a.getY() - b.getY()) * (a.getY() - b.getY())));
    }

    public static Point getPointWithMinY(List<Point> points) {
        Collections.sort(points, (b, c) -> {
            if (b.getY() == c.getY())
                return Double.compare(b.getX(), c.getX());
            return b.getY() < c.getY() ? -1 : 1;
        });
        return points.get(0);
    }
}
