package fileHandler;

import data.elements.Road;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.util.zip.DataFormatException;

import static org.junit.Assert.assertEquals;

public class ObjectsReaderTest {
    private ClassLoader classLoader;
    private String absolutePath;
    String resourceName;
    File file;

    @Before
    public void setUp() {
        classLoader = getClass().getClassLoader();
    }

    @Test
    public void pass_correct_hospitals_objects_roads() throws IOException, DataFormatException {
        //        given
        resourceName = "ObjectsReader/correct_data_hor.txt";
        int numberOfHospitals = 5;
        int numberOfObjects = 3;
        int numberOfRoads = 7;
        try {
            file = new File(classLoader.getResource(resourceName).getFile());
            absolutePath = file.getAbsolutePath();
        } catch (NullPointerException e) {
            assert false;
        }
//        when
        ObjectsReader or = new ObjectsReader(absolutePath);
        or.readObjects();
//        then
        assertEquals(numberOfHospitals, or.getHospitals().size());
        assertEquals(numberOfObjects, or.getObjects().size());
        assertEquals(numberOfRoads, or.getRoads().size());

    }

    @Test
    public void pass_correct_hospitals_roads() throws IOException, DataFormatException {
        //        given
        resourceName = "ObjectsReader/correct_data_hr.txt";
        int numberOfHospitals = 5;
        int numberOfRoads = 7;
        try {
            file = new File(classLoader.getResource(resourceName).getFile());
            absolutePath = file.getAbsolutePath();
        } catch (NullPointerException e) {
            assert false;
        }
//        when
        ObjectsReader or = new ObjectsReader(absolutePath);
        or.readObjects();
        for (Road r : or.getRoads())
            System.out.println(r.toString());
//        then
        assertEquals(numberOfHospitals, or.getHospitals().size());
        assertEquals(numberOfRoads, or.getRoads().size());
    }

    @Test
    public void pass_correct_hospitals() throws IOException, DataFormatException {
        //        given
        resourceName = "ObjectsReader/correct_data_h.txt";
        int numberOfHospitals = 5;
        try {
            file = new File(classLoader.getResource(resourceName).getFile());
            absolutePath = file.getAbsolutePath();
        } catch (NullPointerException e) {
            assert false;
        }
//        when
        ObjectsReader or = new ObjectsReader(absolutePath);
        or.readObjects();
//        then
        assertEquals(numberOfHospitals, or.getHospitals().size());

    }

    @Test(expected = RuntimeException.class)
    public void pass_hospital_with_the_same_id_as_any_previous() throws IOException, DataFormatException {
//        given
        resourceName = "ObjectsReader/same_id_hospital.txt";
        try {
            file = new File(classLoader.getResource(resourceName).getFile());
            absolutePath = file.getAbsolutePath();
        } catch (NullPointerException e) {
            assert false;
        }
//        when
        ObjectsReader or = new ObjectsReader(absolutePath);
        or.readObjects();
    }

    @Test(expected = RuntimeException.class)
    public void pass_object_with_the_same_id_as_any_previous() throws IOException, DataFormatException {
//        given
        resourceName = "ObjectsReader/same_id_object.txt";
        try {
            file = new File(classLoader.getResource(resourceName).getFile());
            absolutePath = file.getAbsolutePath();
        } catch (NullPointerException e) {
            assert false;
        }
//        when
        ObjectsReader or = new ObjectsReader(absolutePath);
        or.readObjects();
    }

    @Test(expected = RuntimeException.class)
    public void pass_road_with_the_same_id_as_any_previous() throws IOException, DataFormatException {
//        given
        resourceName = "ObjectsReader/same_id_road.txt";
        try {
            file = new File(classLoader.getResource(resourceName).getFile());
            absolutePath = file.getAbsolutePath();
        } catch (NullPointerException e) {
            assert false;
        }
//        when
        ObjectsReader or = new ObjectsReader(absolutePath);
        or.readObjects();
    }

    @Test(expected = RuntimeException.class)
    public void pass_hospital_with_the_same_coordinates_as_any_previous() throws IOException, DataFormatException {
//        given
        resourceName = "ObjectsReader/same_coordinates_hospital.txt";
        try {
            file = new File(classLoader.getResource(resourceName).getFile());
            absolutePath = file.getAbsolutePath();
        } catch (NullPointerException e) {
            assert false;
        }
//        when
        ObjectsReader or = new ObjectsReader(absolutePath);
        or.readObjects();
    }

