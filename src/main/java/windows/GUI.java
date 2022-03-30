package windows;

import algorithm.*;
import fileHandler.*;

import java.awt.geom.Rectangle2D;
import java.awt.geom.Line2D;
import java.awt.geom.Ellipse2D;
import java.awt.geom.RoundRectangle2D;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import data.elements.Crossroad;
import data.elements.Hospital;
import data.elements.ImportantObject;
import data.elements.Patient;
import data.graph.Point;
import data.elements.Road;

import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.*;
import java.util.Timer;
import java.util.zip.DataFormatException;

public class GUI implements Runnable {

    PatientTransport patientTransport;
    JFrame frame;
    JButton saveB, loadB, addB, addPatient, clearB;
    ButtonShow showBListener;
    JSlider fastS;
    JFileChooser fileChooser;
    BoardCanvas canvas;
    ObjectsReader objects_reader;
    PatientsReader patients_reader;
    private JLabel info_label;
    Color color_hospital = Color.YELLOW;
    Color color_road = Color.RED;
    Color color_object = Color.BLUE;
    Color color_patient = Color.CYAN;
    final int boardSize = 60;
    final int pointSize = 10;

    final int frameSize = boardSize * pointSize + 100;
    final int maxSpeed = 700;
    int Speed = 350;
    private final JToggleButton playB = new JToggleButton("Start");
    private final JToggleButton showB = new JToggleButton("Show");

    private HashSet<Hospital> hospitals;
    private HashSet<Road> roads;
    private HashSet<ImportantObject> objects;
    private ArrayList<Patient> patients;
    private ArrayList<Patient> donePatients;

    HashSet<Crossroad> crossroads_set;

    private float multiply_factor = 1;
    private JButton zoomB;

    private JTextArea patientsOutside = new JTextArea(50,15);
    private JScrollPane displayOutside =new JScrollPane(patientsOutside);
    private JLabel patientsOutsideLabel =new JLabel("Pacjenci z poza państwa:");
    private JTextArea displayRoadTextArea = new JTextArea(50,30);
    private JScrollPane displayRoadScroll =new JScrollPane(displayRoadTextArea);
    private JLabel roadLabel =new JLabel("Droga pacjentów:");


    private ArrayList<String> log_text = new ArrayList<>();
    @Override
    public void run() {
        createGUI();
    }


    private void createGUI() {
        frame = new JFrame("PomocnikNaczelnika");
        frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        options();
        setupBoard();
        frame.setVisible(true);
        frame.setResizable(false);
        frame.setLocationRelativeTo(null);


    }

    public void info(String info) {
        info_label.setText(info);
    }

    public void log(String msg) {
        saveB.setEnabled(true);
        log_text.add(msg);
    }


    public ArrayList<Point> getBoarder(){
        if(objects_reader == null) return null;
        return objects_reader.getBoarder();
    }

