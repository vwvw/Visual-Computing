
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
Cylinder cylinder;



void setup() 
{
  size(windowSize, windowSize, P3D); 
  noStroke();
  mover = new Mover();
  cylinder = new Cylinder();
}

void draw() {
  //ambient settings
  directionalLight(50, 100, 125, 0, 1, 0); 
  ambientLight(102, 102, 102);
  background(200); 



  if (cylinder.shiftMode) // place cylinder
  {
    camera(0, -400, 0, 0, 0, 0, 1, 1, 0); // on se place droit en dessus
    box(lBoard, wBoard, lBoard);

    pushMatrix();
    rotateX(PI/2);
    rotateZ(-PI/2);
    //We draw a "preview Cylinder" of where the cylinder will be placed once the player clicks, the cylinder isn't drawn if it can't be placed where the mouse is being pointed at
    if (cylinder.canPlaceCylinder()) 
    {
    PVector cyl = new PVector(map(mouseX, cylinder.minXBoundariesCylinder, cylinder.maxXBoundariesCylinder, cylinder.cylinderRadius, lBoard-cylinder.cylinderRadius), map(mouseY, cylinder.minYBoundariesCylinder, cylinder.maxYBoundariesCylinder, cylinder.cylinderRadius, lBoard-cylinder.cylinderRadius));
    shape(cylinder.cylinderShaper(cyl.x-lBoard/2, cyl.y-lBoard/2));
    }
    //drawing existing cylinder
    for (int i = 0; i< cylinder.arrayCylinderShape.size (); i++)
    {
      shape(cylinder.arrayCylinderShape.get(i));
    }
    popMatrix();
    
    //ball drwaing
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
    for (int i = 0; i< cylinder.arrayCylinderShape.size (); i++)
    {
      shape(cylinder.arrayCylinderShape.get(i));
    }
    popMatrix();

    //move and draw ball
    pushMatrix();
    mover.update();
    mover.display();
    popMatrix();
  }
}

//we save the mouse position, and archive the rotation level
void mousePressed()
{
  if (cylinder.shiftMode)
  {
    pushMatrix();
    rotateX(PI/2);
    rotateZ(-PI/2);

    if (cylinder.canPlaceCylinder()) 
    {
    PVector cyl = new PVector(map(mouseX, cylinder.minXBoundariesCylinder, cylinder.maxXBoundariesCylinder, cylinder.cylinderRadius, lBoard-cylinder.cylinderRadius), map(mouseY, cylinder.minYBoundariesCylinder, cylinder.maxYBoundariesCylinder, cylinder.cylinderRadius, lBoard-cylinder.cylinderRadius));
    cylinder.arrayCylinderPosition.add(cyl);
    cylinder.arrayCylinderShape.add(cylinder.cylinderShaper(cyl.x-lBoard/2, cyl.y-lBoard/2));
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
      cylinder.shiftMode = true;
    }
  }
}

void keyReleased() { 
  if (key == CODED) {
    if (keyCode == SHIFT) { 
      cylinder.shiftMode = false;
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



