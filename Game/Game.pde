
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



float minXBoundariesCylinder;
float maxXBoundariesCylinder;
float minYBoundariesCylinder;
float maxYBoundariesCylinder;

PGraphics scoreSurface;
PGraphics gameSurface;

void setup() 
{
  size(windowSize, windowSize, P2D); 
  
  mover = new Mover();
  scoreSurface = createGraphics(windowSize, 100, P2D);
  gameSurface = createGraphics(windowSize, windowSize-100, P3D);
  gameSurface.noStroke();
  scoreSurface.noStroke();
  noStroke();
}

void draw() {
  //ambient settings

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

    if (canPlaceCylinder()) 
    {
      PVector cyl = new PVector(map(mouseX, minXBoundariesCylinder, maxXBoundariesCylinder, cylinderRadius, lBoard-cylinderRadius), map(mouseY, minYBoundariesCylinder, maxYBoundariesCylinder, cylinderRadius, lBoard-cylinderRadius));
      arrayCylinderPosition.add(cyl);
      arrayCylinderShape.add(cylinderShaper(cyl.x-lBoard/2, cyl.y-lBoard/2));
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



boolean canPlaceCylinder()
{
  //Check if we're trying to place a cylinder outside of the board
  float BoardOnScreenSize = gameSurface.screenX(lBoard/2- cylinderRadius, lBoard/2- cylinderRadius, wBoard/2+cylinderHeight) -  gameSurface.screenX(-lBoard/2+ cylinderRadius, -lBoard/2+ cylinderRadius, wBoard/2+cylinderHeight);
  minXBoundariesCylinder =  gameSurface.screenX(-lBoard/2+ cylinderRadius, -lBoard/2+ cylinderRadius, wBoard/2+cylinderHeight) ;
  maxXBoundariesCylinder = minXBoundariesCylinder + BoardOnScreenSize;
  minYBoundariesCylinder = gameSurface.screenY(-lBoard/2+ cylinderRadius, -lBoard/2+ cylinderRadius, wBoard/2+cylinderHeight);
  maxYBoundariesCylinder = minYBoundariesCylinder + BoardOnScreenSize; 
  boolean outsideX = mouseX >= minXBoundariesCylinder && mouseX <= maxXBoundariesCylinder;
  boolean outsideY = mouseY > minYBoundariesCylinder && mouseY < maxYBoundariesCylinder;


  //stop right now if we're trying to draw out of bounds
  if (!(outsideX && outsideY)) {
    return false;
  }

  //Check if we're trying to place a cylinder on an other
  PVector tmp = new PVector(map(mouseX, minXBoundariesCylinder, maxXBoundariesCylinder, cylinderRadius, lBoard-cylinderRadius), map(mouseY, minYBoundariesCylinder, maxYBoundariesCylinder, cylinderRadius, lBoard-cylinderRadius));

  for (int i = 0; i < arrayCylinderPosition.size (); i++) {
    if (PVector.sub(tmp, arrayCylinderPosition.get(i)).mag()  < cylinderRadius*2) {
      return false;
    }
  }

  //Check if we're trying to place a cylinder on the ball
  tmp = new PVector(tmp.x - lBoard/2, -wBoard/2 - ballRadius, tmp.y- lBoard/2);
  if (PVector.sub(tmp, mover.ballLocation).mag() < cylinderRadius + ballRadius) {
    return false;
  }
  return true;
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

//create a new shape for a cylinder, you can add it to cylinderShape Array
PShape cylinderShaper(float positionX, float positionY)
{

  // position x : centre du cylindre par rapport à la plaque. 
  // position y : centre du cylindre par rapport à la plaque. 

  float angle;
  float[] x = new float[cylinderResolution + 1]; 
  float[] y = new float[cylinderResolution + 1];
  completeCylinder = gameSurface.createShape(GROUP);

  //get the x and y position on a circle for all the sides
  for (int i = 0; i < x.length; i++) {
    angle = (TWO_PI / cylinderResolution) * i; 
    x[i] = sin(angle) * cylinderRadius + positionX ; 
    y[i] = cos(angle) * cylinderRadius + positionY ;
  }

  //corps
  openCylinder = gameSurface.createShape();
  openCylinder.beginShape(QUAD_STRIP);
  openCylinder.fill(color(#10B43D));

  //draw the border of the cylinder
  for (int i = 0; i < x.length; i++) { 
    openCylinder.vertex(x[i], y[i], wBoard/2); 
    openCylinder.vertex(x[i], y[i], cylinderHeight + wBoard/2);
  }
  openCylinder.endShape();

  cylinderTop = gameSurface.createShape();
  cylinderTop.beginShape(TRIANGLES);
  cylinderTop.fill(color(#10B43D));

  cylinderBottom = gameSurface.createShape();
  cylinderBottom.beginShape(TRIANGLES);
  cylinderBottom.fill(color(#10B43D));

  for (int i = 0; i< x.length-1; i++) {
    cylinderTop.vertex(x[i], y[i], cylinderHeight+ wBoard/2); 
    cylinderTop.vertex(x[i+1], y[i+1], cylinderHeight+ wBoard/2);
    cylinderTop.vertex(positionX, positionY, cylinderHeight+ wBoard/2);
    cylinderBottom.vertex(x[i], y[i], wBoard/2); 
    cylinderBottom.vertex(x[i+1], y[i+1], wBoard/2);
    cylinderBottom.vertex(positionX, positionY, wBoard/2);
  }

  cylinderBottom.endShape();
  cylinderTop.endShape();

  completeCylinder.addChild(openCylinder);
  completeCylinder.addChild(cylinderTop);
  completeCylinder.addChild(cylinderBottom);
  return completeCylinder;
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
    if (canPlaceCylinder()) 
    {
      PVector cyl = new PVector(map(mouseX, minXBoundariesCylinder, maxXBoundariesCylinder, cylinderRadius, lBoard-cylinderRadius), map(mouseY, minYBoundariesCylinder, maxYBoundariesCylinder, cylinderRadius, lBoard-cylinderRadius));
      gameSurface.shape(cylinderShaper(cyl.x-lBoard/2, cyl.y-lBoard/2));
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