    private void options() {
        clearB = new JButton("Clear");
        clearB.addActionListener(new ButtonClear(this));
        zoomB = new JButton("Zoom");
        zoomB.addActionListener(new ButtonZoom(this));
        saveB = new JButton("Save");
        saveB.addActionListener(new ButtonSave(this));
        loadB = new JButton("Load");
        loadB.addActionListener(new ButtonLoad(this));
        addB = new JButton("Load patient");
        addB.addActionListener(new ButtonLoadPatient(this));
        addPatient = new JButton("Add patient");
        addPatient.addActionListener(new ButtonAddPatient(this));
        fastS = new JSlider(JSlider.HORIZONTAL, 1, maxSpeed, 350);
        playB.addItemListener(new ButtonPlay(this));
        showBListener = new ButtonShow(this);
        showB.addItemListener(showBListener);


        Hashtable lTable = new Hashtable();
        lTable.put(50, new JLabel("Slow"));
        lTable.put(650, new JLabel("Fast"));
        fastS.setLabelTable(lTable);
        fastS.setPaintLabels(true);

        fastS.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                JSlider source = (JSlider) e.getSource();
                Speed = (maxSpeed+1)-source.getValue();
            }
        });

        patientsOutside.setEditable(false);
        displayOutside.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
        displayRoadTextArea.setEditable(false);


        JPanel pp = new JPanel(new BorderLayout());
        patientsOutsideLabel.setLabelFor(displayOutside);
        pp.add(displayOutside);
        pp.add(patientsOutsideLabel,BorderLayout.NORTH);

        JPanel roadDisplayer = new JPanel(new BorderLayout());
        roadLabel.setLabelFor(displayRoadScroll);
        roadDisplayer.add(displayRoadScroll);
        roadDisplayer.add(roadLabel,BorderLayout.NORTH);




        JPanel controlPanel = new JPanel();
        controlPanel.add(showB);
        controlPanel.add(fastS);
        controlPanel.add(playB);
        controlPanel.add(zoomB);
        controlPanel.add(clearB);
        JPanel optFilePanel = new JPanel();
        optFilePanel.add(saveB);
        optFilePanel.add(loadB);
        optFilePanel.add(addB);
        optFilePanel.add(addPatient);
        showB.setEnabled(false);
        playB.setEnabled(false);
        saveB.setEnabled(false);
        addB.setEnabled(false);
        addPatient.setEnabled(false);
        zoomB.setEnabled(false);
        clearB.setEnabled(false);
        JPanel optFilePane2 = new JPanel();
        info_label = new JLabel("Kliknij element, aby uzyskać informacje");
        info_label.setHorizontalAlignment(JLabel.HORIZONTAL);
        optFilePane2.add(info_label);
        canvas = new BoardCanvas(this);
        JPanel centerPanel = new JPanel();
        centerPanel.add(canvas);
        JPanel cont = new JPanel();
        frame.getContentPane().add(BorderLayout.NORTH ,controlPanel);
        frame.getContentPane().add(BorderLayout.CENTER, canvas);
        frame.getContentPane().add(BorderLayout.SOUTH, cont);
        frame.getContentPane().add(BorderLayout.WEST,pp);
        frame.getContentPane().add(BorderLayout.EAST,roadDisplayer);
        cont.setLayout(new FlowLayout(FlowLayout.LEFT));
        cont.add(optFilePanel);
        cont.add(optFilePane2);
    }

    protected void start_travel() {
        try{
            if (patients == null || patients.size() == 0) {
                showMessage("Brak pacjentów w kolejce");
                return;
            }
            if (hospitals == null || hospitals.size() == 0) {
                showMessage("Brak szpitali w kolejce");
                return;
            }

            if (objects.size() < 3) {
                showMessage("Potrzebne są minimum 1 szpital i 3 obiekty do uruchomienia");
                return;
            }

            PatientTransport patientTransport = new PatientTransport(hospitals, roads);
            patientTransport.calculateCrossroads();
            crossroads_set = patientTransport.getCrossroads();

            if(donePatients == null){
                donePatients = new ArrayList<>();
            }
            TransportResult[] transportResult = patientTransport.transportPatientsFromSet(patients, donePatients);
            donePatients = new ArrayList<>(patients);

            for (TransportResult t:transportResult){
                showMessage(t.toString());
                displayRoadTextArea.append(t.toString()+"\n");
                log(t.toString());
            }

        }
        catch (RuntimeException | DataFormatException e) {
            this.showMessage(e.getMessage());
        }
    }

    private void setupBoard() {
        canvas.setSize(boardSize,boardSize);
        canvas.setBGColor(Color.WHITE);
    }
    public void readObjects(ObjectsReader reader) {
        objects_reader = reader;
        hospitals = objects_reader.getHospitals();
        roads = objects_reader.getRoads();
        objects = objects_reader.getObjects();
        patientTransport = new PatientTransport(hospitals, roads);
        calculateCrossroads();
        drawRoads();
        drawImportantObjects();
        drawHospitals();
        drawBoarder();
    }
    private void drawBoarder() {
        canvas.drawBorder();
    }

    private void calculateCrossroads(){
        try {
            patientTransport.calculateCrossroads();
            crossroads_set = patientTransport.getCrossroads();
        } catch (DataFormatException e) {
            this.showMessage("Błąd krytyczny podczas obliczania skrzyżowań");
            return;
        }
    }

    private void drawPatients() {
        patients = patients_reader.getInTheCountry();
        for(Patient p : patients) {
            canvas.addPatient(p.getAppearance(), p.getId(),p.getLocation());
        }
    }
    private void drawHospitals() {
        for(Hospital h : hospitals) {
            canvas.addHospital(h.getAppearance(), h.getName(),h.getBeds(),h.getFreeBeds(),h.getId(),h.getLocation());
        }
    }

    private Hospital retrieveHospital(Hospital h) {
        if (hospitals.contains(h)) {
            for (Hospital o:hospitals) {
                if(h.getId() == o.getId()) {
                    return o;
                }
            }
        }
        return null;
    }

    public void calculateMultiply() {
        if (hospitals == null || objects == null) return;
        float max_x = 1,max_y = 1;
        for (Hospital h: hospitals) {
            float _x = (float) h.getAppearance().x,_y = (float) h.getAppearance().y;
            if (_x >max_x) {
                max_x = _x;
            }
            if (_y >max_y) {
                max_y = _y;
            }
        }
        for (ImportantObject ob: objects) {
            float _x = (float) ob.getAppearance().x,_y = (float) ob.getAppearance().y;
            if (_x >max_x) {
                max_x = _x;
            }
            if (_y >max_y) {
                max_y = _y;
            }
        }
        float _mul = 1;
        if (max_x >= max_y) _mul = max_x;
        if (max_y > max_x) _mul = max_y;
        multiply_factor = (float)(frameSize-180)/_mul;
        canvas.setMultiply(multiply_factor);
    }

    private void drawRoads() {
        for(Road r : roads) {
            Hospital h1 = retrieveHospital((Hospital) r.getHospital1());
            Hospital h2 = retrieveHospital((Hospital) r.getHospital2());
            if (h1 == null || h2 == null) {
                this.showMessage("Błąd krytyczny podczas rysowania dróg: niezgodność szpitali");
                return;
            }
            Point h_1 = h1.getAppearance();
            Point h_2 = h2.getAppearance();
            canvas.addRoad(h_1,h_2);
        }
    }

    private void drawImportantObjects() {
        for (ImportantObject o:objects) {
            canvas.addImportantObject(o.getAppearance(), o.getName(), o.getLocation());
        }
    }

    public void showMessage(String msg) {
        try {
            Thread.sleep(Speed);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        JOptionPane.showMessageDialog(null, msg);
    }
    protected int getFreeBeds(int id) {
        for (Hospital h:hospitals) {
            if (h.getId() == id) {
                return h.getFreeBeds();
            }
        }
        return -1;
    }

    public String[] addPatientForm() {

        String patient_id,patient_x,patient_y;
        JTextField id_Field = new JTextField(5);
        JTextField x_Field = new JTextField(5);
        JTextField y_Field = new JTextField(5);
        JPanel patient_form = new JPanel();
        patient_form.add(new JLabel("ID:"));
        patient_form.add(id_Field);
        patient_form.add(new JLabel("X:"));
        patient_form.add(x_Field);
        patient_form.add(new JLabel("Y:"));
        patient_form.add(y_Field);
        int result = JOptionPane.showConfirmDialog(null, patient_form,
                "Wprowadź dane pacjenta", JOptionPane.OK_CANCEL_OPTION);
        if (result == JOptionPane.OK_OPTION) {

            patient_id = id_Field.getText();
            patient_x = x_Field.getText();
            patient_y = y_Field.getText();

            String[] patient_data = new java.lang.String[3];
            patient_data[0] = patient_id;
            patient_data[1] = patient_x;
            patient_data[2] = patient_y;
            return patient_data;
        }
        return null;
    }

    Timer t;
    public class ButtonPlay implements ItemListener {
        private GUI gui;
        public void itemStateChanged(ItemEvent e) {
            playB.setText(playB.isSelected() ? "Stop" : "Start");
            if (playB.isSelected()) {
                t = new Timer();
                t.schedule(new TimerTask() {
                    @Override
                    public void run() {
                    }
                }, 0, Speed);
                gui.start_travel();
            }
            else {
                t.cancel();
            }
        }
        public ButtonPlay(GUI _gui) {
            gui = _gui;
        }
    }
    public class ButtonShow implements ItemListener {
        private GUI gui;
        private Boolean hide = false;
        public void itemStateChanged(ItemEvent e) {

            showB.setText(showB.isSelected() ? "Head" : "Show");
            if (showB.isSelected()) {
                hide = true;
                t = new Timer();
                t.schedule(new TimerTask() {
                    @Override
                    public void run() {


                    }
                }, 0, Speed);
            }
            else {
                hide = false;
                t.cancel();
            }
            canvas.redraw();
        }
        public Boolean isHide() {
            return this.hide;
        }
        public ButtonShow(GUI _gui) {
            gui = _gui;
        }
    }

    public class ButtonSave implements ActionListener {
        private GUI gui;
        public void actionPerformed(ActionEvent ev) {

            fileChooser = new JFileChooser("program/out");
            fileChooser.setDialogTitle("Wybierz folder do zapisania");
            int returnVal = fileChooser.showSaveDialog(frame);
            if(returnVal != JFileChooser.APPROVE_OPTION) {
                gui.showMessage("Plik nie został wybrany");
                return;
            }
            File fileToSave = fileChooser.getSelectedFile();
            String file_ext = ".log";
            String file_path = fileToSave.getAbsolutePath()+file_ext;
            try {
                FileWriter fw = new FileWriter(file_path);
                PrintWriter pw = new PrintWriter(fw);
                for (String msg: log_text) {
                    pw.println(msg);
                }
                pw.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        public ButtonSave(GUI _gui) {
            gui = _gui;
        }
    }
    public class ButtonAddPatient implements ActionListener {
        private GUI gui;
        public void actionPerformed(ActionEvent ev) {
            if (hospitals == null) {
                gui.showMessage("Najpierw dodaj co najmniej 1 szpital");
                return;
            }
            String[] patient_data = addPatientForm();

            if (patient_data == null) {
                gui.showMessage("Wprowadź dane pacjenta");
                return;
            }
            if (patients_reader == null) {
                patients_reader = new PatientsReader(objects_reader.getBoarder());
            }
            try {
                patients_reader.addPatient(patient_data[0], patient_data[1], patient_data[2]);
                if(patients_reader.isWasOutside()) {
                    patientsOutside.append(patients_reader.getOutsideOfTheCountry().get(patients_reader.getOutsideOfTheCountry().size() - 1).toString() + "\n");
                    gui.showMessage("Pacjent jest poza granicami państwa");
                }
            } catch (DataFormatException |RuntimeException e) {
                gui.showMessage(e.getMessage());
                return;
            }
            if (!patients_reader.isWasOutside()) {
                gui.showMessage("Pacjent jest dodany");
            }

            drawPatients();
            playB.setEnabled(true);
        }
        public ButtonAddPatient(GUI _gui) {
            gui = _gui;
        }
    }
    public class ButtonLoad implements ActionListener {
        GUI gui;
        public void actionPerformed(ActionEvent e) {
            fileChooser = new JFileChooser("program/in");
            fileChooser.setDialogTitle("Wybierz plik z obiektami");
            int returnVal = fileChooser.showOpenDialog(frame);
            if(returnVal != JFileChooser.APPROVE_OPTION) {
                gui.showMessage("Plik nie został wybrany.");
                return;
            }
            String path = fileChooser.getSelectedFile().toString();
            try {
                ObjectsReader ob = new ObjectsReader(path);
                ob.readObjects();
                readObjects(ob);
                patients_reader=new PatientsReader(ob.getBoarder());
                addPatient.setEnabled(true);
                addB.setEnabled(true);
                showB.setEnabled(true);
                zoomB.setEnabled(true);
                clearB.setEnabled(true);
                loadB.setEnabled(false);
            } catch (RuntimeException | IOException | DataFormatException e1) {
                gui.showMessage(e1.getMessage());
                return;
            }
        }
        public ButtonLoad(GUI _gui) {
            gui = _gui;
        }
    }

    public class ButtonLoadPatient implements ActionListener {
        private GUI gui;
        public void actionPerformed(ActionEvent ev) {
            if (hospitals == null) {
                gui.showMessage("Najpierw dodaj co najmniej 1 szpital");
                return;
            }
            fileChooser = new JFileChooser("program/in");
            fileChooser.setDialogTitle("Wybierz plik z pacjentami");
            int returnVal = fileChooser.showOpenDialog(frame);
            if(returnVal != JFileChooser.APPROVE_OPTION) {
                gui.showMessage("Plik nie został wybrany.");
                return;
            }
            String path = fileChooser.getSelectedFile().toString();
            try {
                if (patients_reader == null) {
                    patients_reader = new PatientsReader(objects_reader.getBoarder());
                }
                int start = patients_reader.getOutsideOfTheCountry().size();
                patients_reader.readFromFile(path);
                for(int i =start;i<patients_reader.getOutsideOfTheCountry().size();i++) {
                    patientsOutside.append(patients_reader.getOutsideOfTheCountry().get(i).toString()+"\n");
                }
                drawPatients();
                playB.setEnabled(true);
            } catch (RuntimeException | IOException e) {
                gui.showMessage("Nie znaleziono pliku.");
                return;
            }
        }
        public ButtonLoadPatient(GUI _gui) {
            gui = _gui;
        }
    }
    public class ButtonZoom implements ActionListener {
        private GUI gui;
        public void actionPerformed(ActionEvent ev) {
            zoomB.setEnabled(false);
            gui.calculateMultiply();
        }
        public ButtonZoom(GUI _gui) {
            gui = _gui;
        }
    }

    public class ButtonClear implements ActionListener {
        private GUI gui;

        public void actionPerformed(ActionEvent ev) {
            canvas.clearCanvas();
            canvas.resetShapes();
            canvas.clearLists();
            showB.setEnabled(false);
            playB.setEnabled(false);
            saveB.setEnabled(false);
            addB.setEnabled(false);
            addPatient.setEnabled(false);
            zoomB.setEnabled(false);
            clearB.setEnabled(false);
            loadB.setEnabled(true);
        }
        public ButtonClear(GUI _gui) {
            gui = _gui;
        }
    }

    protected class BoardCanvas extends JPanel implements MouseListener{
        ArrayList<Point> mult_boarder;
        private GUI gui;
        float multiply = 1;
        float dash1[] = {2.0f};
        BasicStroke dashed = new BasicStroke(2.0f,BasicStroke.CAP_BUTT,BasicStroke.JOIN_MITER,10.0f, dash1, 0.3f);
        BasicStroke standard = new BasicStroke(2.0f,BasicStroke.CAP_BUTT,BasicStroke.JOIN_MITER,10.0f);
        ArrayList<Shape> shapes;
        ArrayList<Shape> multiplied_shapes;
        ArrayList<Map<String, String>> shapes_info;
        ArrayList<Point> boarder;

        private ArrayList<Point> multiplyBoarder(ArrayList<Point> boarder){
            ArrayList<Point> mult_boarder = new ArrayList<>();
            for (Point p : boarder) {
                double m_x = p.x*multiply;
                double m_y = p.y*multiply;
                try {
                    mult_boarder.add(new Point(m_x,m_y));
                } catch (DataFormatException e) {
                    e.printStackTrace();
                }
            }
            return mult_boarder;
        }

        private void multiplyShapes() {



            multiplied_shapes = new ArrayList<>();
            for (Shape shape : shapes)
            {
                float _x = shape.getBounds().x*multiply;
                float _y = shape.getBounds().y*multiply;
                if (shape instanceof Rectangle2D) {
                    Rectangle2D _shape = new Rectangle2D.Double(_x,_y,pointSize,pointSize);
                    multiplied_shapes.add(_shape);
                }
                else if (shape instanceof Line2D) {
                    Line2D _l = (Line2D)shape;
                    double _x1 = _l.getX1()*multiply;
                    double _x2 = _l.getX2()*multiply;
                    double _y1 = _l.getY1()*multiply;
                    double _y2 = _l.getY2()*multiply;
                    Line2D _shape = new Line2D.Double(_x1,_y1,_x2,_y2);
                    multiplied_shapes.add(_shape);
                }
                else if (shape instanceof Ellipse2D) {
                    Ellipse2D _shape = new Ellipse2D.Double(_x,_y,pointSize,pointSize);
                    multiplied_shapes.add(_shape);
                }
                else if (shape instanceof RoundRectangle2D) {
                    RoundRectangle2D _shape = new RoundRectangle2D.Double(_x,_y,pointSize,pointSize,20,20);
                    multiplied_shapes.add(_shape);
                }
            }
        }

        public void resetShapes() {
            this.boarder.clear();
            this.shapes.clear();
            this.shapes_info.clear();
            this.multiplied_shapes.clear();
            this.multiply = 1;

        }

        @Override
        protected void paintComponent(Graphics g)
        {
            super.paintComponent(g);
            Graphics2D g2d = (Graphics2D)g;
            for (Shape shape : multiplied_shapes)
            {
                g2d.setStroke(standard);
                if (shape instanceof Rectangle2D) {
                    g2d.setColor(color_hospital);
                    g2d.fill(shape);
                    g2d.setColor(Color.BLACK);
                    g2d.draw( shape );
                }
                else if (shape instanceof Line2D) {
                    if(showBListener.isHide()) {
                        continue;
                    }
                    g2d.setStroke(dashed);
                    g2d.setColor(color_road);
                    g2d.draw( shape );
                }
                else if (shape instanceof Ellipse2D) {
                    g2d.setColor(color_object);
                    g2d.fill(shape);
                    g2d.setColor(Color.BLACK);
                    g2d.draw( shape );
                }
                else if (shape instanceof RoundRectangle2D) {
                    g2d.setColor(color_patient);
                    g2d.fill(shape);
                    g2d.setColor(Color.BLACK);
                    g2d.draw( shape );
                }
                else{
                    g2d.draw( shape );
                }
            }
            drawBorder();
        }

        private void drawBorder()  {
            try {
                if (gui.getBoarder() == null) return;
                this.boarder = gui.getBoarder();
                boarder = multiplyBoarder(boarder);
                Graphics2D g2d = (Graphics2D) this.getGraphics();
                g2d.setColor(Color.DARK_GRAY);
//            (Point p1 : boarder)
                for (int i = 0; i < boarder.size(); i++) {
                    Point p2;
                    Point p1 = Scaling.applyOffset(boarder.get(i));
                    if (i + 1 == boarder.size()) {
                        p2 = Scaling.applyOffset(boarder.get(0));
                    } else {

                        p2 = Scaling.applyOffset(boarder.get(i + 1));
                    }
                    g2d.drawLine((int) p1.x, (int) p1.y, (int) p2.x, (int) p2.y);

                }
            }catch(DataFormatException e){
                gui.showMessage(e.getMessage());
            }
        }


        protected void setMultiply(float _m) {
            multiply = _m;
            this.redraw();
        }

        public void clearCanvas() {
            super.paintComponent(this.getGraphics());
        }

        public void clearLists() {
            log_text.clear();
            patientsOutside.selectAll();
            patientsOutside.replaceSelection(null);
            displayRoadTextArea.selectAll();
            displayRoadTextArea.replaceSelection(null);
            donePatients = null;

        }

        public void redraw() {
            this.clearCanvas();
            multiplyShapes();
            this.paintComponent(this.getGraphics());
        }

        public void addHospital(Point p, String name,int beds, int freeBeds, int id, Point location) {
            int x = (int)p.getX();
            int y = (int)p.getY();
            Rectangle2D rect = new Rectangle2D.Double(x,y,pointSize,pointSize);
            shapes.add(rect);
            Map<String,String> map = new HashMap<>();
            map.put("type", "hospital");
            map.put("id", String.valueOf(id));
            map.put("name", name);
            map.put("position", (location.x)+"|"+ (location.y));
            map.put("beds", String.valueOf(beds));
            map.put("freeBeds", String.valueOf(freeBeds));
            shapes_info.add(map);
            this.redraw();
        }
        public void addPatient(Point p, int id, Point location) {
            int x = (int)p.getX();
            int y = (int)p.getY();
            RoundRectangle2D rect = new RoundRectangle2D.Double(x,y,pointSize,pointSize,20,20);
            shapes.add(rect);
            Map<String,String> map = new HashMap<>();
            map.put("type", "patient");
            map.put("name", " ID "+id);
            map.put("position", (location.x)+"|"+ (location.y));
            shapes_info.add(map);
            this.redraw();
        }
        public void addImportantObject(Point p, String name, Point location) {
            int x = (int)p.getX();
            int y = (int)p.getY();
            Ellipse2D ell = new Ellipse2D.Double(x,y,pointSize,pointSize);
            shapes.add(ell);
            Map<String,String> map = new HashMap<>();
            map.put("type", "object");
            map.put("name", name);
            map.put("position", (location.x)+"|"+ (location.y));
            shapes_info.add(map);
            this.redraw();
        }
        public void addRoad(Point p_1, Point p_2) {
            int x1 = (int)p_1.getX();
            int x2 = (int)p_2.getX();
            int y1 = (int)p_1.getY();
            int y2 = (int)p_2.getY();
            Line2D road = new Line2D.Double(x1,y1,x2,y2);
            shapes.add(road);
            Map<String,String> map = new HashMap<>();
            map.put("type", "road");
            map.put("name", "test_road");
            shapes_info.add(map);
            this.redraw();
        }


        public void setBGColor(Color col) {
            this.setBackground(col);
        }
        public BoardCanvas(GUI _gui) {
            mult_boarder = new ArrayList<>();
            gui = _gui;
            shapes = new ArrayList<>();
            multiplied_shapes = new ArrayList<>();
            shapes_info = new ArrayList<>();
            boarder = new ArrayList<>();
            this.addMouseListener(this);
        }

        @Override
        public void mouseClicked(MouseEvent event) {
            ToolTipManager.sharedInstance().setInitialDelay(0);
            java.awt.Point p = event.getPoint();

            Boolean fit = false;
            for (Shape shape: multiplied_shapes)
            {
                if (shape.contains(p)) {
                    fit = true;
                    Map<String, String> shape_map = shapes_info.get(multiplied_shapes.indexOf(shape));
                    String name = shape_map.get("name");

                    if(shape instanceof Rectangle2D) {
                        int h_id = Integer.parseInt(shape_map.get("id"));
                        int freeBeds = gui.getFreeBeds(h_id);
                        if (freeBeds == -1) {
                            gui.showMessage("Błąd podczas pobierania szpitala według identyfikatora: "+h_id);
                            return;
                        }
                        gui.info(shape_map.get("position") +"|"+freeBeds+" zajętych z "+shape_map.get("beds")+" : " + name);
                        return;
                    }
                    else if (shape instanceof Ellipse2D) {
                        gui.info(shape_map.get("position") +"|"+" OBIEKT " + name);
                        return;
                    }
                    else if (shape instanceof RoundRectangle2D) {
                        gui.info(shape_map.get("position") +"|"+ " PACJENT " + name);
                        return;
                    }
                    else{
                        gui.info("NIEZNANY KSZTAŁT");
                    }
                }
                if (!fit) {
                    gui.info("Kliknij element, aby uzyskać informacje");
                }
            }
        }
        @Override
        public void mousePressed(MouseEvent e) {
        }
        @Override
        public void mouseReleased(MouseEvent e) {
        }
        @Override
        public void mouseEntered(MouseEvent e) {
        }
        @Override
        public void mouseExited(MouseEvent e) {
        }
    }
}