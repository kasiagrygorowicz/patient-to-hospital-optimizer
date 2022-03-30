package fileHandler;

import algorithm.Border;
import algorithm.Scaling;
import data.elements.Patient;
import data.graph.Point;
import exceptions.DuplicateDataException;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.LineNumberReader;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.zip.DataFormatException;

public class PatientsReader {
    private HashSet<Patient> patients = new HashSet<>();
    private LineNumberReader lineReader;
    private ArrayList<Patient> inTheCountry = new ArrayList<>();
    private ArrayList<Patient> outsideOfTheCountry = new ArrayList<>();
    private final ArrayList border;
    private boolean wasOutside = false;

    public PatientsReader(ArrayList<Point> border) {
        this.border = border;
    }

    public void readFromFile(String path) throws IOException {
        BufferedReader bf = new BufferedReader(new FileReader(path));
        lineReader = new LineNumberReader(bf);
        readPatients();
    }

    private void readPatients() throws IOException {

        String doublePattern = "-?\\d+\\.?\\d*";
        Pattern patientPattern = Pattern.compile("\\s*(-?\\d*)\\s*\\|\\s*(" + doublePattern + ")\\s*\\|\\s*(" + doublePattern + ")\\s*");
        try {
            String line;
            line = lineReader.readLine();
            Matcher matcher = Pattern.compile("^\\s*\\#.*").matcher(line);
            if (line == null || line.isEmpty())
                throw new DataFormatException("Pierwsza linijka pliku musi zaczynać się od #.\nW pliku nie może być pustych linii.");
            if (!matcher.matches())
                throw new DataFormatException("Pierwsza linijka pliku musi zaczynać się od #.\nW pliku nie może być pustych linii.");
            matcher = patientPattern.matcher("");
            while ((line = lineReader.readLine()) != null) {
                matcher.reset(line);
                if (!matcher.matches()) {
                    String msg = "Linia o numerze: " + lineReader.getLineNumber() + " posiada źle przedstawione dane\nMuszą być podane 3 argumenty oddzielone od siebie znakiem '|'. Argumenty muszą mieć odpowiednio typy danych: całkowity, double, double";
                    throw new IllegalStateException(msg);
                }
                addPatient(matcher.group(1), matcher.group(2), matcher.group(3));
            }
        } catch (RuntimeException | DataFormatException e) {
            throw new RuntimeException("Błąd w linijce: " + lineReader.getLineNumber() + "\n" + e.getMessage());
        }
    }

    public boolean isWasOutside() {
        return wasOutside;
    }

    public void addPatient(String id, String x, String y) throws DataFormatException {
        Patient patient = new Patient(Integer.parseInt(id), new Point(Double.parseDouble(x), Double.parseDouble(y)));
        if (!patients.add(patient))
            throw new DuplicateDataException("Pacjent o id " + patient.getId() + " już istnieje.");
        if (Border.isInsidePolygon(border, patient.getLocation())) {
            inTheCountry.add(patient);
            wasOutside = false;
        } else {
            outsideOfTheCountry.add(patient);
            wasOutside = true;
        }
        patient.setAppearance(Scaling.applyOffset(patient.getLocation()));
    }

    public ArrayList<Patient> getInTheCountry() {
        return inTheCountry;
    }

    public ArrayList<Patient> getOutsideOfTheCountry() {
        return outsideOfTheCountry;
    }
}