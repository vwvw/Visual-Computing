
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
float radius = 30;
float lBoard = 250;
float wBoard = 1;

//movement attributes
PVector gravityForce = new PVector(0, 0, 0);
float gravityConstant = 0.3;
Mover mover;


// cylinder declaration 
float cylinderBaseSize = 10; 
float cylinderHeight = 25; 
int cylinderResolution = 30;
PShape completeCylinder = new PShape();
PShape openCylinder = new PShape();
PShape cylinderTop = new PShape();
PShape cylinderBottom = new PShape();
boolean shiftMode = false; //true = place cylinder
ArrayList<PVector> arrayCylinder = new ArrayList<PVector>();

void setup() 
{
  size(500, 500, P3D); 
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
    translate(0, -wBoard/2, 0);
    rotateX(PI/2);
    rotateZ(-PI/2);
    float BoardOnScreenSize = screenX(lBoard/2, lBoard/2, 0) -  screenX(-lBoard/2, -lBoard/2, 0);

    float minX =  screenX(-lBoard/2, -lBoard/2, 0);
    float maxX = minX + BoardOnScreenSize;
    float minY = screenY(-lBoard/2, -lBoard/2, 0);
    float maxY = minY + BoardOnScreenSize; 


    if ((mouseX >= minX && mouseX <= maxX) && (mouseY > minY && mouseY < maxY)) // trouve les valeurs exactes...
    {
      cylinderAdd(map(mouseX, minX, maxX, 0, lBoard)-lBoard/2, map(mouseY, minY, maxY, 0, lBoard)-lBoard/2);
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
    camera(250, -1, 250, width/2, height/2, 0, 0, 1, 0); 
    //we move the coodinates to have the board in the center of the window
    translate(width/2, height/2, 0);

    rotateZ(rotZ); 
    rotateX(-rotX);


    box(lBoard, wBoard, lBoard);
    gravityForce.x = sin(rotZ) * gravityConstant;
    gravityForce.z = sin(rotX) * gravityConstant;


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
    translate(0, -wBoard/2, 0);
    pushMatrix();
    rotateX(PI/2);
    rotateZ(-PI/2);
    float BoardOnScreenSize = screenX(lBoard/2, lBoard/2, 0) -  screenX(-lBoard/2, -lBoard/2, 0);

    float minX =  screenX(-lBoard/2, -lBoard/2, 0);
    float maxX = minX + BoardOnScreenSize;
    float minY = screenY(-lBoard/2, -lBoard/2, 0);
    float maxY = minY + BoardOnScreenSize; 


    if ((mouseX >= minX && mouseX <= maxX) && (mouseY > minY && mouseY < maxY)) // trouve les valeurs exactes...
    {
      PVector cyl = new PVector(map(mouseX, minX, maxX, 0, lBoard), map(mouseY, minY, maxY, 0, lBoard));
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
    x[i] = sin(angle) * cylinderBaseSize + positionX ; 
    y[i] = cos(angle) * cylinderBaseSize + positionY ;
  }

  //corps
  openCylinder = createShape();
  openCylinder.beginShape(QUAD_STRIP);
  //draw the border of the cylinder
  for (int i = 0; i < x.length; i++) { 
    openCylinder.vertex(x[i], y[i], wBoard/2); 
    openCylinder.vertex(x[i], y[i], cylinderHeight + wBoard/2);
  }
  openCylinder.endShape();

  //top
  cylinderTop = createShape();
  cylinderTop.beginShape(TRIANGLE_FAN);
  cylinderTop.vertex(positionX, positionY, cylinderHeight + wBoard/2);
  for (int i = 0; i < x.length; i++) {
    cylinderTop.vertex(x[i], y[i], cylinderHeight + wBoard/2);
  }
  cylinderTop.vertex(x[0], y[0], cylinderHeight + wBoard/2);
  cylinderTop.endShape();  

  //bottom
  cylinderBottom = createShape();
  cylinderBottom.beginShape(TRIANGLE_FAN);
  cylinderBottom.vertex(positionX, positionY, wBoard/2);
  for (int i = 0; i < x.length; i++) {
    cylinderBottom.vertex(x[i], y[i], wBoard/2);
  }
  cylinderBottom.vertex(x[0], y[0], wBoard/2);
  cylinderBottom.endShape();

  completeCylinder.addChild(openCylinder);
  completeCylinder.addChild(cylinderTop);
  completeCylinder.addChild(cylinderBottom);

  shape(completeCylinder);
}

