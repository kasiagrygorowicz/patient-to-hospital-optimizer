package algorithm;

import data.elements.Stack;
import data.graph.Point;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class GrahamAlgorithm {

    public static ArrayList<Point> grahamScan(ArrayList<Point> points) {
        if (points.size() < 3)
            throw new RuntimeException("Podano " + points.size() + " punktów (szpitali/obiektów).\nNależy podać min 3");
        Stack stack = new Stack<>();
        Point pointMinY = GraphUtil.getPointWithMinY(points);
        stack.push(pointMinY);
        sortPointsByAngle(points, pointMinY);
        stack.push(points.get(1));
        Point lastPointHolder;

        for (int k = 2; k < points.size(); k++) {
            lastPointHolder = stack.pop();
            Point possible = points.get(k);

            while (stack.peek() != null && GraphUtil.orientation(stack.peek(), lastPointHolder, possible) != Orientation.COUNTERCLOCKWISE) {
                lastPointHolder = stack.pop();
            }
            stack.push(lastPointHolder);
            stack.push(points.get(k));
        }

        lastPointHolder = stack.pop();
        if (GraphUtil.orientation(stack.peek(), lastPointHolder, pointMinY) != Orientation.COUNTERCLOCKWISE) {
            if (stack.getSize() == 0)
                throw new RuntimeException("Nie można stworzyć państwa gdyż wszytskie obiekty i szpitale mają wzajemne połorzenie linearne");
            return stack.getElementsOnStack();
        }
        stack.push(lastPointHolder);
        if (stack.getSize() == 0)
            throw new RuntimeException("Nie można stworzyć państwa gdyż wszytskie obiekty i szpitale mają wzajemne połorzenie linearne");
        return stack.getElementsOnStack();
    }

    private static void sortPointsByAngle(List<Point> points, Point referenceMinPoint) {
        Collections.sort(points, (p1, p2) -> {

            if (p1 == referenceMinPoint) return -1;
            if (p2 == referenceMinPoint) return 1;

            Orientation orientation = GraphUtil.orientation(referenceMinPoint, p1, p2);

            if (orientation == Orientation.COLLINEAR) {
                if (GraphUtil.distance(referenceMinPoint, p1) > GraphUtil.distance(referenceMinPoint, p2))
                    return 1;
                return -1;
            } else {
                if (orientation == Orientation.COUNTERCLOCKWISE)
                    return -1;
                return 1;
            }
        });
    }
}






