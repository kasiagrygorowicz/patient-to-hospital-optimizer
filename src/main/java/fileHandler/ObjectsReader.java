package fileHandler;

import algorithm.GrahamAlgorithm;
import algorithm.Scaling;
import data.elements.Hospital;
import data.elements.ImportantObject;
import data.elements.Road;
import data.graph.Point;
import exceptions.DuplicateDataException;

import java.io.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.zip.DataFormatException;

public class ObjectsReader {
    private BufferedReader bf;
    private HashSet<Hospital> hospitals = new HashSet<>();
    private HashSet<Road> roads = new HashSet<>();
    ;
    private HashSet<ImportantObject> objects = new HashSet<>();
    ;
    private LineNumberReader lineReader;
    private HashSet<Point> points = new HashSet<>();
    private ArrayList<Point> boarder = new ArrayList<>();

    public ObjectsReader(String path) throws FileNotFoundException {
        bf = new BufferedReader(new FileReader(path));
        lineReader = new LineNumberReader(bf);
    }

    private void calculateAppearance() throws DataFormatException {
        Scaling.searchMinPoints(points);

        for (Hospital h : hospitals) {
            h.setAppearance(Scaling.applyOffset(h.getLocation()));
        }
        for (ImportantObject o : objects) {
            o.setAppearance(Scaling.applyOffset(o.getLocation()));
        }
    }

    public void readObjects() throws IOException, RuntimeException, DataFormatException {
        String doublePattern = "-?\\d+\\.?\\d*";
        Pattern hospitalPattern = Pattern.compile("\\s*(?<id>-?\\d+)\\s*\\|(?<name>.*\\w.*)\\|\\s*(?<x>" + doublePattern + ")\\s*\\|\\s*(?<y>" + doublePattern + ")\\s*\\|\\s*(?<beds>-?\\d+)\\s*\\|\\s*(?<freeBeds>-?\\d+)");
        Pattern importantObjectPattern = Pattern.compile("\\s*(?<id>-?\\d+)\\s*\\|\\s*(?<name>.*\\w.*)\\|\\s*(?<x>" + doublePattern + ")\\s*\\|\\s*(?<y>" + doublePattern + ")");
        Pattern roadPattern = Pattern.compile("\\s*(?<id>-?\\d+)\\s*\\|\\s*(?<idh1>-?\\d+)\\s*\\|\\s*(?<idh2>-?\\d+)\\s*\\|\\s*(?<distance>" + doublePattern + ")");
        Pattern hp = Pattern.compile("^\\s*\\#.*szpital.*", Pattern.CASE_INSENSITIVE);
        Pattern ob = Pattern.compile("^\\s*\\#.*obiekt.*", Pattern.CASE_INSENSITIVE);
        Pattern rd = Pattern.compile("^\\s*\\#.*drogi.*", Pattern.CASE_INSENSITIVE);

        Matcher matcher = hp.matcher("");
        Matcher matcherOB = ob.matcher("");
        Matcher matcherRD = rd.matcher("");
        int helper = -1;
        String line;

        try {
            line = lineReader.readLine();
            if (line == null || line.isEmpty())
                throw new DataFormatException("Pierwsza linijka pliku musi zaczynać się od # oraz zawierać nazwę obiektu, którym ma być szpital\nNa poczatku obowiązkowo należy podać szpitale.\nW pliku nie może być pustych linii.");
            matcher.reset(line);
            if (!matcher.matches())
                throw new DataFormatException("Pierwsza linijka pliku musi zaczynać się od # oraz zawierać nazwę obiektu, którym ma być szpital\nNa poczatku obowiązkowo należy podać szpitale.\nW pliku nie może być pustych linii.");

            matcher = hospitalPattern.matcher("");
            while ((line = lineReader.readLine()) != null) {
                matcher.reset(line);
                if (!matcher.matches()) {
                    matcherOB.reset(line);
                    matcherRD.reset(line);
                    if (matcherOB.matches()) {
                        helper = 1;
                        matcher = importantObjectPattern.matcher("");
                        break;
                    } else if (matcherRD.matches()) {
                        helper = 2;
                        matcher = roadPattern.matcher("");
                        break;
                    } else {
                        String msg = "Posiada źle podane dane\nMusi być podanych 6 argumentów oddzielonych od siebie znakiem '|'. Argumenty muszą mieć odpowiednie typy danych: całkowity, tesktowy, zmiennoprzecinkowy, zmiennoprzecinkowy, całkowity, całowity\nWięcej informacji w specyfikacji funckjonalnej";
                        throw new IllegalStateException(msg);
                    }
                }
                addHospital(matcher.group("id"), matcher.group("name").trim(), matcher.group("x"), matcher.group("y"), matcher.group("beds"), matcher.group("freeBeds"));
            }

            if (helper == 1) {
                while ((line = lineReader.readLine()) != null) {
                    matcher.reset(line);
                    if (!matcher.matches()) {
                        matcherRD.reset(line);
                        if (!matcherRD.matches()) {
                            String msg = "Posiada źle przedstawione dane\nMuszą być podane 4 argumenty oddzielone od siebie znakiem '|'. Argumenty muszą mieć odpowiednio typy danych: całkowity, tekstowy, double, double";
                            throw new IllegalStateException(msg);
                        } else {
                            helper = 2;
                            matcher = roadPattern.matcher("");
                            break;
                        }
                    }
                    addImportantObject(matcher.group("id"), matcher.group("name").trim(), matcher.group("x"), matcher.group("y"));
                }
            }

            while ((line = lineReader.readLine()) != null && helper == 2) {
                matcher.reset(line);
                if (!matcher.matches()) {
                    String msg = "Posiada źle przedstawione dane\nMuszą być podane 4 argumenty oddzielone od siebie znakiem '|'. Argumenty muszą mieć odpowiednio typy danych: całkowity, całkowity, całkowity, double";
                    throw new IllegalStateException(msg);
                }
                addRoad(matcher.group("id"), matcher.group("idh1"), matcher.group("idh2"), matcher.group("distance"));
            }

            isEnoughObjects();
            createBoarder();
        } catch (RuntimeException | DataFormatException e) {

            throw new RuntimeException("Błąd w linijce: " + lineReader.getLineNumber() + "\n" + e.getMessage());
        }
        calculateAppearance();
    }

