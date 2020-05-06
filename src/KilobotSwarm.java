import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class KilobotSwarm {
    int numKilobots;
    double kilobotRadius = 10;
    Shape desiredShape;
    List<Kilobot> allKilobots = new ArrayList<>();

    KilobotSwarm(int numKilobots, Shape desiredShape, Space kilobotSpace){
        this.desiredShape = desiredShape;
        this.numKilobots = numKilobots;

        KilobotCreator creator = new KilobotCreator(desiredShape, kilobotRadius, kilobotSpace);
        allKilobots = creator.createKilobots(numKilobots - 4);
    }

    public Kilobot getKilobotByID(int id){
        return allKilobots.stream().filter(x -> x.id == id).findAny().orElse(null);
    }

    List<Kilobot> getKilobotsByState(Status status){
        return allKilobots.stream().filter(x -> x.status == status).collect(Collectors.toList());
    }

    List<SpaceInfo> getSpaceInfos() {
        return allKilobots.stream().map(Kilobot::getSpaceInfo).collect(Collectors.toList());
    }



}
