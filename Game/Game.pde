
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
int windowSize = 900;

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
ArrayList<PVector> arrayCylinder = new ArrayList<PVector>();


float minXBoundariesCylinder;
float maxXBoundariesCylinder;
float minYBoundariesCylinder;
float maxYBoundariesCylinder;




void setup() 
{
  size(windowSize, windowSize, P3D); 
  noStroke();
  mover = new Mover();
}

void draw() {
  //ambient settings
  directionalLight(50, 100, 125, 0, 1, 0); 
  ambientLight(102, 102, 102);
  background(200); 



  if (shiftMode) // place cylinder
  {
    camera(0, -400, 0, 0, 0, 0, 1, 1, 0); // on se place droit en dessus
    box(lBoard, wBoard, lBoard);

    pushMatrix();
    rotateX(PI/2);
    rotateZ(-PI/2);

    //We draw a "preview Cylinder" of where the cylinder will be placed once the player clicks, the cylinder isn't drawn if it can't be placed where the mouse is being pointed at
    if (placeCylinder()) 
    {
      cylinderAdd(map(mouseX, minXBoundariesCylinder, maxXBoundariesCylinder, cylinderRadius, lBoard-cylinderRadius)-lBoard/2, map(mouseY, minYBoundariesCylinder, maxYBoundariesCylinder, cylinderRadius, lBoard-cylinderRadius)-lBoard/2);
    }

    for (int i = 0; i< arrayCylinder.size (); i++)
    {
      float positionX = arrayCylinder.get(i).x-lBoard/2;
      float positionY = arrayCylinder.get(i).y-lBoard/2;
      cylinderAdd(positionX, positionY);
    } 

    popMatrix();
    pushMatrix();
    rotateY(PI/2);
    mover.display();
    popMatrix();
  } else { // not in shift mode
    camera(windowSize/2, -1, windowSize/2, width/2, height/2, 0, 0, 1, 0); 
    //we move the coodinates to have the board in the center of the window
    translate(width/2, height/2, 0);

    rotateZ(rotZ); 
    rotateX(-rotX);


    box(lBoard, wBoard, lBoard);


    //draw cylinder
    pushMatrix();
    rotateX(PI/2);

    for (int i = 0; i< arrayCylinder.size (); i++)
    {
      float positionX = arrayCylinder.get(i).x-lBoard/2;
      float positionY = arrayCylinder.get(i).y-lBoard/2;
      cylinderAdd(positionX, positionY);
    }
    popMatrix();


    pushMatrix();
    mover.update();
    mover.display();
    popMatrix();
  }
}

//we save the mouse position, and archive the rotation level
void mousePressed()
{
  if (shiftMode)
  {
    pushMatrix();
    rotateX(PI/2);
    rotateZ(-PI/2);

    if (placeCylinder()) 
    {
      PVector cyl = new PVector(map(mouseX, minXBoundariesCylinder, maxXBoundariesCylinder, cylinderRadius, lBoard-cylinderRadius), map(mouseY, minYBoundariesCylinder, maxYBoundariesCylinder, cylinderRadius, lBoard-cylinderRadius));
      arrayCylinder.add(cyl);
    }
    popMatrix();
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



boolean placeCylinder()
{
  //Check if we're trying to place a cylinder outside of the board
  float BoardOnScreenSize = screenX(lBoard/2- cylinderRadius, lBoard/2- cylinderRadius, wBoard/2+cylinderHeight) -  screenX(-lBoard/2+ cylinderRadius, -lBoard/2+ cylinderRadius, wBoard/2+cylinderHeight);
  minXBoundariesCylinder =  screenX(-lBoard/2+ cylinderRadius, -lBoard/2+ cylinderRadius, wBoard/2+cylinderHeight) ;
  maxXBoundariesCylinder = minXBoundariesCylinder + BoardOnScreenSize;
  minYBoundariesCylinder = screenY(-lBoard/2+ cylinderRadius, -lBoard/2+ cylinderRadius, wBoard/2+cylinderHeight);
  maxYBoundariesCylinder = minYBoundariesCylinder + BoardOnScreenSize; 
  boolean outsideX = mouseX >= minXBoundariesCylinder && mouseX <= maxXBoundariesCylinder;
  boolean outsideY = mouseY > minYBoundariesCylinder && mouseY < maxYBoundariesCylinder;
  
  
  //stop right now if we're trying to draw out of bounds
  if (!(outsideX && outsideY)) {
    return false;
  }

  //Check if we're trying to place a cylinder on an other
  PVector tmp = new PVector(map(mouseX, minXBoundariesCylinder, maxXBoundariesCylinder, cylinderRadius, lBoard-cylinderRadius), map(mouseY, minYBoundariesCylinder, maxYBoundariesCylinder, cylinderRadius, lBoard-cylinderRadius));

  for (int i = 0; i < arrayCylinder.size (); i++) {
    if (PVector.sub(tmp, arrayCylinder.get(i)).mag()  < cylinderRadius*2) {
      return false;
    }
  }

  //Check if we're trying to place a cylinder on the ball
  /*tmp2 = tmp;
   if (PVector.sub(tmp, mover.ballLocation).mag() < cylinderRadius + ballRadius) {
   return false;
   }*/

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

void cylinderAdd(float positionX, float positionY)
{

  // position x : centre du cylindre par rapport à la plaque. 
  // position y : centre du cylindre par rapport à la plaque. 

  float angle;
  float[] x = new float[cylinderResolution + 1]; 
  float[] y = new float[cylinderResolution + 1];
  completeCylinder = createShape(GROUP);

  //get the x and y position on a circle for all the sides
  for (int i = 0; i < x.length; i++) {
    angle = (TWO_PI / cylinderResolution) * i; 
    x[i] = sin(angle) * cylinderRadius + positionX ; 
    y[i] = cos(angle) * cylinderRadius + positionY ;
  }

  //corps
  openCylinder = createShape();
  openCylinder.beginShape(QUAD_STRIP);
  openCylinder.fill(color(#10B43D));

  //draw the border of the cylinder
  for (int i = 0; i < x.length; i++) { 
    openCylinder.vertex(x[i], y[i], wBoard/2); 
    openCylinder.vertex(x[i], y[i], cylinderHeight + wBoard/2);
  }
  openCylinder.endShape();

  cylinderTop = createShape();
  cylinderTop.beginShape(TRIANGLES);
  cylinderTop.fill(color(#10B43D));

  cylinderBottom = createShape();
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

  shape(completeCylinder);
}

