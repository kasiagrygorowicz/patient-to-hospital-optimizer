package algorithm;

import data.elements.Crossroad;
import data.elements.Hospital;
import data.elements.RoadAndCrossroads;
import data.graph.Point;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;

public class MatrixCreator {

    private final HashSet<Hospital> hospitals;
    private final HashSet<Crossroad> crossroads;
    private Hospital[] orderOfHospitals;

    public MatrixCreator(HashSet<Hospital> hospitals, HashSet<Crossroad> crossroads) {
        this.hospitals = hospitals;
        this.crossroads = crossroads;
    }

    public double[][] createAdjacencyMatrix(List<RoadAndCrossroads> roads) {

        Hospital[] hospitalsTab = new Hospital[this.hospitals.size()];
        Crossroad[] crossroadsTab = new Crossroad[this.crossroads.size()];
        int n = crossroads.size() + hospitals.size();

        int iter = 0;
        for (Hospital hospital : this.hospitals) {
            hospitalsTab[iter] = hospital;
            iter++;
        }

        this.orderOfHospitals = hospitalsTab;

        iter = 0;
        for (Crossroad crossroad : this.crossroads) {
            crossroadsTab[iter] = crossroad;
            iter++;
        }

        double[][] adjacencyMatrix = new double[n][n];

        for (RoadAndCrossroads roadC : roads) {
            if (roadC.getCrossroads().size() == 0) {
                int id1 = roadC.getRoad().getHospital1().getId();
                int id2 = roadC.getRoad().getHospital2().getId();

                int number1 = -1;
                int number2 = -1;
                for (int i = 0; i < hospitalsTab.length; i++) {
                    if (hospitalsTab[i].getId() == id1) {
                        number1 = i;
                    }
                    if (hospitalsTab[i].getId() == id2) {
                        number2 = i;
                    }
                }
                adjacencyMatrix[number1][number2] = roadC.getRoad().getDistance();
                adjacencyMatrix[number2][number1] = roadC.getRoad().getDistance();
            } else {
                Hospital tmpHospital = roadC.getRoad().getHospital1();
                Hospital tmpHospital1 = roadC.getRoad().getHospital2();

                for (Hospital h : hospitals) {
                    if (tmpHospital.getId() == h.getId()) {
                        tmpHospital = h;
                    }
                    if (tmpHospital1.getId() == h.getId()) {
                        tmpHospital1 = h;
                    }
                }
                Crossroad[] tmpCrossroads = Arrays.copyOf(crossroadsTab, crossroadsTab.length);
                for (Crossroad cros : tmpCrossroads) {
                    double distanceHC = calculateDistance(tmpHospital.getLocation(), cros.getLocation());
                    double distanceHH = calculateDistance(tmpHospital.getLocation(), tmpHospital1.getLocation());
                    cros.setDistanceToVertex(distanceHC / distanceHH * roadC.getRoad().getDistance());
                }

                int j;
                if (tmpCrossroads.length > 1) {
                    for (int i = 1; i < tmpCrossroads.length; i++) {
                        Crossroad tmp = tmpCrossroads[i];
                        for (j = i - 1; j >= 0 && tmpCrossroads[j].getDistanceToVertex() > tmp.getDistanceToVertex(); j--) {
                            tmpCrossroads[j + 1] = tmpCrossroads[j];
                        }
                        tmpCrossroads[j + 1] = tmp;
                    }
                }
                for (int i = tmpCrossroads.length - 1; i > 0; i--) {
                    double newDistance = tmpCrossroads[i].getDistanceToVertex() - tmpCrossroads[i - 1].getDistanceToVertex();
                    tmpCrossroads[i].setDistanceToVertex(newDistance);
                }

                int id1 = tmpHospital.getId();
                int id2 = tmpHospital1.getId();
                int number1H = -1;
                int number2H = -1;

                for (int i = 0; i < hospitalsTab.length; i++) {
                    if (hospitalsTab[i].getId() == id1) {
                        number1H = i;
                    }
                    if (hospitalsTab[i].getId() == id2) {
                        number2H = i;
                    }
                }

                for (Crossroad crossroad : crossroadsTab) {
                    int crossroadId = crossroad.getId();
                    for (Crossroad tmpCrossroad : tmpCrossroads) {
                        if (tmpCrossroad.getId() == crossroadId) {
                            crossroad.setDistanceToVertex(tmpCrossroad.getDistanceToVertex());
                        }
                    }
                }

                for (int i = 0; i < crossroadsTab.length; i++) {
                    int number2 = hospitalsTab.length + i;
                    adjacencyMatrix[number1H][number2] = crossroadsTab[i].getDistanceToVertex();
                    adjacencyMatrix[number2][number1H] = crossroadsTab[i].getDistanceToVertex();
                    number1H = number2;
                }

                int number2 = crossroadsTab.length + hospitalsTab.length - 1;

                double distanceHH = calculateDistance(tmpHospital.getLocation(), tmpHospital1.getLocation());
                double distanceCH = calculateDistance(crossroadsTab[crossroadsTab.length - 1].getLocation(), tmpHospital1.getLocation());
                double newDistance = distanceCH / distanceHH * roadC.getRoad().getDistance();

                adjacencyMatrix[number2H][number2] = newDistance;
                adjacencyMatrix[number2][number2H] = newDistance;
            }
        }
        return adjacencyMatrix;
    }

    public Hospital[] getOrderOfHospitals() {
        return this.orderOfHospitals;
    }

    private double calculateDistance(Point pointA, Point pointB) {
        double firstSquared = Math.pow(pointA.getX() - pointB.getX(), 2);
        double secondSquared = Math.pow(pointA.getY() - pointB.getY(), 2);
        return Math.sqrt(firstSquared + secondSquared);
    }
}