    @Test(expected = RuntimeException.class)
    public void pass_object_with_the_same_coordinates_as_any_previous() throws IOException, DataFormatException {
//        given
        resourceName = "ObjectsReader/same_coordinates_object.txt";
        try {
            file = new File(classLoader.getResource(resourceName).getFile());
            absolutePath = file.getAbsolutePath();
        } catch (NullPointerException e) {
            assert false;
        }
//        when
        ObjectsReader or = new ObjectsReader(absolutePath);
        or.readObjects();

    }

    @Test(expected = RuntimeException.class)
    public void pass_road_with_connection_to_hospital_that_doesnt_exist() throws IOException, DataFormatException {
//        given
        resourceName = "ObjectsReader/road_nonexistent_hospital.txt";
        try {
            file = new File(classLoader.getResource(resourceName).getFile());
            absolutePath = file.getAbsolutePath();
        } catch (NullPointerException e) {
            assert false;
        }

//        when
        ObjectsReader or = new ObjectsReader(absolutePath);
        or.readObjects();
    }

    @Test(expected = RuntimeException.class)
    public void pass_no_hospitals() throws IOException, DataFormatException {
//        given
        resourceName = "ObjectsReader/no_hospitals_given.txt";
        try {
            file = new File(classLoader.getResource(resourceName).getFile());
            absolutePath = file.getAbsolutePath();
        } catch (NullPointerException e) {
            assert false;
        }

//        when
        ObjectsReader or = new ObjectsReader(absolutePath);
        or.readObjects();
    }

    @Test(expected = RuntimeException.class)
    public void pass_wrong_data_type_id_hospital() throws IOException, DataFormatException {
//        given
        resourceName = "ObjectsReader/wrong_data_type_id_hospital.txt";
        try {
            file = new File(classLoader.getResource(resourceName).getFile());
            absolutePath = file.getAbsolutePath();
        } catch (NullPointerException e) {
            assert false;
        }

//        when
        ObjectsReader or = new ObjectsReader(absolutePath);
        or.readObjects();
    }

    @Test(expected = RuntimeException.class)
    public void pass_wrong_data_type_x_coordinate_hospital() throws IOException, DataFormatException {
//        given
        resourceName = "ObjectsReader/wrong_data_type_x_coordinate_hospital.txt";
        try {
            file = new File(classLoader.getResource(resourceName).getFile());
            absolutePath = file.getAbsolutePath();
        } catch (NullPointerException e) {
            assert false;
        }

//        when
        ObjectsReader or = new ObjectsReader(absolutePath);
        or.readObjects();
    }

    @Test(expected = RuntimeException.class)
    public void pass_wrong_data_type_y_coordinate_hospital() throws IOException, DataFormatException {
//        given
        resourceName = "ObjectsReader/wrong_data_type_y_coordinate_hospital.txt";
        try {
            file = new File(classLoader.getResource(resourceName).getFile());
            absolutePath = file.getAbsolutePath();
        } catch (NullPointerException e) {
            assert false;
        }

//        when
        ObjectsReader or = new ObjectsReader(absolutePath);
        or.readObjects();
    }

    @Test(expected = RuntimeException.class)
    public void pass_wrong_number_of_arguments_hospital() throws IOException, DataFormatException {
//        given
        resourceName = "ObjectsReader/wrong_number_of_arguments_hospital.txt";
        try {
            file = new File(classLoader.getResource(resourceName).getFile());
            absolutePath = file.getAbsolutePath();
        } catch (NullPointerException e) {
            assert false;
        }

//        when
        ObjectsReader or = new ObjectsReader(absolutePath);
        or.readObjects();
    }

    @Test(expected = RuntimeException.class)
    public void pass_wrong_number_of_arguments_object() throws IOException, DataFormatException {
//        given
        resourceName = "ObjectsReader/wrong_number_of_arguments_object.txt";
        try {
            file = new File(classLoader.getResource(resourceName).getFile());
            absolutePath = file.getAbsolutePath();
        } catch (NullPointerException e) {
            assert false;
        }

//        when
        ObjectsReader or = new ObjectsReader(absolutePath);
        or.readObjects();
    }

    @Test(expected = RuntimeException.class)
    public void pass_wrong_number_of_arguments_road() throws IOException, DataFormatException {
//        given
        resourceName = "ObjectsReader/wrong_number_of_arguments_road.txt";
        try {
            file = new File(classLoader.getResource(resourceName).getFile());
            absolutePath = file.getAbsolutePath();
        } catch (NullPointerException e) {
            assert false;
        }
//        when
        ObjectsReader or = new ObjectsReader(absolutePath);
        or.readObjects();
    }
}