package algorithm;

import data.elements.Hospital;
import data.elements.Patient;

import java.util.ArrayList;
import java.util.List;

public class TransportResult {

    private final Patient patient;
    private List<Hospital> orderOfHospitals;
    private boolean isAdmitted = false;

    public TransportResult(Patient patient) {
        this.patient = patient;
        this.orderOfHospitals = new ArrayList<>();
    }

    public void addHospital(Hospital hospital) {
        this.orderOfHospitals.add(hospital);
    }

    public void setAdmitted(boolean flag) {
        this.isAdmitted = flag;
    }

    public Patient getPatient() {
        return patient;
    }

    public List<Hospital> getOrderOfHospitals() {
        return orderOfHospitals;
    }

    public boolean isAdmitted() {
        return isAdmitted;
    }

    @Override
    public String toString() {
        StringBuilder hospitals = new StringBuilder();
        for (Hospital h : this.orderOfHospitals) {
            hospitals.append(" -> ").append(h.getId());
        }

        String admitted = "";
        if (this.isAdmitted) {
            admitted += "PRZYJĘTY";
        } else {
            admitted += "W KOLECJE";
        }

        return "Pacjent z ID " + this.getPatient().getId() + " trafi kolejno do szpitali o ID:  " + hospitals + " " + admitted;
    }
}
