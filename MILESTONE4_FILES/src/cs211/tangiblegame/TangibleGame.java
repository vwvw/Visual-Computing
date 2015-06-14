package cs211.tangiblegame;

import processing.event.MouseEvent;
import processing.video.Capture;
import processing.video.Movie;

import java.util.ArrayList;
import java.util.List;

import processing.core.*;

@SuppressWarnings("serial")
public class TangibleGame extends PApplet {

    // processing convention

    // -y
    // |
    // |
    // |
    // |
    // |__________ x
    // /
    // /
    // /
    // z

    // used for arrowKeys turn
    // float rotVertical = 0;

    // windowSizeHeight
    final int windowSizeWidth = 700 ;
    final int windowSizeHeight = 700;
    // ScoreBoard
    int scoreBoardSize = 120;

    // TopView
    int topViewSize = 100;
    float offsetLeft = 10;

    // scoreView
    int scoreViewHeight = 110;
    int scoreViewWidth = 70;

    // BarChar
    int barChartHeight = 90;

    // rotation that we do
    float rotX = 0;
    float rotZ = 0;

    // the rotation that was already done
    float previousrotX;
    float previousrotZ;

    // the position of the mouse at the begining of the movement
    float mousePositionX;
    float mousePositionY;

    // baord movementscaler
    float movementScale = 1;

    // ball and board attributes
    float ballRadius = 10;
    float lBoard = 250;
    float wBoard = 10;

    // movement attributes
    PVector gravityForce = new PVector(0, 0, 0);
    float gravityConstant = 0.9f;
    Mover mover;

    // cylinder declaration
    float cylinderRadius = 10;
    float cylinderHeight = 25;
    int cylinderResolution = 30;
    PShape completeCylinder = new PShape();
    PShape openCylinder = new PShape();
    PShape cylinderTop = new PShape();
    PShape cylinderBottom = new PShape();
    boolean shiftMode = false; // true = place cylinder
    ArrayList<PVector> arrayCylinderPosition = new ArrayList<PVector>();
    ArrayList<PShape> arrayCylinderShape = new ArrayList<PShape>();
    Cylinder cylinder;

    float minXBoundariesCylinder;
    float maxXBoundariesCylinder;
    float minYBoundariesCylinder;
    float maxYBoundariesCylinder;

    DataVisual dataVisual;
    TopView topView;
    View3D view3D;
    ScoreView scoreView;
    BarChart barChart;
    HScrollbar scrollBar;

    PShape tree;
    
    public Movie cam;
    ImageProcessing imageProcessor;
    
    Thread process;
    Thread thingProcess;
    
    boolean firstTime = true;
    PImage m_image;
    PImage m_result;
    
  
    public void movieEvent(Movie m) {
        m.read();
    }
    public void setup() {
        this.frameRate(60);
        size(windowSizeWidth , windowSizeHeight, P2D);
        if (frame != null) {
            frame.setResizable(false);
        }
        
        
        cam = new Movie(this, "../../testvideo.mp4");     
        cam.loop(); 
        
  

        mover = new Mover(this);
        cylinder = new Cylinder(this);
        
        view3D = new View3D(this);
        dataVisual = new DataVisual(this, scoreBoardSize);
        topView = new TopView(this, dataVisual, topViewSize);
        scoreView = new ScoreView(this, scoreViewHeight, scoreViewWidth);
        barChart = new BarChart(this, barChartHeight);
        scrollBar = new HScrollbar(this, 0, 0, 10);

        tree = loadShape("chateauBase13.obj");
        tree.scale(6.2f);
        
        imageProcessor = new ImageProcessing(this);
        
    }

