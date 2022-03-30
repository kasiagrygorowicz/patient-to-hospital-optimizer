package algorithm;

import data.elements.Hospital;
import data.elements.Patient;

import java.util.HashSet;

public class FirstTravel {

    public static Hospital goToFirstHospital(Patient patient, HashSet<Hospital> hospitals) {
        double minDistance = Double.POSITIVE_INFINITY;
        Hospital destinationHospital = null;

        for (Hospital hospital : hospitals) {
            double firstSquared = Math.pow(patient.getLocation().getX() - hospital.getLocation().getX(), 2);
            double secondSquared = Math.pow(patient.getLocation().getY() - hospital.getLocation().getY(), 2);
            double distance = Math.sqrt(firstSquared + secondSquared);

            if (distance < minDistance) {
                minDistance = distance;
                destinationHospital = hospital;
            } else if (distance == minDistance) {
                if (hospital.getId() < destinationHospital.getId()) {
                    destinationHospital = hospital;
                    minDistance = distance;
                }
            }
        }
        return destinationHospital;
    }
}


