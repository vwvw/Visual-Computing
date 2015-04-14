
//processing convention

//    -y
//    |
//    |
//    |
//    |
//    |__________ x
//   /
//  /
// /
// z


//used for arrowKeys turn
//float rotVertical = 0;

//WindowSize
int windowSize = 500;

//rotation that we do
float rotX = 0;
float rotZ = 0;

//the rotation that was already done
float previousrotX ;
float previousrotZ;

//the position of the mouse at the begining of the movement
float mousePositionX;
float mousePositionY;

//baord movementscaler
float movementScale = 1;

//ball and board attributes
float ballRadius = 10;
float lBoard = 250;
float wBoard = 10;

//movement attributes
PVector gravityForce = new PVector(0, 0, 0);
float gravityConstant = 0.3;
Mover mover;


// cylinder declaration 
float cylinderRadius = 10; 
float cylinderHeight = 25; 
int cylinderResolution = 30;
PShape completeCylinder = new PShape();
PShape openCylinder = new PShape();
PShape cylinderTop = new PShape();
PShape cylinderBottom = new PShape();
boolean shiftMode = false; //true = place cylinder
ArrayList<PVector> arrayCylinderPosition = new ArrayList<PVector>();
ArrayList<PShape> arrayCylinderShape= new ArrayList<PShape>();
Cylinder cylinder; 


float minXBoundariesCylinder;
float maxXBoundariesCylinder;
float minYBoundariesCylinder;
float maxYBoundariesCylinder;

PGraphics scoreSurface;
PGraphics gameSurface;

PShape tree;
void setup() 
{
  size(windowSize, windowSize, P2D); 
  
  mover = new Mover();
  cylinder = new Cylinder();
  scoreSurface = createGraphics(windowSize, 100, P2D);
  gameSurface = createGraphics(windowSize, windowSize-100, P3D);
  tree = loadShape("simpleTree.obj");
  tree.scale(10);
  
}

void draw() {
  //ambient settings
  gameSurface.noStroke();
  scoreSurface.noStroke();
  noStroke();

  drawScoreSurface();
  image(scoreSurface, 0, windowSize-100);
  drawGameSurface();
  image(gameSurface, 0, 0);
}

//we save the mouse position, and archive the rotation level
void mousePressed()
{
  if (shiftMode)
  {
    gameSurface.pushMatrix();
    gameSurface.rotateX(PI/2);
    gameSurface.rotateZ(-PI/2);

    if (cylinder.canPlaceCylinder()) 
    {
      PVector cyl = new PVector(map(mouseX, minXBoundariesCylinder, maxXBoundariesCylinder, cylinderRadius, lBoard-cylinderRadius), map(mouseY, minYBoundariesCylinder, maxYBoundariesCylinder, cylinderRadius, lBoard-cylinderRadius));
      arrayCylinderPosition.add(cyl);
      arrayCylinderShape.add(cylinder.cylinderShaper(cyl.x-lBoard/2, cyl.y-lBoard/2));
    }
    gameSurface.popMatrix();
  } else {
    mousePositionX = mouseX;
    mousePositionY = mouseY;
    previousrotZ = rotZ;
    previousrotX = rotX;
  }
}


//when the mouse is dragged we compare the distance mouved
void mouseDragged() 
{
  rotX = (float) (previousrotX + Math.pow(1.1, movementScale) * map(mouseY - mousePositionY, -height, height, -PI/3, PI/3)); 
  rotZ = (float) (previousrotZ + Math.pow(1.1, movementScale) * map(mouseX - mousePositionX, -width, width, -PI/3, PI/3));
  if (rotX > PI/3) rotX = PI/3; 
  if (rotX < -PI/3) rotX = -PI/3; 
  if (rotZ > PI/3) rotZ = PI/3; 
  if (rotZ < -PI/3) rotZ = -PI/3;
}






//vertical rotation with arrow keys
/*void keyPressed() { 
 if (key == CODED) {
 if (keyCode == LEFT) { 
 rotVertical -= 50;
 } else if (keyCode == RIGHT) {
 rotVertical += 50;
 }
 }
 }*/


//shift key for placing cylinder
void keyPressed() { 
  if (key == CODED) {
    if (keyCode == SHIFT) { 
      shiftMode = true;
    }
  }
}

void keyReleased() { 
  if (key == CODED) {
    if (keyCode == SHIFT) { 
      shiftMode = false;
    }
  }
}


//Change the rotation speed of the baord
void mouseWheel(MouseEvent event) {
  movementScale -= event.getCount();
  if (movementScale >= 20) {
    movementScale = 20;
  }
  if (movementScale <= -8) {
    movementScale = -8;
  }
}



void drawScoreSurface()
{
  scoreSurface.beginDraw(); 
  scoreSurface.background(0);
  scoreSurface.endDraw();
}

void drawGameSurface()
{
  gameSurface.beginDraw();
  gameSurface.directionalLight(50, 100, 125, 0, 1, 0); 
  gameSurface.ambientLight(102, 102, 102);
  gameSurface.background(200); 


  if (shiftMode) // place cylinder
  {
    gameSurface.camera(0, -400, 0, 0, 0, 0, 1, 1, 0); // on se place droit en dessus
    gameSurface.box(lBoard, wBoard, lBoard);

    gameSurface.pushMatrix();
    gameSurface.rotateX(PI/2);
    gameSurface.rotateZ(-PI/2);
    //We draw a "preview Cylinder" of where the cylinder will be placed once the player clicks, the cylinder isn't drawn if it can't be placed where the mouse is being pointed at
    if (cylinder.canPlaceCylinder()) 
    {
      PVector cyl = new PVector(map(mouseX, minXBoundariesCylinder, maxXBoundariesCylinder, cylinderRadius, lBoard-cylinderRadius), map(mouseY, minYBoundariesCylinder, maxYBoundariesCylinder, cylinderRadius, lBoard-cylinderRadius));
      //gameSurface.shape(cylinder.cylinderShaper(cyl.x-lBoard/2, cyl.y-lBoard/2));
      gameSurface.pushMatrix();
      gameSurface. translate(cyl.x-lBoard/2,  cyl.y-lBoard/2,0);
      gameSurface.rotateX(PI/2);
      gameSurface.shape(tree);
      gameSurface.popMatrix();
    }
    
    //drawing existing cylinder
    for (int i = 0; i< arrayCylinderShape.size(); i++)
    {
      gameSurface.shape(arrayCylinderShape.get(i));
    }
    //println(arrayCylinderShape.size());
    gameSurface.popMatrix();

    //ball drwaing
    gameSurface.pushMatrix();
    gameSurface.rotateY(PI/2);
    mover.display();
    gameSurface.popMatrix();
  } else { // not in shift mode
    gameSurface.camera(windowSize/2, -1, windowSize/2, width/2, height/2, 0, 0, 1, 0); 
    //we move the coodinates to have the board in the center of the window
    gameSurface.translate(width/2, height/2, 0);
    gameSurface.rotateZ(rotZ); 
    gameSurface.rotateX(-rotX);
    gameSurface.box(lBoard, wBoard, lBoard);

    //draw cylinder
    gameSurface.pushMatrix();
    gameSurface.rotateX(PI/2);
    for (int i = 0; i< arrayCylinderShape.size (); i++)
    {
      gameSurface.shape(arrayCylinderShape.get(i));
    }
    gameSurface.popMatrix();

    //move and draw ball
    gameSurface.pushMatrix();
    mover.update();
    mover.display();
    gameSurface.popMatrix();
  }
  gameSurface.endDraw();
}

