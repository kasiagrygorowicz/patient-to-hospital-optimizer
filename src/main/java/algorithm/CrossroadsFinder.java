package algorithm;

import data.elements.Crossroad;
import data.elements.Hospital;
import data.elements.Road;
import data.elements.RoadAndCrossroads;
import data.graph.Point;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.zip.DataFormatException;

public class CrossroadsFinder {

    private HashSet<Crossroad> crossroads;

    public CrossroadsFinder() {
        this.crossroads = new HashSet<>();
    }

    public List<RoadAndCrossroads> calculateCrossroads(HashSet<Road> roads, HashSet<Hospital> hospitals) throws DataFormatException {

        Hospital[] hospitals_2 = new Hospital[hospitals.size()];
        int n = 0;
        for (Hospital hospital : hospitals) {
            hospitals_2[n] = hospital;
            n++;
        }

        List<RoadAndCrossroads> roadsWithCrossroads = new ArrayList<>();
        int numberId = 0;
        for (Road road1 : roads) {
            RoadAndCrossroads roadAndCrossroads = new RoadAndCrossroads(road1);
            for (Road road2 : roads) {
                Point intersection;
                try {
                    intersection = calculateIntersection(road1, road2, hospitals_2);
                } catch (DataFormatException e) {
                    continue;
                }
                if (intersection != null) {
                    if (checkIntersection(intersection, road1, road2, hospitals_2)) {
                        Crossroad crossroad;
                        try {
                            crossroad = new Crossroad(numberId, intersection.getX(), intersection.getY());
                        } catch (DataFormatException e) {
                            throw new DataFormatException("Nieznany błąd przy tworzeniu skrzyżowania");
                        }
                        if (!crossroads.contains(crossroad)) {
                            this.crossroads.add(crossroad);
                            numberId++;
                        }
                        roadAndCrossroads.addCrossroad(crossroad);
                    }
                }
            }
            roadsWithCrossroads.add(roadAndCrossroads);
        }
        return roadsWithCrossroads;
    }

    public HashSet<Crossroad> getCrossroads() {
        return this.crossroads;
    }

    private Point calculateIntersection(Road A, Road B, Hospital[] fullHospitals) throws DataFormatException {


        Point A1 = getFullHospitalFromId(A.getHospital1().getId(), fullHospitals).getLocation();
        Point A2 = getFullHospitalFromId(A.getHospital2().getId(), fullHospitals).getLocation();
        Point B1 = getFullHospitalFromId(B.getHospital1().getId(), fullHospitals).getLocation();
        Point B2 = getFullHospitalFromId(B.getHospital2().getId(), fullHospitals).getLocation();

        double aA = A2.getY() - A1.getY();
        double bA = A1.getX() - A2.getX();
        double cA = aA * A1.getX() + bA * A1.getY();

        double aB = B2.getY() - B1.getY();
        double bB = B1.getX() - B2.getX();
        double cB = aB * B1.getX() + bB * B1.getY();

        double factor = aA * bB - aB * bA;

        if (factor == 0) {
            return null;
        } else {
            double intersectionX = (bB * cA - bA * cB) / factor;
            double intersectionY = (aA * cB - aB * cA) / factor;

            return new Point(intersectionX, intersectionY);
        }
    }

    private Hospital getFullHospitalFromId(int id, Hospital[] fullHospitals) {
        for (Hospital hospital : fullHospitals) {
            if (hospital.getId() == id) {
                return hospital;
            }
        }
        return null;
    }

    private boolean checkIntersection(Point intersection, Road road1, Road road2, Hospital[] fullHospitals) {

        double x = intersection.getX();
        double y = intersection.getY();

        Point road1_1 = getFullHospitalFromId(road1.getHospital1().getId(), fullHospitals).getLocation();
        Point road1_2 = getFullHospitalFromId(road1.getHospital2().getId(), fullHospitals).getLocation();
        Point road2_1 = getFullHospitalFromId(road2.getHospital1().getId(), fullHospitals).getLocation();
        Point road2_2 = getFullHospitalFromId(road2.getHospital2().getId(), fullHospitals).getLocation();

        if (road1_1.getX() < road1_2.getX()) {
            if (x <= road1_1.getX() || road1_2.getX() <= x) {
                return false;
            }
        } else if (road1_2.getX() < road1_1.getX()) {
            if (x <= road1_2.getX() || road1_1.getX() <= x) {
                return false;
            }
        } else {
            if (road1_1.getY() < road1_2.getY()) {
                if (road1_1.getY() <= y || road1_2.getY() >= y) {
                    return false;
                }
            }
            if (road1_2.getY() < road1_1.getY()) {
                if (road1_2.getY() >= y || road1_1.getY() <= y) {
                    return false;
                }
            }
        }

        if (road2_1.getX() < road2_2.getX()) {
            if (x <= road2_1.getX() || road2_2.getX() <= x) {
                return false;
            }
        } else if (road2_2.getX() < road2_1.getX()) {
            if (x <= road2_2.getX() || road2_1.getX() <= x) {
                return false;
            }
        } else {
            if (road2_1.getY() < road2_2.getY()) {
                if (road2_1.getY() >= y || road2_2.getY() <= y) {
                    return false;
                }
            }
            if (road2_2.getY() < road2_1.getY()) {
                if (road2_2.getY() >= y || road2_1.getY() <= y) {
                    return false;
                }
            }
        }
        return true;
    }
}
