package algorithm;

import data.elements.Hospital;
import data.elements.Patient;
import data.graph.Point;
import org.junit.Test;

import java.util.HashSet;
import java.util.zip.DataFormatException;

import static org.junit.Assert.*;

public class FirstTravelTest {

    @Test
    public void different_distances() throws DataFormatException {
        //given
        Patient patient = new Patient(1, new Point(10,20));
        HashSet<Hospital> hospitals = new HashSet<>();
        Hospital h1 = new Hospital(1, "hospital_1", 30, 20, 1, 1 );
        Hospital h2 = new Hospital(2, "hospital_2", 10, 30, 1, 1 );
        Hospital h3 = new Hospital(3, "hospital_3", 20, 10, 1, 1 );
        hospitals.add(h1);
        hospitals.add(h2);
        hospitals.add(h3);
        //when
        Hospital destination = FirstTravel.goToFirstHospital(patient, hospitals);
        //then
        assertEquals(destination, h2);
    }

    @Test
    public void same_distances() throws DataFormatException {
        //given
        Patient patient = new Patient(1, new Point(10,10));
        HashSet<Hospital> hospitals = new HashSet<>();
        Hospital h1 = new Hospital(1, "hospital_1", 20, 20, 1, 1 );
        Hospital h2 = new Hospital(2, "hospital_2", 0, 20, 1, 1 );
        Hospital h3 = new Hospital(3, "hospital_3", 20, 0, 1, 1 );
        hospitals.add(h1);
        hospitals.add(h2);
        hospitals.add(h3);
        //when
        Hospital destination = FirstTravel.goToFirstHospital(patient, hospitals);
        //then
        assertEquals(destination, h1);
    }
}
