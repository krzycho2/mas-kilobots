import sim.engine.SimState;
import sim.field.continuous.Continuous2D;
import sim.util.Double2D;

import java.awt.*;
import java.util.List;

public class KilobotSimState extends SimState {
    public Double2D yardXY = new Double2D(700,700);
    public Continuous2D yard = new Continuous2D(1.0, yardXY.x, yardXY.y);

    int numKilobots = 200;
    double kilobotRadius = 10;
    public Shape desiredShape = new Rectangle.Double(0,0,200,200); // only width and height are crucial

    KilobotSwarm kilobotSwarm;

    Space kilobotSpace = new Space(50,50, yardXY.y - 100, yardXY.x - 200);

    public KilobotSimState(long seed){
        super(seed);
    }

    public void start(){
        super.start();
        yard.clear();

        // create kilobots and set its location. Then add to a schedule
        setKilobots();

    }

    private void setKilobots(){

        // create swarm
        kilobotSwarm = new KilobotSwarm(numKilobots, desiredShape, kilobotSpace);

        for(int i =0; i < numKilobots; i++){
            Kilobot kilobot = kilobotSwarm.getKilobotByID(i);

            // add kilobot to yard
            yard.setObjectLocation(kilobot, kilobot.getSpaceInfo().location);

            // add kilobot to a schedule - it becomes an agent
            schedule.scheduleRepeating(kilobot);
        }
    }

    List<Kilobot> getKilobotsByState(Status status){
        return kilobotSwarm.getKilobotsByState(status);
    }

    List<SpaceInfo> getSpaceInfos() {
        return kilobotSwarm.getSpaceInfos();
    }

}
