import sim.engine.SimState;
import sim.engine.Steppable;
import sim.util.Double2D;

import java.awt.*;
import java.util.List;

public class Kilobot implements Steppable {
    int id;
    private Double2D location;
    private int gradient = (int)1e6; // big number initially, then it will get lower
    public Shape desiredShape;

    Status status = Status.Init;
    private double kilobotRadius = 10;

    DriveModule driveModule;

    public void step(SimState state){
        KilobotSimState kilobotSimState = (KilobotSimState) state;

        if(driveModule == null)
            driveModule = new DriveModule(getSpaceInfo(), kilobotSimState.getSpaceInfos(), state.random);

        if(status != Status.Seed)
            setGradientBasedOnNeighbors(kilobotSimState.getSpaceInfos());

        driveModule.update(getSpaceInfo(), kilobotSimState.getSpaceInfos());

        if(driveModule.statusToChange()){
            status = driveModule.getStatus();
        }


        if(driveModule.locationToChange()){

            location = driveModule.getLocation();
            setLocationInYard(location, kilobotSimState);

        }

    }

    private void setLocationInYard(Double2D location, KilobotSimState state) {
        KilobotSimState kilobotSimState = (KilobotSimState) state;
        kilobotSimState.yard.setObjectLocation(this, location);
    }

    private void setGradientBasedOnNeighbors(List<SpaceInfo> othersInfo) {//TODO
        Neighborhood neighborhood = new Neighborhood(getSpaceInfo(), othersInfo);

        setGradient(neighborhood.getLowestNeighborGradient() + 1);
    }


    // getters
    public SpaceInfo getSpaceInfo(){
        return new SpaceInfo(id, location, status, kilobotRadius, gradient, desiredShape);
    }

    // setters
    public void setLocation(Double2D location) {
        this.location = location;
    }

    public void setGradient(int gradient){
        this.gradient = gradient;
    }

    void setId(int id){
        this.id = id;
    }
}
