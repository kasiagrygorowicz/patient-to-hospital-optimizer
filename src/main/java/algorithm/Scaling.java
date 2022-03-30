package algorithm;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.zip.DataFormatException;

import data.graph.Point;

public class Scaling {

    private static double minY = Double.POSITIVE_INFINITY;
    private static double minX = Double.POSITIVE_INFINITY;
    private static double maxY = Double.NEGATIVE_INFINITY;
    private static double maxX = Double.NEGATIVE_INFINITY;
    private static Double x, y;
    private static Double kx, ky;
    private static Double shiftY, shiftX;

    private static double offsetX = 0.0f;
    private static double offsetY = 0.0f;
    private static Point offset;

    public static Point getOffset(){
        return offset;
    }
    public static Point applyOffset(Point p) throws DataFormatException {
        double new_x = p.x+offsetX;
        double new_y = p.y+offsetY;
        try {
            return new Point(new_x,new_y);
        } catch (DataFormatException e) {
            throw new DataFormatException("Problem przy skalowaniu punktu\nJedna ze współrzędnych po przeskalowaniu wychodzi poza zakres dla punktu");
        }
    }
    public static void searchMinPoints(HashSet<data.graph.Point> points){
        for (Point p:points){
            if (offsetX>p.x) offsetX=p.x;
            if (offsetY>p.y) offsetY=p.y;
        }
        offsetX=Math.abs(offsetX);
        offsetY=Math.abs(offsetY);
        try {
            offset = new Point(offsetX,offsetY);
        } catch (DataFormatException e) {
            e.printStackTrace();
        }
    }
    public static void setX(double x) {
        Scaling.x = x;
    }

    public static void setY(double y) {
        Scaling.y = y;
    }

    public static double getScaledX(double xCoordinate){
        if(kx ==null ||shiftX==null)
            throw new RuntimeException("Nie podano wszystkich parametrów niezbednych do wykonania skalowania pnktu!\nNa poczatku użyj metody calculateScale potem tej metody");
        return xCoordinate*kx-shiftX;
    }
    public static double getScaledY(double yCoordinate){
        if(ky ==null ||shiftY==null)
            throw new RuntimeException("Nie podano wszystkich parametrów niezbednych do wykonania skalowania pnktu!\nNa poczatku użyj metody calculateScale potem tej metody");
        return yCoordinate*ky-shiftY;
    }

    public static void calculateScale(ArrayList<Point> points) {
        if(x<=0||y<=0)
            throw new RuntimeException("Nie podano wymiarów mapy lub podane wymiary są nieodpowiednie");
        getY(points);
        if (minY > points.get(0).getY())
            minY = points.get(0).getY();
        if (maxY < points.get(points.size() - 1).getY())
            maxY = points.get(points.size() - 1).getY();
        getX(points);
        if (minX > points.get(0).getX())
            minX = points.get(0).getX();
        if (maxX < points.get(points.size() - 1).getX())
            maxX = points.get(points.size() - 1).getX();

        kx = x / (maxX - minX);
        ky = y / (maxY - minY);

        shiftX = kx*minX;
        shiftY=ky*minY;
    }

    private static void getY(ArrayList<Point> points) {
        Collections.sort(points, (a, b) -> {
            if (a.getY() > b.getY())
                return 1;
            if (a.getY() == b.getY())
                return 0;
            else
                return -1;
        });
    }

    private static void getX(ArrayList<Point> points) {
        Collections.sort(points, (a, b) -> {
            if (a.getX() > b.getX())
                return 1;
            if (a.getX() == b.getX())
                return 0;
            else
                return -1;
        });
    }
}



