import sim.display.Console;
import sim.display.Controller;
import sim.display.Display2D;
import sim.display.GUIState;
import sim.engine.SimState;
import sim.portrayal.continuous.ContinuousPortrayal2D;
import sim.portrayal.simple.ShapePortrayal2D;
import sim.util.Double2D;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.Ellipse2D;

public class KilobotsWithUI extends GUIState {
    public Display2D display;
    public JFrame displayFrame;
    ContinuousPortrayal2D yardPortrayal = new ContinuousPortrayal2D();
    ContinuousPortrayal2D shapePortrayal = new ContinuousPortrayal2D();

    public static void main(String[] args){
        KilobotsWithUI vid = new KilobotsWithUI();
        Console c = new Console(vid);
        c.setVisible(true);
    }

    public void start(){
        super.start();
        setupPortrayals();
    }

    public void load(SimState state){
        super.load(state);
        setupPortrayals();
    }

    public void setupPortrayals(){
        KilobotSimState kilobots = (KilobotSimState) state;

        // tell the portrayals what to portray and how to portray them
        yardPortrayal.setField(kilobots.yard);

        // Draw a circle for each kilobot
        setKilobotsPortrayals();

        // draw desired shape
//        shapePortrayal.setField(kilobots.yard);
//        setDesiredShapePortrayal();

        // reschedule the displayer
        display.reset();
        display.setBackdrop(Color.white);


        //redraw the display
        display.repaint();
    }



    private void setKilobotsPortrayals(){
        // seed kilobots in green
        setPortrayalByStatus(Status.Seed, Color.green);

        // init kilobots in blue
        setPortrayalByStatus(Status.Init, Color.blue);

        // moving kilobots in red
        setPortrayalByStatus(Status.Moving, Color.red);

        // stationary kilobots in green
        setPortrayalByStatus(Status.Stationary, Color.magenta);
    }

    private void setPortrayalByStatus(Status status, Color color){
        KilobotSimState kilobots = (KilobotSimState) state;
        Ellipse2D.Double circle = new Ellipse2D.Double(0,0,2 * kilobots.kilobotRadius,2 * kilobots.kilobotRadius);

        for(Kilobot kilobot : kilobots.kilobotSwarm.getKilobotsByState(status)){
            ShapePortrayal2D portrayal = new ShapePortrayal2D(circle, color);
            yardPortrayal.setPortrayalForObject(kilobot, portrayal);
        }
    }

//    private void setDesiredShapePortrayal() {
//        KilobotsSimState kilobots = (KilobotsSimState) state;
//        Rectangle.Double square = (Rectangle.Double) kilobots.desiredShape;
////        yardPortrayal.setPortrayalForAll(new ShapePortrayal2D(square));
//
//        ImageIcon imageIcon = new ImageIcon("unt.png");
//        Image image = imageIcon.getImage();
//        int dimension = (int)Math.min(kilobots.yard.width, kilobots.yard.height);
//        shapePortrayal.setPortrayalForAll(new ImagePortrayal2D(image, dimension));
//
//    }

    public void init(Controller c){
        super.init(c);

        KilobotSimState simState = (KilobotSimState) state;
        display = new Display2D(simState.yardXY.x, simState.yardXY.y, this);
        display.setClipping(false);

        displayFrame = display.createFrame();
        displayFrame.setTitle("Simulation");
        c.registerFrame(displayFrame);
        displayFrame.setVisible(true);
        display.attach(yardPortrayal, "Yard");
//        display.attach(shapePortrayal, "Overlay");
    }

    public KilobotsWithUI(){
        super(new KilobotSimState(System.currentTimeMillis()));
    }

    public KilobotsWithUI(SimState state ){
        super(state);
    }

    public static String getName(){
        return "Kilobots sim";
    }
}
