package data.elements;

import java.util.HashSet;

public class RoadAndCrossroads {

    private final Road road;
    private HashSet<Crossroad> crossroads;

    public RoadAndCrossroads(Road road) {
        this.road = road;
        this.crossroads = new HashSet<>();
    }

    public void addCrossroad(Crossroad crossroad) {
        crossroads.add(crossroad);
    }

    public Road getRoad() {
        return road;
    }

    public HashSet<Crossroad> getCrossroads() {
        return crossroads;
    }
}
