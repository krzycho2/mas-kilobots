import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import sim.util.Double2D;

import java.awt.*;
import java.awt.geom.Rectangle2D;

import static org.junit.jupiter.api.Assertions.*;

class NeighborhoodTest {

    @Test
    void getLowestNeighborGradient() {
        KilobotSwarm swarm = createSwarm();
        Kilobot owner = swarm.getKilobotByID(1);

        // set gradients
        swarm.allKilobots.get(0).setGradient(1);
        swarm.allKilobots.get(2).setGradient(1);
        swarm.allKilobots.get(3).setGradient(1);
        swarm.allKilobots.get(6).setGradient(0);

        Neighborhood neighborhood = new Neighborhood(owner.getSpaceInfo(), swarm.getSpaceInfos());

        Assertions.assertEquals(0, neighborhood.getLowestNeighborGradient());

    }

    @Test
    void isGradientUnique_expectFalse() {
        KilobotSwarm swarm = createSwarm();
        Kilobot owner = swarm.getKilobotByID(1);
        int gradient = 2;
        owner.setGradient(gradient);
        // set gradients
        swarm.allKilobots.get(0).setGradient(5);
        swarm.allKilobots.get(2).setGradient(2);
        swarm.allKilobots.get(3).setGradient(10);
        swarm.allKilobots.get(6).setGradient(12);

        Neighborhood neighborhood = new Neighborhood(owner.getSpaceInfo(), swarm.getSpaceInfos());

        assertFalse(neighborhood.isOwnerGradientUnique());
    }

    @Test
    void isGradientUnique_expectTrue() {
        KilobotSwarm swarm = createSwarm();
        Kilobot owner = swarm.getKilobotByID(1);
        int gradient = 2;
        owner.setGradient(gradient);
        // set gradients
        swarm.allKilobots.get(0).setGradient(5);
        swarm.allKilobots.get(2).setGradient(15);
        swarm.allKilobots.get(3).setGradient(10);
        swarm.allKilobots.get(6).setGradient(12);

        Neighborhood neighborhood = new Neighborhood(owner.getSpaceInfo(), swarm.getSpaceInfos());

        assertTrue(neighborhood.isOwnerGradientUnique());
    }

    @Test
    void isSeedInNeighbors_expectTrue() {
        KilobotSwarm swarm = createSwarm();
        Kilobot owner = swarm.getKilobotByID(1);
        swarm.allKilobots.get(2).status = Status.Seed;

        Neighborhood neighborhood = new Neighborhood(owner.getSpaceInfo(), swarm.getSpaceInfos());
        assertTrue(neighborhood.isSeedInNeighbors());
    }

    @Test
    void isSeedInNeighbors_expectFalse() {
        KilobotSwarm swarm = createSwarm();
        Kilobot owner = swarm.getKilobotByID(1);

        Neighborhood neighborhood = new Neighborhood(owner.getSpaceInfo(), swarm.getSpaceInfos());
        assertFalse(neighborhood.isSeedInNeighbors());
    }

    @Test
    void isInTheMiddleOfLine_expectFalse() {
        int num = 5;
        Shape shape = new Rectangle2D.Double(0,0,10,10);
        Space space = new Space(0,0,200,200);
        KilobotSwarm swarm = new KilobotSwarm(num, shape, space);
        Kilobot owner = swarm.getKilobotByID(0);

        Neighborhood neighborhood = new Neighborhood(owner.getSpaceInfo(), swarm.getSpaceInfos());
        assertFalse(neighborhood.isInTheMiddleOfLine());

    }

    @Test
    void isInTheMiddleOfLine_expectTrue() {
        int num = 5;
        Shape shape = new Rectangle2D.Double(0,0,200,200);
        Space space = new Space(0,0,200,200);
        KilobotSwarm swarm = new KilobotSwarm(num, shape, space);
        Kilobot owner = swarm.getKilobotByID(1);

        Neighborhood neighborhood = new Neighborhood(owner.getSpaceInfo(), swarm.getSpaceInfos());
        assertTrue(neighborhood.isInTheMiddleOfLine());

    }

    KilobotSwarm createSwarm(){
        int num = 20;
        Shape shape = new Rectangle2D.Double(0,0,10,10);
        Space space = new Space(0,0,100,100);
        return new KilobotSwarm(num, shape, space);
    }



}