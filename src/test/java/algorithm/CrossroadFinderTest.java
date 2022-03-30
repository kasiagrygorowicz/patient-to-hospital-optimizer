package algorithm;

import data.elements.Crossroad;
import data.elements.Hospital;
import data.elements.Road;
import data.elements.RoadAndCrossroads;
import org.junit.Test;

import java.util.HashSet;
import java.util.List;
import java.util.zip.DataFormatException;

import static org.junit.Assert.*;

public class CrossroadFinderTest {

    private CrossroadsFinder crossroadsFinder;

    @Test
    public void correct_data () throws DataFormatException {
        //given
        crossroadsFinder = new CrossroadsFinder();
        HashSet<Hospital> hospitals = new HashSet<>();
        Hospital h1 = new Hospital(1, "hospital_1", 10, 10, 1, 1 );
        Hospital h2 = new Hospital(2, "hospital_2", 30, 30, 1, 1 );
        Hospital h3 = new Hospital(3, "hospital_3", 10, 20, 1, 1 );
        Hospital h4 = new Hospital(4, "hospital_4", 20, 10, 1, 1 );
        hospitals.add(h1);
        hospitals.add(h2);
        hospitals.add(h3);
        hospitals.add(h4);

        HashSet<Road> roads = new HashSet<>();
        Road r1 = new Road(1,h1,h2,1);
        Road r2 = new Road(2,h3,h4,1);
        roads.add(r1);
        roads.add(r2);

        HashSet<Crossroad> crossroads = new HashSet<>();
        Crossroad crossroad = new Crossroad(1, 15, 15);
        crossroads.add(crossroad);
        //when
        List<RoadAndCrossroads> roadAndCrossroads = crossroadsFinder.calculateCrossroads(roads,hospitals);
        //then
        assertEquals(crossroads,roadAndCrossroads.get(0).getCrossroads());
        assertEquals(crossroads,roadAndCrossroads.get(1).getCrossroads());
    }

    @Test
    public void no_crossroad_but_not_parallel () throws DataFormatException {
        //given
        crossroadsFinder = new CrossroadsFinder();
        HashSet<Hospital> hospitals = new HashSet<>();
        Hospital h1 = new Hospital(1, "hospital_1", 20, 20, 1, 1 );
        Hospital h2 = new Hospital(2, "hospital_2", 30, 30, 1, 1 );
        Hospital h3 = new Hospital(3, "hospital_3", 10, 20, 1, 1 );
        Hospital h4 = new Hospital(4, "hospital_4", 20, 10, 1, 1 );
        hospitals.add(h1);
        hospitals.add(h2);
        hospitals.add(h3);
        hospitals.add(h4);

        HashSet<Road> roads = new HashSet<>();
        Road r1 = new Road(1,h1,h2,1);
        Road r2 = new Road(2,h3,h4,1);
        roads.add(r1);
        roads.add(r2);

        HashSet<Crossroad> crossroads = new HashSet<>();
        //when
        List<RoadAndCrossroads> roadAndCrossroads = crossroadsFinder.calculateCrossroads(roads,hospitals);
        //then
        assertEquals(crossroads,roadAndCrossroads.get(0).getCrossroads());
        assertEquals(crossroads,roadAndCrossroads.get(1).getCrossroads());
    }

    @Test
    public void roads_are_parallel () throws DataFormatException {
        //given
        crossroadsFinder = new CrossroadsFinder();
        HashSet<Hospital> hospitals = new HashSet<>();
        Hospital h1 = new Hospital(1, "hospital_1", 20, 20, 1, 1 );
        Hospital h2 = new Hospital(2, "hospital_2", 10, 20, 1, 1 );
        Hospital h3 = new Hospital(3, "hospital_3", 15, 25, 1, 1 );
        Hospital h4 = new Hospital(4, "hospital_4", 30, 10, 1, 1 );
        hospitals.add(h1);
        hospitals.add(h2);
        hospitals.add(h3);
        hospitals.add(h4);

        HashSet<Road> roads = new HashSet<>();
        Road r1 = new Road(1,h1,h2,1);
        Road r2 = new Road(2,h3,h4,1);
        roads.add(r1);
        roads.add(r2);

        HashSet<Crossroad> crossroads = new HashSet<>();
        //when
        List<RoadAndCrossroads> roadAndCrossroads = crossroadsFinder.calculateCrossroads(roads,hospitals);
        //then
        assertEquals(crossroads,roadAndCrossroads.get(0).getCrossroads());
        assertEquals(crossroads,roadAndCrossroads.get(1).getCrossroads());
    }

}