    public void draw() {
       if(!cam.available()){
           return;
       }
       if(firstTime && cam.available()){
          imageProcessor.setup(cam.get());
          firstTime = false;
       }
       
        // ambient settings
           
        noStroke();

        
        //thingProcess.start();
        //process.run();
        
        
        /*
        List<PVector> allLines = imageProcessor.hough(tmp, 6);
        List<int[]> quads = imageProcessor.getQuads(allLines);
        if(!quads.isEmpty()){
            int[] bestQuad = imageProcessor.getBestQuad(quads);
            
            List<PVector>bestLines = imageProcessor.linesForQuad(bestQuad, allLines);
            List<PVector> intersections = imageProcessor.getIntersections(bestLines);
             
            imageProcessor.drawLines(bestLines);
            imageProcessor.drawIntersections(intersections);
            imageProcessor.drawQuad(bestLines);
        }
  

        */
       
        
        view3D.draw();
        dataVisual.draw();
        
        topView.draw(offsetLeft);
        scoreView.draw();
        barChart.draw();
        scrollBar.update();
        scrollBar.display();
        
       
        
        m_image = cam.get();
        if(m_image.width != 0){
           m_image.resize(160, 120);
        }
        image(m_image, 0,0);
        imageProcessor.getAngles();

        /*
        try {
            process.join();
        } catch (InterruptedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }*/
        
     
        rotX = (1 / 3f) *(imageProcessor.angles.x + 2 * rotX);
        rotZ = (1 / 3f) *(-imageProcessor.angles.y + 2 * rotZ);

        if (rotX > PI / 3)
            rotX = PI / 3;
        if (rotX < -PI / 3)
            rotX = -PI / 3;
        if (rotZ > PI / 3)
            rotZ = PI / 3;
        if (rotZ < -PI / 3)
            rotZ = -PI / 3;
        
        text("Press SHIFT to add obstacles !", 180,
                10);
    }

    // we save the mouse position, and archive the rotation level
    public void mousePressed() {
        if (shiftMode) {
            view3D.getPGraphics().pushMatrix();
            view3D.getPGraphics().rotateX(PI / 2);
            view3D.getPGraphics().rotateZ(-PI / 2);
            if (cylinder.canPlaceCylinder()) {
                PVector cyl = new PVector(map(mouseX, minXBoundariesCylinder,
                        maxXBoundariesCylinder, cylinderRadius, lBoard
                                - cylinderRadius), map(mouseY,
                        minYBoundariesCylinder, maxYBoundariesCylinder,
                        cylinderRadius, lBoard - cylinderRadius));
                arrayCylinderPosition.add(cyl);
                arrayCylinderShape.add(cylinder.cylinderShaper(cyl.x - lBoard
                        / 2, cyl.y - lBoard / 2));
            }
            view3D.getPGraphics().popMatrix();
        } else {
            mousePositionX = mouseX;
            mousePositionY = mouseY;
            previousrotZ = rotZ;
            previousrotX = rotX;
        }
    }

    // when the mouse is dragged we compare the distance mouved
    
    public void mouseDragged() {
        rotX = (float) (previousrotX + Math.pow(1.1, movementScale)
                * map(mouseY - mousePositionY, -height, height, -PI / 3, PI / 3));
        rotZ = (float) (previousrotZ + Math.pow(1.1, movementScale)
                * map(mouseX - mousePositionX, -width, width, -PI / 3, PI / 3));
        if (rotX > PI / 3)
            rotX = PI / 3;
        if (rotX < -PI / 3)
            rotX = -PI / 3;
        if (rotZ > PI / 3)
            rotZ = PI / 3;
        if (rotZ < -PI / 3)
            rotZ = -PI / 3;
    }

    // vertical rotation with arrow keys
    /*
     * void keyPressed() { if (key == CODED) { if (keyCode == LEFT) {
     * rotVertical -= 50; } else if (keyCode == RIGHT) { rotVertical += 50; } }
     * }
     */

    // shift key for placing cylinder
    public void keyPressed() {
        if (key == CODED) {
            if (keyCode == SHIFT) {
                shiftMode = true;
            }
        }
    }

    public void keyReleased() {
        if (key == CODED) {
            if (keyCode == SHIFT) {
                shiftMode = false;
            }
        }
    }

    // Change the rotation speed of the baord
    public void mouseWheel(MouseEvent event) {
        movementScale -= event.getCount();
        if (movementScale >= 20) {
            movementScale = 20;
        }
        if (movementScale <= -8) {
            movementScale = -8;
        }
    }

}