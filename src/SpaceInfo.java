import sim.util.Double2D;

import java.awt.*;

public class SpaceInfo {
    int id;
    Double2D location;
    Status status;
    double radius;
    int gradient;
    Shape desiredShape;


    public SpaceInfo(int id, Double2D location, Status status, double radius, int gradient, Shape shape){
        this.id = id;
        this.location = location;
        this.status = status;
        this.radius = radius;
        this.gradient = gradient;
        this.desiredShape = shape;
    }
}
