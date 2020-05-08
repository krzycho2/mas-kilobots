import ec.util.MersenneTwisterFast;
import sim.engine.SimState;
import sim.util.Double2D;

import java.awt.*;
import java.awt.geom.Point2D;
import java.util.List;

public class DriveModule {
    private EdgeFollower edgeFollower;

    SpaceInfo ownerInfo;
    List<SpaceInfo> othersInfo;
    Double2D nextLocation;

    private Double2D lastLocation;
    private Neighborhood neighborhood;

    boolean inShape;
    MersenneTwisterFast randomGen;

    Status beforeStatus;
    Double2D beforeLocation;

    public DriveModule(SpaceInfo ownerInfo, List<SpaceInfo> othersInfo, MersenneTwisterFast random) {
        this.ownerInfo = ownerInfo;
        this.othersInfo = othersInfo;
        this.edgeFollower = new EdgeFollower(ownerInfo, othersInfo);
        this.inShape = false;
        this.randomGen = random;
        this.neighborhood = new Neighborhood(ownerInfo, othersInfo);
    }

    public void update(SpaceInfo ownerInfo, List<SpaceInfo> othersInfo) {
        updateInfo(ownerInfo, othersInfo);
        updateLocationStatus();
    }

    private void updateInfo(SpaceInfo ownerInfo, List<SpaceInfo> othersInfo) {
        this.ownerInfo = ownerInfo;
        this.othersInfo = othersInfo;
    }

    private void updateLocationStatus() {
        beforeStatus = ownerInfo.status;
        beforeLocation = ownerInfo.location;

        selfAssembly();

        lastLocation = beforeLocation;
    }

    private void selfAssembly() {
        neighborhood = new Neighborhood(ownerInfo, othersInfo);

        if(neighborhood.isAlone()){
            ownerInfo.location = lastLocation;
        }

        else{
            if(ownerInfo.status == Status.Init && canBecomeMoving()){
                ownerInfo.status = Status.Moving;
            }

            if(ownerInfo.status == Status.Moving){
                nextLocation = findNextLocationByEdgeFollowing();

                if(!isLocationInShape(ownerInfo.location)){

                    ownerInfo.location = nextLocation;
                }

                else
                {
                    if(isFinalLocation()){
                        ownerInfo.status = Status.Stationary;
                    }

                    else {
                        ownerInfo.location = nextLocation;
                    }
                }
            }
        }
    }

    private boolean isFinalLocation() {
        // If I'm not in shape -> not final
        boolean nextLocationInshape = isLocationInShape(nextLocation);
        neighborhood = new Neighborhood(ownerInfo, othersInfo);
        boolean uniqueGradient = neighborhood.isOwnerGradientUnique();

        return !nextLocationInshape || !uniqueGradient;


    }

    private Double2D findNextLocationByEdgeFollowing() {
        edgeFollower.update(ownerInfo, othersInfo);
        return edgeFollower.getNextLocation();
    }

    private boolean isLocationInShape(Double2D location){
        return ownerInfo.desiredShape.contains(new Point2D.Double(location.x, location.y));
    }

    boolean canBecomeMoving() {

        // 1. Must have luck - randomness
        boolean isLucky = false;
        int randomNumber = randomGen.nextInt(1000);
        if(randomNumber == 1)
            isLucky = true;

        // 2. Must have 3 or less neighbors
        neighborhood = new Neighborhood(ownerInfo, othersInfo);
        boolean fewOccupiedNeighbors = (neighborhood.getNumOccupiedNeighbors() <= 3);

        // 3. Kilobot not in the middle of line of kilobots
        boolean seedCondition = false;
        if(neighborhood.getNumOccupiedNeighbors() == 1){
            seedCondition = true;
        }
        else if(!neighborhood.isSeedInNeighbors())
            seedCondition = true;



        return isLucky && fewOccupiedNeighbors && seedCondition && !neighborhood.isInTheMiddleOfLine();
    }

    public Status getStatus() {
        return ownerInfo.status;
    }

    public Double2D getLocation(){
        return ownerInfo.location;
    }

    public boolean statusToChange() {
        return beforeStatus != ownerInfo.status;
    }

    public boolean locationToChange() {
        return beforeLocation != ownerInfo.location;
    }
}
