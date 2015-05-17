package game;

import java.util.LinkedList;

import processing.core.PGraphics;
import processing.core.PVector;

public class TopView {

    private PGraphics surface;
    private Game parent;
    private int topViewSize;
    private DataVisual dataVisual;
    private LinkedList<PVector> previousBallLocation;

    public TopView(Game p, DataVisual dataVisual, int topViewSize) {
        parent = p;
        this.dataVisual = dataVisual;
        this.topViewSize = topViewSize;
        surface = parent.createGraphics(topViewSize, topViewSize, Game.P2D);
        previousBallLocation = new LinkedList<>();
        previousBallLocation.add(parent.mover.ballLocation.get());
    }

    public void draw(float offsetLeft) {
        surface.noStroke();

        surface.beginDraw();
        surface.background(149, 215, 237);
        
        surface.fill(62, 142, 191);
        
        for (PVector v : previousBallLocation) {          
            surface.ellipse(v.x * topViewSize
                    / parent.lBoard + topViewSize / 2, v.z
                    * topViewSize / parent.lBoard + topViewSize / 2,
                    parent.ballRadius * topViewSize / parent.lBoard * 2,
                    parent.ballRadius * 2 * topViewSize / parent.lBoard);
        }
        
        if(previousBallLocation.size() > 100){
            for(int i = 0; i < previousBallLocation.size() - 100; i++){
                previousBallLocation.remove();
            }
        }

        surface.fill(222, 0, 0);
        surface.ellipse(parent.mover.ballLocation.x * topViewSize
                / parent.lBoard + topViewSize / 2, parent.mover.ballLocation.z
                * topViewSize / parent.lBoard + topViewSize / 2,
                parent.ballRadius * topViewSize / parent.lBoard * 2,
                parent.ballRadius * 2 * topViewSize / parent.lBoard);

        surface.fill(0, 135, 6);
        for (int i = 0; i < parent.arrayCylinderShape.size(); i++) {
            surface.ellipse(parent.arrayCylinderPosition.get(i).x * topViewSize
                    / parent.lBoard, parent.arrayCylinderPosition.get(i).y
                    * topViewSize / parent.lBoard, parent.cylinderRadius
                    * topViewSize / parent.lBoard * 2, parent.cylinderRadius
                    * 2 * topViewSize / parent.lBoard);
        }

        surface.endDraw();
        parent.image(surface, offsetLeft, parent.windowSize - dataVisual.size()
                + (dataVisual.size() - topViewSize) / 2);
        
        if(!parent.shiftMode){
            previousBallLocation.add(parent.mover.ballLocation.get());        
        }
    }
}
