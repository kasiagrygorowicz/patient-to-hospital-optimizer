package fileHandler;

import data.graph.Point;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.zip.DataFormatException;

import static org.junit.Assert.*;

public class PatientsReaderTest {

    private ArrayList<Point> border;
    private ClassLoader classLoader;
    private String absolutePath;
    String resourceName;
    File file;

    @Before
    public void setUp() throws DataFormatException {
        classLoader = getClass().getClassLoader();
        border =new ArrayList<>();
        border.add(new Point(3,0));
        border.add(new Point(-3,0));
        border.add(new Point(0,3));
        border.add(new Point(0,-3));
    }

    @Test(expected=RuntimeException.class)
    public void pass_id_not_integer() throws IOException {
        //        given
        resourceName = "PatientsReader/id_not_int.txt";
        try {
            file = new File(classLoader.getResource(resourceName).getFile());
            absolutePath = file.getAbsolutePath();
        } catch (NullPointerException e) {
            assert false;
        }
//        when
        PatientsReader pr = new PatientsReader(border);
        pr.readFromFile(absolutePath);
    }

    @Test(expected=RuntimeException.class)
    public void pass_x_not_double() throws IOException {
        //        given
        resourceName = "PatientsReader/x_coordinate_not_double.txt";
        try {
            file = new File(classLoader.getResource(resourceName).getFile());
            absolutePath = file.getAbsolutePath();
        } catch (NullPointerException e) {
            assert false;
        }
//        when
        PatientsReader pr = new PatientsReader(border);
        pr.readFromFile(absolutePath);
    }
    @Test(expected=RuntimeException.class)
    public void pass_y_not_double() throws IOException {
        //        given
        resourceName = "PatientsReader/y_coordinate_not_double.txt";
        try {
            file = new File(classLoader.getResource(resourceName).getFile());
            absolutePath = file.getAbsolutePath();
        } catch (NullPointerException e) {
            assert false;
        }
//        when
        PatientsReader pr = new PatientsReader(border);
        pr.readFromFile(absolutePath);
    }

    @Test(expected=RuntimeException.class)
    public void pass_no_special_character_at_the_beginning() throws IOException {
        //        given
        resourceName = "PatientsReader/no_hashtag_at_the_beginning.txt";
        try {
            file = new File(classLoader.getResource(resourceName).getFile());
            absolutePath = file.getAbsolutePath();
        } catch (NullPointerException e) {
            assert false;
        }
//        when
        PatientsReader pr = new PatientsReader(border);
        pr.readFromFile(absolutePath);
    }

    @Test(expected=RuntimeException.class)
    public void pass_too_little_arguments() throws IOException {
        //        given
        resourceName = "PatientsReader/too_little_arguments.txt";
        try {
            file = new File(classLoader.getResource(resourceName).getFile());
            absolutePath = file.getAbsolutePath();
        } catch (NullPointerException e) {
            assert false;
        }
//        when
        PatientsReader pr = new PatientsReader(border);
        pr.readFromFile(absolutePath);
    }

    @Test(expected=RuntimeException.class)
    public void pass_too_many_arguments() throws IOException {
        //        given
        resourceName = "PatientsReader/too_many_arguments.txt";
        try {
            file = new File(classLoader.getResource(resourceName).getFile());
            absolutePath = file.getAbsolutePath();
        } catch (NullPointerException e) {
            assert false;
        }
//        when
        PatientsReader pr = new PatientsReader(border);
        pr.readFromFile(absolutePath);
    }

    @Test(expected=RuntimeException.class)
    public void pass_empty_line() throws IOException {
        //        given
        resourceName = "PatientsReader/empty_line.txt";
        try {
            file = new File(classLoader.getResource(resourceName).getFile());
            absolutePath = file.getAbsolutePath();
        } catch (NullPointerException e) {
            System.out.println(absolutePath);
            assert false;
        }
//        when
        PatientsReader pr = new PatientsReader(border);
        pr.readFromFile(absolutePath);
    }

    @Test
    public void pass_correct_data() throws IOException {
//                given
        resourceName = "PatientsReader/correct.txt";
        int expectedPatientsInCountry =2;
        int expectedPatientsOutside =1;
        int expectedIdOutside =3;
        try {
            file = new File(classLoader.getResource(resourceName).getFile());
            absolutePath = file.getAbsolutePath();
        } catch (NullPointerException e) {
            assert false;
        }
//        when
        PatientsReader pr = new PatientsReader(border);
        pr.readFromFile(absolutePath);
//        then
        assertEquals(expectedPatientsInCountry, pr.getInTheCountry().size());
        assertEquals(expectedPatientsOutside,pr.getOutsideOfTheCountry().size());
        assertEquals(pr.getOutsideOfTheCountry().get(0).getId(), expectedIdOutside);

    }

}