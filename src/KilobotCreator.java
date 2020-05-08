import sim.util.Double2D;

import java.awt.*;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Class for creating list of kilobots with set locations to fit kilobts side by side
 */
public class KilobotCreator {
    Space kilobotSpace;

    double kilobotRadius;
    Shape desiredShape;

    private List<Kilobot> seedKilobots = new ArrayList<>();
    private List<Kilobot> initKilobots = new ArrayList<>();
    
    private List<Kilobot> kilobots = new ArrayList<>();
    
    List<Double2D> initLocations = new ArrayList<>();
    
    Double2D shapeInitPoint;

    int numInitKilobots;
    int numSeedKilobots = 4;

    int currentID = 0;


    KilobotCreator(Shape shape, double radius, Space kilobotSpace){
        this.desiredShape = shape;
        this.kilobotRadius = radius;
        this.kilobotSpace = kilobotSpace;
    }

    List<Kilobot> createKilobots(int initKilobotsNum){
       addInitKilobots(initKilobotsNum);
       addSeedKilobots();
       setDesiredShape();
       setGradients();

       return kilobots;
    }



     void addInitKilobots(int initKilobotsNum){
        initLocations = createInitLocations(initKilobotsNum);
        int initGradient = (int)1e6;
        for(int i = 0; i < initKilobotsNum; i++){
            Kilobot kilobot = new Kilobot();
            kilobot.setLocation(initLocations.get(i));
            kilobot.setGradient(initGradient);
            kilobot.setId(currentID++);

            kilobot.status = Status.Init;

            kilobots.add(kilobot);
        }
    }

    private List<Double2D> createInitLocations(int numLocations) {
        List<Double2D> locations = new ArrayList<>();

        double x0Even = kilobotSpace.getMinX() + kilobotRadius;
        double x0Odd = x0Even + kilobotRadius;  // x0Even shifted by one radius
        double deltaY = kilobotRadius * Math.sqrt(3);

        int row = 0;
        int locCounter = 0;

        double x;
        double y = kilobotSpace.getMaxY();
        while(locCounter < numLocations){
            if(row % 2 == 0) x = x0Even;
            else x = x0Odd;

            while(x < kilobotSpace.getMaxX()){
                locCounter++;
                locations.add( new Double2D(x,y) );
                x += 2 * kilobotRadius;
            }

            y -= deltaY;
            row++;
        }

        return locations;
    }

    // 4 stationary (seed) kilobots
    public void addSeedKilobots(){
        double deltaY = kilobotRadius * Math.sqrt(3);
        Double2D lowerLeft = getLowerLeftLocation();

        Double2D down = new Double2D(lowerLeft.x + kilobotRadius,lowerLeft.y - deltaY); // gradient: 1
        Double2D left = new Double2D(lowerLeft.x, down.y - deltaY); // gradient: 0
        Double2D right = new Double2D(left.x + 2 * kilobotRadius, down.y - deltaY); // gradient: 1
        Double2D up = new Double2D(down.x, down.y - 2 * deltaY); // gradient: 1

        int[] seedGradients = new int[] {1,0,1,1};
        Double2D[] seedLocations = new Double2D[] {down, left, right, up};

        for(int i = 0; i < numSeedKilobots; i++) {
            Kilobot kilobot = new Kilobot();
            kilobot.status = Status.Seed;
            kilobot.setLocation(seedLocations[i]);
            kilobot.setGradient(seedGradients[i]);
            kilobot.setId(currentID++);

            kilobots.add(kilobot);
            seedKilobots.add(kilobot);

        }

        setShapeInitPoint(left);

    }

    private void setShapeInitPoint(Double2D leftKilobot) {
        double margin = 0.1; // to assert that kilobot is inside
        shapeInitPoint = new Double2D(
                leftKilobot.x - margin, // shift a bit left
                leftKilobot.y + kilobotRadius + margin // shift a bit down
        );
    }

    private Double2D getLowerLeftLocation(){
        List yy = initLocations.stream().map( x -> x.y).collect(Collectors.toList());
        double minY = (double) Collections.min(yy);

        List<Double2D> locForMinY = initLocations.stream().filter(loc -> loc.y == minY).collect(Collectors.toList());
        List xx = locForMinY.stream().map(loc -> loc.x).collect(Collectors.toList());
        double minX = (double) Collections.min(xx);

        return new Double2D(minX, minY);
    }

    public void setGradients(){
        for(int i = 0; i < 2; i++) // do this twice
            for(int j = kilobots.size() - 1; j >= 0 ; j--){
                if(kilobots.get(j).getSpaceInfo().status != Status.Seed){
                    Neighborhood neighborhood = new Neighborhood(kilobots.get(j).getSpaceInfo(), getKilobotsInfos());
                    kilobots.get(j).setGradient(neighborhood.getLowestNeighborGradient() + 1);
                }

            }

    }

    private void setDesiredShape() {
        Rectangle2D.Double dshape = (Rectangle2D.Double) desiredShape;
        double minY = shapeInitPoint.y - dshape.height;
        dshape.setRect(shapeInitPoint.x, minY, dshape.height, dshape.width);

        kilobots.forEach(kilobot -> {
            kilobot.desiredShape = dshape;
        });
    }


    public List<Kilobot> getKilobots(){
        return kilobots;
    }
    
    // in fact returns middle of seed kilobots
    public Double2D getShapeInitPoint() {
        return shapeInitPoint;
    }

    public List<SpaceInfo> getKilobotsInfos(){
        return kilobots.stream().map(Kilobot::getSpaceInfo).collect(Collectors.toUnmodifiableList());
    }
}