    public void addHospital(String id, String name, String x, String y, String beds, String freeBeds) throws DataFormatException {
        Hospital hospital = new Hospital(Integer.parseInt(id), name, Double.parseDouble(x), Double.parseDouble(y), Integer.parseInt(beds), Integer.parseInt(freeBeds));
        if (!points.add(hospital.getLocation()))
            throw new DuplicateDataException("Próba dodania o loalizacji (współrzędnych) identycznych jak szpital wcześniej dodany.\nSzpitale muszą znajdować się w unikatowych lokalizacjach");

        if (!hospitals.add(hospital))
            throw new DuplicateDataException("Podano szpital, który już wczesniej został dodany do listy.\nKażdy szpiatal musi mieć unikatowe id oraz lokalizacje");
    }

    private void addImportantObject(String id, String name, String x, String y) throws DataFormatException {

        ImportantObject object = new ImportantObject(Integer.parseInt(id), name, Double.parseDouble(x), Double.parseDouble(y));
        if (!objects.add(object))
            throw new DuplicateDataException("Podano specjalny obiekt, który już wczesniej został dodany do listy.\nKażdy obiekt musi mieć unikatowe id oraz lokalizacje");
        if (!points.add(object.getLocation()))
            throw new DuplicateDataException("Próba wstawienia obiektu specjalnego na miejsce juz zajęte przez inny obiekt lub szpital\nOperacja niedozwolona");
    }

    private void addRoad(String id, String idh1, String idh2, String distance) throws DataFormatException {
        Hospital h1 = new Hospital(Integer.parseInt(idh1));
        Hospital h2 = new Hospital(Integer.parseInt(idh2));
        boolean hh1 = hospitals.contains(h1);
        boolean hh2 = hospitals.contains(h2);
        if (!(hh1 && hh2))
            throw new DataFormatException("Próba stworzenia drogi w której szpital do którego droga prowadzi nie istnieje");
        if (!roads.add(new Road(Integer.parseInt(id), h1, h2, Double.parseDouble(distance))))
            throw new DuplicateDataException("Podano drogę, która już wczesniej została dodany do listy.\nKażda obiekt musi mieć unikatowe id oraz parę spitali, którą łączy");
    }

    public ArrayList<Point> getBoarder() {
        return boarder;
    }

    private void createBoarder() {
        boarder = GrahamAlgorithm.grahamScan(new ArrayList<>(points));
    }

    private void isEnoughObjects() {
        if (hospitals.size() + objects.size() < 3) {
            throw new RuntimeException("Nie podano prawidłowej ilości obiektów i szpitali\nPotrzeba minimum 3");
        }
    }

    public HashSet<Hospital> getHospitals() {
        return hospitals;
    }

    public HashSet<Road> getRoads() {
        return roads;
    }

    public HashSet<ImportantObject> getObjects() {
        return objects;
    }
}