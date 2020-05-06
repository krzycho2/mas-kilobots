import sim.util.Double2D;

import java.util.ArrayList;
import java.util.List;

public class Neighborhood {

    private List<Neighbor> neighbors = new ArrayList<>();
    public int numNeighbors = 6;
    public List<SpaceInfo> allInfos;
    public SpaceInfo ownerInfo;

    public Neighborhood(SpaceInfo ownerInfo, List<SpaceInfo> spaceInfos){
        this.ownerInfo = ownerInfo;
        this.allInfos = spaceInfos;
        setNeighbors();

//        System.out.println("Zajęci sąsiedzi: " + getNumOccupiedNeighbors());
    }

    public void setNeighbors(){
        List<Double2D> locations = createNeighborsLocations();
        for(int i = 0; i < numNeighbors; i++){
            neighbors.add(new Neighbor(
                    locations.get(i),
                    locationOccupied(locations.get(i)),
                    getSpaceInfo(locations.get(i)) // nullable
            ));
        }
    }

    private List<Double2D> createNeighborsLocations(){
        double x0 = ownerInfo.location.x;
        double y0 = ownerInfo.location.y;
        double radius = ownerInfo.radius;
        double deltaY = radius * Math.sqrt(3);

        List<Double2D> temp = new ArrayList<>();

        temp.add(new Double2D(x0 + radius, y0 - deltaY));   // 1 o'clock
        temp.add(new Double2D(x0 + 2 * radius, y0));           // 3 o'clock
        temp.add(new Double2D(x0 + radius, y0 + deltaY));   // 5 o'clock
        temp.add(new Double2D(x0 - radius, y0 + deltaY));   // 7 o'clock
        temp.add(new Double2D(x0 - 2 * radius, y0));           // 9 o'clock
        temp.add(new Double2D(x0 - radius, y0 - deltaY));   // 11 o'clock

        return temp;
    }

    private boolean locationOccupied(Double2D location) {
        return allInfos.stream().anyMatch(kilobot ->
                Math.abs(kilobot.location.x - location.x) < 0.1 &&
                        Math.abs(kilobot.location.y - location.y) < 0.1
        );
    }

    private SpaceInfo getSpaceInfo(Double2D location) {
        return allInfos.stream().filter(info ->
                Math.abs(info.location.x - location.x) < 0.1 &&
                        Math.abs(info.location.y - location.y) < 0.1).
                findAny().orElse(null);
    }



    public int getNumOccupiedNeighbors(){
        return (int) neighbors.stream().filter(x -> x.occupied).count();
    }

    public int getLowestNeighborGradient(){
//        System.out.println("Gradienty sąsiadów.");
        for(int i = 0; i < numNeighbors; i++){
            Neighbor neighbor = neighbors.get(i);
//            if(neighbor.occupied)
//                System.out.println("Gradient "  + i + " = " + neighbors.get(i).spaceInfo.gradient);
        }

        return neighbors.stream().
                filter(x -> x.occupied).                            // only occupied
                mapToInt( x -> x.spaceInfo.gradient).             // get all gradients
                min().orElse(1000000);                        // get min gradient
    }


    public Neighbor getNeighbor(int num){
        return neighbors.get(num);

    }

    public boolean isOwnerGradientUnique(){
        return neighbors.stream().
                filter(x -> x.occupied).
                noneMatch(x -> x.spaceInfo.gradient == ownerInfo.gradient);
    }

    public boolean isSeedInNeighbors(){
        return neighbors.stream().
                filter(x -> x.occupied).
                anyMatch(x -> x.spaceInfo.status == Status.Seed);
    }

    public boolean isAlone(){
        return neighbors.stream().
                noneMatch(x -> x.occupied);
    }

    // Return true if owner with other kilobots creates a line - internal have two neighbors, externals have one.

    public boolean isInTheMiddleOfLine(){
        if(getNumOccupiedNeighbors() == 2)
        {
            return getNeighbor(0).occupied && getNeighbor(3).occupied ||
                    getNeighbor(1).occupied && getNeighbor(4).occupied ||
                    getNeighbor(2).occupied && getNeighbor(5).occupied;
        }
        return false;
    }

}
