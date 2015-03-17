
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





float rotVertical = 0;

//rotation that we do
float rotX = 0;
float rotZ = 0;
float rotYNeg = 0;

//the rotation that was already done
float previousrotX ;
float previousrotZ ;
//the position of the mouse at the begining of the movement
float mousePositionX;
float mousePositionY ;

//baord movementscaler
float movementScale = 1;

//ball and board attributes
float radius = 5;
float lBoard = 250;

//movement attributes
PVector gravityForce = new PVector(0, 0, 0);
float gravityConstant = 0.3;
Mover mover;


// cylinder declaration 
float cylinderBaseSize = 50; 
float cylinderHeight = 50; 
int cylinderResolution = 40;
PShape openCylinder = new PShape();
PShape cylinderTop = new PShape();
PShape cylinderBottom = new PShape();


void setup() 
{
  size(500, 500, P3D); 
  noStroke();
  mover = new Mover();

  //Open cylinder
  float angle;
  float[] x = new float[cylinderResolution + 1]; 
  float[] y = new float[cylinderResolution + 1];
  
  //get the x and y position on a circle for all the sides
  for (int i = 0; i < x.length; i++) {
    angle = (TWO_PI / cylinderResolution) * i; 
    x[i] = sin(angle) * cylinderBaseSize; 
    y[i] = cos(angle) * cylinderBaseSize;
  }
  
  openCylinder = createShape();
  openCylinder.beginShape(QUAD_STRIP);
  //draw the border of the cylinder
  for (int i = 0; i < x.length; i++) { 
    openCylinder.vertex(x[i], y[i], 0); 
    openCylinder.vertex(x[i], y[i], cylinderHeight);
  }
  openCylinder.endShape();
  
  cylinderTop = createShape();
  cylinderTop.beginShape(TRIANGLE_FAN);
  cylinderTop.vertex(0,0, cylinderHeight);
  for (int i = 0; i < x.length; i++) {
    cylinderTop.vertex(x[i], y[i], cylinderHeight);
  }
  cylinderTop.vertex(x[0], y[0], cylinderHeight);
  cylinderTop.endShape();  
  
  cylinderBottom = createShape();
  cylinderBottom.beginShape(TRIANGLE_FAN);
  cylinderBottom.vertex(0,0, 0);
  for (int i = 0; i < x.length; i++) {
    cylinderBottom.vertex(x[i], y[i], 0);
  }
  cylinderBottom.vertex(x[0], y[0], 0);
  cylinderBottom.endShape();
}

void draw() {
  //ambient settings
  camera(250, -1, 250, width/2, height/2, 0, 0, 1, 0); 
  directionalLight(50, 100, 125, 0, 1, 0); 
  ambientLight(102, 102, 102);
  background(200);

  //we move the coodinates to have the board in the center of the window
  translate(width/2, height/2, 0);

  //we map the vertical rotation induced by the arrow keys to a rotation in the z axis
  rotYNeg = map(rotVertical, 0, width, 0, PI); 
  rotateY(-rotYNeg);
  rotateZ(rotZ); 
  rotateX(-rotX);


  box(lBoard, 10, lBoard);
  gravityForce.x = sin(rotZ) * gravityConstant;
  gravityForce.z = sin(rotX) * gravityConstant;
  // draw cylinder
  pushMatrix();
  rotateX(PI/2);
  shape(openCylinder);
  shape(cylinderTop);
  shape(cylinderBottom);
  popMatrix();


  pushMatrix();
  mover.update();
  mover.display();
  popMatrix();
}

//we save the mouse position, and archive the rotation level
void mousePressed()
{
  mousePositionX = mouseX;
  mousePositionY = mouseY;
  previousrotZ = rotZ;
  previousrotX = rotX;
}


//when the mouse is dragged we compare the distance mouvec
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

void mouseWheel(MouseEvent event) {
    movementScale -= event.getCount();
}

