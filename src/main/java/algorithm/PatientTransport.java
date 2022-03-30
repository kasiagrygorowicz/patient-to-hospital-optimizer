package algorithm;

import data.elements.*;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.zip.DataFormatException;

import static algorithm.FirstTravel.goToFirstHospital;

public class PatientTransport {

    private HashSet<Hospital> hospitals;
    private HashSet<Road> roads;
    private HashSet<Crossroad> crossroads;
    private List<RoadAndCrossroads> roadsAndCrossroads;

    public PatientTransport(HashSet<Hospital> hospitals, HashSet<Road> roads) {
        this.hospitals = hospitals;
        this.roads = roads;
    }

    public void calculateCrossroads() throws DataFormatException {
        CrossroadsFinder crossroadsFinder = new CrossroadsFinder();
        this.roadsAndCrossroads = crossroadsFinder.calculateCrossroads(this.roads, this.hospitals);
        this.crossroads = crossroadsFinder.getCrossroads();
    }

    public HashSet<Crossroad> getCrossroads() {
        return this.crossroads;
    }

    public TransportResult[] transportPatientsFromSet(ArrayList<Patient> patients, ArrayList<Patient> dPatients) {
        Patient[] tmpPatients = new Patient[patients.size() - dPatients.size()];
        int iter = 0;
        if (dPatients.size() == 0) {
            for (Patient patient : patients) {
                tmpPatients[iter] = patient;
                iter++;
            }
        } else {
            for (Patient patient : patients) {
                boolean flag = true;
                for (Patient donePatient : dPatients) {
                    if (patient.getId() == donePatient.getId()) {
                        flag = false;
                        break;
                    }
                }
                if (flag) {
                    tmpPatients[iter] = patient;
                    iter++;
                }
            }
        }

        if (tmpPatients.length > 1) {
            for (int i = 0; i < tmpPatients.length; i++) {
                Patient tmp = tmpPatients[i];
                int j;
                for (j = i - 1; j >= 0 && tmpPatients[j].getId() > tmp.getId(); j--) {
                    tmpPatients[j + 1] = tmpPatients[j];
                }
                tmpPatients[j + 1] = tmp;
            }
        }

        TransportResult[] transportResults = new TransportResult[tmpPatients.length];
        for (int i = 0; i < tmpPatients.length; i++) {
            transportResults[i] = transportPatientsForOne(tmpPatients[i]);
        }

        return transportResults;
    }

    public TransportResult transportPatientsForOne(Patient patient) {
        TransportResult transportResult = new TransportResult(patient);
        Hospital tmpHospital = goToFirstHospital(transportResult.getPatient(), this.hospitals);
        patientWasInTheHospital(tmpHospital.getId());
        transportResult.addHospital(tmpHospital);

        if (tmpHospital.getFreeBeds() == 0) {
            while (!transportResult.isAdmitted()) {
                MatrixCreator matrixCreator = new MatrixCreator(this.hospitals, this.crossroads);
                double[][] matrix = matrixCreator.createAdjacencyMatrix(this.roadsAndCrossroads);
                Hospital[] orderOfHospitals = matrixCreator.getOrderOfHospitals();
                if (!checkIfTheHospitalHasRoads(matrix, orderOfHospitals, tmpHospital.getId())) {
                    break;
                }
                Hospital[] orderFromTheNearestHospital = Dijkstra.calculateTheNearestHospitals(matrix, orderOfHospitals, tmpHospital);
                int iter = 0;
                for (; iter < orderFromTheNearestHospital.length; iter++) {
                    if (!getHospitalFromId(orderFromTheNearestHospital[iter].getId()).isWasThePatientHere()) {
                        tmpHospital = orderFromTheNearestHospital[iter];
                        break;
                    }
                }

                if (iter == orderFromTheNearestHospital.length) {
                    break;
                }

                transportResult.addHospital(tmpHospital);
                patientWasInTheHospital(tmpHospital.getId());
                if (tmpHospital.getFreeBeds() != 0) {
                    addPatientToHospital(tmpHospital);
                    transportResult.setAdmitted(true);
                }
            }
        } else {
            addPatientToHospital(tmpHospital);
            transportResult.setAdmitted(true);
        }
        clearPresenceInHospitals();

        return transportResult;
    }

    private int getIndexFromArrayWithId(Hospital[] hospitals, int id) {
        int i;
        for (i = 0; i < hospitals.length; i++)
            if (hospitals[i].getId() == id) {
                return i;
            }
        return i;
    }

    private boolean checkIfTheHospitalHasRoads(double[][] matrix, Hospital[] hospitals, int id) {
        int index = getIndexFromArrayWithId(hospitals, id);
        for (int i = 0; i < matrix[index].length; i++) {
            if (matrix[index][i] != 0) {
                return true;
            }
        }
        return false;
    }

    private void patientWasInTheHospital(int id) {
        for (Hospital hospital : this.hospitals) {
            if (hospital.getId() == id) {
                hospital.setWasThePatientHere(true);
            }
        }
    }

    private void clearPresenceInHospitals() {
        for (Hospital hospital : this.hospitals) {
            hospital.setWasThePatientHere(false);
        }
    }


    public Hospital getHospitalFromId(int id) {
        for (Hospital hospital : this.hospitals) {
            if (hospital.getId() == id) {
                return hospital;
            }
        }
        return null;
    }

    private void addPatientToHospital(Hospital hospital) {
        for (Hospital h : this.hospitals) {
            if (h.getId() == hospital.getId()) {
                h.addPatient();
            }
        }
    }


}
