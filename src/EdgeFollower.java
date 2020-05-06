import sim.util.Double2D;

import java.util.List;

public class EdgeFollower {
    List<SpaceInfo> othersInfo;
    SpaceInfo ownerInfo;

    public EdgeFollower(SpaceInfo ownerInfo, List<SpaceInfo> infos){
        update(ownerInfo, infos);
    }

    public void update(SpaceInfo ownerInfo, List<SpaceInfo> infos){
        this.ownerInfo = ownerInfo;
        this.othersInfo = infos;
    }

    /**
     * @return  Next location going clockwise
     */
    public Double2D getNextLocation(){
        Neighborhood neighborhood = new Neighborhood(ownerInfo, othersInfo);
        Double2D nextLocation = determineNextLocation(neighborhood);

        return nextLocation;
    }

    private Double2D determineNextLocation(Neighborhood neighborhood) {

        // condition to go to i
        for(int i = 0; i < neighborhood.numNeighbors; i++){

            int lower = i - 1;
            if(i == 0)
                lower = neighborhood.numNeighbors - 1;

            int higher = (i+1) % neighborhood.numNeighbors;

            if(!neighborhood.getNeighbor(i).occupied &&
                !neighborhood.getNeighbor(lower).occupied &&
                neighborhood.getNeighbor(higher).occupied){

                return neighborhood.getNeighbor(i).location;
            }
        }

        // else stay
        return ownerInfo.location;
    }
}
