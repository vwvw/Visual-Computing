package game;
import processing.core.*;

public class Cylinder {

    private Game parent;

    Cylinder(Game p) {
        parent = p;
    }

    // create a new shape for a cylinder, you can add it to cylinderShape Array
    PShape cylinderShaper(float positionX, float positionY) {

        // position x : centre du cylindre par rapport à la plaque.
        // position y : centre du cylindre par rapport à la plaque.

        float angle;
        float[] x = new float[parent.cylinderResolution + 1];
        float[] y = new float[parent.cylinderResolution + 1];
        parent.completeCylinder =  parent.view3D.getPGraphics().createShape(PShape.GROUP);

        // get the x and y position on a circle for all the sides
        for (int i = 0; i < x.length; i++) {
            angle = (float) ((Math.PI * 2 / parent.cylinderResolution) * i);
            x[i] = (float) (Math.sin(angle) * parent.cylinderRadius + positionX);
            y[i] = (float) (Math.cos(angle) * parent.cylinderRadius + positionY);
        }

        // corps
        parent.openCylinder =  parent.view3D.getPGraphics().createShape();
        parent.openCylinder.beginShape(PShape.QUAD_STRIP);
        parent.openCylinder.fill(parent.color(0,135,6));

        // draw the border of the cylinder
        for (int i = 0; i < x.length; i++) {
            parent.openCylinder.vertex(x[i], y[i], parent.wBoard / 2);
            parent.openCylinder.vertex(x[i], y[i], parent.cylinderHeight
                    + parent.wBoard / 2);
        }
        parent.openCylinder.endShape();

        parent.cylinderTop =  parent.view3D.getPGraphics().createShape();
        parent.cylinderTop.beginShape(PShape.TRIANGLES);
        parent.cylinderTop.fill(parent.color(0,135,6));

        parent.cylinderBottom =  parent.view3D.getPGraphics().createShape();
        parent.cylinderBottom.beginShape(PShape.TRIANGLES);
        parent.cylinderBottom.fill(parent.color(0,135,6));

        for (int i = 0; i < x.length - 1; i++) {
            parent.cylinderTop.vertex(x[i], y[i], parent.cylinderHeight
                    + parent.wBoard / 2);
            parent.cylinderTop.vertex(x[i + 1], y[i + 1], parent.cylinderHeight
                    + parent.wBoard / 2);
            parent.cylinderTop.vertex(positionX, positionY,
                    parent.cylinderHeight + parent.wBoard / 2);
            parent.cylinderBottom.vertex(x[i], y[i], parent.wBoard / 2);
            parent.cylinderBottom.vertex(x[i + 1], y[i + 1], parent.wBoard / 2);
            parent.cylinderBottom.vertex(positionX, positionY,
                    parent.wBoard / 2);
        }

        parent.cylinderBottom.endShape();
        parent.cylinderTop.endShape();

        parent.completeCylinder.addChild(parent.openCylinder);
        parent.completeCylinder.addChild(parent.cylinderTop);
        parent.completeCylinder.addChild(parent.cylinderBottom);
        return parent.completeCylinder;
    }

    public boolean canPlaceCylinder() {
        // Check if we're trying to place a cylinder outside of the board
        float BoardOnScreenSize =  parent.view3D.getPGraphics().screenX(parent.lBoard / 2
                - parent.cylinderRadius, parent.lBoard / 2
                - parent.cylinderRadius, parent.wBoard / 2
                + parent.cylinderHeight)
                -  parent.view3D.getPGraphics().screenX(-parent.lBoard / 2
                        + parent.cylinderRadius, -parent.lBoard / 2
                        + parent.cylinderRadius, parent.wBoard / 2
                        + parent.cylinderHeight);
        parent.minXBoundariesCylinder =  parent.view3D.getPGraphics().screenX(
                -parent.lBoard / 2 + parent.cylinderRadius, -parent.lBoard / 2
                        + parent.cylinderRadius, parent.wBoard / 2
                        + parent.cylinderHeight);
        parent.maxXBoundariesCylinder = parent.minXBoundariesCylinder
                + BoardOnScreenSize;
        parent.minYBoundariesCylinder =  parent.view3D.getPGraphics().screenY(
                -parent.lBoard / 2 + parent.cylinderRadius, -parent.lBoard / 2
                        + parent.cylinderRadius, parent.wBoard / 2
                        + parent.cylinderHeight);
        parent.maxYBoundariesCylinder = parent.minYBoundariesCylinder
                + BoardOnScreenSize;
        boolean outsideX = parent.mouseX >= parent.minXBoundariesCylinder
                && parent.mouseX <= parent.maxXBoundariesCylinder;
        boolean outsideY = parent.mouseY > parent.minYBoundariesCylinder
                && parent.mouseY < parent.maxYBoundariesCylinder;

        // stop right now if we're trying to draw out of bounds
        if (!(outsideX && outsideY)) {
            return false;
        }

        // Check if we're trying to place a cylinder on an other
        PVector tmp = new PVector(PApplet.map(parent.mouseX,
                parent.minXBoundariesCylinder, parent.maxXBoundariesCylinder,
                parent.cylinderRadius, parent.lBoard - parent.cylinderRadius),
                PApplet.map(parent.mouseY, parent.minYBoundariesCylinder,
                        parent.maxYBoundariesCylinder, parent.cylinderRadius,
                        parent.lBoard - parent.cylinderRadius));

        for (int i = 0; i < parent.arrayCylinderPosition.size(); i++) {
            if (PVector.sub(tmp, parent.arrayCylinderPosition.get(i)).mag() < parent.cylinderRadius * 2) {
                return false;
            }
        }

        // Check if we're trying to place a cylinder on the ball
        tmp = new PVector(tmp.x - parent.lBoard / 2, -parent.wBoard / 2
                - parent.ballRadius, tmp.y - parent.lBoard / 2);
        if (PVector.sub(tmp, parent.mover.ballLocation).mag() < parent.cylinderRadius
                + parent.ballRadius) {
            return false;
        }
        return true;
    }

}
