import sim.util.Double2D;

public class Neighbor {
    Double2D location;
    boolean occupied;
    SpaceInfo spaceInfo;

    public Neighbor(Double2D location, boolean occupied, SpaceInfo spaceInfo){
        this.location = location;
        this.occupied = occupied;
        this.spaceInfo = spaceInfo;
    }
}
