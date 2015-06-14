package cs211.tangiblegame;

import processing.core.PGraphics;
import processing.core.PVector;

public class View3D {
    
    
    private PGraphics surface;
    private TangibleGame parent;
    
    public View3D(TangibleGame p){
        parent = p;
        surface = parent.createGraphics(parent.windowSizeHeight, parent.windowSizeHeight - parent.scoreBoardSize, TangibleGame.P3D);
    }
    
    public void draw(){
        surface.noStroke();
        surface.beginDraw();
        surface.directionalLight(50, 100, 125, 0, 1, 0);
        surface.ambientLight(102, 102, 102);
        surface.background(200);

        if (parent.shiftMode) // place cylinder
        {
            surface.camera(0, -400, 0, 0, 0, 0, 1, 1, 0); // on se place
                                                              // droit en dessus
            surface.box(parent.lBoard, parent.wBoard, parent.lBoard);

            surface.pushMatrix();
            surface.rotateX(TangibleGame.PI / 2);
            surface.rotateZ(-TangibleGame.PI / 2);
            // We draw a "preview Cylinder" of where the cylinder will be placed
            // once the player clicks, the cylinder isn't drawn if it can't be
            // placed where the mouse is being pointed at
            if (parent.cylinder.canPlaceCylinder()) {
                PVector cyl = new PVector(TangibleGame.map(parent.mouseX, parent.minXBoundariesCylinder,
                        parent.maxXBoundariesCylinder, parent.cylinderRadius, parent.lBoard
                                - parent.cylinderRadius), TangibleGame.map(parent.mouseY,
                                        parent.minYBoundariesCylinder, parent.maxYBoundariesCylinder,
                        parent.cylinderRadius, parent.lBoard - parent.cylinderRadius));
                // surface.shape(cylinder.cylinderShaper(cyl.x-lBoard/2, cyl.y-lBoard/2));
                surface.pushMatrix();
                surface
                        .translate(cyl.x - parent.lBoard / 2, cyl.y - parent.lBoard / 2, 0);
                surface.rotateX(TangibleGame.PI / 2);
                surface.shape(parent.tree);
                surface.popMatrix();
            }

            // drawing existing cylinder
            for (int i = 0; i < parent.arrayCylinderShape.size(); i++) {
            	surface.pushMatrix();
            	
            	surface.translate(parent.arrayCylinderPosition.get(i).x - parent.lBoard / 2, parent.arrayCylinderPosition.get(i).y - parent.lBoard / 2);
            	surface.rotateX(TangibleGame.PI/2);
            	
            	surface.shape(parent.tree);
            	surface.popMatrix();
            }
            // println(arrayCylinderShape.size());
            surface.popMatrix();

            // ball drwaing
            surface.pushMatrix();
            surface.rotateY(TangibleGame.PI / 2);
            parent.mover.display();
            surface.popMatrix();
        } else { // not in shift mode
            surface.camera(parent.windowSizeHeight / 2, -1, parent.windowSizeHeight / 2, parent.width / 2,
                    parent.height / 2, 0, 0, 1, 0);
            // we move the coodinates to have the board in the center of the
            // window
            surface.translate(parent.width / 2, parent.height / 2, 0);
            surface.rotateZ(parent.rotZ);
            surface.rotateX(-parent.rotX);
            surface.fill(149, 215, 237);
            surface.box(parent.lBoard, parent.wBoard, parent.lBoard);

            // draw cylinder
            surface.pushMatrix();
            //surface.rotateX(Game.PI / 2);
            for (int i = 0; i < parent.arrayCylinderShape.size(); i++) {
                //surface.shape(parent.arrayCylinderShape.get(i));
            	surface.pushMatrix();
                surface.translate(parent.arrayCylinderPosition.get(i).x -parent.lBoard / 2, -parent.wBoard/2, parent.arrayCylinderPosition.get(i).y -parent.lBoard / 2);
                surface.rotateX(TangibleGame.PI);
                surface.shape(parent.tree);
                surface.popMatrix();
            }
            surface.popMatrix();

            // move and draw ball
            surface.pushMatrix();
            parent.mover.update();
            surface.noStroke();
            parent.mover.display();
            surface.popMatrix();
        }
        surface.endDraw();
        parent.image(surface, 0, 0);
    }
    
    public PGraphics getPGraphics(){
        return surface;
    }
}
